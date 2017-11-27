package com.perry.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.text.Position.Bias;

import com.perry.common.HttpContext;

public class HttpResponse {
	private OutputStream out;
	//响应行：版本协议 状态码 状态描述
	private int status;//状态码
	/**
	 * 响应头：至少2个： T:Content-Type:test/html
	 * 				  L:Cpntent-length:7(个字节)
	 */
	private String contentType;
	private int contentLength=-1;
	
	//响应体
	private File entity;
	//set,get属性
	public HttpResponse(OutputStream out) {
		this.out=out;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	public File getEntity() {
		return entity;
	}
	public void setEntity(File entity) {
		this.entity = entity;
	}
	public OutputStream getOut() {
		return out;
	}
	//发送响应
	public void flush() {
		//发送响应行
		responseLine();
		//发送响应头
		responseHeader();
		//发送响应实体
		responseEntity();
	}
	//发送响应行
	private void responseLine() {
		String line="HTTP/1.1 "+getStatus()+" "+HttpContext.statusMap.get(getStatus());
		System.out.println("一行响应："+line);
		printLine(line);
	}
	//发送响应头
	private void responseHeader() {
		//数据类型
		String line="Content-Type:"+getContentType();
		System.out.println("一行响应："+line);
		printLine(line);
		//数据长度
		if(contentLength!=-1) {
			line="Content-Length:"+getContentLength();
			printLine(line);
			System.out.println("一行响应："+line);
		}
		//...
		
		//响应头结束标志，空行
		printLine("");
		
	}
	//发送响应实体
		private void responseEntity() {
			if(entity!=null) {
				System.out.println("发送文件！");
				BufferedInputStream bis=null;
				BufferedOutputStream bos=null;
				try {
					bis=new BufferedInputStream(new FileInputStream(entity));
					bos=new BufferedOutputStream(out);
					int b=-1;
					while((b=bis.read())!=-1) {
						bos.write(b);
					}
					bos.flush();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}finally {
					try {
						bis.close();
						bos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	
	//发送一行响应
	private void printLine(String line) {
		try {
			//可以设置编码:"ISO8559-1"欧州编码，都为一个字节，便于网络传输
			out.write(line.getBytes());
			//发送结尾标志
			out.write(HttpContext.CR);
			out.write(HttpContext.LF);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
