package mqserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 访问类
 *
 * @author shaoyijiong
 * @date 2019/3/26
 */
public class BrokerServer implements Runnable {

  public static final int SERVER_PORT = 2333;
  private final Socket socket;

  public BrokerServer(Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
      while (true) {
        String s = reader.readLine();
        if (s == null) {
          continue;
        }
        System.out.println("收到数据" + s);
        //CONSUME 表示要消费一条数据
        if ("CONSUME".equals(s)) {
          String message = Broker.consume();
          System.out.println(message);
          writer.flush();
        } else {
          //其他情况都表示生产消息放到消息队列中去
          Broker.produce(s);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 启动消息队列线程
   */
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
    while (true) {
      BrokerServer brokerServer = new BrokerServer(serverSocket.accept());
      new Thread(brokerServer).start();
    }
  }
}
