package cn.learn.frontbackend.controller;

import cn.learn.frontbackend.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shaoyijiong
 * @date 2019/1/31
 */
@RequestMapping("user")
@RestController
public class UserController {

  /**
   * 获取用户相关数据
   *
   * @return 返回结果
   */
  @GetMapping("list")
  public Object list(@CookieValue("token") String token) {
    //正式开放中使用aop filter拦截器统一拦截校验
    try {
      Claims claims = JwtUtils.parserToken(token);
      String userName = claims.get("userName", String.class);
      return "success:" + userName + System.currentTimeMillis();
    } catch (Exception e) {
      return "error" + e.getMessage();
    }
  }

  @GetMapping("login")
  public String login(String name, String password) {
    if ("aa".equals(name) && "123".equals(password)) {
      Claims claims = new DefaultClaims();
      claims.put("userName", name);
      return JwtUtils.generateToken(claims, 30);
    }
    return "";
  }
}
