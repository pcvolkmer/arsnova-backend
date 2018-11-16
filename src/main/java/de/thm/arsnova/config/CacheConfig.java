package de.thm.arsnova.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class CacheConfig {

	@Value("${cache.redis.host:'localhost'}") private String redisHost;
	@Value("${cache.redis.port:6379}") private int redisPort;

	@Bean
	@ConditionalOnExpression("'${cache.type}' == 'redis'")
	public LettuceConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisHost, redisPort);
	}

	@Bean(name = "cacheManager")
	@ConditionalOnExpression("'${cache.type}' == 'redis'")
	@Primary
	public CacheManager redisCacheManager() {
		RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
		configuration.entryTtl(Duration.ofMinutes(5));
		return RedisCacheManager.builder(redisConnectionFactory()).cacheDefaults(configuration).build();
	}
	
	@Bean(name = "cacheManager")
	@ConditionalOnMissingBean(CacheManager.class)
	public CacheManager concurrentMapCacheManager() {
		return new ConcurrentMapCacheManager();
	}

}
