package cn.learn.designpatterns.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * spring 中的redis template 模板方法的使用
 *
 * @author shaoyijiong
 * @date 2018/12/25
 */
@Service
public class RedisTemplateA {

  private final RedisTemplate redisTemplate;

  @Autowired
  public RedisTemplateA(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 通过匿名内部类 实现execute 中与redis操作最关键的部分
   */
  public void intro() {
    redisTemplate.execute(new RedisCallback<Boolean>() {
      @Override
      public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
        connection.set(null, null);
        return null;
      }
    });
  }
}
