package com.csc.xmlgen.handlers;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.w3c.dom.Document;

import com.csc.xmlgen.util.HttpPost;
import com.csc.xmlgen.util.HttpPostUsingClient;
import com.csc.xmlgen.util.XmlUtility;

public class BillingNotifyAyncRequestDispatcher {
	//private static final int MYTHREADS = 30;
	HttpPost httppost;
	HttpPostUsingClient httpPostUsingClient;
	String directory;
	File[] files;
	ArrayList<String> filePath = new ArrayList<String>();
	Document sourceDom = null;
	Document sourceDom1 = null;
	String connectionUrl;
	String connectionUrl1;
	String contentType;
	int threadCount = 30;

	public BillingNotifyAyncRequestDispatcher(String connectionUrl, String connectionUrl1,
			String destDir, String contentType , int threadCount) throws Exception {

		this.connectionUrl = connectionUrl;
		this.connectionUrl1 = connectionUrl1;
		this.contentType = contentType;
		directory = destDir;
		files = new File(directory).listFiles();
		this.threadCount = threadCount;
		for (File file : files) {
			if (file.isFile()) {
				// System.out.println(file.getAbsolutePath());
				filePath.add(file.getAbsolutePath());
			}
		}

	}

	public void postData() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);

		for (int count = 0; count < filePath.size(); count++) {
			sourceDom = XmlUtility.readXMLfromFile(filePath.get(count));
			String message = XmlUtility.convertDOMToString(sourceDom);
			Runnable worker = new MyRunnable(connectionUrl, 10000, contentType,
					message, count);
			executor.execute(worker);

		}
		executor.shutdown();

		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}
		System.out.println("\nFinished all threads");

	}
	
	public void postDataToMultipleURLs() throws Exception {
		if(threadCount%2>0){
			threadCount = threadCount -1 ;
		}
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		String url;
		for (int count = 0; count < filePath.size(); count = count +1) {
			if(count%threadCount<(threadCount/2)){
				url = connectionUrl;
			} else {
				url = connectionUrl1;
			}
			sourceDom = XmlUtility.readXMLfromFile(filePath.get(count));
			String message = XmlUtility.convertDOMToString(sourceDom);
			Runnable worker = new MyRunnable(url, 10000, contentType,
					message, count);
			executor.execute(worker);
		}
		executor.shutdown();

		// Wait until all threads are finish
		while (!executor.isTerminated()) {

		}
		System.out.println("\nFinished all threads");

	}

	public static class MyRunnable implements Runnable {
		private final String connectionUrl;
		private final int timeOut;
		private final String contType;
		private final String message;
		private final int count;
		
		MyRunnable(String url, int timeOut, String contType, String message, int count) {
			this.connectionUrl = url;
			this.timeOut = timeOut;
			this.contType = contType;
			this.message = message;
			this.count = count;
		}

		@Override
		public void run() {
			try {
				HttpPostUsingClient httpPostUsingClient = new HttpPostUsingClient(
						connectionUrl, timeOut, contType);
				System.out.println("Posting message: " +  count + " :: "+ getDateTime());
				httpPostUsingClient.sendPost(message);
				System.out.println("Completed message: " +  count + " :: "+ getDateTime());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public String getDateTime() {
	        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
	        Date date = new Date();
	        return dateFormat.format(date);
	    }
	}
}