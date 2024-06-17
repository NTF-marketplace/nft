package com.api.nft.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisClusterConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.connection.RedisPassword;
import io.lettuce.core.cluster.ClusterClientOptions
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
            "localhost:6379",
            "localhost:6380",
            "localhost:6381",
            "localhost:6382",
            "localhost:6383",
            "localhost:6384"
        )
        return RedisClusterConfiguration(clusterNodes)
    }

    @Bean
    fun lettuceConnectionFactory(redisClusterConfiguration: RedisClusterConfiguration): LettuceConnectionFactory {
        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(10))
            .clientOptions(
                ClusterClientOptions.builder()
                    .autoReconnect(true)
                    .pingBeforeActivateConnection(true)
                    .build()
            )            
            .build()
        redisClusterConfiguration.setPassword(RedisPassword.of("bitnami"))
        redisClusterConfiguration.setMaxRedirects(3)
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