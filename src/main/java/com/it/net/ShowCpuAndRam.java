package com.it.net;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShowCpuAndRam {
	
	@Autowired
	private windowsResourceMonitoring wrm;
	
	@RequestMapping("/show")
	@ResponseBody
	public Map ShowCpu() {
		Map m=new HashMap();
		m.put("Cpu", wrm.getCpuRatioForWindows());
		m.put("Ram", wrm.getProcessRAM());
		return m;
	}
	
	
    @RequestMapping("/show1")
    public String idx() {
        return "ShowCpu";
    }
	


}
