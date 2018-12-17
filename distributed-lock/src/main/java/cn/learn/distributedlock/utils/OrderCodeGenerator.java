package cn.learn.distributedlock.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 订单编号生成.
 *
 * @author shaoyijiong
 * @date 2018/12/16
 */
public class OrderCodeGenerator {

  private int i = 0;

  /**
   * 按照时间与自增长序号生成订单编号.
   */
  public String getOrderCode() {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
    return sdf.format(date) + ++i;
  }
}
