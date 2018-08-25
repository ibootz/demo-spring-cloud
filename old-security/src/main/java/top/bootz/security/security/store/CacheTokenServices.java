package top.bootz.security.security.store;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.yxt.common.Constants;
import com.yxt.common.RedisConstants;
import com.yxt.common.auth.TokenUtil;
import com.yxt.common.util.APIUtil;
import com.yxt.lecai.entity.mysql.org.Org;
import com.yxt.lecai.entity.mysql.role.Permission;
import com.yxt.lecai.entity.mysql.role.UserRoleMap;
import com.yxt.lecai.entity.mysql.user.OrgUser;
import com.yxt.lecai.repo.mysql.org.OrgRepository;
import com.yxt.lecai.repo.mysql.orguser.OrgUserRepository;
import com.yxt.lecai.repo.mysql.rolepermissionmap.RolePermissionMapRepository;
import com.yxt.lecai.repo.mysql.userrolemap.UserRoleMapRepository;

public class CacheTokenServices extends DefaultTokenServices {

	private int refreshTokenValiditySeconds = 60 * 60 * 24 * 1; // default 1
																// days.

	private int accessTokenValiditySeconds = 60 * 60 *2; // default 2 hours.
	
	@Autowired
	private TokenStore tokenStore;

	private ClientDetailsService clientDetailsService;

	private TokenEnhancer accessTokenEnhancer;

	@Autowired
	@Qualifier("stringRedisTemplate")
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private OrgUserRepository orgUserRepository;

	@Autowired
	private RolePermissionMapRepository rolePermissionMapRepository;

	@Autowired
	private UserRoleMapRepository userRoleMapRepository;

	@Autowired
	private OrgRepository orgRepository;

	private AuthenticationManager authenticationManager;

	private boolean supportRefreshToken = false;

	private boolean reuseRefreshToken = true;

	@Transactional
	public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {

		OAuth2AccessToken existingAccessToken = tokenStore.getAccessToken(authentication);
		OAuth2RefreshToken refreshToken = null;
		if (existingAccessToken != null) {
			String userId = (String) stringRedisTemplate.opsForHash().get(RedisConstants.REDIS_NAME_TOKEN + existingAccessToken, "userId");
			if (existingAccessToken.isExpired() || userId==null) {
				if (existingAccessToken.getRefreshToken() != null) {
					refreshToken = existingAccessToken.getRefreshToken();
					tokenStore.removeRefreshToken(refreshToken);
				}
				tokenStore.removeAccessToken(existingAccessToken);
			} else {
				tokenStore.storeAccessToken(existingAccessToken, authentication);
				return existingAccessToken;
			}
		}

		if (refreshToken == null) {
			refreshToken = createRefreshToken(authentication);
		}

		else if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
			ExpiringOAuth2RefreshToken expiring = (ExpiringOAuth2RefreshToken) refreshToken;
			if (System.currentTimeMillis() > expiring.getExpiration().getTime()) {
				refreshToken = createRefreshToken(authentication);
			}
		}

		OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
		tokenStore.storeAccessToken(accessToken, authentication);
		// In case it was modified
		refreshToken = accessToken.getRefreshToken();
		if (refreshToken != null) {
			tokenStore.storeRefreshToken(refreshToken, authentication);
		}
		return accessToken;
	}

	@Transactional(noRollbackFor = { InvalidTokenException.class, InvalidGrantException.class })
	public OAuth2AccessToken refreshAccessToken(String refreshTokenValue, TokenRequest tokenRequest)
			throws AuthenticationException {

		if (!supportRefreshToken) {
			throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
		} 

		OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(refreshTokenValue);
		if (refreshToken == null) {
			throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
		}

		OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(refreshToken);
		if (this.authenticationManager != null && !authentication.isClientOnly()) {
			// The client has already been authenticated, but the user
			// authentication might be old now, so give it a
			// chance to re-authenticate.
			Authentication user = new PreAuthenticatedAuthenticationToken(authentication.getUserAuthentication(), "",
					authentication.getAuthorities());
			user = authenticationManager.authenticate(user);
			Object details = authentication.getDetails();
			authentication = new OAuth2Authentication(authentication.getOAuth2Request(), user);
			authentication.setDetails(details);
		}
		String clientId = authentication.getOAuth2Request().getClientId();
		if (clientId == null || !clientId.equals(tokenRequest.getClientId())) {
			throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
		}

		// clear out any access tokens already associated with the refresh
		// token.
		tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);

		if (isExpired(refreshToken)) {
			tokenStore.removeRefreshToken(refreshToken);
			throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
		}

		authentication = createRefreshedAuthentication(authentication, tokenRequest);

		if (!reuseRefreshToken) {
			tokenStore.removeRefreshToken(refreshToken);
			refreshToken = createRefreshToken(authentication);
		}

		OAuth2AccessToken accessToken = createAccessToken(authentication, refreshToken);
		tokenStore.storeAccessToken(accessToken, authentication);
		if (!reuseRefreshToken) {
			tokenStore.storeRefreshToken(refreshToken, authentication);
		}
		return accessToken;
	}

	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		return tokenStore.getAccessToken(authentication);
	}

	private OAuth2AccessToken createAccessToken(OAuth2Authentication authentication, OAuth2RefreshToken refreshToken) {
		String userId = authentication.getUserAuthentication().getName();
		DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(generateToken(userId));
		int validitySeconds = getAccessTokenValiditySeconds(authentication.getOAuth2Request());
		if (validitySeconds > 0) {
			token.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
		}
		token.setRefreshToken(refreshToken);
		token.setScope(authentication.getOAuth2Request().getScope());

		return accessTokenEnhancer != null ? accessTokenEnhancer.enhance(token, authentication) : token;
	}

	protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
		if (clientDetailsService != null) {
			ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
			Integer validity = client.getAccessTokenValiditySeconds();
			if (validity != null) {
				return validity;
			}
		}
		return accessTokenValiditySeconds;
	}

	private OAuth2RefreshToken createRefreshToken(OAuth2Authentication authentication) {
		if (!isSupportRefreshToken(authentication.getOAuth2Request())) {
			return null;
		}
		int validitySeconds = getRefreshTokenValiditySeconds(authentication.getOAuth2Request());
		String value = UUID.randomUUID().toString();
		if (validitySeconds > 0) {
			return new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis()
					+ (validitySeconds * 1000L)));
		}
		return new DefaultOAuth2RefreshToken(value);
	}

	protected int getRefreshTokenValiditySeconds(OAuth2Request clientAuth) {
		if (clientDetailsService != null) {
			ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
			Integer validity = client.getRefreshTokenValiditySeconds();
			if (validity != null) {
				return validity;
			}
		}
		return refreshTokenValiditySeconds;
	}

	private String generateToken(String userId) {
		String token = TokenUtil.generateToken(userId, "10", 0);
		OrgUser user = orgUserRepository.findOne(userId);
		List<Permission> ListPermissionsOfUser = rolePermissionMapRepository.findPermissionsOfUser(userId);
		UserRoleMap userRoleMap = userRoleMapRepository.findFirstByUserID(userId);
		if (userRoleMap == null) {
			createUserDefaultRole(userId);
		}
		String roleId = userRoleMap == null ? Constants.NORMAL_ROLEID : userRoleMap.getRoleID();
		stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_TOKEN + token,
				RedisConstants.REDIS_NAME_SOURCETYPE, Constants.SOURCETYPE_LECAI);
		stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_USERID,
				user.getPid());
		stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_TOKEN + token,
				RedisConstants.REDIS_NAME_CONSUMERUSERID, user.getConsumerUserId());
		stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_ORGID,
				user.getOrgId());
		stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_ROLE_ID,
				roleId);
		Org org = orgRepository.findOne(user.getOrgId());
		if (org != null) {
			stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_TOKEN + token,
					RedisConstants.REDIS_NAME_DOMAIN, org.getDomain());
			stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_TOKEN + token,
					RedisConstants.REDIS_NAME_ORGNAME, org.getOrgName());
		}

		// store userIds:token
		stringRedisTemplate.opsForList().leftPush(RedisConstants.REDIS_NAME_UIDS_TOKEN + user.getPid(), token);
		// stroe userIds:fullName
		if (user.getFullName() != null) {
			stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_UIDS_PROPERTY + user.getPid(),
					RedisConstants.REDIS_NAME_USERFULLNAME, user.getFullName());
		}
		if (user.getImageUrl() != null) {
			stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_UIDS_PROPERTY + user.getPid(),
					RedisConstants.REDIS_NAME_USERAVATAR, user.getImageUrl());
		}
		if (user.getMobile() != null) {
			stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_UIDS_PROPERTY + user.getPid(),
					RedisConstants.REDIS_NAME_USERMOBILE, user.getMobile());
		}
		JSONArray pemissionArray = new JSONArray();
		for (Permission permission : ListPermissionsOfUser) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(RedisConstants.REDIS_NAME_PID, permission.getPid());
			jsonObject.put(RedisConstants.REDIS_NAME_RESOURCENAME, permission.getResourceName());
			jsonObject.put(RedisConstants.REDIS_NAME_OPERATIONMETHOD, permission.getOperationMethod());
			pemissionArray.add(jsonObject);
		}
		stringRedisTemplate.opsForHash().put(RedisConstants.REDIS_NAME_TOKEN + token,
				RedisConstants.REDIS_NAME_PERMISSIONLIST, pemissionArray.toString());
		stringRedisTemplate.expire(RedisConstants.REDIS_NAME_TOKEN + token, 2, TimeUnit.HOURS);
		stringRedisTemplate.expire(RedisConstants.REDIS_NAME_UIDS_TOKEN + user.getPid(), 2, TimeUnit.HOURS);
		stringRedisTemplate.expire(RedisConstants.REDIS_NAME_UIDS_PROPERTY + user.getPid(), 2, TimeUnit.HOURS);
		return token;
	}

	private void createUserDefaultRole(String userId) {
		UserRoleMap entity = new UserRoleMap();
		String rolePid = APIUtil.getUUID();
		entity.setPid(rolePid);
		entity.setUserID(userId);
		entity.setCreator(userId);
		entity.setRoleID(Constants.NORMAL_ROLEID);
		String now = APIUtil.now();
		entity.setCreateTime(now);
		userRoleMapRepository.deleteUserRoleMapByUserID(userId);
		userRoleMapRepository.save(entity);
	}

	public OAuth2AccessToken readAccessToken(String accessToken) {
		return tokenStore.readAccessToken(accessToken);
	}

	public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException,
			InvalidTokenException {
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
		if (accessToken == null) {
			throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
		} else if (accessToken.isExpired()) {
			tokenStore.removeAccessToken(accessToken);
			throw new InvalidTokenException("Access token expired: " + accessTokenValue);
		}

		OAuth2Authentication result = tokenStore.readAuthentication(accessToken);
		if (clientDetailsService != null) {
			String clientId = result.getOAuth2Request().getClientId();
			try {
				clientDetailsService.loadClientByClientId(clientId);
			} catch (ClientRegistrationException e) {
				throw new InvalidTokenException("Client not valid: " + clientId, e);
			}
		}
		return result;
	}

	public String getClientId(String tokenValue) {
		OAuth2Authentication authentication = tokenStore.readAuthentication(tokenValue);
		if (authentication == null) {
			throw new InvalidTokenException("Invalid access token: " + tokenValue);
		}
		OAuth2Request clientAuth = authentication.getOAuth2Request();
		if (clientAuth == null) {
			throw new InvalidTokenException("Invalid access token (no client id): " + tokenValue);
		}
		return clientAuth.getClientId();
	}

	public boolean revokeToken(String tokenValue) {
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
		if (accessToken == null) {
			return false;
		}
		if (accessToken.getRefreshToken() != null) {
			tokenStore.removeRefreshToken(accessToken.getRefreshToken());
		}
		tokenStore.removeAccessToken(accessToken);
		return true;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(tokenStore, "tokenStore must be set");
	}

	protected boolean isExpired(OAuth2RefreshToken refreshToken) {
		if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
			ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken) refreshToken;
			return expiringToken.getExpiration() == null
					|| System.currentTimeMillis() > expiringToken.getExpiration().getTime();
		}
		return false;
	}

	private OAuth2Authentication createRefreshedAuthentication(OAuth2Authentication authentication, TokenRequest request) {
		OAuth2Authentication narrowed = authentication;
		Set<String> scope = request.getScope();
		OAuth2Request clientAuth = authentication.getOAuth2Request().refresh(request);
		if (scope != null && !scope.isEmpty()) {
			Set<String> originalScope = clientAuth.getScope();
			if (originalScope == null || !originalScope.containsAll(scope)) {
				throw new InvalidScopeException("Unable to narrow the scope of the client authentication to " + scope
						+ ".", originalScope);
			} else {
				clientAuth = clientAuth.narrowScope(scope);
			}
		}
		narrowed = new OAuth2Authentication(clientAuth, authentication.getUserAuthentication());
		return narrowed;
	}

	protected boolean isSupportRefreshToken(OAuth2Request clientAuth) {
		if (clientDetailsService != null) {
			ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
			return client.getAuthorizedGrantTypes().contains("refresh_token");
		}
		return this.supportRefreshToken;
	}
}
