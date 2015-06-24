package com.csc.xmlgen.servlet;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.csc.xmlgen.handlers.BillingNotifyAyncRequestDispatcher;

/**
 * Servlet implementation class BnrPostServlet
 */
@WebServlet("/BnrPostServlet")
public class BnrPostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String sourceDirectory = "";
	private static String connectionURL = "";
	private static String connectionURL1 = "";
	private static String contentType = "text/xml";
	private static int threadCount = 0;
	private static String alertMessage = "";
	private static boolean error = false;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BnrPostServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		sourceDirectory = request.getParameter("inputSrcPath");
		checkIfDirExists(sourceDirectory);
		if (error) {
			request.setAttribute("alertMessage", alertMessage);
			request.setAttribute("error", "true");
			request.getRequestDispatcher("/index.jsp").forward(request,
					response);
			return;
		}
		connectionURL = request.getParameter("inputCommUrl");
		connectionURL1 = request.getParameter("inputCommUrl1");
		threadCount = Integer.parseInt(request.getParameter("threadCount"));
		if (threadCount <= 0) {
			alertMessage = "Thread Count is Zero or less";
			request.setAttribute("alertMessage", alertMessage);
			request.setAttribute("error", "true");
			request.getRequestDispatcher("/index.jsp").forward(request,
					response);
			return;
		}
		try {
			BillingNotifyAyncRequestDispatcher bnrd = new BillingNotifyAyncRequestDispatcher(
					connectionURL, connectionURL1, sourceDirectory, contentType, threadCount);
			request.setAttribute("httpPostStart", getDateTime());
			if(connectionURL1.trim().length()>0){
				bnrd.postDataToMultipleURLs();
			} else {
				bnrd.postData();
			}
			
			request.setAttribute("httpPostEnd", getDateTime());
			alertMessage = "XML`s Posted to Communication Framework Successfully";
			request.setAttribute("alertMessage", alertMessage);
			// request.setAttribute("uuid", uuid);
			request.setAttribute("error", "false");
		} catch (Exception e) {
			// TODO: handle exception
			alertMessage = "Error generating or Posting Data";
			request.setAttribute("alertMessage", alertMessage);
			request.setAttribute("error", "true");
			request.getRequestDispatcher("/index.jsp").forward(request,
					response);
			return;
		}
		request.getRequestDispatcher("/Result.jsp").forward(request, response);

	}

	private void checkIfDirExists(String destDirectory) {
		// TODO Auto-generated method stub
		File theDir = new File(destDirectory);

		// if the directory does not exist, create it
		if (!theDir.exists()) {
			alertMessage = "Error Creating Directory " + destDirectory;
			error = true;
		}
	}

	public String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:SSS");
		Date date = new Date();
		return dateFormat.format(date);
	}

}
