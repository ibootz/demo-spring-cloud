package com.yxt.oauth.security.cache;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.yxt.oauth.security.store.CacheTokenStore;

@Configuration
@PropertySource("classpath:config.properties")
public class CacheTokenConfig implements InitializingBean {
	protected final Log logger = LogFactory.getLog(this.getClass());
	public static final String CACHE_NAME = "Oauth";

	public static final String[] CACHE_NAMES = new String[] { "oauth:accessTokenCache", "oauth:authenticationToAccessTokenCache",
			"oauth:userNameToAccessTokenCache", "oauth:clientIdToAccessTokenCache", "oauth:refreshTokenCache",
			"oauth:accessTokenToRefreshTokenCache", "oauth:refreshTokenAuthenticationCache", "oauth:authenticationCache", "oauth:authorizationCodeCache" };

	@Value("${redis.hostName}")
	private String redisHostName;

	@Value("${redis.port}")
	private String redisPort;

	@Value("${redis.password}")
	private String redisPassword;

	/*** Full class name or 1 (default) : in memory, 2 redis */
	private String cacheToUse = "2";// TODO constants

	/** 1 InMemory, 2 : cache **/
	private String storeToUse = "2";

	private TokenStore tokenStore;

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName(redisHostName);
		factory.setPort(Integer.parseInt(redisPort));
		factory.setPassword(redisPassword);
		factory.setUsePool(true);
		return factory;
	}

	@Bean
	RedisTemplate<Object, Object> redisTemplate() {
		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(jedisConnectionFactory());		
		return redisTemplate;
	}

	@Bean
	CacheManager createSimpleCacheManager() {
		CacheManager cm = null;
		logger.info("Cache to use :" + cacheToUse);
		if ("2".equals(cacheToUse)) {
			RedisCacheManager rcm = new RedisCacheManager(redisTemplate());
			rcm.setDefaultExpiration(7200);
			boolean usePrefixes = true;
			rcm.setUsePrefix(usePrefixes);
			if (usePrefixes) {
				// seperator
				RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix("RedisCache");
				rcm.setCachePrefix(cachePrefix);
				logger.info("Cac ore :" + cachePrefix.prefix(CACHE_NAME));
			}
			// might not need this in spring 4
			for (int i = 0; i < CACHE_NAMES.length; i++) {
				Cache ca = rcm.getCache(CACHE_NAMES[i]);
				logger.info(i + CACHE_NAMES[i] + " " + ca);
			}
			cm = rcm;
		} else if ("1".equals(cacheToUse)) {
			// do below in default
			logger.info("-default below- :");
		} else {
			try {
				cm = (CacheManager) Class.forName(cacheToUse).newInstance();
			} catch (Throwable e) {
				logger.info("ERR cacheManager :" + e);
			}
		}
		if (cm == null) {
			SimpleCacheManager scm = new SimpleCacheManager();
			Collection<Cache> caches = new ArrayList();
			for (int i = 0; i < CACHE_NAMES.length; i++) {
				ConcurrentMapCache cmc = new ConcurrentMapCache(CACHE_NAMES[i]);
				caches.add(cmc);
			}
			scm.setCaches(caches);
			cm = scm;
		}
		logger.info("-CacheManager- :" + cm);
		return cm;
	}

	@Bean
	public TokenStore tokenStore() {
		if (tokenStore == null) {
			if ("1".equals(storeToUse)) {
				tokenStore = new InMemoryTokenStore();// std spring
			} else if ("2".equals(storeToUse)) {
				tokenStore = new CacheTokenStore();
			} else {
				try {
					tokenStore = (TokenStore) Class.forName(storeToUse).newInstance();
				} catch (Throwable e) {
					logger.info("ERR :" + e + "cache use :" + cacheToUse + " " + tokenStore);
				}
			}
			logger.info("Token use :" + storeToUse + " " + tokenStore);
		}
		return tokenStore;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("CacheConfig redisHost -:" + redisHostName + ":" + redisPort);

	}
}