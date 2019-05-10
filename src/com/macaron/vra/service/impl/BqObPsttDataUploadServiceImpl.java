package com.macaron.vra.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.Selectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.macaron.vra.constant.AppConst;
import com.macaron.vra.util.DateUtil;
import com.macaron.vra.util.FileSystemUtil;
import com.macaron.vra.util.FtpUtil;
import com.macaron.vra.util.WavUtil;
import com.macaron.vra.vo.BqObCallResultRecordInfoVo;
import com.macaron.vra.vo.BqObProcessVo;

@Service
public class BqObPsttDataUploadServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(BqObPsttDataUploadServiceImpl.class);
	
	private static final String PSTT_DATA_SUFFIX_BQ = "-bq";
	private static final String FUGO_API_LOCATION = "1005";
	private static final String BASE_RECORDING_DIR_PATH = "\\\\bq-recnas\\RecordFiles/Recording/";
	
	public static final String PSTT_TASK_SERIAL_PREFIX_DATE_PATTERN = "yyyy-MM-dd-HH-mm";
	
	@Autowired
	private FugoApiServiceImpl fugoApiService;
	
	public void process(Date dataTimeStart, Date dataTimeEnd){
		FileObject cvtWavTmpDirFo = null;
		FileObject batchSerialDirFo = null;
		try {
			logger.info("BqPsttDataUpload start");
			LocalDateTime ldtStart = DateUtil.toLdt(dataTimeStart).truncatedTo(ChronoUnit.MINUTES);
			LocalDateTime ldtEnd = DateUtil.toLdt(dataTimeEnd).truncatedTo(ChronoUnit.MINUTES);
			logger.info("dataTime range:{}~{}", ldtStart, ldtEnd);
//			Integer hoursLimit = 1;
//			long h = Duration.between(ldtStart, ldtEnd).toHours();
//			logger.info("h:{}", h);
//			if(!hoursLimit.equals(Duration.between(ldtStart, ldtEnd).toHours())){
//				throw new RuntimeException("dataTime range must be " + hoursLimit + " hour(s)");
//			}
			//limit change 15 min
			Long timeLimit = 15L;
			long range = Duration.between(ldtStart, ldtEnd).toMinutes();
			logger.info("range:{}", range);
			if(!timeLimit.equals(range)){
				throw new RuntimeException("dataTime range must be " + timeLimit + " min(s)");
			}
			
			String batchSerial = DateUtil.getNowFullAd() + PSTT_DATA_SUFFIX_BQ;
			logger.info("batchSerial:{}", batchSerial);
			
			String psttTaskSerial = DateTimeFormatter.ofPattern(PSTT_TASK_SERIAL_PREFIX_DATE_PATTERN).format(ldtStart) + PSTT_DATA_SUFFIX_BQ;
			logger.info("psttTaskSerial:{}", psttTaskSerial);
			//only support yyyy-mm-dd.dim
			String dimSerial = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ldtStart);
			logger.info("dimSerial:{}", dimSerial);
			
			//ex:/root/PSAE2837/DataSource
			String psttFtpDataSourceDirPath = AppConst.bqObPsttFtpDataSourceDirPath;
			logger.info("{}", psttFtpDataSourceDirPath);
			//ex:/Dimension
			String subDimensionDirPath = "/Dimension";
			logger.info("{}", subDimensionDirPath);
			//ex:/Dimension/2018-05-05-23-gq
			String subDimensionTaskDirPath = subDimensionDirPath + "/" + psttTaskSerial;
			logger.info("{}", subDimensionTaskDirPath);
			//ex:/Dimension/2018-05-05-23-gq/2018-05-05-23-gq.dim
			String subDimensionTaskDimPath = subDimensionTaskDirPath + "/" + dimSerial + ".dim";
			logger.info("{}", subDimensionTaskDimPath);
			//ex:/Dimension/2018-05-05-23-gq/flag.new
			String subDimensionTaskFlagPath = subDimensionTaskDirPath + "/flag.new";
			logger.info("{}", subDimensionTaskFlagPath);
			//ex:/Speech
			String subSpeechDirPath = "/Speech";
			logger.info("{}", subSpeechDirPath);
			//ex:/Speech/2018-05-05-23-gq
			String subSpeechTaskDirPath = subSpeechDirPath + "/" + psttTaskSerial;
			logger.info("{}", subSpeechTaskDirPath);
			//ex:/Speech/2018-05-05-23-gq/2018-05-05-23-gq.dim
			String subSpeechTaskDimPath = subSpeechTaskDirPath + "/" + dimSerial + ".dim";
			logger.info("{}", subSpeechTaskDimPath);
			//ex:/Speech/2018-05-05-23-gq/file
			String subSpeechTaskRecsDirPath = subSpeechTaskDirPath + "/file";
			logger.info("{}", subSpeechTaskRecsDirPath);
			//ex:/Speech/2018-05-05-23-gq/flag.new
			String subSpeechTaskFlagPath = subSpeechTaskDirPath + "/flag.new";
			logger.info("{}", subSpeechTaskFlagPath);
			
			//check ftp taskDir exists
			String ftpDimensionTaskDirPath = psttFtpDataSourceDirPath + subDimensionTaskDirPath;
			logger.info("{}", ftpDimensionTaskDirPath);
			if(exists(ftpDimensionTaskDirPath)){
				throw new RuntimeException("file exists:" + ftpDimensionTaskDirPath);
			}
			String ftpSpeechTaskDirPath = psttFtpDataSourceDirPath + subSpeechTaskDirPath;
			logger.info("{}", ftpSpeechTaskDirPath);
			if(exists(ftpSpeechTaskDirPath)){
				throw new RuntimeException("file exists:" + ftpSpeechTaskDirPath);
			}
			
			String ftpDimensionTaskFlagPath = psttFtpDataSourceDirPath + subDimensionTaskFlagPath;
			logger.info("{}", ftpDimensionTaskFlagPath);
			String ftpSpeechTaskFlagPath = psttFtpDataSourceDirPath + subSpeechTaskFlagPath;
			logger.info("{}", ftpSpeechTaskFlagPath);

			//batch base
			String batchWorkTmpDirPath = new File("./batchWorkTmp").getCanonicalPath();
			logger.info("{}", batchWorkTmpDirPath);
			FileObject batchWorkTmpDirFo = FileSystemUtil.createFolder(batchWorkTmpDirPath);
			
			String batchSerialDirPath = 
					new StringBuilder(batchWorkTmpDirPath)
					.append("/")
					.append(batchSerial)
					.toString();
			logger.info("{}", batchSerialDirPath);
			batchSerialDirFo = FileSystemUtil.createFolder(batchSerialDirPath);
			
			String cvtWavTmpDirPath = 
					new StringBuilder(batchSerialDirPath)
					.append("/cvtWavTmp")
					.toString();
			logger.info("{}", cvtWavTmpDirPath);
			cvtWavTmpDirFo = FileSystemUtil.createFolder(cvtWavTmpDirPath);
			
			//dimension
			String dimensionTaskDirPath = 
					new StringBuilder(batchSerialDirPath)
					.append(subDimensionTaskDirPath)
					.toString();
			logger.info("{}", dimensionTaskDirPath);
			FileObject dimensionTaskDirFo = FileSystemUtil.createFolder(dimensionTaskDirPath);
			
			String dimensionTaskDimPath = 
					new StringBuilder(batchSerialDirPath)
					.append(subDimensionTaskDimPath)
					.toString();
			logger.info("{}", dimensionTaskDimPath);
			FileObject dimensionTaskDimFo = FileSystemUtil.createFile(dimensionTaskDimPath);

			String dimensionTaskFlagPath = 
					new StringBuilder(batchSerialDirPath)
					.append(subDimensionTaskFlagPath)
					.toString();
			logger.info("{}", dimensionTaskFlagPath);
			//speech
			String speechTaskDirPath = 
					new StringBuilder(batchSerialDirPath)
					.append(subSpeechTaskDirPath)
					.toString();
			logger.info("{}", speechTaskDirPath);
			FileObject speechTaskDirFo = FileSystemUtil.createFolder(speechTaskDirPath);
			
			String speechTaskDimPath = 
					new StringBuilder(batchSerialDirPath)
					.append(subSpeechTaskDimPath)
					.toString();
			logger.info("{}", speechTaskDimPath);
			FileObject speechTaskDimFo = FileSystemUtil.createFile(speechTaskDimPath);
			
			String speechTaskRecsDirPath = 
					new StringBuilder(batchSerialDirPath)
					.append(subSpeechTaskRecsDirPath)
					.toString();
			logger.info("{}", speechTaskRecsDirPath);
			FileObject speechTaskRecsDirFo = FileSystemUtil.createFolder(speechTaskRecsDirPath);
			
			String speechTaskFlagPath = 
					new StringBuilder(batchSerialDirPath)
					.append(subSpeechTaskFlagPath)
					.toString();
			logger.info("{}", speechTaskFlagPath);
			
			//設定日期格式
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			//進行轉換
			String dts = sdf.format(dataTimeStart)+"00";
			String dte = sdf.format(dataTimeEnd)+"00";
			
			/*
			 * 呼叫FugoAPI取得data
			 */
			
			JsonObject obj = new JsonParser().parse(fugoApiService.ob(dts, dte, FUGO_API_LOCATION)).getAsJsonObject();
			
			if(obj.get("rtncode").getAsInt()==1){

				JsonElement dataArray = obj.get("data").getAsJsonArray();
				Type listType = new TypeToken<List<BqObCallResultRecordInfoVo>>() {}.getType();
				List<BqObCallResultRecordInfoVo> resultRecordInfoVos = new Gson().fromJson(dataArray, listType);

				/*
				 * 拉音檔，轉檔，放置對應目錄
				 */
				Map<String, BqObProcessVo> processVoMap = combineDataAndCvtWav(resultRecordInfoVos, speechTaskRecsDirPath, cvtWavTmpDirPath);
				logger.info("processVoMap size:{}", processVoMap.entrySet().size());
				for (Entry<String, BqObProcessVo> e : processVoMap.entrySet()) {
					logger.info("key:{}", e.getKey());
					logger.info("value:{}", e.getValue());
				}
				/*
				 * 依據轉檔成功的記錄產出DIM檔放置對應目錄
				 */
				writeDim(processVoMap, dimensionTaskDimFo, speechTaskDimFo);
				/*
				 * 上傳音檔及DIM檔
				 */
				String username = AppConst.bqObPsttFtpUsername;
				logger.info("{}", username);
				String password = AppConst.bqObPsttFtpPassword;
				logger.info("{}", password);
				String host = AppConst.bqObPsttFtpHost;
				logger.info("{}", host);
				Integer port = AppConst.bqObPsttFtpPort;
				logger.info("{}", port);
				String dataSourceDirPath = AppConst.bqObPsttFtpDataSourceDirPath;
				logger.info("{}", dataSourceDirPath);
//				upload dimensioin
				upload(dimensionTaskDirPath, ftpDimensionTaskDirPath);
//				upload Speech
				upload(speechTaskDirPath, ftpSpeechTaskDirPath);
				/*
				 * 傳flag.new
				 */
//				upload dimensioin			
				FileObject dimensionTaskFlagFo = FileSystemUtil.createFile(dimensionTaskFlagPath);
				upload(dimensionTaskFlagPath, ftpDimensionTaskFlagPath);
				
				FileObject speechTaskFlagFo = FileSystemUtil.createFile(speechTaskFlagPath);
//				upload Speech
				upload(speechTaskFlagPath, ftpSpeechTaskFlagPath);

				logger.info("BqObPsttDataUpload end");
				
			}else{
				logger.info("FugoApiRtnCode-{}",obj.get("rtncode").getAsInt());
				logger.info("FugoApiSuccess-{}",obj.get("success").getAsBoolean());
			}
			
		} catch (Exception e) {
			logger.error("process failed", e);
		}finally {
			if(cvtWavTmpDirFo != null){
				try {
					cvtWavTmpDirFo.delete(Selectors.SELECT_ALL);
				} catch (FileSystemException e) {
					logger.error("delete failed", e);
				}
			}
			if(batchSerialDirFo != null){
				try {
					batchSerialDirFo.delete(Selectors.SELECT_ALL);
				} catch (FileSystemException e) {
					logger.error("delete failed", e);
				}
			}
		}
	}
	
	private Map<String, BqObProcessVo> combineDataAndCvtWav(
			List<BqObCallResultRecordInfoVo> resultRecordInfoVos,
			String speechTaskRecsDirPath,
			String cvtWavWorkDirPath){
		logger.info("start grouping fugoData ");
		Map<String, List<BqObCallResultRecordInfoVo>> groupMap = new HashMap<String, List<BqObCallResultRecordInfoVo>>();
		for (BqObCallResultRecordInfoVo infoVo : resultRecordInfoVos) {
			String connId = infoVo.getConnid();
			List<BqObCallResultRecordInfoVo> list = groupMap.get(connId);
			if(list==null){
				list = new ArrayList<BqObCallResultRecordInfoVo>();
				list.add(infoVo);
				groupMap.put(connId, list);
			}else{
				list.add(infoVo);
			}
		}
		logger.info("groupMap size:{}", groupMap.size());
		
		Map<String, BqObProcessVo> processVoMap = new HashMap<String, BqObProcessVo>();
		logger.info("start combine by grouping fugoData ");
		for (Entry<String, List<BqObCallResultRecordInfoVo>> entry : groupMap.entrySet()) {
			String connId = entry.getKey();
			List<BqObCallResultRecordInfoVo> list = entry.getValue();
			BqObProcessVo procVo = new BqObProcessVo();
			if(list!=null && list.size()>0){
				try {
					for (int i=0; i<list.size(); i++) {
						BqObCallResultRecordInfoVo vo = list.get(i);
						if(i==0){
							procVo.setResultRecordInfoVo(vo);
						}
						if(i>0){
							String prodName = vo.getSalename();
							if(!StringUtils.hasText(prodName)){
								continue;
							}
							BqObCallResultRecordInfoVo cbnVo = procVo.getResultRecordInfoVo();
							
							if(!StringUtils.hasText(cbnVo.getSalename())){
								cbnVo.setSalename(vo.getSalename());
								cbnVo.setSaleno(vo.getSaleno());
								cbnVo.setSubchannelname(vo.getSubchannelname());
								cbnVo.setPaymenttype(vo.getPaymenttype());
								cbnVo.setPayamount(vo.getPayamount());
							}
							
/*
 * 20190415 kyle 已無多項產品							
 */
//							if(!StringUtils.hasText(cbnVo.getProductName2()) && 
//									!prodName.equals(cbnVo.getProductName())){
//								cbnVo.setProductName2(vo.getProductName());
//								cbnVo.setProductSerialNum2(vo.getProductSerialNum());
//								cbnVo.setOrderChannel2(vo.getOrderChannel());
//								cbnVo.setPaymentType2(vo.getPaymentType());
//								cbnVo.setPaymentAmount2(vo.getPaymentAmount());
//							}
						}
					}
					/*
					 * Fugo API無FileName，利用ConnId搜尋檔案名稱
					 */
					
					String fDate = procVo.getResultRecordInfoVo().getContactdate().substring(0, 10).replaceAll("/", "-");
					String chkPath = BASE_RECORDING_DIR_PATH+procVo.getResultRecordInfoVo().getCtiplace()+"/"+fDate;
					logger.info("{}",chkPath);
					
					File src = new File(chkPath);
					boolean wavExists = false;
					if (src.exists()) {
						File[] files = src.listFiles();
						Arrays.sort(files, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
						
						for (int i = 0; i < files.length; i++) {
							if(files[i].getName().contains(procVo.getResultRecordInfoVo().getConnid())){
								logger.info("{}",procVo.getResultRecordInfoVo().getCtiplace()+"/"+fDate+"/"+files[i].getName());
								procVo.getResultRecordInfoVo().setFileName(procVo.getResultRecordInfoVo().getCtiplace()+"/"+fDate+"/"+files[i].getName());
								procVo.getResultRecordInfoVo().setDataLocation("1004");
								procVo.setSrcWavPath(BASE_RECORDING_DIR_PATH+procVo.getResultRecordInfoVo().getFileName());
								wavExists = true;
								continue;
							}
						}
					}
					
					//setting info
					settingProcessData(procVo, speechTaskRecsDirPath);
					logger.info("procVo:{}", procVo);
					
					if(!wavExists){
						logger.error("{}{}","Wav file not found : ",procVo.getResultRecordInfoVo().getConnid());
					}else{
						//cvt Bq wav
						WavUtil.cvtBqWav(procVo.getSrcWavPath(), speechTaskRecsDirPath, cvtWavWorkDirPath);
					}
					//put uni vo for cbn after combine success
					processVoMap.put(connId, procVo);
				} catch (Exception ex) {
					logger.error("combine failed:{}", ex);
				}
			}
		}
		logger.info("processVoMap size:{}", processVoMap.size());
		return processVoMap;
	}
	
	private void settingProcessData(BqObProcessVo processVo, String speechTaskRecsDirPath){
		BqObCallResultRecordInfoVo infoVo = processVo.getResultRecordInfoVo();
		String fileName = infoVo.getFileName()==null? "": infoVo.getFileName().replaceAll("\\\\", "/");

		String subFilePath = fileName;
//		String subFilePath = fileName.replaceAll("Recording/", "");
		processVo.setSubFilePath(subFilePath);
		
		String baseFileName = subFilePath.substring(subFilePath.lastIndexOf("/")+1);
		processVo.setBaseFileName(baseFileName);
		
		String srcWavPath = BASE_RECORDING_DIR_PATH + "/" + subFilePath;
		processVo.setSrcWavPath(srcWavPath);
		
		String destWavPath = speechTaskRecsDirPath + "/" + baseFileName;
		processVo.setDestWavPath(destWavPath);
	}
	
	private void writeDim(
			Map<String, BqObProcessVo> processVoMap, 
			FileObject dimensionTaskDimFo,
			FileObject speechTaskDimFo){
		String spliter = "|";
		StringBuilder speechText = new StringBuilder();
		speechText.append("电话流水号|FilePath");
		StringBuilder dimensionText = new StringBuilder();
//		dimensionText
//		.append("任务流水号|录音列表")
//		.append("|序號")
//		.append("|群組")
//		.append("|小結清單")
//		.append("|部")
//		.append("|單位")
//		.append("|組別")
//		.append("|姓名")
//		.append("|同仁客服編號")
//		.append("|通話秒數")
//		.append("|客代")
//		.append("|聯繫時間")
//		.append("|結束時間")
//		.append("|CTI位置")
//		.append("|人員ID")
//		.append("|OB舊客會員等級")
//		.append("|品名")
//		.append("|品號")
//		.append("|訂單通路")
//		.append("|付款方式")
//		.append("|付款金額")
//		.append("|Connid")
//		.append("|品名2")
//		.append("|品號2")
//		.append("|訂單通路2")
//		.append("|付款方式2")
//		.append("|付款金額2");
		dimensionText
		.append("任务流水号").append(spliter)
		.append("录音列表").append(spliter)
		.append("DATA_LOCATION").append(spliter)
		.append("CONTACTGROUP").append(spliter)
		.append("CWCNAME").append(spliter)
		.append("DEPTNAME1").append(spliter)
		.append("DEPTNAME2").append(spliter)
		.append("DEPTNAME3").append(spliter)
		.append("USERNAME").append(spliter)
		.append("ATTENDBYID").append(spliter)
		.append("CONTACTTIME").append(spliter)
		.append("CUSTOMERID").append(spliter)
		.append("CONTACTDATE").append(spliter)
		.append("ENDDATE").append(spliter)
		.append("CTIPLACE").append(spliter)
		.append("AGENTID").append(spliter)
		.append("OBLEVELNAME").append(spliter)
		.append("SALENAME").append(spliter)
		.append("SALENO").append(spliter)
		.append("SUBCHANNELNAME").append(spliter)
		.append("PAYMENTTYPE").append(spliter)
		.append("PAYAMOUNT").append(spliter)
		.append("CONNID").append(spliter)
		.append("FileName");
//		int c = 0;
		int c = 0;
		BufferedWriter speechBw = null;
		BufferedWriter dimensionBw = null;
		try {
			speechBw = new BufferedWriter(new OutputStreamWriter(speechTaskDimFo.getContent().getOutputStream(), StandardCharsets.UTF_8));
			dimensionBw = new BufferedWriter(new OutputStreamWriter(dimensionTaskDimFo.getContent().getOutputStream(), StandardCharsets.UTF_8));
			//speech header
			speechBw.write(speechText.toString());
			speechBw.newLine();
			speechText.delete(0, speechText.length());
			//dimension header
			dimensionBw.write(dimensionText.toString());
			dimensionBw.newLine();
			dimensionText.delete(0, dimensionText.length());
			
			for(Entry<String, BqObProcessVo> entry : processVoMap.entrySet()){
				BqObProcessVo processVo = entry.getValue();
				//ex:3241@@#.wav
				String baseFileName = processVo.getBaseFileName();
				BqObCallResultRecordInfoVo vo = processVo.getResultRecordInfoVo();
				//speech
				speechText.append(c).append("|").append("file/").append(baseFileName);
				speechBw.write(speechText.toString());
				speechBw.newLine();
				speechText.delete(0, speechText.length());
				
				//dimension
				dimensionText
				.append(c)
				.append("|")
				.append(c)
				.append("|")
				.append((vo.getDataLocation()==null)?"":vo.getDataLocation())
				.append("|")
				.append((vo.getSerialNum()==null)?"":vo.getSerialNum())
				.append("|")
				.append((vo.getContactgroup()==null)?"":vo.getContactgroup())
				.append("|")
				.append((vo.getCwcname()==null)?"":vo.getCwcname())
				.append("|")
				.append((vo.getDeptname1()==null)?"":vo.getDeptname1())
				.append("|")
				.append((vo.getDeptname2()==null)?"":vo.getDeptname2())
				.append("|")
				.append((vo.getDeptname3()==null)?"":vo.getDeptname3())
				.append("|")
				.append((vo.getUsername()==null)?"":vo.getUsername())
				.append("|")
				.append((vo.getAttendbyid()==null)?"":vo.getAttendbyid())
				.append("|")
				.append((vo.getContacttime()==null)?"":vo.getContacttime())
				.append("|")
				.append((vo.getCustomerid()==null)?"":vo.getCustomerid())
				.append("|")
				.append((vo.getContactdate()==null)?"":vo.getContactdate())
				.append("|")
				.append((vo.getEnddate ()==null)?"":vo.getEnddate ())
				.append("|")
				.append((vo.getCtiplace()==null)?"":vo.getCtiplace())
				.append("|")
				.append((vo.getAgentid()==null)?"":vo.getAgentid())
				.append("|")
				.append((vo.getOblevelname()==null)?"":vo.getOblevelname())
				.append("|")
				.append((vo.getSalename()==null)?"":vo.getSalename())
				.append("|")
				.append((vo.getSaleno()==null)?"":vo.getSaleno())
				.append("|")
				.append((vo.getSubchannelname()==null)?"":vo.getSubchannelname())
				.append("|")
				.append((vo.getPaymenttype()==null)?"":vo.getPaymenttype())
				.append("|")
				.append((vo.getPayamount()==null)?"":vo.getPayamount())
				.append("|")
				.append((vo.getConnid()==null)?"":vo.getConnid())
				.append("|")
				.append("file/").append(baseFileName)
//				.append("|")
//				.append((vo.getProductName2()==null)?"":vo.getProductName2())
//				.append("|")
//				.append((vo.getProductSerialNum2()==null)?"":vo.getProductSerialNum2())
//				.append("|")
//				.append((vo.getOrderChannel2()==null)?"":vo.getOrderChannel2())
//				.append("|")
//				.append((vo.getPaymentType2()==null)?"":vo.getPaymentType2())
//				.append("|")
//				.append((vo.getPaymentAmount2()==null)?"":vo.getPaymentAmount2())
				;
				String text = dimensionText.toString().replaceAll("\n", "").replaceAll("\r", "").replaceAll("\r\n", "");
				dimensionBw.write(text);
				dimensionBw.newLine();
				dimensionText.delete(0, dimensionText.length());
				
				c++;
			}			
		} catch (Exception e) {
			logger.error("{}", e);
			throw new RuntimeException("write dim file failed");
		}finally {
			if(speechBw!=null){
				try {
					speechBw.close();
				} catch (IOException e) {
					logger.error("{}", e);
					throw new RuntimeException("close speech dim file failed");
				}
			}
			if(dimensionBw!=null){
				try {
					dimensionBw.close();
				} catch (IOException e) {
					logger.error("{}", e);
					throw new RuntimeException("close dimension dim file failed");
				}
			}
		}	
	}
	
	private boolean exists(String destFilePath){
		try {
			return FtpUtil.exists(
					AppConst.bqObPsttFtpUsername, 
					AppConst.bqObPsttFtpPassword, 
					AppConst.bqObPsttFtpHost, 
					AppConst.bqObPsttFtpPort,  
					destFilePath);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new RuntimeException("upload failed");
		}
	}
	
	private void upload(String srcDirPath, String destDirPath){
		try {
			
			FtpUtil.uploadDir(
					AppConst.bqObPsttFtpUsername, 
					AppConst.bqObPsttFtpPassword, 
					AppConst.bqObPsttFtpHost, 
					AppConst.bqObPsttFtpPort, 
					destDirPath, 
					srcDirPath);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new RuntimeException("upload failed");
		}
	}
}
