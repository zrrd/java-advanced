package cn.learn.distribution.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * 业务处理
 *
 * @author shaoyijiong
 * @date 2019/1/3
 */
@Component
public class DistributionService {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public DistributionService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @SuppressWarnings("CheckStyle")
  public void dispatch(String orderId) {
    String sql = "INSERT INTO tb_distribution VALUES(?,?,?,NOW())";
    int i = jdbcTemplate.update(sql, orderId, "小化", "送外卖了");
    if (i != 1) {
      throw new RuntimeException("插入数据有错");
    }
  }
}
