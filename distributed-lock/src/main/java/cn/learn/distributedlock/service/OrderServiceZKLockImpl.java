package cn.learn.distributedlock.service;

import cn.learn.distributedlock.utils.OrderCodeGenerator;
import cn.learn.distributedlock.utils.ZKDistributeImproveLock;
import cn.learn.distributedlock.utils.ZKDistributeLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.springframework.stereotype.Service;

/**
 * 订单生成类1. 基于zookeeper分布式锁
 *
 * @author shaoyijiong
 * @date 2018/12/16
 */
public class OrderServiceZKLockImpl implements OrderService {

  private OrderCodeGenerator ocg = new OrderCodeGenerator();

  private Lock lock1 = new ZKDistributeLock("/lock/orderId");
  private Lock lock = new ZKDistributeImproveLock("/lock");


  @SuppressWarnings("Duplicates")
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
