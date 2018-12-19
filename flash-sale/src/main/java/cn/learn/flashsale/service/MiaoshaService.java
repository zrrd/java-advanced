package cn.learn.flashsale.service;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MiaoshaService {

  private final JdbcTemplate jdbcTemplate;

  private AtomicInteger count = new AtomicInteger(0);

  @Autowired
  public MiaoshaService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Transactional(rollbackFor = Throwable.class)
  public boolean buy(String goodCode, String userId) {
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
