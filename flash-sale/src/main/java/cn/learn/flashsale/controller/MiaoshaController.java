package cn.learn.flashsale.controller;

import cn.learn.flashsale.service.MiaoshaService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
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
@SuppressWarnings("ALL")
@Controller
public class MiaoshaController {

  private final StringRedisTemplate redisTemplate;
  private final MiaoshaService miaoshaService;

  @Autowired
  public MiaoshaController(MiaoshaService miaoshaService, StringRedisTemplate redisTemplate) {
    this.miaoshaService = miaoshaService;
    this.redisTemplate = redisTemplate;
  }

  @RequestMapping("index")
  public String index() {
    return "index.html";
  }

  @RequestMapping("miaosha")
  @ResponseBody
  public Object getUserInfo(String goodsCode, String userId) {
    final boolean isSuccess = redisTemplate.execute(new RedisCallback<Boolean>() {
      @Override
      public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
        //不要两个操作 setnx setex ,一定要用组合命令
        return connection
            .set(userId.getBytes(), goodsCode.getBytes(), Expiration.milliseconds(10000L),
                SetOption.SET_IF_ABSENT);
      }
    });
    if (isSuccess) {
      return miaoshaService.buy(goodsCode, userId) ? "秒杀成功" : "秒杀失败";
    } else {
      return "操作频率过高" + userId;
    }
  }
}
