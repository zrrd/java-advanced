package cn.learn.frontbackend;

import cn.learn.frontbackend.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FrontBackendApplicationTests {

  @Test
  public void contextLoads() {
  }

  @Test
  public void testJwt() {
    Claims claims = new DefaultClaims();
    claims.put("name", "das");
    String token = JwtUtils.generateToken(claims, 30);
    claims = JwtUtils.parserToken(token);
    claims.get("name");
    claims.getExpiration();
    Jwt jwt = JwtUtils.parserTokenAll(token);
    System.out.println(claims);
  }
}

