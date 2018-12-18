package cn.learn.distributedlock.utils;

import java.nio.charset.Charset;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;

/**
 * 自定义zookeeper序列化.
 *
 * @author shaoyijiong
 * @date 2018/12/18
 */
public class MyZkSerializer implements ZkSerializer {

  private Charset charset = Charset.forName("UTF-8");

  /**
   * 序列化.
   */
  @Override
  public byte[] serialize(Object o) throws ZkMarshallingError {
    return String.valueOf(o).getBytes(charset);
  }

  /**
   * 反序列化.
   */
  @Override
  public Object deserialize(byte[] bytes) throws ZkMarshallingError {
    return new String(bytes, charset);
  }
}
