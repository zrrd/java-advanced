package cn.learn.order;

import cn.learn.order.controller.OrderController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderApplicationTests {

  @Autowired
  private OrderController orderController;

  @Commit
  @Test
  public void contextLoads() {
    orderController.saveOrder("丁丁", "点了一份兰州拉面");
  }

}

