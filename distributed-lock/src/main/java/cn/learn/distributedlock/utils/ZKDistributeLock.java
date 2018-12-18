package cn.learn.distributedlock.utils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

/**
 * 基于Zookeeper实现分布式锁.
 *
 * @author shaoyijiong
 * @date 2018/12/18
 */
@SuppressWarnings("ALL")
public class ZKDistributeLock implements Lock {

  private static final String HOST_PORT = "47.99.73.15:2181";

  private String lockPath;
  private ZkClient client;

  public ZKDistributeLock(String lockPath) {
    super();
    this.lockPath = lockPath;
    client = new ZkClient(HOST_PORT);
    client.setZkSerializer(new MyZkSerializer());
  }

  @Override
  public void lock() {
    //如果获取不到锁 阻塞等待
    if (!tryLock()) {
      //阻塞自己
      waitForLock();
      //再次尝试
      lock();
    }
  }

  /**
   * 创建一个watch 来监听节点变化 判断该节点是否被删除 -> 被删除的化就能再次尝试获得锁
   */
  private void waitForLock() {
    CountDownLatch countDownLatch = new CountDownLatch(1);

    IZkDataListener listener = new IZkDataListener() {
      @Override
      public void handleDataChange(String path, Object o) throws Exception {
        System.out.println("节点数据变化，路径：" + path + "===数据：" + o);
      }

      @Override
      public void handleDataDeleted(String path) throws Exception {
        System.out.println("节点被删除，路径：" + path);
        //节点被删除 不再阻塞自己 让该线程去争抢这把锁
        countDownLatch.countDown();
      }
    };
    client.subscribeDataChanges(lockPath, listener);
    if (client.exists(lockPath)) {
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    client.unsubscribeDataChanges(lockPath, listener);
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {

  }

  @Override
  public boolean tryLock() {
    //不会阻塞
    try {
      client.createEphemeral(lockPath);
    } catch (ZkNodeExistsException e) {
      return false;
    }
    return true;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public void unlock() {
    client.delete(lockPath);
  }

  @Override
  public Condition newCondition() {
    return null;
  }
}
