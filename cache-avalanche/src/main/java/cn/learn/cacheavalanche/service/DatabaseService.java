package cn.learn.cacheavalanche.service;

import cn.learn.cacheavalanche.mapper.TicketMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 具体数据库操作实现.
 *
 * @author shaoyijiong
 * @date 2018/12/15
 */
@Repository
public class DatabaseService {

  private final TicketMapper mapper;

  @Autowired
  public DatabaseService(TicketMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * 去数据库查询指定车票剩余.
   */
  String queryFromDatabase(String ticketSeq) {
    return mapper.queryStock(ticketSeq);
  }
}
