package com.sf.wxc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan("com.sf.wxc.schedule")
public class WxclientApplication {

	public static void main(String[] args) {
		SpringApplication.run(WxclientApplication.class, args);
	}
}
