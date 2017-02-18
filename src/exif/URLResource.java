package exif;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class URLResource {

	private CloseableHttpClient httpclient = HttpClients.createDefault();
	
	public String get(String url) {
		HttpGet httpget = new HttpGet(url);
		
		httpget.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpget.setHeader("Accept-Encoding","gzip,deflate,sdch");
		httpget.setHeader("Accept-Language","zh-CN,zh;q=0.8");
		httpget.setHeader("Connection","keep-alive");
		httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");

        // Create a custom response handler
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

            @Override
			public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }

        };
        
        String responseBody = null;
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
	        //System.out.println("----------------------------------------");
	        //System.out.println(responseBody);
	        
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return responseBody;
	}

	public void close() {
		try {
			httpclient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
