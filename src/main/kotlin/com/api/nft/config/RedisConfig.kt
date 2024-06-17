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
            "10.10.148.169:6379",
            // "10.10.148.169:6380",
            // "10.10.148.169:6381",
            // "10.10.148.169:6382",
            // "10.10.148.169:6383",
            // "10.10.148.169:6384",
            // "10.10.148.169:6385",
            // "10.10.148.169:6386",
            // "10.10.148.169:6387"
        )
        return RedisClusterConfiguration(clusterNodes)
    }

    @Bean
    fun lettuceConnectionFactory(redisClusterConfiguration: RedisClusterConfiguration): LettuceConnectionFactory {
        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(10))
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