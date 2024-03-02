package com.atguigu.cloud.controller;

import cn.hutool.core.bean.BeanUtil;
import com.atguigu.cloud.entities.Pay;
import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import com.atguigu.cloud.resp.ReturnCodeEnum;
import com.atguigu.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@Tag(name = "支付微服务模块", description = "支付crud")
public class PayController {
    @Resource
    private PayService payService;

    @PostMapping("/pay/add")
    @Operation(summary = "新增", description = "新增支付流水方法,json串参数")
    public ResultData<String> addPay(@RequestBody Pay pay) {
        log.info(pay.toString());
        int result = payService.add(pay);
//        return "成功插入记录"+result;
        return ResultData.success("成功插入记录返回值" + result);
    }

    @DeleteMapping("/pay/del/{id}")
    @Operation(summary = "删除", description = "删除支付流水方法")
    public ResultData<Integer> deletePay(@PathVariable("id") Integer id) {
        int result = payService.delete(id);
        return ResultData.success(result);
    }

    @PutMapping("/pay/update")
    @Operation(summary = "修改", description = "修改支付流水方法")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO) {
        Pay pay = new Pay();
        BeanUtil.copyProperties(payDTO, pay);
        int result = payService.update(pay);
        return ResultData.success("成功修改记录" + result);
    }

    @GetMapping("/pay/get/{id}")
    @Operation(summary = "按id查看流水", description = "查看支付流水方法")
    public ResultData<Pay> getById(@PathVariable("id") Integer id) {
        if (id < 0) throw new RuntimeException("id不能为负数");
        try {
            TimeUnit.SECONDS.sleep(62);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }

    @GetMapping("/pay/getAll")
    @Operation(summary = "查看所有人流水", description = "查看支付流水方法")
    public ResultData<List<Pay>> getAll() {
        List<Pay> all = payService.getAll();
        return ResultData.success(all);
    }

    @GetMapping(value = "/pay/error")
    public ResultData<Integer> getPayError1() {
        Integer integer = Integer.valueOf(200);
        try {
            System.out.println("com in pay error test");
            int data = 10 / 0;
        } catch (Exception e) {
            e.printStackTrace();
            return ResultData.fail(ReturnCodeEnum.RC500.getCode(), e.getMessage());
        }
        return ResultData.success(integer);
    }

    @Value("${server.port}")
    public String port;
    @GetMapping("/pay/get/info")
    public String getInfoByConsul(@Value("${atguigu.info}") String atguiguinfo) {
        return "atguiguinfo" + atguiguinfo + "\t" + "port:" + port;
    }
}