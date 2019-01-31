package cn.learn.frontbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author shaoyijiong
 * @date 2019/1/31
 */
@Controller
public class IndexController {

  @GetMapping("index")
  public String index() {
    return "/index";
  }
}
