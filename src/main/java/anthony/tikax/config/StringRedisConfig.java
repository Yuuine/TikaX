package anthony.tikax.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Slf4j
@Configuration
public class StringRedisConfig {

    /**
     * 创建并配置StringRedisTemplate实例
     *
     * @param Factory Redis连接工厂，用于创建Redis连接
     * @return 配置完成的StringRedisTemplate实例
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory Factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(Factory);

        // 设置Redis键值的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.string());
        template.setHashKeySerializer(RedisSerializer.string());
        template.setHashValueSerializer(RedisSerializer.string());

        template.afterPropertiesSet();
        log.info("初始化RedisTemplate完成");
        return template;

    }

}
