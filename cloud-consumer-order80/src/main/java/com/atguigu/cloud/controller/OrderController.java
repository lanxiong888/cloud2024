package com.atguigu.cloud.controller;

import com.atguigu.cloud.entities.PayDTO;
import com.atguigu.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class OrderController {
//    public static final String Payment_URL="http://localhost:8001";
    public static final String Payment_URL="http://cloud-payment-service";
    @Resource
    private RestTemplate restTemplate;
    @PostMapping("/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO){
        return restTemplate.postForObject(Payment_URL+"/pay/add",payDTO,ResultData.class);
    }
        @DeleteMapping("/consumer/pay/del/{id}")
        public ResultData delOrder(@PathVariable("id") Integer id){
        ResultData response = restTemplate.exchange(
                Payment_URL + "/pay/del/" + id,
                HttpMethod.DELETE,
                null,
                ResultData.class
        ).getBody();
        return response;
    }

    @PutMapping("/consumer/pay/update")
    public ResultData updateOrder(@RequestBody PayDTO payDTO){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 构建请求体
        HttpEntity<PayDTO> requestEntity = new HttpEntity<>(payDTO, headers);
        // 发送PUT请求
        ResultData responseEntity = restTemplate.exchange(
                Payment_URL + "/pay/update",
                HttpMethod.PUT,
                requestEntity,
                ResultData.class
        ).getBody();
        System.out.println(responseEntity);
        return responseEntity;
    }

    @GetMapping("/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id){
        return restTemplate.getForObject(Payment_URL+"/pay/get/"+id,ResultData.class,id);
    }


    @GetMapping(value = "/consumer/pay/get/info")
    private String getInfoByConsul()
    {
        return restTemplate.getForObject(Payment_URL + "/pay/get/info", String.class);
    }

    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/discovery")
    public String discovery()
    {
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");

        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }

        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }
}
