package javatools.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {
	
	private static final String Http_Url="http://localhost:8080/MMpay/cmcc/mmiap";

	public static void testPost(String xml) throws IOException {
		
		URL url = new URL(Http_Url);
		 /* 
	     * URL请求的类别分为二类,GET与POST请求。二者的区别在于：  
	     * a:) get请求可以获取静态页面，也可以把参数放在URL字串后面，传递给servlet，  
	     * b:) post与get的不同之处在于post的参数不是放在URL字串里面，而是放在http请求的正文内。 
	     */  
		HttpURLConnection  connection = (HttpURLConnection) url.openConnection();	
		// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在  
	    // http正文内，因此需要设为true, 默认情况下是false; 
		connection.setDoOutput(true);	
		connection.setDoInput(true);
		
		connection.setRequestMethod("POST");
		
		
		// 设定传送的内容类型是可序列化的java对象  
	    // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)  
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setRequestProperty("Accept", "application/xml");
		
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
		out.write(xml); //post的关键所在！
		out.flush();
		out.close();
		
		
		// 一旦发送成功，用以下方法就可以得到服务器的回应：
		StringBuilder totalBuffer = new StringBuilder();
		String temp = null;
		InputStream responStream = connection.getInputStream();
		// 传说中的三层包装阿！
		BufferedReader reader = new BufferedReader(new InputStreamReader(responStream));
		while ((temp = reader.readLine()) != null) {
			totalBuffer.append(temp + "\r\n");
		}
		reader.close();
		connection.disconnect();
		System.out.println(totalBuffer.toString());
		
		//----------------------------------  
	    /* 
	     * 总结： 
	     * HttpURLConnection的connect()函数，实际上只是建立了一个与服务器的tcp连接，并没有实际发送http请求。 
	     * 无论是post还是get，http请求实际上直到HttpURLConnection的getInputStream()这个函数里面才正式发送出去。 
	     *  
	     * 对HttpURLConnection对象的一切配置都必须要在connect()函数执行之前完成。 
	     * 而对outputStream的写操作，又必须要在inputStream的读操作之前。 
	     * 这些顺序实际上是由http请求的格式决定的。 
	     *  
	     * 在http头后面紧跟着的是http请求的正文，正文的内容是通过outputStream流写入的， 
	     * 实际上outputStream不是一个网络流，充其量是个字符串流，往里面写入的东西不会立即发送到网络， 
	     * 而是存在于内存缓冲区中，待outputStream流关闭时，根据输入的内容生成http正文。 
	     * 至此，http请求的东西已经全部准备就绪。在getInputStream()函数调用的时候，就会把准备好的http请求 
	     * 正式发送到服务器了，然后返回一个输入流，用于读取服务器对于此次http请求的返回信息。由于http 
	     * 请求在getInputStream的时候已经发送出去了（包括http头和正文），因此在getInputStream()函数 
	     * 之后对connection对象进行设置（对http头的信息进行修改）或者写入outputStream（对正文进行修改） 
	     * 都是没有意义的了，执行这些操作会导致异常的发生。 
	     *  
	     */  
	}

	public static void main(String[] args) throws IOException {
		testPost(getXmlTest());
	}

	
	public static String getXmlTest(){
		String str[] = {"<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
				"<SyncAppOrderReq xmlns=\"http://www.monternet.com/dsmp/schemas/\">",
					"<TransactionID>CSSP16122856</TransactionID>",
					"<MsgType>SyncAppOrderReq</MsgType>",
					"<Version>1.0.0</Version>",
					"<Send_Address>",
					"<DeviceType>200</DeviceType>",
					"<DeviceID>CSSP</DeviceID>",
					"</Send_Address>",
					"<Dest_Address>",
					"<DeviceType>400</DeviceType>",
					"<DeviceID>SPSYN</DeviceID>",
					"</Dest_Address>",
					"<OrderID>11130619144434998192</OrderID>",
					"<CheckID>0</CheckID>",
					"<ActionTime>20130619144435</ActionTime>",
					"<ActionID>1</ActionID>",
					"<MSISDN></MSISDN>",
					"<FeeMSISDN>ECAD2EVFADF3AE2A</FeeMSISDN>",
					"<AppID>300001489326</AppID>",
					"<PayCode>30000148932601</PayCode>",
					"<TradeID>L0IF7AF2J4L5IF1B</TradeID>",
					"<Price>100</Price>",
					"<TotalPrice>100</TotalPrice>",
					"<SubsNumb>1</SubsNumb>",
					"<SubsSeq>1</SubsSeq>",
					"<ChannelID>2000000032</ChannelID>",
					"<ExData></ExData>",
					"<OrderType>1</OrderType>",
					"<MD5Sign>ABCDEFGHIKDFIEJFLAKDJFSIDF</MD5Sign>",
					"</SyncAppOrderReq>",};
		String xml="";
		for(int i=0;i<str.length;i++){
			xml += str[i].trim();
		}
		return xml;
	}
}