package com.perry;

import java.awt.Window.Type;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Provider.Service;

import com.perry.common.HttpContext;
import com.perry.http.HttpRequest;
import com.perry.http.HttpResponse;

//逻辑处理
public class Servlet {
	public HttpRequest request;
	public HttpResponse response;
	public Servlet() {
	}
	public Servlet(HttpRequest request, HttpResponse response) {
		this.request=request;
		this.response=response;
		System.out.println("来到Servlet");
		//页面处理和响应
		String url=request.getUri();
		if("/".equals(url)){
			url="/index.html";
		}
		File file=new File("webapp"+url);
		System.out.println("文件是否有："+file.exists());
		service(request, response);
		String className=null;
		//通过反射处理业务逻辑
		if(file.exists()) {
			response.setStatus(HttpContext.STATUS_CODE_OK);
			responseFlush(file);
		}else if((className=HttpContext.uriMap.get(url))!=null) {
			invoke(className,request,response);
		}else {
			file=new File("webapp"+File.separator+"404.html");
			response.setStatus(HttpContext.STATUS_CODE_NOTFOUND);
			responseFlush(file);
		}
		
		
	}
	
	/**
	 * 发送响应:如果是文件
	 * @param file
	 */
	private void responseFlush(File file) {
		String type=fileType(file);
		response.setContentType(HttpContext.contentTypeMap.get(type));
		response.setContentLength((int)file.length());
		response.setEntity(file);	
		response.flush();
	}
	
	private String fileType(File file) {
		String fileName=file.getName();
		int index=fileName.lastIndexOf(".");
		String type=fileName.substring(index+1);
		return type;
	}
	//利用反射处理逻辑业务
	private void invoke(String className, HttpRequest request2, HttpResponse response2) {
		
		try {
			//加载类
			Class cls=Class.forName(className);
			//创建无参对象
			Object obj=cls.newInstance();
			//创建有参方法对象
			Method method=cls.getDeclaredMethod("service", HttpRequest.class,HttpResponse.class);
			//调用方法
			method.invoke(obj, request,response);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public void  service(HttpRequest request, HttpResponse response) {
		
	}
}
