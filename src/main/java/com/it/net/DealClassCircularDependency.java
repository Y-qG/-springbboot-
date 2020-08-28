package com.it.net;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;

public class DealClassCircularDependency {
	
	
	//探索spring循环依赖问题,getbean函数怎么处理循环依赖问题
	private Map<String,Object> earlymap=new HashMap();
	private Map<String,Object> SingletonMap=new HashMap();
	private Map<String,Class> BeanDefinitionMap=new HashMap<>();
	
	public DealClassCircularDependency(){
		BeanDefinitionMap.put(Monitor.class.getTypeName(), Monitor.class);
		BeanDefinitionMap.put(Teacher.class.getTypeName(), Teacher.class);
		BeanDefinitionMap.put(Student.class.getTypeName(), Student.class);
	}
	
	
	public Map<String, Object> getSingletonMap() {
		return SingletonMap;
	}
	
	

	//1.判断该类下是否有循环依赖的出现
	/* 
	 * 1.没有循环依赖的出现，如果不是BeanDefinitionMap中的类，就不启动循环依赖函数判断，如果是，则启动判断
	 * 2.过滤之后，只剩下beanDefinitionMap中的bean
	 * 
	 * 情况一：Monitor--Teacher--Student--Monitor
	 *   当半实例化工厂里没有这个私有变量的对象，就new Monitor()；放入earlyMap中，继续往里递归，
	 * 	   当earlymap.get(field.getType().getName())！=null时，判断成功，说明有Monitor，返回true，
	 *   earlymap.get(s1.getTypeName()), earlymap.get(field.getType().getName())将student里Monitor赋值，然后
	 *   返回出来，再加入SingletonMap中;
	 * 情况二：Monitor--Teacher--Student
	 *   当到达student层时，往下走BeanDefinitionMap.get(field.getType().getName())!=null已经不是BeanDefinitionMap中的
	 *   类了，返回false；在teacher层就earlymap.get(field.getType().getName() get不出来student，因为没有，就为空
	 * 		
	 */
	public boolean getBean(Class s1) {
		boolean i=false;
		Field[] declaredFields = s1.getDeclaredFields();
		if(SingletonMap.get(s1.getTypeName())!=null) {
			return i;
		}
		for (Field field : declaredFields) {
			field.setAccessible(true);
			//当拿到私有变量已经不是BeanDefinitionMap里的bean时，就跳过不做任何处理
			if(BeanDefinitionMap.get(field.getType().getName())!=null) {
			  if(earlymap.get(field.getType().getName())==null) {
				try {
					earlymap.put(s1.getTypeName(), s1.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i=getBean(field.getType());
				try {
					field.set(earlymap.get(s1.getTypeName()), earlymap.get(field.getType().getName()));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				SingletonMap.put(s1.getTypeName(), earlymap.get(s1.getTypeName()));
			  	}else {
			  		try {
						earlymap.put(s1.getTypeName(), s1.newInstance());
						field.set(earlymap.get(s1.getTypeName()), earlymap.get(field.getType().getName()));
			  		} catch (InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SingletonMap.put(s1.getTypeName(), earlymap.get(s1.getTypeName()));
			  		return true;
			  	}
			}else {
				if(earlymap.get(s1.getTypeName())==null) {
					try {
						earlymap.put(s1.getTypeName(), s1.newInstance());
					} catch (InstantiationException | IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return i;
	}
	
	
	public static void main(String[] args) {
		DealClassCircularDependency dcc=new DealClassCircularDependency();
		System.out.println(dcc.getBean(Monitor.class));
		Map<String, Object> singletonMap2 = dcc.getSingletonMap();
		System.out.println(dcc.getBean(Monitor.class));
		Teacher object = (Teacher) singletonMap2.get(Teacher.class.getTypeName());
		Student s=(Student) singletonMap2.get(Student.class.getTypeName());
		Monitor m=(Monitor) singletonMap2.get(Monitor.class.getTypeName());
		System.out.println(object.getSt()+" "+" "+"  "+m.getName());
	}
}
