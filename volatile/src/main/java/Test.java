import java.util.concurrent.TimeUnit;

/**
 * @author shaoyijiong
 * @date 2019/1/8
 */
@SuppressWarnings({"ALL", "AlibabaAvoidManuallyCreateThread"})
public class Test {

  private volatile static boolean is = true;
  private static final String a = "";

  public static void main(String[] args) {
    new Thread(() -> {
      int i = 0;
      while (Test.is) {
/*        synchronized (a){
          i++;
        }*/
        i++;
      }
      System.out.println(i);
    }).start();

    try {
      TimeUnit.SECONDS.sleep(2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    Test.is = false;
    System.out.println("被设置为false");
  }
}
