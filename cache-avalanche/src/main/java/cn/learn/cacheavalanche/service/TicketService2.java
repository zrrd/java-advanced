package cn.learn.cacheavalanche.service;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 加上一把锁,将异步化为同步.
 *
 * @author shaoyijiong
 * @date 2018/12/15
 */
@Slf4j
@Service
public class TicketService2 {

  private final DatabaseService databaseService;

  @Resource(name = "stringRedisTemplate")
  private StringRedisTemplate redisTemplate;

  @Autowired
  public TicketService2(DatabaseService databaseService) {
    this.databaseService = databaseService;
  }

  /**
   * 查询车次余票.
   */
  public Object queryTicketStock(String ticketSeq) {
    //1.从redis中获取余票信息
    String value = redisTemplate.opsForValue().get(ticketSeq);
    if (value != null) {
      log.info(Thread.currentThread().getName() + "缓存中得到数据=======" + value);
      return value;
    }

    //2.缓存中没有取得数据取数据库
    value = databaseService.queryFromDatabase(ticketSeq);
    log.info(Thread.currentThread().getName() + "数据库中得到数据=======" + value);

    //3.基于缓存120s过期时间
    final String v = value;
    redisTemplate.execute((RedisCallback<Object>) connection -> connection
        .setEx(ticketSeq.getBytes(), 120, v.getBytes()));
    return value;
  }
}
