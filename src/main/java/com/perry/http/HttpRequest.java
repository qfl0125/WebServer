package com.perry.http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
/**
 * 请求对象，解析请求
 */
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
	// 请求行 GET /index HTTP/1.1
	private String method;
	private String uri;
	public Map<String,String> getDataMap;
	public String getMethod() {
		return method;
	}



	public String getUri() {
		return uri;
	}



	public String getData(String key) {
		return getDataMap.get(key);
	}



	public String getProtocol() {
		return protocol;
	}



	public Map<String, String> getHeader() {
		return header;
	}



	public String getBody() {
		return body;
	}



	private String protocol;
	// 请求头 name:value的形式
	private Map<String, String> header;
	// 请求体
	private String body;

	public HttpRequest(InputStream is) {
		// 解析请求行
		parseRequestLine(is);
	
		//解析请求头
		parseRequestHeader(is);
		parseRequestBody(is);
		System.out.println("请求解析完毕！");
	}



	// 解析请求行
	private void parseRequestLine(InputStream is) {
			String line=null;
			if((line=readerLine(is))!=null&&line.length()>0) {
				System.out.println("请求行："+line+"123");
				String[] lines=line.split("\\s");
				this.method=lines[0];
				System.out.println("uri:"+lines[1]);
				parseUri(lines[1]);
				this.protocol=lines[2];
			}
	}
	//解析URI
	private void parseUri(String uri) {
		//System.out.println("url:"+string);
		int index=-1;
		System.out.println("indexUrl:"+uri.indexOf("?"));
		if((index=uri.indexOf("?"))!=-1) {
			this.uri=uri.substring(0,index);
			String data=uri.substring(index+1);
			if(data.trim().length()>0) {
				parseGETData(data);
				//System.out.println("date:"+data);
			}
		}else {
			this.uri=uri;
		}
		
	}
	//解析请求GET请求数据:name:value
	private void parseGETData(String data) {
		getDataMap=new HashMap<String, String>();
		String[] dataArr=data.split("&");
		for(String i:dataArr) {
			String[] iArr=i.split("=");
			if(iArr.length>0) {
				String key=iArr[0];
				String value=iArr[1];
				getDataMap.put(key, value);
			}
		}

	}

	//解析请求头
	private void parseRequestHeader(InputStream is) {
		String line=null;
		//System.out.println("解析请求头！");
		header=new HashMap<String, String>();
		int falg=-1;
		while(((line=readerLine(is))!=null)&&((falg=line.indexOf(":"))!=-1)) {
				System.out.println("请求头："+line);
				String key=line.substring(0,falg);
				String value=line.substring(falg+1);
				header.put(key, value);
			}
		//	System.out.println("请求头Map："+header);
	}
	
	////解析请求体
	
	private void parseRequestBody(InputStream is) {
		if((this.method)!=null&&"POST".equals(this.method.toUpperCase())) {
			parstPost(is);
		}
	}
	//POST解析
	private void parstPost(InputStream is) {
		//分析响应
		
		
	}
	//解析文件，存储临时文件：
	private void pareFile(InputStream is) {
		FileOutputStream fos=null;
		try {
			 fos=new FileOutputStream("tempFile/file");			
			int b=-1;
			while((b=is.read())!=-1) {
				fos.write(b);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// 读取一行请求,注意一行的结束标志CRLF（回车13，黄行10）
	private String readerLine(InputStream is) {
		StringBuffer line = new StringBuffer();

		try {
			int c1 = -1, c2 = -1;
			while ((c1 = is.read()) != -1) {
				if (c2 == 13 &&c1 == 10) {
					break;
				}
				c2 = c1;
				line.append((char) c1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line.toString().trim();
	}

}
