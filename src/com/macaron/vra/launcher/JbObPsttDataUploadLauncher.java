package com.macaron.vra.launcher;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import com.macaron.vra.processor.JbObPsttDataUploadProcessor;
import com.macaron.vra.service.impl.JbObPsttDataUploadServiceImpl;
import com.macaron.vra.util.DateUtil;

@Component
public class JbObPsttDataUploadLauncher {

	private static final Logger logger = LoggerFactory.getLogger(JbObPsttDataUploadLauncher.class);

	private static String JB_OB_TASK_SCHEDULE_CRON_EX = "0 */15 * * * ?";
//	private static String JB_OB_TASK_SCHEDULE_CRON_EX = "5 * * * * ?";
	
	@Autowired
	private TaskScheduler taskScheduler;
	@Autowired
	private JbObPsttDataUploadProcessor processor;
	
	public void launchSchedule(){
		Runnable hourTask = new Runnable() {
			
			@Override
			public void run() {
				//each task h-2 ~ h-1
				LocalDateTime now = LocalDateTime.now();
//				Date s = DateUtil.toDate(now.minusHours(2));
//				Date e = DateUtil.toDate(now.minusHours(1));
//				logger.info("schedule exec start , dataTime:{} ~ {}", s, e);
//				processor.processTask(s, e);
//				logger.info("schedule exec end ");
				//change to 15min
				int minRank = 15;
				Date s = DateUtil.toDate(now.minusMinutes(minRank*2));
				Date e = DateUtil.toDate(now.minusMinutes(minRank));
				logger.info("schedule exec start , dataTime:{} ~ {}", s, e);
				processor.processTask(s, e);
				logger.info("schedule exec end ");
			}
		};
		taskScheduler.schedule(hourTask, new CronTrigger(JB_OB_TASK_SCHEDULE_CRON_EX));
		logger.info("schedule CRON:" + JB_OB_TASK_SCHEDULE_CRON_EX);
	}

	public void launchManual(Date dataTimeStart, Date dataTimeEnd) throws InterruptedException, ExecutionException{
		logger.info("launchManual start");
		processor.processTask(dataTimeStart, dataTimeEnd).get().get();
		logger.info("launchManual end");
	}
	
	public static void main(String[] args) {
		ApplicationContext context = null;
		try {
			logger.info(System.getProperty("user.dir"));
			context = new ClassPathXmlApplicationContext("spring-context.xml");

			JbObPsttDataUploadLauncher launcher = (JbObPsttDataUploadLauncher)context.getBean(JbObPsttDataUploadLauncher.class);
			
			LocalDate today = LocalDate.now();
//			LocalDate today = LocalDate.of(2018, 2, 3);
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(JbObPsttDataUploadServiceImpl.PSTT_TASK_SERIAL_PREFIX_DATE_PATTERN);
			logger.debug("{}", dtf.format(LocalDateTime.of(2018, 5, 5, 0, 5).truncatedTo(ChronoUnit.HOURS)));
			
			Date todayD = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Date yesterdayD = Date.from(todayD.toInstant().minus(1, ChronoUnit.DAYS));
			logger.debug("sync date:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(todayD));
			logger.debug("data date:{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(yesterdayD));
			
			String mode = args[0];
//			String mode = "a";
//			String mode = "m";
			if("a".equalsIgnoreCase(mode)){
				launcher.launchSchedule();
			}else if("m".equalsIgnoreCase(mode)){
				String startDtStr = args[1];
				String endDtStr = args[2];
//				String startDtStr = "2019-04-22-09-30";
//				String endDtStr = "2019-04-22-12-00";
				dtf = DateTimeFormatter.ofPattern(JbObPsttDataUploadServiceImpl.PSTT_TASK_SERIAL_PREFIX_DATE_PATTERN);
				LocalDateTime now = LocalDateTime.now();
				LocalDateTime startDt = LocalDateTime.parse(startDtStr, dtf).truncatedTo(ChronoUnit.MINUTES);
				LocalDateTime endDt = LocalDateTime.parse(endDtStr, dtf).truncatedTo(ChronoUnit.MINUTES);
				while(startDt.isBefore(endDt)){
					logger.info("validate task dataTimeStart:{}", startDt);
					long h = Duration.between(startDt, now).toHours();
					logger.info("h:{}", h);
					if(h < 3){
						throw new RuntimeException("dataTimeStart must less than now for 3 hours");
					}
					Date s = DateUtil.toDate(startDt);
					Date e = DateUtil.toDate(startDt.plusMinutes(15));
					logger.info("put task in pool dataTime:{} ~ {}", s, e);
					launcher.launchManual(s, e);
					
					//from start plus 1 hour by each task
					startDt = startDt.plusMinutes(15);
				}
				
//				LocalDateTime now = LocalDateTime.now();
//				LocalDateTime now = LocalDateTime.of(2018, 5, 29, 13, 5);
//				Date s = DateUtil.toDate(now);
//				Date e = DateUtil.toDate(now.plusHours(1));
//				logger.info("put task in pool dataTime:{} ~ {}", s, e);
//				launcher.launchManual(s, e);
				((ConfigurableApplicationContext)context).close();
			}
		} catch (Exception e) {
			logger.error("{}", e);
			((ConfigurableApplicationContext)context).close();
		}
	}
}
