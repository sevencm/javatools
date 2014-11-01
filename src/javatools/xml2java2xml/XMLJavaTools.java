package javatools.xml2java2xml;

import java.io.StringReader;
import java.io.StringWriter;

import javatools.http.HttpRequest;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XMLJavaTools {
	
	/**
	 * 将JAVA对象转为XML对象输出：注意:需使用jaXB的注解
	 * @param t
	 * @param context
	 * @return
	 * @throws JAXBException
	 */
	public static String java2XML(Object obj) throws JAXBException{		
		JAXBContext context =JAXBContext.newInstance(obj.getClass());
          Marshaller mar = context.createMarshaller();  
          mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
          mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8"); 
          StringWriter writer = new StringWriter();              
          mar.marshal(obj, writer);          
          return writer.toString();
	}
	
	/**
	 * xml转换为java对象
	 * @param xml
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2Java(String xml, Class<T> c) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(c);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}
	
	
	public static void main(String[] args) throws JAXBException {
		SyncAppOrderResp syncAppOrderResp = new SyncAppOrderResp();
		syncAppOrderResp.setMsgType("dddddddd");		
		System.out.println(java2XML(syncAppOrderResp));
		
		SyncAppOrderReq syOrderReq = xml2Java(HttpRequest.getXmlTest(), SyncAppOrderReq.class);
		System.out.println(syOrderReq.getAppID());
	}
}
