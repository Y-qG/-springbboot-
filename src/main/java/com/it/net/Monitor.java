package com.it.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Monitor {
		
	@Autowired
	private Teacher name;

	public Teacher getName() {
		return name;
	}

	public void setName(Teacher name) {
		this.name = name;
	}
	
	
	
}
