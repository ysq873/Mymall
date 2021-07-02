package com.yyyysq.mall_order_service;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableDubbo
@MapperScan("com.yyyysq.mall_order_service.dao")
@SpringBootApplication
public class MallOrderServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(MallOrderServiceApplication.class, args);
	}

}
