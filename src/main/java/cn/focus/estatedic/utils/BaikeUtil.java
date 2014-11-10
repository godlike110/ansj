package cn.focus.estatedic.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.sohu.sce.repackaged.net.rubyeye.xmemcached.utils.ByteUtils;

public class BaikeUtil {

	private static int SUCCESS_CODE = 200;

	public static boolean hasBaike(String str) throws UnsupportedEncodingException {
		String searchRt = getHtml("http://www.baidu.com/s?wd=" + URLEncoder.encode(str, "utf8"));
		
		if(searchRt.contains("http://baike.baidu.com/view/")) {
			if(searchRt.contains("<em>" + str + "</em>_百度百科")) {
			return true;
			} else {
				return false;
			}
		} else {
		return false;
		}
	}

	public static String getContent(String strUrl)
	// 一个public方法，返回字符串，错误则返回"error open url"
	{
		try {

			URL url = new URL(strUrl);
			BufferedReader br = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String s = "";
			StringBuffer sb = new StringBuffer("");
			while ((s = br.readLine()) != null) {
				sb.append(s + "\r\n");
			}
			br.close();
			return sb.toString();
		} catch (Exception e) {
			return "error open url" + strUrl;

		}
	}

	public static String getHtml(String urlString) {
	         try {
	   	       HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
		        // 连接
		        connection.connect();
		        InputStream inputStream = connection.getInputStream();
		        int statusCode = connection.getResponseCode();
		        //System.out.println("响应码:" + statusCode);
		        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
		        StringBuffer content = new StringBuffer();
		        byte[] buff = new byte[1024];
		        int len;
		        while ((len = bufferedInputStream.read(buff,0,buff.length)) != -1) {
		            content.append(new String(buff,0,len));
		        }
		        //System.out.println(content);
		      //  System.out.println(content.toString());
		        String a = content.toString();
		       // System.out.println(a);
		        // 关闭
		        connection.disconnect();
		        return a;
	         } catch(Exception e) {
	        	return null; 
	         }
	}

	public static void main(String argv[]) throws HttpException, IOException {
		System.out.println(BaikeUtil.hasBaike("毛泽东"));
	}

}
