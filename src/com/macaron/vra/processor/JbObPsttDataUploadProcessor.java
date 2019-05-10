package com.macaron.vra.processor;

import java.util.Date;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.macaron.vra.service.impl.JbObPsttDataUploadServiceImpl;

@Component
public class JbObPsttDataUploadProcessor {

	private static final Logger logger = LoggerFactory.getLogger(JbObPsttDataUploadProcessor.class);

	@Autowired
	@Qualifier("taskExecutors")
	private ThreadPoolTaskExecutor taskExecutors;

	@Autowired
	private JbObPsttDataUploadServiceImpl psttDataUploadService;

	@Async
	public Future<Future<?>> processTask(Date dataTimeStart, Date dataTimeEnd){
		logger.info("dataTimeStart:{}", dataTimeStart);
		logger.info("dataTimeEnd:{}", dataTimeEnd);
		logger.info("put task in taskExecPool start");
		Future<?> ret = taskExecutors.submit(new JbObPsttDataUploadTask(dataTimeStart, dataTimeEnd));
		logger.info("put task in taskExecPool end");
		return new AsyncResult<Future<?>>(ret);
	}
	
	private class JbObPsttDataUploadTask implements Runnable {
		private Date dataTimeStart;
		private Date dataTimeEnd;

		public JbObPsttDataUploadTask(Date dataTimeStart, Date dataTimeEnd) {
			super();
			this.dataTimeStart = dataTimeStart;
			this.dataTimeEnd = dataTimeEnd;
		}

		@Override
		public void run() {
			logger.info("dataTimeStart:{}", dataTimeStart);
			logger.info("dataTimeEnd:{}", dataTimeEnd);
			logger.info("task in taskExecPool start running");
			psttDataUploadService.process(dataTimeStart, dataTimeEnd);
			logger.info("task in taskExecPool end running");
		}
	}
}
