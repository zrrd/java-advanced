package cn.learn.cacheavalanche.service;

import java.util.concurrent.ConcurrentHashMap;
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
public class TicketService3 {

  private final DatabaseService databaseService;

  @Resource(name = "stringRedisTemplate")
  private StringRedisTemplate redisTemplate;

  @Autowired
  public TicketService3(DatabaseService databaseService) {
    this.databaseService = databaseService;
  }


  /**
   * 重建缓存的标记.  redis 等其他工具也能保存标记  实现细粒度的锁
   */
  private ConcurrentHashMap<String, String> maplock = new ConcurrentHashMap<>();

  /**
   * 查询车次余票.
   */
  @SuppressWarnings("Duplicates")
  public Object queryTicketStock(String ticketSeq) {
    //1.从redis中获取余票信息
    String value = redisTemplate.opsForValue().get(ticketSeq);
    if (value != null) {
      log.info(Thread.currentThread().getName() + "缓存中得到数据=======" + value);
      return value;
    }
    boolean lock = false;
    try {
      // 为null表示该锁没有被争抢  非null 表示其他锁已经抢到了 putIfAbsent 存在返回值 插入成功返回null
      // 1000 个线程并发 只有1个被抢到 999 个抢不到
      lock = maplock.putIfAbsent(ticketSeq, "true") == null;
      if (lock) {
        //拿到锁了 操作缓存重建
        //2.缓存中没有取得数据取数据库
        value = databaseService.queryFromDatabase(ticketSeq);
        log.info(Thread.currentThread().getName() + "数据库中得到数据=======" + value);

        //3.基于缓存120s过期时间
        final String v = value;
        redisTemplate.execute((RedisCallback<Object>) connection -> connection
            .setEx(ticketSeq.getBytes(), 120, v.getBytes()));
      } else {
        //没拿到锁的怎么办 -- 缓存降级
        //根据业务需要 降级

        //方案1 返回固定值
        value = "0";
        log.info("降级 返回固定值");

        //方案2 备份缓存  两个redis 操作缓存的时候双写 两个缓存都写入
      }
    } finally {
      if (lock) {
        maplock.remove(ticketSeq);
      }
    }

    return value;
  }
}
