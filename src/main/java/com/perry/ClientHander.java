package com.perry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/**
 * 客户端，后台任务调度
 */
import java.net.Socket;

import com.perry.http.HttpRequest;
import com.perry.http.HttpResponse;

public class ClientHander implements Runnable {
	private Socket socket;

	public ClientHander(Socket socket) {
		this.socket = socket;
	}

	/**
	 * 创建请求对象-----》创建响应对象---》分析和处理请求----》发出响应
	 */
	public void run() {
		
		InputStream is;
		try {
			// 创建请求对象,传入输入流解析请求
			is = socket.getInputStream();
			HttpRequest request = new HttpRequest(is);
			//创建响应对象
			OutputStream out=socket.getOutputStream();
			HttpResponse response=new HttpResponse(out);
			//设置响应对象
			Servlet servlet=new Servlet(request,response);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if(socket!=null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
