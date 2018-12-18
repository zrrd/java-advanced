package cn.learn.distributedlock.service;

import cn.learn.distributedlock.utils.MyZkSerializer;
import java.util.concurrent.TimeUnit;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

/**
 * zookeeper实现数据的改变与监听.
 *
 * @author shaoyijiong
 * @date 2018/12/18
 */
@SuppressWarnings("ALL")
public class ZKWatchDemo {

  public static void main(String[] args) throws InterruptedException {
    ZkClient client = new ZkClient("47.99.73.15:2181");
    client.setZkSerializer(new MyZkSerializer());
    client.subscribeDataChanges("/lock/a", new IZkDataListener() {
      @Override
      public void handleDataChange(String path, Object o) throws Exception {
        System.out.println("节点数据变化，路径：" + path + "===数据：" + o);
      }

      @Override
      public void handleDataDeleted(String path) throws Exception {
        System.out.println("节点被删除，路径：" + path);
      }
    });

    //这里修改了数据后，上面就能监听倒数据变化
    client.writeData("/lock/a", "123456789");
    TimeUnit.HOURS.sleep(1);
  }
}
