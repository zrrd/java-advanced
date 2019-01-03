package cn.learn.order.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author shaoyijiong
 * @date 2019/1/3
 */
@Component
public class Task {

  private final JdbcTemplate jdbcTemplate;
  private final MqService mqService;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public Task(JdbcTemplate jdbcTemplate, MqService mqService,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
    this.mqService = mqService;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Scheduled(fixedRate = 60000)
  private void checkMsgStatus() {
    //查询出所有超过10 分组未发的数据
    String sql = "SELECT message_id FROM tb_message WHERE `status` = 0 AND DATE_ADD(create_time,INTERVAL 10 MINUTE) < NOW()";
    List<String> ids = jdbcTemplate.queryForList(sql, String.class);

    if (ids.size() > 0) {

      Map<String, List> paramMap = Collections.singletonMap("ids", ids);
      //重新发送
      String sql1 = "SELECT order_id AS orderId,`name`,content FROM tb_order WHERE order_id IN (:ids)";
      List<Map<String, Object>> maps = namedParameterJdbcTemplate.queryForList(sql1, paramMap);
      maps.forEach(mqService::sendMsg);
    }

  }
}
