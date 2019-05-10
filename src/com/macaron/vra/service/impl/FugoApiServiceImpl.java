package com.macaron.vra.service.impl;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FugoApiServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(FugoApiServiceImpl.class);
	
	private static final String SERVER_URL_TEST = "http://192.168.1.71:8983";
	private static final String SERVER_URL_PRD = "http://10.33.45.14:8983";
	private static final String URI_SPLITER = "&";
	
	public void ib(){
		String fn = "/IvrTransfromTxt/ibSpeechRecognition.jsp";
		StringBuilder uri = new StringBuilder();
//		uri
//		.append(SERVER_URL_TEST).append(fn).append("?")
//		.append("startDate=20171023000000").append(URI_SPLITER)
//		.append("endDate=20171023231000").append(URI_SPLITER)
//		.append("location=1001").append(URI_SPLITER)
//		;
		//prd
		uri
		.append(SERVER_URL_PRD).append(fn).append("?")
		.append("startDate=20190114090000").append(URI_SPLITER)
		.append("endDate=20190114091500").append(URI_SPLITER)
		.append("location=1001")
		;
		URI url = URI.create(uri.toString());
		
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> re = template.getForEntity(url, String.class);
		String body = re.getBody();
		logger.info("{}", body);
		logger.info("{}", body.replaceAll("\t", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", ""));
	}
	
	public void ob(){
		String fn = "/IvrTransfromTxt/obSpeechRecognition.jsp";
		StringBuilder uri = new StringBuilder();
//		uri
//		.append(SERVER_URL_TEST).append(fn).append("?")
//		.append("startDate=20160727150000").append(URI_SPLITER)
//		.append("endDate=20160727175900").append(URI_SPLITER)
//		.append("location=1002").append(URI_SPLITER)
//		;
		//prd
		uri
		.append(SERVER_URL_PRD).append(fn).append("?")
		.append("startDate=20190114090000").append(URI_SPLITER)
		.append("endDate=20190114091500").append(URI_SPLITER)
		.append("location=1001").append(URI_SPLITER)
		;
		URI url = URI.create(uri.toString());
		
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> re = template.getForEntity(url, String.class);
		String body = re.getBody();
		logger.info("{}", body);
		logger.info("{}", body.replaceAll("\t", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", ""));
	}
	
	public String ib(String sDate, String eDate, String location){
		String fn = "/IvrTransfromTxt/ibSpeechRecognition.jsp";
		StringBuilder uri = new StringBuilder();
//		uri
//		.append(SERVER_URL_TEST).append(fn).append("?")
//		.append("startDate=20171023000000").append(URI_SPLITER)
//		.append("endDate=20171023231000").append(URI_SPLITER)
//		.append("location=1001").append(URI_SPLITER)
//		;
		
		sDate = "startDate="+sDate;
		eDate = "endDate="+eDate;
		location = "location="+location;
		
		//prd
		uri
		.append(SERVER_URL_PRD).append(fn).append("?")
		.append(sDate).append(URI_SPLITER)
		.append(eDate).append(URI_SPLITER)
		.append(location)
		;
		logger.info("{}",uri);
		URI url = URI.create(uri.toString());
		
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> re = template.getForEntity(url, String.class);
		String body = re.getBody();
		logger.info("{}", body);
		logger.info("{}", body.replaceAll("\t", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", ""));
		return body;
	}
	
	public String ob(String sDate, String eDate, String location){
		String fn = "/IvrTransfromTxt/obSpeechRecognition.jsp";
		StringBuilder uri = new StringBuilder();
//		uri
//		.append(SERVER_URL_TEST).append(fn).append("?")
//		.append("startDate=20160727150000").append(URI_SPLITER)
//		.append("endDate=20160727175900").append(URI_SPLITER)
//		.append("location=1002").append(URI_SPLITER)
//		;
		
		sDate = "startDate="+sDate;
		eDate = "endDate="+eDate;
		location = "location="+location;
		
		//prd
		uri
		.append(SERVER_URL_PRD).append(fn).append("?")
		.append(sDate).append(URI_SPLITER)
		.append(eDate).append(URI_SPLITER)
		.append(location)
//		.append("startDate=20190416120000").append(URI_SPLITER)
//		.append("endDate=20190416121000").append(URI_SPLITER)
//		.append("location=1004")
		;
		logger.info("{}",uri);
		URI url = URI.create(uri.toString());
		
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> re = template.getForEntity(url, String.class);
		String body = re.getBody();
		
		logger.info("{}", body);
		logger.info("{}", body.replaceAll("\t", "").replaceAll("\r", "").replaceAll("\n", "").replaceAll(" ", ""));
		return body;
	}
	
	public static void main(String[] args) {
//		new FugoApiServiceImpl().ib();
//		logger.info("sad");
//		new FugoApiServiceImpl().ob();
		new FugoApiServiceImpl().ib("20190114090000","20190114091500","1001");
		logger.info("sad");
		new FugoApiServiceImpl().ob("20190114090000","20190114091500","1001");
	}
}
