package cn.learn.designpatterns.strategy;

/**
 * 打折策略接口
 *
 * @author shaoyijiong
 * @date 2018/12/25
 */

public interface DiscountStrategy {

  /**
   * 返回该用户的策略类型
   */
  String type();

  /**
   * 折扣后价格
   *
   * @param cost 折扣
   * @return 价格
   */
  double discount(double cost);
}
