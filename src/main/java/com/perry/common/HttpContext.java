package com.perry.common;
//存储公共常量，减少内存消耗

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class HttpContext {
	public static final int CR = 13;
	public static final int LF = 10;
	// 状态码和描述
	public static final int STATUS_CODE_OK = 200;
	public static final String STATUS_REASON_OK = "OK";
	public static final int STATUS_CODE_NOTFOUND = 404;
	public static final String STATUS_REASON_NOTFOUND = "NOT FOUND";
	public static final int STATUS_CODE_ERROR = 500;
	public static final String STATUS_REASON_ERROR = "INTERNAT SERVER ERROR";
	// 存储状态码和描述
	public static Map<Integer, String> statusMap;
	static {
		statusMap=new HashMap<Integer, String>();
		statusMap.put(STATUS_CODE_OK, STATUS_REASON_OK);
		statusMap.put(STATUS_CODE_NOTFOUND, STATUS_REASON_NOTFOUND);
		statusMap.put(STATUS_CODE_ERROR, STATUS_REASON_ERROR);
	}

	/**
	 * 存储响应正文的文本类型 Content-type:text/html
	 */
	public static Map<String, String> contentTypeMap;
	static {
		initContentTypeMap();
	}

	private static void initContentTypeMap() {
		contentTypeMap=new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		try {
			Document doc=reader.read("conf"+File.separator+"web.xml");
			Element root=doc.getRootElement();
			Element typeMapping=root.element("type-mapping");
			List<Element> typeMappint=typeMapping.elements();
			for(Element e:typeMappint) {
				String key=e.attributeValue("ext");
				String value=e.attributeValue("type");
				contentTypeMap.put(key, value);
			}
			
		} catch (DocumentException e) {
			System.out.println("找不到文件！");
			e.printStackTrace();
		}	
	}
	//存储请求对应的处理类的映射
	public static Map<String, String> uriMap;
	static {
		initUriMap();
	}

	private static void initUriMap() {
		uriMap=new HashMap<String, String>();
		SAXReader reader=new SAXReader();
		try {
			Document doc=reader.read("conf"+File.separator+"web.xml");
			Element root=doc.getRootElement();
			Element mapping=root.element("web-app");
			List<Element> uriMappong=mapping.elements();
			for(Element i:uriMappong) {
				String url=i.elementText("url");
				String className=i.elementText("class");
				uriMap.put(url,className);
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	/*public static void main(String[] args) {
		System.out.println(uriMap);
	}*/

}
