package com.Ric4.TimeSync;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

import sun.net.www.protocol.http.HttpURLConnection;

/**
 * 
 * @author abh1sh3k47
 *
 */
public class Main {

	
	static String GREENWICH_TIME_SERVER_URL = "http://wwp.greenwichmeantime.com/time/scripts/clock-8/x.php";

	
	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException 
	{ 
		if(args.length < 1)
			return;
	
		 HttpClient client = getHttpClient();
		 HttpGet get = new HttpGet(GREENWICH_TIME_SERVER_URL);
		 HttpResponse response = client.execute(get);
		 
		 Scanner sc = new Scanner(response.getEntity().getContent());
		 Double d = sc.nextDouble();
		 long currentTime = d.longValue();	 
		 Calendar c = Calendar.getInstance();
		 c.setTimeInMillis(currentTime);
		 long decimalDigit = new Double((d-currentTime)*100).longValue();
		 
		 if(args[0].contains("time"))
		 printSystemTime(c.getTime(), decimalDigit); 
		 else if(args[0].contains("date"))
		 printDate(c);
	}
	
	private static void printSystemTime(Date d, long decimalDigit) throws IOException, InterruptedException
	{
		System.out.print(d.getHours()+":"+d.getMinutes()+":"+d.getSeconds()+"."+decimalDigit);
	}
	
	private static void printDate(Calendar c)
	{
		System.out.println((c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR));
	}
	
	private static HttpClient getHttpClient()
	{
		DefaultHttpClient hc = new DefaultHttpClient();
		//HttpHost proxy = new HttpHost("127.0.0.1",8888);
		//hc.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);
		return hc;
	}

}
