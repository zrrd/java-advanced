package cn.learn.flashsale.controller;

import cn.learn.flashsale.service.MiaoshaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * .
 *
 * @author shaoyijiong
 * @date 2018/12/19
 */
@Controller
public class MiaoshaController {

  private final MiaoshaService miaoshaService;

  @Autowired
  public MiaoshaController(MiaoshaService miaoshaService) {
    this.miaoshaService = miaoshaService;
  }

  @RequestMapping("index")
  public String index() {
    return "index.html";
  }

  @RequestMapping("miaosha")
  @ResponseBody
  public Object getUserInfo(String goodsCode, String userId) {
    return miaoshaService.buy(goodsCode, userId) ? "秒杀成功" : "秒杀失败";
  }
}
