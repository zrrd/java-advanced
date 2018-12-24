package cn.learn.designpatterns.service;

import org.springframework.context.ApplicationEvent;

/**
 * 订单事件
 *
 * @author shaoyijiong
 * @date 2018/12/24
 */
public class OrderEvent extends ApplicationEvent {

  public OrderEvent(Object source) {
    super(source);
  }
}
