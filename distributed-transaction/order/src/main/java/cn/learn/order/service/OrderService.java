package cn.learn.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 订单业务
 *
 * @author shaoyijiong
 * @date 2019/1/3
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {

  private final JdbcTemplate jdbcTemplate;
  private final MqService mqService;
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired
  public OrderService(JdbcTemplate jdbcTemplate, MqService mqService) {
    this.jdbcTemplate = jdbcTemplate;
    this.mqService = mqService;
  }


  @SuppressWarnings("CheckStyle")
  public void saveOrder(Map<String,String> map) {
    //将订单信息保存倒数据库
    String sql = "INSERT INTO tb_order VALUES('" + map.get("orderId") + "','"
        + map.get("name") + "','" + map.get("content") + "',now())";
    int i = jdbcTemplate.update(sql);
    if (i != 1) {
      throw new RuntimeException("订单创建失败");
    }

    //将mq消息存储在本地
    saveLocalMsg(map);
    //往MQ发送消息
    mqService.sendMsg(map);
  }

  /**
   * 将往mq发送的消息存入数据库  定期扫描表 确保发送成功
   */
  private void saveLocalMsg(Map map) {
    String sql = "INSERT INTO tb_message VALUES (?,?,?,now())";
    String s = null;
    try {
      s = MAPPER.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    int i = jdbcTemplate.update(sql, map.get("orderId"), s, 0);
    if (i != 1) {
      throw new RuntimeException("数据库错误");
    }
  }
}
