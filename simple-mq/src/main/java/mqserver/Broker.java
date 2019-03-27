package mqserver;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息处理中心
 *
 * @author shaoyijiong
 * @date 2019/3/26
 */
public class Broker {

  /**
   * 保存消息的一个容器 无界阻塞队列
   */
  private static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

  public static void produce(String message) {
    if (queue.offer(message)) {
      System.out.println("消息投递成功:" + message);
    } else {
      System.out.println("消息投递失败");
    }
  }

  public static String consume() {
    String msg = queue.poll();
    if (msg != null) {
      System.out.println("从消息中心消费一条消息" + msg);
    } else {
      System.out.println("消息中心没有可供消费的消息");
    }
    return msg;
  }

}
