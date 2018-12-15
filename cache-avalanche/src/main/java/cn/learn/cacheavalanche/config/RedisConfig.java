package cn.learn.cacheavalanche.config;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis的config. ConditionalOnClass 在导入类redis相关依赖才会生效 使用fastJson的方式进行序列化
 *
 * @author shaoyijiong
 * @date 2018/7/27
 */
@EnableCaching
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

  private static Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
      new Jackson2JsonRedisSerializer<>(Object.class);

  static {
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
  }

  /**
   * 自带序列化.
   */
  @Bean("stringRedisTemplate")
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
    StringRedisTemplate template = new StringRedisTemplate();
    template.setConnectionFactory(redisConnectionFactory);
    return template;
  }


  /**
   * 使用Jackson序列化redis存储. 使用jackson序列化,自动转型
   */
  @Bean(name = "redisTemplate")
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {

    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.afterPropertiesSet();
    //key序列化
    StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    template.setKeySerializer(stringRedisSerializer);
    template.setHashKeySerializer(stringRedisSerializer);

    //value序列化
    template.setValueSerializer(jackson2JsonRedisSerializer);
    template.setHashValueSerializer(jackson2JsonRedisSerializer);
    return template;
  }

  /**
   * 修改SpringBoot默认的redisCache配置,这里设置了序列化方式和缓存失效的时间.
   */
  @Bean
  public RedisCacheConfiguration redisCacheConfiguration() {
    RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
    return redisCacheConfiguration.serializeValuesWith(
        RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
        .entryTtl(Duration.ofDays(30));
  }
}
