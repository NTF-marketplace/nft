package com.api.nft.config

import io.lettuce.core.cluster.ClusterClientOptions
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions
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
            "192.168.50.185:50667",
            "192.168.50.185:50668",
            "192.168.50.185:50669",
            "192.168.50.185:50670",
            "192.168.50.185:50671",
            "192.168.50.185:50672",

            // "localhost:49785",
            // "localhost:49783",
            // "localhost:49782",
            // "localhost:49786",
            // "localhost:49784",
            // "localhost:49778",
            // "localhost:49779",
            // "localhost:49780",
            // "localhost:49781"
        )

        val clusterConfig = RedisClusterConfiguration(clusterNodes)
        clusterConfig.setPassword("foobared") // 비밀번호 설정

        return clusterConfig
    }

    @Bean
    fun lettuceConnectionFactory(redisClusterConfiguration: RedisClusterConfiguration): LettuceConnectionFactory {
        val refreshOptions = ClusterTopologyRefreshOptions.builder()
            .enablePeriodicRefresh(true)
            .dynamicRefreshSources(true) // 동적 갱신 소스를 사용하도록 설정
            .build()

        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(10))
            .clientOptions(
                ClusterClientOptions.builder()
                    .topologyRefreshOptions(refreshOptions)
                    .build()
            )
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