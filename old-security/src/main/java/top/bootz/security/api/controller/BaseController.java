package top.bootz.security.api.controller;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import love.cq.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.yxt.common.Constants;
import com.yxt.common.RedisConstants;
import com.yxt.common.exception.ApiException;
import com.yxt.common.exception.ExceptionKey;
import com.yxt.common.pojo.CommonList;
import com.yxt.common.pojo.PasswordSecurity;
import com.yxt.common.pojo.UserAccount;
import com.yxt.common.service.CacheService;
import com.yxt.common.service.ConfigService;
import com.yxt.common.service.RabbitmqService;
import com.yxt.common.util.APIUtil;
import com.yxt.common.util.BeanHelper;
import com.yxt.lecai.entity.mysql.org.Org;
import com.yxt.lecai.entity.mysql.org.OrgConfig;
import com.yxt.lecai.entity.mysql.role.Permission;
import com.yxt.lecai.entity.mysql.user.ConsumerUser;
import com.yxt.lecai.entity.mysql.user.OrgUser;
import com.yxt.lecai.service.common.SearchCriteria;
import com.yxt.lecai.service.org.OrgConfigService;
import com.yxt.lecai.service.org.OrgService;
import com.yxt.lecai.service.user.ConsumerUserService;
import com.yxt.lecai.service.user.OrgUserService;
import com.yxt.oauth.api.bean.SearchBean;
import com.yxt.oauth.api.bean.common.UserCacheInfo;
import com.yxt.oauth.api.bean.user.UserAccountList;
import com.yxt.oauth.api.common.APIConstants;

/**
 * BaseController
 */
public class BaseController {
	private static final Logger LOGGER = Logger.getLogger(BaseController.class);

	@Autowired
	private MessageSource msgSource;

	@Autowired
	private CacheService cacheService;

	@Autowired
	private OrgService orgService;

	@Autowired
	private ConsumerUserService consumerUserService;

	@Autowired
	private OrgUserService orgUserService;

	@Autowired
	private RabbitmqService rabbitmqService;

	@Autowired
	private OrgConfigService orgConfigService;

	@Autowired
	private ConfigService configService;

	/**
	 * handleApiException
	 *
	 * @param ex
	 *            ApiException
	 * @return ResponseEntity<String>
	 */
	@ExceptionHandler(ApiException.class)
	protected ResponseEntity<String> handleApiException(ApiException ex) {
		return APIUtil.handleApiException(ex, msgSource);
	}

