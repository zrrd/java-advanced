package cn.learn.cacheavalanche.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 票务查询接口.
 *
 * @author shaoyijiong
 * @date 2018/12/15
 */
@Mapper
public interface TicketMapper {

  @Select("SELECT ticket_stock FROM tb_ticket WHERE ticket_seq = #{ticketSeq}")
  String queryStock(String ticketSeq);
}
