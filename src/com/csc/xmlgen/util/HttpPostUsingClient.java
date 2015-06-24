package com.csc.xmlgen.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpPostUsingClient {

	private final String USER_AGENT = "Mozilla/5.0";
	HttpClient client = new DefaultHttpClient();
	HttpPost post;

	public HttpPostUsingClient(String connectionURL, int timeOutInterval,
			String contType) throws Exception {
		post = new HttpPost(connectionURL);
		post.setHeader("User-Agent", USER_AGENT);
		post.setHeader("Content-Type", contType);
	
	}

	// HTTP POST request
	public void sendPost(String requestMsg) throws Exception {
		post.setEntity(new ByteArrayEntity(requestMsg.getBytes("UTF-8")));
		client.execute(post);
		//String result = EntityUtils.toString(response.getEntity());
	}
}