package cn.learn.distributedlock.service;

import cn.learn.distributedlock.utils.OrderCodeGenerator;
import org.springframework.stereotype.Service;

/**
 * 订单生成类1.
 *
 * @author shaoyijiong
 * @date 2018/12/16
 */
@Service
public class OrderServiceImpl implements OrderService {

  private OrderCodeGenerator ocg = new OrderCodeGenerator();

  @Override
  public void createOrder() {
    //取得订单编号
    String code = ocg.getOrderCode();
    System.out.println(Thread.currentThread().getName() + "====" + code);

    //省略业务代码....
  }
}
