package cn.learn.designpatterns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * 用户-> 下定单 -> 短信通知
 *
 * @author shaoyijiong
 * @date 2018/12/24
 */
@Service
public class OrderService {

  @Autowired
  public OrderService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * 伪代码
   */
  public void saveOrder() {
    //创建订单
    System.out.println("1. 订单创建成功");
    //短信通知
    System.out.println("2. 调用短信发送接口 -> 恭喜喜提羽绒被子");
    //需要增加一个微信通知  新增功能修改原来的类
    System.out.println("3. 微信通知");
  }


  private final ApplicationContext applicationContext;

  public void saveOrder2() {
    System.out.println("1. 创建订单");
    //发出一个订单创建事件 (spring 内部原理，同步模式->循环调用监听，异步多线程模式)
    applicationContext.publishEvent(new OrderEvent(""));
  }
}
