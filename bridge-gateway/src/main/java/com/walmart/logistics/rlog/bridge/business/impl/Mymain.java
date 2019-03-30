package com.walmart.logistics.rlog.bridge.business.impl;

import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Mymain {
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
			//	"spring/bridgeAppContext.xml");
				"spring/ASNAppContext.xml");
		CamelContext camelContext = context.getBean("bridgeContext",
				CamelContext.class);
		camelContext.start();
		Thread.sleep(9 * 100000000);
		camelContext.stop();
	}

}
