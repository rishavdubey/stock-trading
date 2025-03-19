package com.example.stocktrading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAsync
public class StockTradingApplication {
	private static final Logger logger= LoggerFactory.getLogger(StockTradingApplication.class);

	public static void main(String[] args) {
		try{
			String currDir= System.getProperty("user.dir");
			logger.info("curr Dir :: {} ", currDir);
			System.setProperty("spring.config.location",currDir + "/Config/application.properties");
			SpringApplication.run(StockTradingApplication.class, args);
		}catch (Exception e){
			logger.error("Error While loading Config");
		}



	}


}
