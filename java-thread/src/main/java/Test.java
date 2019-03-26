import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 多线程测试
 *
 * @author shaoyijiong
 * @date 2019/3/5
 */
public class Test {


  private String job1() throws InterruptedException {
    Thread.sleep(1000);
    return "job1";
  }

  private String job2() throws InterruptedException {
    Thread.sleep(2000);
    return "job2";
  }

  private String job3() throws InterruptedException {
    Thread.sleep(3000);
    return "job3";
  }

  /**
   * 模拟
   */

  public void client() throws ExecutionException, InterruptedException {
    long l = System.currentTimeMillis();

    //Runnable 与 Callable 的区别 方法名不同 返回值 可以抛出异常
    //还有紧密的联系
    Callable<String> callable = new Callable<String>() {

      @Override
      public String call() throws Exception {
        int i = 1 / 0;
        return Thread.currentThread().getName() + job1();
      }
    };
    Callable<String> callable1 = new Callable<String>() {

      @Override
      public String call() throws Exception {
        return Thread.currentThread().getName() + job2();
      }
    };
    Callable<String> callable2 = new Callable<String>() {

      @Override
      public String call() throws Exception {
        return Thread.currentThread().getName() + job3();
      }
    };

    //这么用多线程运行这个Callable业务封装
    //2. 用FutureTask封装callable
    FutureTask<String> task = new FutureTask<String>(callable);
    FutureTask<String> task1 = new FutureTask<String>(callable1);
    FutureTask<String> task2 = new FutureTask<String>(callable2);

    new Thread(task).start();
    new Thread(task1).start();
    new Thread(task2).start();

    //多线程运行FutureTask业务封装
    System.out.println(task.get() + task1.get() + task2.get());
    long l1 = System.currentTimeMillis();

    System.out.println(l1 - l);

  }

  public static void main(String[] args) throws ExecutionException, InterruptedException {
    Test test = new Test();
    test.client();
  }
}
