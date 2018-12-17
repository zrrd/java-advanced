package cn.learn.distributedlock;

import cn.learn.distributedlock.service.OrderService;
import cn.learn.distributedlock.service.OrderServiceImplWithLock;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistributedLockApplicationTests {

  @Qualifier("orderServiceImplWithLock")
  @Autowired
  private OrderService orderService;

  private static final int THREAD_NUM = 1000;
  private CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

  @Test
  public void contextLoads() {
  }


  @Test
  public void getCode() throws InterruptedException {
    Thread[] threads = new Thread[THREAD_NUM];

    for (int i = 0; i < THREAD_NUM; i++) {
      Thread thread = new Thread(()->{
        try {
          countDownLatch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        orderService.createOrder();
      });
      threads[i] = thread;
      thread.start();
      countDownLatch.countDown();
    }
    for (Thread t:threads) {
      t.join();
    }
  }


  /**
   * 模拟集群环境
   */
  @Test
  public void getCode2() throws InterruptedException {
    Thread[] threads = new Thread[THREAD_NUM];

    for (int i = 0; i < THREAD_NUM; i++) {
      Thread thread = new Thread(()->{
        //每个线程创建单独的实力  相当于20个tomcat
        OrderService orderService = new OrderServiceImplWithLock();
        try {
          countDownLatch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        orderService.createOrder();
      });
      threads[i] = thread;
      thread.start();
      countDownLatch.countDown();
    }
    for (Thread t:threads) {
      t.join();
    }
  }
}

