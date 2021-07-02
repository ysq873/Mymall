package com.yyyysq.mall_goods_service;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
//import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
@MapperScan("com.yyyysq.mall_goods_service.dao")
//@EnableAutoDataSourceProxy
public class MallGoodsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MallGoodsServiceApplication.class, args);
	}

}
