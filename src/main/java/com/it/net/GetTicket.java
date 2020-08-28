package com.it.net;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class GetTicket {
	
	
	//起始车站  FristStation 福州,fzs
	//结束车站	finalStation 厦门，xks
	//出发时间	data
	//
	
	public  String GetUrl(String FristStation,String finalStation,String data) {
		//https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date=2020-08-12&leftTicketDTO.from_station=FZS&leftTicketDTO.to_station=XKS&purpose_codes=ADULT
		return "https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date="+data+"&leftTicketDTO.from_station="+FristStation+"&leftTicketDTO.to_station="+finalStation+"&purpose_codes=ADULT";
	}
	
	
	// 忽略SSL证书
		private void trustAllHttpsCertificates() throws Exception {
			javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
			javax.net.ssl.TrustManager tm = new miTM();
			trustAllCerts[0] = tm;
			javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, null);
			javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		}
	 
		static class miTM implements javax.net.ssl.TrustManager,javax.net.ssl.X509TrustManager {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
	 
			public boolean isServerTrusted(
					java.security.cert.X509Certificate[] certs) {
				return true;
			}
	 
			public boolean isClientTrusted(
					java.security.cert.X509Certificate[] certs) {
				return true;
			}
	 
			public void checkServerTrusted(
					java.security.cert.X509Certificate[] certs, String authType)
					throws java.security.cert.CertificateException {
				return;
			}
	 
			public void checkClientTrusted(
					java.security.cert.X509Certificate[] certs, String authType)
					throws java.security.cert.CertificateException {
				return;
			}
		}
	 
		/**
		 * 
		 * @param urlAll
		 *            :请求接口
		 * @param charset
		 *            :字符编码
		 * @return 返回json结果
		 */
		public  String get(String urlAll, String charset) {
			BufferedReader reader = null;
			String result = null;
			StringBuffer sbf = new StringBuffer();
			String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";// 模拟浏览器
			try {
				trustAllHttpsCertificates();
				HostnameVerifier hv = new HostnameVerifier() {
					@Override
					public boolean verify(String urlHostName, SSLSession session) {
						System.out.println("Warning: URL Host: " + urlHostName
								+ " vs. " + session.getPeerHost());
						return true;
					}
				};
				HttpsURLConnection.setDefaultHostnameVerifier(hv);
				URL url = new URL(urlAll);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");
				connection.setReadTimeout(30000);
				connection.setConnectTimeout(30000);
				connection.setRequestProperty("User-agent", userAgent);
				connection.connect();
				InputStream is = connection.getInputStream();
				reader = new BufferedReader(new InputStreamReader(is, charset));
				String strRead = null;
				while ((strRead = reader.readLine()) != null) {
					sbf.append(strRead);
					sbf.append("\r\n");
				}
				reader.close();
				result = sbf.toString();
	 
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
		
		
		public List json12306(String startCity, String arrCity,
				String date){
			List list = new ArrayList();
			try {
				String charset = "UTF-8";
				String urlname = "https://kyfw.12306.cn/otn/leftTicket/query?leftTicketDTO.train_date="
						+ date
						+ "&leftTicketDTO.from_station="
						+ startCity
						+ "&leftTicketDTO.to_station="
						+ arrCity
						+ "&purpose_codes=ADULT";
				System.out.println(urlname);
				String jsonResult = get(urlname, charset);// 得到JSON字符串
				System.out.println(jsonResult);
				String message;
				JSONObject obj = JSONObject.fromObject(jsonResult);// 转化为JSON类
				/* 获取返回状态码 */
				if (obj.containsKey("httpstatus")) {
					message = obj.getString("httpstatus");
					System.out.println("连接状况码：" + message);
					/* 如果状态码是200说明返回数据成功 */
					if (message != null && message.equals("200")) {
						message = obj.getString("data");
						System.out.println(message);
						JSONObject object = JSONObject.fromObject(message);
						message = object.getString("result");
						System.out.println(message);
						message = message.substring(message.indexOf("[") + 1,message.lastIndexOf("]"));
						System.out.println(message);
						for(String s : message.split(",")){
							System.out.println(s);
							s = s.substring(s.indexOf("\"") + 1,s.lastIndexOf("\""));
							System.out.println(s);
							String ss[] = s.split("\\|");
							for(int i = 0;i < ss.length;i++){
								System.out.println(ss[i]);
							}
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return list;
		}

		public static void main(String[] args) {
			GetTicket gt=new GetTicket();
			gt.json12306("FZS", "XKS", "2020-08-12");
		}

}
