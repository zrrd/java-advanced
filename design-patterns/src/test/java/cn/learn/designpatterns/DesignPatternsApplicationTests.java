package cn.learn.designpatterns;

import cn.learn.designpatterns.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DesignPatternsApplicationTests {

  @Autowired
  private OrderService orderService;

  @Test
  public void contextLoads() {
  }

  @Test
  public void test01() {
    orderService.saveOrder();
  }

  @Test
  public void test02() {
    orderService.saveOrder2();
  }
}

