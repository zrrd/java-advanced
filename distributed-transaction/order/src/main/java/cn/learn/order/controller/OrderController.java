package cn.learn.order.controller;

import cn.learn.order.service.OrderService;
import com.google.common.collect.ImmutableMap;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shaoyijiong
 * @date 2019/1/3
 */
@RestController("order")
public class OrderController {

  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping("save")
  public String saveOrder(String name, String content) {
    String orderId = UUID.randomUUID().toString();
    ImmutableMap<String, String> map = ImmutableMap
        .of("orderId", orderId, "name", name, "content", content);
    orderService.saveOrder(map);
    return "保存成功";
  }
}