	/**
	 * handelMethodArgumentNotValidException
	 *
	 * @param ex
	 *            MethodArgumentNotValidException
	 * @return ResponseEntity<String>
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<String> handelMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		return APIUtil.handelMethodArgumentNotValidException(ex, msgSource);
	}

	/**
	 * saveOrgUserCache
	 *
	 * @param token
	 *            String
	 * @param entity
	 *            OrgUser
	 * @param ListPermissionsOfUser
	 *            List
	 * @param roleId
	 *            String
	 */
	protected void saveOrgUserCache(String token, OrgUser entity, List<Permission> ListPermissionsOfUser, String roleId) {
		// store tokens:userId:orgId
		cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_SOURCETYPE,
				Constants.SOURCETYPE_LECAI);
		cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_USERID,
				entity.getPid());
		cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_CONSUMERUSERID,
				entity.getConsumerUserId());
		cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_ORGID,
				entity.getOrgId());
		cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_ROLE_ID, roleId);
		Org org = orgService.findOne(entity.getOrgId());
		if (org != null) {
			cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_DOMAIN,
					org.getDomain());
			cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_ORGNAME,
					org.getOrgName());
		}

		// store userIds:token
		cacheService.getList().leftPush(RedisConstants.REDIS_NAME_UIDS_TOKEN + entity.getPid(), token);
		// stroe userIds:fullName
		if (entity.getFullName() != null) {
			cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
					RedisConstants.REDIS_NAME_USERFULLNAME, entity.getFullName());
		}
		if (entity.getImageUrl() != null) {
			cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
					RedisConstants.REDIS_NAME_USERAVATAR, entity.getImageUrl());
		}
		if (entity.getMobile() != null) {
			cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
					RedisConstants.REDIS_NAME_USERMOBILE, entity.getMobile());
		}
		JSONArray pemissionArray = new JSONArray();
		for (Permission permission : ListPermissionsOfUser) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(RedisConstants.REDIS_NAME_PID, permission.getPid());
			jsonObject.put(RedisConstants.REDIS_NAME_RESOURCENAME, permission.getResourceName());
			jsonObject.put(RedisConstants.REDIS_NAME_OPERATIONMETHOD, permission.getOperationMethod());
			pemissionArray.add(jsonObject);
		}
		cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_PERMISSIONLIST,
				pemissionArray.toString());
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_TOKEN + token, 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_TOKEN + entity.getPid(), 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(), 2, TimeUnit.HOURS);
	}

	protected void saveConsumerUserCache(String token, ConsumerUser entity) {
		cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_SOURCETYPE,
				Constants.SOURCETYPE_LECAI);
		cacheService.setHashValue(RedisConstants.REDIS_NAME_TOKEN + token, RedisConstants.REDIS_NAME_CONSUMERUSERID,
				entity.getPid());

		cacheService.getList().leftPush(RedisConstants.REDIS_NAME_UIDS_TOKEN + entity.getPid(), token);
		cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
				RedisConstants.REDIS_NAME_USERFULLNAME, entity.getNickName());
		cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
				RedisConstants.REDIS_NAME_USERAVATAR, entity.getAvatar());
		cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
				RedisConstants.REDIS_NAME_USERMOBILE, entity.getMobile());
		cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
				RedisConstants.REDIS_NAME_USERINDUSTRYID, entity.getIndustryId());
		cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
				RedisConstants.REDIS_NAME_USERFUNCTIONID, entity.getFunctionId());
		cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(),
				RedisConstants.REDIS_NAME_USERPOSITIONID, entity.getPositionId());

		cacheService.setExpireTime(RedisConstants.REDIS_NAME_TOKEN + token, 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_TOKEN + entity.getPid(), 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_PROPERTY + entity.getPid(), 2, TimeUnit.HOURS);
		saveUserAccountCache(entity);
	}

	/**
	 * getUserCacheInfo
	 *
	 * @param token
	 *            String
	 * @return UserCacheInfo
	 */
	protected UserCacheInfo getUserCacheInfo(String token) {
		Map<Object, Object> tokenCacheMap = null;
		if (StringUtils.isNotBlank(token)) {
			tokenCacheMap = cacheService.getUserMapByToken(token);
		}
		UserCacheInfo result;
		if (tokenCacheMap != null) {
			String orgId = (String) tokenCacheMap.get(RedisConstants.REDIS_NAME_ORGID);
			String userId = (String) tokenCacheMap.get(RedisConstants.REDIS_NAME_USERID);
			String consumerUserId = (String) tokenCacheMap.get(RedisConstants.REDIS_NAME_CONSUMERUSERID);
			String fourdUserId = (String) tokenCacheMap.get(RedisConstants.REDIS_NAME_FOURDUSERID);
			String roleId = (String) tokenCacheMap.get(RedisConstants.REDIS_ROLE_ID);
			String permissionJson = (String) tokenCacheMap.get(RedisConstants.REDIS_NAME_PERMISSIONLIST);
			result = new UserCacheInfo(orgId, userId, consumerUserId, roleId, permissionJson, fourdUserId);
		} else {
			result = new UserCacheInfo("", "", "", "", "", "");
		}
		return result;
	}

	/**
	 * getUserCacheInfo
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return UserCacheInfo UserCacheInfo
	 */
	protected UserCacheInfo getUserCacheInfo(HttpServletRequest request) {
		return getUserCacheInfo(request.getHeader(APIConstants.HEADER_NAME_TOKEN));
	}

	/**
	 * getUserIdByToken
	 *
	 * @param token
	 *            String
	 * @return String
	 */
	protected String getUserIdByToken(String token) {
		String userId = null;
		if (StringUtils.isNotBlank(token)) {
			userId = cacheService.getPropertyValueByToken(token, RedisConstants.REDIS_NAME_USERID);
		}
		return userId;
	}

	protected String getConsumerUserId(String token) {
		String consumerUserId = null;
		if (StringUtils.isNotBlank(token)) {
			consumerUserId = cacheService.getPropertyValueByToken(token, RedisConstants.REDIS_NAME_CONSUMERUSERID);
		}
		return consumerUserId;
	}

	protected String getFourdUserId(String token) {
		String fourdUserId = null;
		if (StringUtils.isNotBlank(token)) {
			fourdUserId = cacheService.getPropertyValueByToken(token, RedisConstants.REDIS_NAME_FOURDUSERID);
		}
		return fourdUserId;
	}

	/**
	 * getToken
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	protected String getToken(HttpServletRequest request) {
		return request.getHeader(APIConstants.HEADER_NAME_TOKEN);
	}

	/**
	 * getUserId
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	protected String getUserId(HttpServletRequest request) {
		String token = request.getHeader(APIConstants.HEADER_NAME_TOKEN);
		return getUserIdByToken(token);
	}

	/**
	 * getConsumerUserId
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	protected String getConsumerUserId(HttpServletRequest request) {
		String token = request.getHeader(APIConstants.HEADER_NAME_TOKEN);
		return getConsumerUserId(token);
	}

	/**
	 * getSource
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return int
	 */
	protected int getSource(HttpServletRequest request) {
		return str2Int(request.getHeader(APIConstants.HEADER_NAME_SOURCE), 0);
	}

	/**
	 * getOrgIdByToken
	 *
	 * @param token
	 *            String
	 * @return String
	 */
	protected String getOrgIdByToken(String token) {
		String orgId = null;
		if (StringUtils.isNotBlank(token)) {
			orgId = cacheService.getPropertyValueByToken(token, RedisConstants.REDIS_NAME_ORGID);
		}
		return orgId;
	}

	/**
	 * getOrgIdByToken
	 *
	 * @param token
	 *            String
	 * @return String
	 */
	protected String getOrgId(HttpServletRequest request) {
		String orgId = null;
		String token = getToken(request);
		if (StringUtils.isNotBlank(token)) {
			orgId = cacheService.getPropertyValueByToken(token, RedisConstants.REDIS_NAME_ORGID);
		}
		return orgId;
	}

	private boolean hasPermission(String resourceName, int operationMethod, JSONArray permissionArray) {
		JSONObject permissionJson;
		boolean hasPermission = false;
		String resourceStr;
		for (int i = 0; i < permissionArray.size(); i++) {
			permissionJson = permissionArray.getJSONObject(i);
			resourceStr = permissionJson.getString("resourceName") + permissionJson.getString("operationMethod");
			if ((resourceName + operationMethod).equals(resourceStr)) {
				hasPermission = true;
			}

		}
		return hasPermission;
	}

	/**
	 * checkPermission
	 *
	 * @param resourceName
	 *            String
	 * @param operationMethod
	 *            int
	 * @param token
	 *            String
	 */
	protected void checkPermission(String resourceName, int operationMethod, String token) {
		String roleId = cacheService.getPropertyValueByToken(token, RedisConstants.REDIS_ROLE_ID);
		if (Constants.SUPERADMIN_ROLEID.equals(roleId)) {
			return;
		}
		String strPermissionArray = cacheService.getPropertyValueByToken(token,
				RedisConstants.REDIS_NAME_PERMISSIONLIST);
		JSONArray permissionArray = JSONArray.fromObject(strPermissionArray);
		if (strPermissionArray == null) {
			throw new ApiException(ExceptionKey.AUTHOR_PERMISSIONLISTNULL_ERROR);
		}
		boolean hasPermission = hasPermission(resourceName, operationMethod, permissionArray);
		// not add permission on method so set true temporily
		// hasPermission = true;
		if (!hasPermission) {
			throw new ApiException(ExceptionKey.AUTHOR_NOPERMISSION_ERROR);
		}
	}

	/**
	 * verifyToken
	 *
	 * @param token
	 *            String
	 */
	protected void verifyToken(String token) {
		if (token == null) {
			throw new ApiException("global.token.empty");
		}
		String userId = getUserIdByToken(token);
		String consumerUserId = getConsumerUserId(token);
		String fourdUserId = getFourdUserId(token);
		if (StringUtils.isBlank(userId) && StringUtils.isBlank(consumerUserId) && StringUtils.isBlank(fourdUserId)) {
			throw new ApiException("global.token.invalid");
		} else if (userId != null) {
			updateUserCache(token, userId);
		} else if (consumerUserId != null) {
			updateConsumerUserCache(token, consumerUserId);
		} else {
			updateFourdUserCache(token, fourdUserId);
		}
	}

	/**
	 * Checks if is self ops.
	 *
	 * @param token
	 *            the token
	 * @param userId
	 *            the user id
	 * @return true, if is self ops
	 */
	protected boolean isSelfOps(String token, String userId) {
		return userId.equals(getUserIdByToken(token));
	}

	private void updateUserCache(String token, String userId) {
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_TOKEN + token, 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_TOKEN + userId, 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_PROPERTY + userId, 2, TimeUnit.HOURS);
	}

	private void updateConsumerUserCache(String token, String consumerUserId) {
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_TOKEN + token, 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_TOKEN + consumerUserId, 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_PROPERTY + consumerUserId, 2, TimeUnit.HOURS);
	}

	private void updateFourdUserCache(String token, String fourdUserid) {
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_TOKEN + token, 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_TOKEN + fourdUserid, 2, TimeUnit.HOURS);
		cacheService.setExpireTime(RedisConstants.REDIS_NAME_UIDS_PROPERTY + fourdUserid, 2, TimeUnit.HOURS);
	}

	/**
	 * getEventStr
	 *
	 * @param action
	 *            int
	 * @param target
	 *            String
	 * @param errorKey
	 *            String
	 * @param request
	 *            HttpServletRequest
	 * @param description
	 *            String
	 * @return String
	 */
	protected String getEventStr(int action, String target, String errorKey, HttpServletRequest request,
			String description) {
		String result = "";
		String trackingCode = request.getHeader("path");
		result += "\"" + APIUtil.getUUID() + "\",";
		result += "\"" + action + "\",";
		if (StringUtils.isBlank(errorKey)) {
			result += "\"1\",";
		} else {
			result += "\"0\",";
		}
		result += "\"" + target + "\",";
		result += "\"" + trackingCode + " " + request.getRequestURI() + "\",";
		if (StringUtils.isBlank(errorKey)) {
			result += "\"" + description + "\",";
		} else {
			result += "\"" + errorKey + "\",";
		}
		result += "\"" + request.getHeader(APIConstants.HEADER_NAME_USERAGENT) + "\",";
		result += "\"" + getClientIp(request) + "\",";
		result += "\"" + APIUtil.now() + "\",";
		result += "\"" + getSource(request) + "\",";
		UserCacheInfo userCacheInfo = getUserCacheInfo(request);
		result += filterNullValue(userCacheInfo.getUserId(), false);
		result += filterNullValue(userCacheInfo.getOrgId(), true);
		return result;
	}

	private String filterNullValue(String input, boolean isLast) {
		String output;
		if (input == null) {
			output = "null";
		} else {
			output = "\"" + input + "\"";
		}
		if (!isLast) {
			output += ",";
		}
		return output;
	}

	/**
	 * getClientIp
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	protected String getClientIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (StringUtils.isBlank(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	/**
	 * str2Int
	 *
	 * @param str
	 *            String
	 * @param nullVal
	 *            int
	 * @return int
	 */
	protected int str2Int(String str, int nullVal) {
		int result;
		try {
			result = Integer.parseInt(str);
		} catch (Exception e) {
			result = nullVal;
			LOGGER.info("Integer.parseInt() error:" + e.getMessage());
		}
		return result;
	}

	/**
	 * getPageRequest
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return PageRequest
	 */
	protected PageRequest getPageRequest(HttpServletRequest request) {
		return getPageRequest(request, "createTime");
	}

	/**
	 * getPageRequest
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return PageRequest
	 */
	protected PageRequest getPageRequest(HttpServletRequest request, String defaultOrderBy) {
		int offset = str2Int(request.getParameter(APIConstants.PARAM_NAME_OFFSET), 0);
		int limit = str2Int(request.getParameter(APIConstants.PARAM_NAME_LIMIT), APIConstants.DEFAULT_LIMIT);
		if (limit > 100) {
			limit = 100;
		}
		String orderBy = request.getParameter(APIConstants.PARAM_NAME_ORDERBY);
		if (StringUtils.isBlank(orderBy)) {
			orderBy = defaultOrderBy;
		}
		String direction = request.getParameter(APIConstants.PARAM_NAME_DIRECTION);
		Sort.Direction d = Sort.Direction.DESC;
		if ("ASC".equalsIgnoreCase(direction)) {
			d = Sort.Direction.ASC;
		}
		Sort st = new Sort(d, orderBy);

		return new PageRequest(offset / limit, limit, st);
	}

	protected PageRequest getPageRequestWithoutLimit(HttpServletRequest request) {
		int offset = str2Int(request.getParameter(APIConstants.PARAM_NAME_OFFSET), 0);
		int limit = str2Int(request.getParameter(APIConstants.PARAM_NAME_LIMIT), APIConstants.DEFAULT_LIMIT);
		String orderBy = request.getParameter(APIConstants.PARAM_NAME_ORDERBY);
		if (StringUtils.isBlank(orderBy)) {
			orderBy = "createTime";
		}
		String direction = request.getParameter(APIConstants.PARAM_NAME_DIRECTION);
		Sort.Direction d = Sort.Direction.DESC;
		if ("ASC".equalsIgnoreCase(direction)) {
			d = Sort.Direction.ASC;
		}
		Sort st = new Sort(d, orderBy);

		return new PageRequest(offset / limit, limit, st);
	}

	protected SearchCriteria getSearchCriteria(HttpServletRequest request) {
		SearchCriteria sc = new SearchCriteria();
		int offset = str2Int(request.getParameter(APIConstants.PARAM_NAME_OFFSET), 0);
		int limit = str2Int(request.getParameter(APIConstants.PARAM_NAME_LIMIT), APIConstants.DEFAULT_LIMIT);
		if (limit > 100) {
			limit = 100;
		}
		sc.setOffSet(offset);
		sc.setPageSize(limit);
		String orderBy = request.getParameter(APIConstants.PARAM_NAME_ORDERBY);
		if (StringUtils.isBlank(orderBy)) {
			orderBy = "createTime";
		}
		sc.setOrderByField(orderBy);
		String direction = request.getParameter(APIConstants.PARAM_NAME_DIRECTION);
		if ("ASC".equalsIgnoreCase(direction)) {
			sc.setOrderType("ASC");
		} else {
			sc.setOrderType("DESC");
		}
		return sc;
	}

	/**
	 * verifyOrderby
	 *
	 * @param request
	 *            HttpServletRequest
	 * @param entityClass
	 *            Class
	 */
	protected void verifyOrderby(HttpServletRequest request, Class entityClass) {
		String orderBy = request.getParameter(APIConstants.PARAM_NAME_ORDERBY);
		verifyOrderby(orderBy, entityClass);
	}

	/**
	 * verifyOrderby
	 *
	 * @param orderBy
	 *            String
	 * @param entityClass
	 *            Class
	 */
	protected void verifyOrderby(String orderBy, Class entityClass) {
		if (!StringUtils.isBlank(orderBy)) {
			Field[] fields = entityClass.getDeclaredFields();
			boolean valid = false;
			for (Field field : fields) {
				if (orderBy.equalsIgnoreCase(field.getName())) {
					valid = true;
					break;
				}
			}
			if (!valid) {
				throw new ApiException("global.invalid.param.orderby");
			}
		}
	}

	/**
	 * verifyOrgId
	 *
	 * @param orgId
	 *            String
	 * @param cachedOrgId
	 *            String
	 */
	protected void verifyOrgId(String orgId, String cachedOrgId) {
		if (!orgId.equals(cachedOrgId)) {
			throw new ApiException("apis.org.noorgfound");
		}
	}

	/**
	 * verifyOrgAndUser
	 *
	 * @param userId
	 *            String
	 * @param orgId
	 *            String
	 * @param request
	 *            HttpServletRequest
	 */
	protected void verifyOrgAndUser(String userId, String orgId, HttpServletRequest request) {
		String token = request.getHeader(APIConstants.HEADER_NAME_TOKEN);
		UserCacheInfo userCacheInfo = getUserCacheInfo(token);
		if (!orgId.equals(userCacheInfo.getOrgId())) {
			throw new ApiException("apis.org.noorgfound");
		}
		if (!userId.equals(userCacheInfo.getUserId())) {
			throw new ApiException("global.noprivilege");
		}
	}

	/**
	 * verifyOrgAndUser
	 *
	 * @param userId
	 *            String
	 * @param orgId
	 *            String
	 * @param userCacheInfo
	 *            UserCacheInfo
	 */
	protected void verifyOrgAndUser(String userId, String orgId, UserCacheInfo userCacheInfo) {
		if (!orgId.equals(userCacheInfo.getOrgId())) {
			throw new ApiException("apis.org.noorgfound");
		}
		if (!userId.equals(userCacheInfo.getUserId())) {
			throw new ApiException("global.noprivilege");
		}
	}

	/**
	 * isAdmin
	 *
	 * @param request
	 *            HttpServletRequest
	 * @return boolean
	 */
	protected boolean isAdmin(HttpServletRequest request) {
		String token = request.getHeader(APIConstants.HEADER_NAME_TOKEN);
		return Constants.ADMIN_ROLEID.equals(cacheService.getHashValue(RedisConstants.REDIS_NAME_TOKEN + token,
				RedisConstants.REDIS_ROLE_ID));
	}

	/**
	 * getFullName
	 *
	 * @param cachedUserId
	 *            String
	 * @return String
	 */
	protected String getFullName(String cachedUserId) {
		return cacheService.getHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + cachedUserId,
				RedisConstants.REDIS_NAME_USERFULLNAME);
	}

	/**
	 * getAvatar
	 *
	 * @param cachedUserId
	 *            String
	 * @return String
	 */
	protected String getAvatar(String cachedUserId) {
		return cacheService.getHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + cachedUserId,
				RedisConstants.REDIS_NAME_USERAVATAR);
	}

	protected String getCUserIndustryId(String cachedUserId) {
		return cacheService.getHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + cachedUserId,
				RedisConstants.REDIS_NAME_USERINDUSTRYID);
	}

	protected String getCUserFunctionId(String cachedUserId) {
		return cacheService.getHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + cachedUserId,
				RedisConstants.REDIS_NAME_USERFUNCTIONID);
	}

	protected String getCUserPositionId(String cachedUserId) {
		return cacheService.getHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + cachedUserId,
				RedisConstants.REDIS_NAME_USERPOSITIONID);
	}

	/**
	 * getUserFromList
	 *
	 * @param list
	 *            List
	 * @param userId
	 *            String
	 * @return OrgUser
	 */
	protected OrgUser getUserFromList(List<OrgUser> list, String userId) {
		OrgUser result = new OrgUser();
		for (OrgUser orgUser : list) {
			if (orgUser.getPid().equals(userId)) {
				result = orgUser;
				break;
			}
		}
		return result;
	}

	protected ConsumerUser getCUserFromList(List<ConsumerUser> list, String cuserId) {
		ConsumerUser result = new ConsumerUser();
		for (ConsumerUser cUser : list) {
			if (cUser.getPid().equals(cuserId)) {
				result = cUser;
				break;
			}
		}
		return result;
	}

	/**
	 * getLikeStr
	 *
	 * @param searchKey
	 *            String
	 * @return String
	 */
	protected String getLikeStr(String searchKey) {
		return "%" + searchKey + "%";
	}

	/**
	 * getSearchCriteria
	 *
	 * @param bean
	 *            SearchBean
	 * @return SearchCriteria
	 */
	protected SearchCriteria getSearchCriteria(SearchBean bean) {
		SearchCriteria sc = new SearchCriteria();
		if (bean != null) {
			BeanHelper.copyProperties(bean, sc);
		}
		return sc;
	}

	/**
	 * createConsumerUser
	 *
	 * @param orgUser
	 *            OrgUser
	 * @param channelId
	 *            String
	 * @return ConsumerUser
	 */
	protected ConsumerUser createConsumerUser(OrgUser orgUser, String channelId) {
		String cuid = orgUser.getConsumerUserId();
		if (StringUtils.isBlank(cuid) || cuid.length() < 36) {
			throw new ApiException("apis.orguser.consumerUserId.invalid");
		}
		ConsumerUser consumerUser = new ConsumerUser();
		consumerUser.setPid(orgUser.getConsumerUserId());
		consumerUser.setPassword(orgUser.getPassword());
		consumerUser.setEmail(orgUser.getEmail());
		consumerUser.setMobile(orgUser.getMobile());
		consumerUser.setFullName(orgUser.getFullName());
		consumerUser.setGender(orgUser.getGendar());
		consumerUser.setBirthday(orgUser.getBirthday());
		consumerUser.setSign(orgUser.getSign());
		consumerUser.setAvatar(orgUser.getImageUrl());
		consumerUser.setCreator(orgUser.getCreator());
		consumerUser.setCreateTime(orgUser.getCreateTime());
		consumerUser.setUpdater(orgUser.getUpdater());
		consumerUser.setUpdateTime(orgUser.getUpdateTime());
		consumerUser.setChannelId(channelId);
		return consumerUserService.create(consumerUser);
	}

	protected String getConsumerUserId(OrgUser entity) {
		return consumerUserService.getConsumerUserId(entity.getMobile(), entity.getEmail());
	}

	protected void checkAdminPrivilege(HttpServletRequest request) {
		String userId = getUserId(request);
		if (StringUtils.isBlank(userId)) {
			throw new ApiException("global.token.invalid");
		}
		String token = request.getHeader(APIConstants.HEADER_NAME_TOKEN);
		UserCacheInfo userCacheInfo = getUserCacheInfo(token);
		String roleId = userCacheInfo.getRoleId();
		boolean hasAdminPrivilege = userId.equals(userCacheInfo.getUserId()) && Constants.ADMIN_ROLEID.equals(roleId);
		if (!hasAdminPrivilege) {
			throw new ApiException("apis.author.nopermission.failed");
		}
	}

	/**
	 * saveUserAccountCache
	 *
	 * @param cuser
	 *            ConsumerUser
	 */
	@Async
	public void saveUserAccountCache(ConsumerUser cuser) {
		if (cuser != null) {
			CommonList<UserAccount> list = getUserAccountList(cuser);
			String jsonStr = BeanHelper.bean2Json(list);
			if (StringUtils.isNotBlank(jsonStr)) {
				String consumerUserId = cuser.getPid();
				cacheService.setHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + consumerUserId,
						RedisConstants.REDIS_NAME_USERACCOUNTLIST, jsonStr);
				String syncKey = RedisConstants.REDIS_NAME_MALLACCOUNTSYNCING + consumerUserId;
				if (!"1".equals(cacheService.getPropertyValueByPropertyName(syncKey))) {
					cacheService.setValue(syncKey, "1");
					cacheService.setExpireTime(syncKey, 60, TimeUnit.SECONDS);
					rabbitmqService.sendString(Constants.QUEUE_SYNCUSERACCOUNT, consumerUserId);
				}
			}
		}
	}

	private CommonList<UserAccount> getUserAccountList(ConsumerUser cuser) {
		UserAccount cuserAccount = new UserAccount();
		cuserAccount.setSourceId(Constants.INT_SOURCETYPE_LECAI);
		cuserAccount.setUserId(cuser.getPid());
		cuserAccount.setConsumerUserId(cuser.getPid());
		cuserAccount.setFullName(cuser.getNickName());
		cuserAccount.setAvatar(cuser.getAvatar());
		List<UserAccount> datas = new ArrayList<UserAccount>();
		CommonList<UserAccount> list = new CommonList<UserAccount>(datas);
		datas.add(cuserAccount);
		List<UserAccount> orgUserAccounts = orgUserService.getUserAccounts(cuser.getPid());
		if (orgUserAccounts != null && !orgUserAccounts.isEmpty()) {
			datas.addAll(orgUserAccounts);
		}
		return list;
	}

	protected UserAccount getUserAccount(String orgId, String consumerUserId) {
		String jsonStr = cacheService.getHashValue(RedisConstants.REDIS_NAME_UIDS_PROPERTY + consumerUserId,
				RedisConstants.REDIS_NAME_USERACCOUNTLIST);
		UserAccountList list = BeanHelper.json2Bean(jsonStr, UserAccountList.class);
		UserAccount userAccount = new UserAccount();
		if (list != null && list.getDatas() != null) {
			for (UserAccount ua : list.getDatas()) {
				if (orgId.equals(ua.getOrgId())) {
					userAccount = ua;
					break;
				}
			}
		}
		return userAccount;
	}

	/**
	 * generateNickName
	 *
	 * @param entity
	 *            ConsumerUser
	 */
	protected void generateNickName(ConsumerUser entity) {
		if (StringUtil.isBlank(entity.getNickName())) {
			Random r = new Random();
			String nickName;
			if (StringUtils.isNotBlank(entity.getEmail())) {
				nickName = "yxt_" + entity.getEmail().split("@")[0];
			} else if (StringUtils.isNotBlank(entity.getMobile())) {
				nickName = "yxt_"
						+ entity.getMobile().substring(entity.getMobile().length() - 7, entity.getMobile().length());
			} else {
				nickName = "yxt_" + r.nextInt(99999999);
			}
			while (consumerUserService.findByNickName(nickName) != null) {
				nickName = nickName + r.nextInt(10);
			}
			entity.setNickName(nickName);
		}
	}

	protected String solrQueryString(String keys) {
		if ("null".equals(keys) || StringUtils.isBlank(keys)) {
			return null;
		} else {
			return ClientUtils.escapeQueryChars(keys);
		}

	}

	protected OrgUser createOrgadmin4Cuser(String orgId, ConsumerUser cuser, String userAgent) {
		OrgUser admin = new OrgUser();
		admin.setEmail(cuser.getEmail());
		admin.setMobile(cuser.getMobile());
		admin.setPassword(cuser.getPassword());
		admin.setFullName(cuser.getFullName());
		admin.setOrgId(orgId);
		admin.setUserAgent(userAgent);
		String now = APIUtil.now();
		admin.setLastLoginTime(now);
		admin.setCreateTime(now);
		admin.setUpdateTime(now);
		admin.setCreator("");
		admin.setUpdater("");
		admin.setType(1);
		admin.setStatus(1);
		admin.setGendar(cuser.getGender());
		admin.setBirthday(cuser.getBirthday());
		admin.setSign(cuser.getSign());
		admin.setImageUrl(cuser.getAvatar());
		admin.setConsumerUserId(cuser.getPid());
		admin.setPoints(cuser.getPoints());
		admin = orgUserService.save(admin, Constants.ADMIN_ROLEID);
		return admin;
	}

	protected void createPwsConfig(Org entity) {
		OrgConfig orgConfig = new OrgConfig();
		orgConfig.setOrgId(entity.getPid());
		String now = APIUtil.now();
		orgConfig.setCreateTime(now);
		orgConfig.setUpdateTime(now);
		orgConfig.setCreator("");
		orgConfig.setUpdater("");
		orgConfig.setName(Constants.ORG_CONFIG_PWS);
		orgConfig.setJson(BeanHelper.bean2Json(new PasswordSecurity()));
		orgConfigService.save(orgConfig);
	}

	protected boolean isCuser(String token) {
		return StringUtils.isBlank(getOrgIdByToken(token));
	}

	protected String getTokenUserId(String token) {
		String userId;
		if (isCuser(token)) {
			userId = getConsumerUserId(token);
		} else {
			userId = getUserIdByToken(token);
		}
		return userId;
	}

	protected String getTokenUserId(HttpServletRequest request) {
		return getTokenUserId(request.getHeader(APIConstants.HEADER_NAME_TOKEN));
	}

	/**
	 * 
	 * @param request
	 */
	protected void authRequest(HttpServletRequest request) {
		String token = request.getHeader(Constants.HEADER_NAME_TOKEN);
		if (token == null) {
			throw new ApiException("global.token.empty");
		}
		if (!Constants.STARTFISH_TOKEN.equals(token)) {
			verifyToken(token);
		}
	}
}
