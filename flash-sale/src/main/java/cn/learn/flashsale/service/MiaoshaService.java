package cn.learn.flashsale.service;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * .
 *
 * @author shaoyijiong
 * @date 2018/12/19
 */
@Service
@SuppressWarnings("CheckStyle")
public class MiaoshaService {

  private final StringRedisTemplate redisTemplate;

  private final JdbcTemplate jdbcTemplate;

  private AtomicInteger count = new AtomicInteger(0);

  @PostConstruct
  public void init() {
    System.out.println("初始化完毕");
    //思路:redis list 结构 实现令牌桶的效果
    //真实环境  通过任务调度  异步加载  加载一次就够了
    for (int i = 0; i < 100; i++) {
      redisTemplate.opsForList().leftPush("token_list", String.valueOf(i));
    }
    System.out.println("100个token初始化完毕");
  }

  @Autowired
  public MiaoshaService(JdbcTemplate jdbcTemplate, StringRedisTemplate redisTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.redisTemplate = redisTemplate;
  }

  @Transactional(rollbackFor = Throwable.class)
  public boolean buy(String goodCode, String userId) {

    //令牌桶
    //获取令牌
    String token = redisTemplate.opsForList().rightPop("token_list");
    if (token == null) {
      System.out.println("该商品已经秒杀完毕" + userId);
      return false;
    }

    //数据库商品数量 减1
    System.out.println("数据库操作:" + count.incrementAndGet());
    String sql = "update tb_miaosha set goods_num = goods_num - 1 where goods_code = '"
        + goodCode + "' and goods_num - 1 >0";
    int update = jdbcTemplate.update(sql);
    if (update != 1) {
      //代表秒杀失败
      return false;
    }

    String insertSql = "insert into tb_records(goods_code,user_id) values ('" + goodCode + "','"
        + userId + "')";
    int insertCount = jdbcTemplate.update(insertSql);
    if (insertCount != 1) {
      return false;
    }
    return true;
  }
}
