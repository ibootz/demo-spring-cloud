package top.bootz.security.security.code;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;

public class CacheAuthorizationCodeService extends RandomValueAuthorizationCodeServices implements InitializingBean {

	protected final ConcurrentHashMap<String, OAuth2Authentication> authorizationCodeStore = new ConcurrentHashMap<String, OAuth2Authentication>();

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private CacheManager cacheManager = null;

	private Cache authorizationCodeCahce;

	@Override
	protected void store(String code, OAuth2Authentication authentication) {
		this.authorizationCodeCahce.put(code, authentication);
	}

	@Override
	public OAuth2Authentication remove(String code) {
		if (this.authorizationCodeCahce.get(code) == null) {
			return null;
		}
		OAuth2Authentication auth = (OAuth2Authentication) (this.authorizationCodeCahce.get(code).get());
		authorizationCodeCahce.evict(code);
		return auth;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			logger.info("CacheStore afterPropertiesSet :" + cacheManager);
			authorizationCodeCahce = cacheManager.getCache("authorizationCodeCache");
		} catch (Throwable e) {
			logger.info("ERR " + e);
			e.printStackTrace();
		}

	}
}
