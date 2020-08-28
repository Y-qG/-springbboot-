package com.it.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Student {
	
	@Autowired
	private String t;

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}



	
}
