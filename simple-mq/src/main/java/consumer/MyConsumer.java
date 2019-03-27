package consumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author shaoyijiong
 * @date 2019/3/26
 */
public class MyConsumer {

  public static void produce(String message) throws IOException {
    Socket socket = new Socket(InetAddress.getLocalHost(), 2333);
    OutputStream outputStream = socket.getOutputStream();
    outputStream.write(message.getBytes());
    outputStream.flush();
    outputStream.close();
    socket.close();
  }

  public static String consumer() throws IOException {
    Socket socket = new Socket(InetAddress.getLocalHost(), 2333);
    try (PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(socket.getInputStream()))) {
      out.println("CONSUME");
      out.flush();
      return reader.readLine();
    }
  }

  public static void main(String[] args) throws IOException {
    produce("你好");
    //System.out.println(MyConsumer.consumer());
  }
}
