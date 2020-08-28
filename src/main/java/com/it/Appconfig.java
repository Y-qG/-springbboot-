package com.it;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import com.it.net.Monitor;
import com.it.net.windowsResourceMonitoring;

@SpringBootApplication
@Import(windowsResourceMonitoring.class)
@ComponentScan("com.it.net")
@SpringBootConfiguration
public class Appconfig {
	
	
	public static void main(String[] args) {
		Monitor bean = SpringApplication.run(Appconfig.class).getBean(Monitor.class);
		//System.out.println(bean.getT());
	}

}
