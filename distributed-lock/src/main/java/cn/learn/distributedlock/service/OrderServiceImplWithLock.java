package cn.learn.distributedlock.service;

import cn.learn.distributedlock.utils.OrderCodeGenerator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;

/**
 * 订单生成类2.
 * 加锁
 *
 * @author shaoyijiong
 * @date 2018/12/16
 */
@Service
public class OrderServiceImplWithLock implements OrderService {

  private OrderCodeGenerator ocg = new OrderCodeGenerator();

  private Lock lock = new ReentrantLock();

  @Override
  public void createOrder() {
    String code;
    //取得订单编号
    try {
      lock.lock();
      code = ocg.getOrderCode();
    } finally {
      lock.unlock();
    }
    System.out.println(Thread.currentThread().getName() + "====" + code);

    //省略业务代码....
  }
}
