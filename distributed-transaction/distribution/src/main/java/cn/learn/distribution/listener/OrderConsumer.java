package cn.learn.distribution.listener;

import cn.learn.distribution.service.DistributionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 订单消费者
 *
 * @author shaoyijiong
 * @date 2019/1/3
 */
@Component
@Slf4j
public class OrderConsumer {

  private final JdbcTemplate jdbcTemplate;
  private final DistributionService distributionService;
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired
  public OrderConsumer(DistributionService distributionService, JdbcTemplate jdbcTemplate) {
    this.distributionService = distributionService;
    this.jdbcTemplate = jdbcTemplate;
  }

  @SuppressWarnings("CheckStyle")
  @RabbitListener(queues = "OrderQueue")
  public void messageConsumer(String message, Channel channel,
      @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
    try {
      log.info("收到MQ中的消息" + message);
      HashMap<String, String> map = MAPPER.readValue(message, HashMap.class);
      //业务操作  同一数据不能处理两次 根据业务去重 判断数据库中是否存在(redis)
      String orderId = map.get("orderId");
      String sql = "SELECT * FROM tb_distribution WHERE order_id = ?";
      List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, orderId);
      if (list.size() == 0) {
        //分配一个外卖小哥
        distributionService.dispatch(orderId);
      } else {
        log.info("已经消费过了");
      }

      //ack 告诉mq  我已经收到了
      channel.basicAck(tag, false);
    } catch (Exception e) {
      e.printStackTrace();
      //异常请客： 根据需要 重发丢弃
      //重发一定次数后 丢弃  日志管理
      channel.basicNack(tag, false, true);
      //通过人工干预
    }

  }

}
