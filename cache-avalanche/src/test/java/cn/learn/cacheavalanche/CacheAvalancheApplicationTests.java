package cn.learn.cacheavalanche;

import cn.learn.cacheavalanche.service.TicketService;
import cn.learn.cacheavalanche.service.TicketService2;
import java.util.concurrent.CountDownLatch;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheAvalancheApplicationTests {

  @Autowired
  private TicketService ticketService;

  @Autowired
  private TicketService2 ticketService2;

  private static final String TICKET_SEQ = "G296";
  private static final int THREAD_NUM = 1000;
  private CountDownLatch countDownLatch = new CountDownLatch(THREAD_NUM);

  @Test
  public void contextLoads() {
  }


  /**
   * 模拟高并发场景
   */
  @Test
  public void queryStock() throws InterruptedException {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start("开始测试");

    //缓存过期以后  并发请求都从数据库查询
    Thread[] threads = new Thread[THREAD_NUM];
    for (int i = 0; i < THREAD_NUM; i++) {
      Thread thread = new Thread(() -> {
        try {
          //等待countDownLatch 为0 再运行后续的代码
          countDownLatch.await();
          ticketService.queryTicketStock(TICKET_SEQ);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      });
      threads[i] = thread;
      thread.start();
      //计数器 减一 表示该线程已经准备好了
      countDownLatch.countDown();
    }

    //等待所有线程结束
    for (Thread thread : threads) {
      thread.join();
    }

    stopWatch.stop();
    System.out.println(stopWatch.getTotalTimeMillis());

  }
}

