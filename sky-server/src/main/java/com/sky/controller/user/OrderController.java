package com.sky.controller.user;

import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户下单相关接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> order(@RequestBody OrdersSubmitDTO ordersSubmitDTO)
    {
        OrderSubmitVO orderSubmitVO = orderService.order(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        log.info("模拟交易成功");
        return Result.success(orderPaymentVO);
    }

    /**
     * 查看历史订单方法
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> history(int page,int pageSize,Integer status)
    {
        PageResult pageResult= orderService.pageQuery4User(page, pageSize, status);
        return Result.success(pageResult);
    }
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("订单详情查询")
    public Result<OrderVO> details(@PathVariable("id") Long id)
    {
        OrderVO orderVO = orderService.details(id);
        return Result.success(orderVO);
    }
    /**
     * 订单取消
     * @param id
     * @return
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("订单取消")
    public Result cancel(@PathVariable("id")Long id)throws Exception
    {
        orderService.userCancelId(id);
        return Result.success();
    }
    /**
     * 再来一单
     *
     * @param id
     * @return
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable Long id) {
        orderService.repetition(id);
        return Result.success();
    }
    @GetMapping("/reminder/{id}")
    @ApiOperation("催单")
    public Result reminder(@PathVariable("id") Long id)
    {
        orderService.reminder(id);
        return Result.success();
    }
}
