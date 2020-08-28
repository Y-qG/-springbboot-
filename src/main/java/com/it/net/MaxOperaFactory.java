package com.it.net;

import java.lang.management.ManagementFactory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import com.sun.management.OperatingSystemMXBean;
@Component
public class MaxOperaFactory implements FactoryBean<OperatingSystemMXBean> {

	@Override
	public OperatingSystemMXBean getObject() throws Exception {
		// TODO Auto-generated method stub
		return (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return OperatingSystemMXBean.class;
	}

}
