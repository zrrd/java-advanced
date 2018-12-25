package cn.learn.designpatterns.strategy;

import org.springframework.stereotype.Component;

/**
 * vip价格
 *
 * @author shaoyijiong
 * @date 2018/12/25
 */
@Component
public class VipDiscountStrategy implements DiscountStrategy {

  @Override
  public String type() {
    return "vip";
  }

  @Override
  public double discount(double cost) {
    return cost * 0.8;
  }
}
