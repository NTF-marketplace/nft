package com.api.nft.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig {

    @Bean
    fun redisClusterConfiguration(): RedisClusterConfiguration {
        val clusterNodes = listOf(
            "172.24.0.2:6379",
            "172.24.0.3:6379",
            "172.24.0.4:6379",
            "172.24.0.5:6379",
            "172.24.0.6:6379",
            "172.24.0.7:6379",
            "172.24.0.8:6379",
            "172.24.0.9:6379",
            "172.24.0.10:6379"
        )
        return RedisClusterConfiguration(clusterNodes)
    }

    @Bean
    fun lettuceConnectionFactory(redisClusterConfiguration: RedisClusterConfiguration): LettuceConnectionFactory {
        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(10))  // 타임아웃 설정
            .build()
        return LettuceConnectionFactory(redisClusterConfiguration, clientConfig)
    }

    @Bean
    fun reactiveRedisTemplate(lettuceConnectionFactory: LettuceConnectionFactory): ReactiveRedisTemplate<String, Any> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = GenericJackson2JsonRedisSerializer()

        val serializationContext = RedisSerializationContext.newSerializationContext<String, Any>()
            .key(keySerializer)
            .value(valueSerializer)
            .hashKey(keySerializer)
            .hashValue(valueSerializer)
            .build()

        return ReactiveRedisTemplate(lettuceConnectionFactory, serializationContext)
    }
}