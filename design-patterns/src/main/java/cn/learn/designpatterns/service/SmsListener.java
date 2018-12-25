package cn.learn.designpatterns.service;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Service;

/**
 * 监听订单创建事件 发送短信
 *
 * @author shaoyijiong
 * @date 2018/12/24
 */
@Service
public class SmsListener implements SmartApplicationListener {

  /**
   * 多个监听 执行顺序 值越大 顺序越后
   */
  @Override
  public int getOrder() {
    return 0;
  }

  /**
   * 仅仅监听制定事件
   */
  @Override
  public boolean supportsEventType(Class<? extends ApplicationEvent> c) {
    return c == OrderEvent.class;
  }

  /**
   * 当事件发生时调用
   */
  @Override
  public void onApplicationEvent(ApplicationEvent applicationEvent) {
    System.out.println("2. 发送短信");
  }
}
