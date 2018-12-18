package cn.learn.distributedlock.utils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

/**
 * 基于Zookeeper实现分布式锁. 改进版
 *
 * @author shaoyijiong
 * @date 2018/12/18
 */
@SuppressWarnings("ALL")
public class ZKDistributeImproveLock implements Lock {

  private static final String HOST_PORT = "47.99.73.15:2181";

  private String lockPath;
  private ZkClient client;
  private ThreadLocal<String> currentPath = new ThreadLocal<>();
  private ThreadLocal<String> beforePath = new ThreadLocal<>();

  public ZKDistributeImproveLock(String lockPath) {
    super();
    this.lockPath = lockPath;
    client = new ZkClient(HOST_PORT);
    client.setZkSerializer(new MyZkSerializer());
    if (!this.client.exists(lockPath)) {
      try {
        client.createPersistent(lockPath);
      } catch (ZkNodeExistsException e) {

      }
    }
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
    client.subscribeDataChanges(beforePath.get(), listener);
    if (client.exists(beforePath.get())) {
      try {
        countDownLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    client.unsubscribeDataChanges(beforePath.get(), listener);
  }

  @Override
  public void lockInterruptibly() throws InterruptedException {

  }

  @Override
  public boolean tryLock() {
    if (currentPath.get() == null) {
      //创建临时顺序节点
      currentPath.set(client.createEphemeralSequential(lockPath + "/", "aaa"));
    }
    // 0001 0002 类似的
    List<String> children = client.getChildren(lockPath);
    Collections.sort(children);
    //判断当前节点是最小的节点
    if (currentPath.get().equals(lockPath + "/" + children.get(0))) {
      return true;
    } else {
      //取前一个
      //取得字节的索引号                      去除父路径
      int curIndex = children.indexOf(currentPath.get().substring(lockPath.length() + 1));
      beforePath.set(lockPath + "/" + children.get(curIndex - 1));
    }
    return false;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public void unlock() {
    client.delete(currentPath.get());
    currentPath.remove();
    beforePath.remove();
  }

  @Override
  public Condition newCondition() {
    return null;
  }
}
