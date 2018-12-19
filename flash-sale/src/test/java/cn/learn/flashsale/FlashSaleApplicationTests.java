package cn.learn.flashsale;

import cn.learn.flashsale.controller.MiaoshaController;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FlashSaleApplicationTests {

  @Autowired
  private MiaoshaController controller;

  private static final int THREAD_SIZE = 1000;

  @Test
  public void testReq() throws InterruptedException {
    Thread[] threads = new Thread[THREAD_SIZE];
    CountDownLatch countDownLatch = new CountDownLatch(THREAD_SIZE);

    for (int i = 0; i < THREAD_SIZE; i++) {
      String userId = "abc_" + i;
      Thread thread = new Thread(() -> {
        try {
          countDownLatch.await();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(controller.getUserInfo("bike", userId));

      });
      threads[i] = thread;
      thread.start();
      countDownLatch.countDown();
    }

    for (Thread thread : threads) {
      thread.join();
    }
  }

  @Test
  public void contextLoads() {
  }

}

