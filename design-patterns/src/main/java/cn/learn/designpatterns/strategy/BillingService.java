package cn.learn.designpatterns.strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 计费服务
 *
 * @author shaoyijiong
 * @date 2018/12/25
 */
@Service
public class BillingService {

  private Map<String, DiscountStrategy> map = new HashMap<>();

  /**
   * 拿到所有策略对象  通过Spring注入
   */
  @Autowired
  public BillingService(List<DiscountStrategy> discountStrategies) {
    for (DiscountStrategy discountStrategy : discountStrategies) {
      map.put(discountStrategy.type(), discountStrategy);
    }
  }

  public double calulate(String type, double cost) {
    return map.get(type).discount(cost);
  }
}
