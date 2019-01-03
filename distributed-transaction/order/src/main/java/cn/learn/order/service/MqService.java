package cn.learn.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author shaoyijiong
 * @date 2019/1/3
 */
@Slf4j
@Service
public class MqService {

  private final RabbitTemplate rabbitTemplate;
  private final JdbcTemplate jdbcTemplate;
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired
  public MqService(RabbitTemplate rabbitTemplate,JdbcTemplate jdbcTemplate) {
    this.rabbitTemplate = rabbitTemplate;
    this.jdbcTemplate = jdbcTemplate;
  }

  @PostConstruct
  public void setup() {
    //消息发送完毕后 调用次方法ack代表发送是否成功
    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
      //ack 为true 表示MQ已经收到消息
      if (!ack) {
        return;
      }
      try {
        //ack 确认
        String sql = "update tb_message set status = 1 where message_id = ?";
        assert correlationData != null;
        int i = jdbcTemplate.update(sql, correlationData.getId());
        if (i != 1) {
          log.warn("本地消息修改状态不成功");
        }
      } catch (Exception e) {
        log.warn("修改本地消息时出现异常", e);
      }

    });
  }

  /**
   * 发送消息
   */
  void sendMsg(Map map) {
    String s = null;
    try {
      s = MAPPER.writeValueAsString(map);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    assert s != null;
    rabbitTemplate.convertAndSend("OrderExchange", "", s, new CorrelationData(
        (String) map.get("orderId")));
  }
}
