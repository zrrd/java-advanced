package cn.learn.cacheavalanche.controller;

import cn.learn.cacheavalanche.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 *
 * @author shaoyijiong
 * @date 2018/12/15
 */
@RestController
public class TicketController {

  private final TicketService service;

  @Autowired
  public TicketController(TicketService service) {
    this.service = service;
  }

  @RequestMapping("stock")
  public Object getStock(String ticketSeq) {
    return service.queryTicketStock(ticketSeq);
  }
}
