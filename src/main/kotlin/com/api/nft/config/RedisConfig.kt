package com.api.nft.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
class RedisConfig {

    @Bean
    fun redisClusterConfiguration(): RedisClusterConfiguration {
        val clusterNodes = listOf(
            "172.24.0.2:6379",
            "172.24.0.3:6379",
            "172.24.0.4:6379"
        )
        return RedisClusterConfiguration(clusterNodes)
    }

    @Bean
    fun lettuceConnectionFactory(redisClusterConfiguration: RedisClusterConfiguration): LettuceConnectionFactory {
        return LettuceConnectionFactory(redisClusterConfiguration)
    }

    @Bean
    fun redisTemplate(lettuceConnectionFactory: LettuceConnectionFactory): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(lettuceConnectionFactory)
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()
        return redisTemplate
    }
}