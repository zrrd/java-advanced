import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * @author shaoyijiong
 * @date 2019/1/8
 */

public class A {
  private volatile int a = 0;
  private CountDownLatch countDownLatch = new CountDownLatch(1000);

  //不能保证线程安全
  @Test
  public void a() throws InterruptedException {
    for (int i = 0; i < 1000; i++) {
      new Thread(() -> {
        try {
          countDownLatch.await();
          a = a +1;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }).start();
      countDownLatch.countDown();
    }
    TimeUnit.SECONDS.sleep(10);
    System.out.println(a);

  }
}
