package com.it.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Teacher {
	
	
	@Autowired
	private Student St;

	public Student getSt() {
		return St;
	}

	public void setSt(Student st) {
		St = st;
	}
	
	
	
	

}
