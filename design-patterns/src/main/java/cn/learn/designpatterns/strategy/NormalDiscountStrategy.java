package cn.learn.designpatterns.strategy;

import org.springframework.stereotype.Component;

/**
 * 普通用户价格
 *
 * @author shaoyijiong
 * @date 2018/12/25
 */
@Component
public class NormalDiscountStrategy implements DiscountStrategy {

  @Override
  public String type() {
    return "normal";
  }

  @Override
  public double discount(double cost) {
    return cost;
  }
}
