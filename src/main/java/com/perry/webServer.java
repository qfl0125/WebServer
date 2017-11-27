package com.perry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class webServer {
	/**
	 * 申请端口 监听端口
	 */
	ServerSocket server;
	/**
	 * 线程池
	 */
	ExecutorService threadPool;

	/**
	 * 申请端口,创建线程池管理管理用户
	 * 
	 * @throws IOException
	 */
	public webServer() throws IOException {
		try {
			server = new ServerSocket(8088);
			threadPool = Executors.newFixedThreadPool(10);
		} catch (IOException e) {
			System.err.println("申请端口失败！");
			throw e;
		}
	}

	/**
	 * 监听端口， 创建客户端管理类 启动线程
	 * 
	 */
	private void start() {
		try {
			while (true) {
				Socket socket = server.accept();
				System.out.println("一个用户连接成功！");
				ClientHander hander = new ClientHander(socket);
				threadPool.execute(hander);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			webServer server = new webServer();
			server.start();
		} catch (IOException e) {
			System.err.println("服务器启动失败！");
			e.printStackTrace();
		}

	}

}
