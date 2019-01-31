package cn.learn.frontbackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author shaoyijiong
 * @date 2019/1/31
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class JwtUtils {

  /**
   * 密钥用于加密 与校验(对称加密)
   */
  private static final SecretKeySpec KEY_SPEC = new SecretKeySpec("123456".getBytes(),
      SignatureAlgorithm.HS512.getJcaName());
  /**
   * 载荷 用来记录用户信息
   */
  private static final String PAYLOAD = "name";

  /**
   * name 用户名 expireMinutes 过期时间单位 分钟
   */
  public static String generateToken(Claims claims, int expireMinutes) {
    return Jwts.builder()
        //设置载荷
        .setClaims(claims)
        .setExpiration(DateUtils.addMinutes(new Date(), expireMinutes))
        //通过 Key byte[] String加密  加密算法为RS256
        .signWith(SignatureAlgorithm.HS512, KEY_SPEC)
        .compact();
  }

  /**
   * 获得jwt中的声明
   */
  public static Claims parserToken(String token) {
    return Jwts.parser().setSigningKey(KEY_SPEC).parseClaimsJws(token).getBody();
  }

  public static Jwt parserTokenAll(String token) {
    return Jwts.parser().setSigningKey(KEY_SPEC).parse(token);
  }


}
