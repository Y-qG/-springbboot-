package com.it.net;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.lang.ProcessBuilder.Redirect;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.text.NumberFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.management.OperatingSystemMXBean;

public class windowsResourceMonitoring {
	
    private final int kb=1024;
    @Autowired
    private OperatingSystemMXBean osMxBean;
    
    private static final int CPUTIME = 500;
    
    private static final int PERCENT = 100;
    
    private static final int FAULTLENGTH = 10;

 
    
    
    //获取RAM的已使用资源
    public String getProcessRAM() { 
        // 总的物理内存   
        double totalMemorySize = osMxBean.getTotalPhysicalMemorySize() / kb;   
        // 剩余的物理内存   
        double freePhysicalMemorySize = osMxBean.getFreePhysicalMemorySize() / kb;   
        // 已使用的物理内存   
        double usedMemory = (totalMemorySize - freePhysicalMemorySize);   
        return Double.toString(usedMemory/totalMemorySize*100).substring(0,5);
        //return Double.toString(usedMemory);
    }

 
    //获取CPU使用率
  //获得cpu使用率
    public  String getCpuRatioForWindows()
    {
        String procCmd =
                System.getenv("windir")
                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
        try {
			String readCpu = readCpu(Runtime.getRuntime().exec(procCmd));
			return readCpu;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
    
    //读取cpu相关信息  自定义一种cpu占用模式，在这么多进程的情况下，cpu消耗资源为5%，以此对比，求出消耗比
    private String  readCpu(final Process proc)
    {
    	double CpuAlreadyExpend=0.05;
    	int i_1=52;
    	int i=0;
    	double CpuExpend=0.0;
			try {
				InputStreamReader ir = new InputStreamReader(proc.getInputStream());
		        LineNumberReader input = new LineNumberReader(ir);
		        //String line = input.readLine();
		        String L="";
		        while((L=input.readLine())!=null) {
		        	i++;
		        }
				CpuExpend=CpuAlreadyExpend*i/i_1;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				try {
					proc.getOutputStream().close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
         return Double.toString(CpuExpend*100).substring(0,4);
    }
}
