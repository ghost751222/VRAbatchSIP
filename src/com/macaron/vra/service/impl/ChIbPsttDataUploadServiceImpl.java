package com.macaron.vra.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.Selectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.macaron.vra.constant.AppConst;
//import com.macaron.vra.dao.impl.ChIbCallResultRecordInfoDaoImpl;
import com.macaron.vra.util.DateUtil;
import com.macaron.vra.util.FileSystemUtil;
import com.macaron.vra.util.FtpUtil;
import com.macaron.vra.util.WavUtil;
import com.macaron.vra.vo.ChIbCallResultRecordInfoVo;
import com.macaron.vra.vo.ChIbProcessVo;

@Service
public class ChIbPsttDataUploadServiceImpl {

	private static final Logger logger = LoggerFactory.getLogger(ChIbPsttDataUploadServiceImpl.class);
	
	private static final String PSTT_DATA_SUFFIX_CH = "-ch";
	private static final String BASE_RECORDING_DIR_PATH = "\\\\192.168.252.1\\Converter\\Recording/CH";
	
//	@Autowired
//	private ChIbCallResultRecordInfoDaoImpl chIbCallResultRecordInfoDao;
	
	public void process(Date dataTimeStart, Date dataTimeEnd){
		FileObject cvtWavTmpDirFo = null;
		FileObject batchSerialDirFo = null;
		try {
			logger.info("ChIbPsttDataUpload start");
			LocalDateTime ldtStart = DateUtil.toLdt(dataTimeStart).truncatedTo(ChronoUnit.HOURS);
			LocalDateTime ldtEnd = DateUtil.toLdt(dataTimeEnd).truncatedTo(ChronoUnit.HOURS);
			logger.info("dataTime range:{}~{}", ldtStart, ldtEnd);
			Integer hoursLimit = 1;
			long h = Duration.between(ldtStart, ldtEnd).toHours();
			logger.info("h:{}", h);
			if(hoursLimit.equals(Duration.between(ldtStart, ldtEnd).toHours())){
				throw new RuntimeException("dataTime range must be " + hoursLimit + " hour(s)");
			}
			
			String batchSerial = DateUtil.getNowFullAd() + PSTT_DATA_SUFFIX_CH;
			logger.info("batchSerial:{}", batchSerial);
			
			String psttTaskSerial = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH").format(ldtStart) + PSTT_DATA_SUFFIX_CH;
			logger.info("psttTaskSerial:{}", psttTaskSerial);
			//only support yyyy-mm-dd.dim
			String dimSerial = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ldtStart);
			logger.info("dimSerial:{}", dimSerial);
			
			//ex:/root/PSAE2837/DataSource
			String psttFtpDataSourceDirPath = AppConst.chIbPsttFtpDataSourceDirPath;
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
			
			/*
			 * 新增batch記錄	20180525130000	>=startTime 2018-05-25 110000		<endTime 2018-05-25 120000
			 * status=start
			 * 依據區間取出 data
			 */
			List<ChIbCallResultRecordInfoVo> resultRecordInfoVos = null;//chIbCallResultRecordInfoDao.query(dataTimeStart, dataTimeEnd);
			/*
			 * 拉音檔，轉檔，放置對應目錄
			 */
			Map<String, ChIbProcessVo> processVoMap = combineDataAndCvtWav(resultRecordInfoVos, speechTaskRecsDirPath, cvtWavTmpDirPath);
			logger.info("processVoMap size:{}", processVoMap.entrySet().size());
			for (Entry<String, ChIbProcessVo> e : processVoMap.entrySet()) {
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
			String username = AppConst.chIbPsttFtpUsername;
			logger.info("{}", username);
			String password = AppConst.chIbPsttFtpPassword;
			logger.info("{}", password);
			String host = AppConst.chIbPsttFtpHost;
			logger.info("{}", host);
			Integer port = AppConst.chIbPsttFtpPort;
			logger.info("{}", port);
			String dataSourceDirPath = AppConst.chIbPsttFtpDataSourceDirPath;
			logger.info("{}", dataSourceDirPath);
//			upload dimensioin
			upload(dimensionTaskDirPath, ftpDimensionTaskDirPath);
//			upload Speech
			upload(speechTaskDirPath, ftpSpeechTaskDirPath);
			/*
			 * 傳flag.new
			 */
//			upload dimensioin			
			FileObject dimensionTaskFlagFo = FileSystemUtil.createFile(dimensionTaskFlagPath);
			upload(dimensionTaskFlagPath, ftpDimensionTaskFlagPath);

//			upload Speech
			FileObject speechTaskFlagFo = FileSystemUtil.createFile(speechTaskFlagPath);
			upload(speechTaskFlagPath, ftpSpeechTaskFlagPath);

			logger.info("ChIbPsttDataUpload end");
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
	
	private Map<String, ChIbProcessVo> combineDataAndCvtWav(
			List<ChIbCallResultRecordInfoVo> resultRecordInfoVos,
			String speechTaskRecsDirPath,
			String cvtWavWorkDirPath){
		logger.info("start grouping fugoData ");
		Map<String, List<ChIbCallResultRecordInfoVo>> groupMap = new HashMap<String, List<ChIbCallResultRecordInfoVo>>();
		for (ChIbCallResultRecordInfoVo infoVo : resultRecordInfoVos) {
			String connId = infoVo.getConnid();
			List<ChIbCallResultRecordInfoVo> list = groupMap.get(connId);
			if(list==null){
				list = new ArrayList<ChIbCallResultRecordInfoVo>();
				list.add(infoVo);
				groupMap.put(connId, list);
			}else{
				list.add(infoVo);
			}
		}
		logger.info("groupMap size:{}", groupMap.size());
		
		Map<String, ChIbProcessVo> processVoMap = new HashMap<String, ChIbProcessVo>();
		logger.info("start combine by grouping fugoData ");
		for (Entry<String, List<ChIbCallResultRecordInfoVo>> entry : groupMap.entrySet()) {
			String connId = entry.getKey();
			List<ChIbCallResultRecordInfoVo> list = entry.getValue();
			ChIbProcessVo procVo = new ChIbProcessVo();
			if(list!=null && list.size()>0){
				try {
					for (int i=0; i<list.size(); i++) {
						ChIbCallResultRecordInfoVo vo = list.get(i);
						if(i==0){
							procVo.setResultRecordInfoVo(vo);
						}
						if(i>0){
							String prodName = vo.getCssalename();
							if(!StringUtils.hasText(prodName)){
								continue;
							}
							ChIbCallResultRecordInfoVo cbnVo = procVo.getResultRecordInfoVo();
							if(!StringUtils.hasText(cbnVo.getCssalename())){
								cbnVo.setCscustomerid(vo.getCscustomerid());
								cbnVo.setComplaintid(vo.getComplaintid());
								cbnVo.setCsreferenceno(vo.getCsreferenceno());
								cbnVo.setCssaleno(vo.getCssaleno());
								cbnVo.setCssalename(vo.getCssalename());
								cbnVo.setComplaintclassname(vo.getComplaintclassname());
								cbnVo.setComplaintreasonname(vo.getComplaintreasonname());
								cbnVo.setComplaintdesc(vo.getComplaintdesc());
								cbnVo.setCscreateddate(vo.getCscreateddate());
							}
//							if(!StringUtils.hasText(cbnVo.getProductName2()) && 
//									!prodName.equals(cbnVo.getProductName())){
//								cbnVo.setComplaintCustomerNo2(vo.getComplaintCustomerNo());
//								cbnVo.setComplaintNo2(vo.getComplaintNo());
//								cbnVo.setComplaintAssociativityNo2(vo.getComplaintAssociativityNo());
//								cbnVo.setSaleNo2(vo.getSaleNo());
//								cbnVo.setProductName2(vo.getProductName());
//								cbnVo.setComplaintMainReasonClass2(vo.getComplaintMainReasonClass());
//								cbnVo.setComplaintMainReason2(vo.getComplaintMainReason());
//								cbnVo.setComplaintContent2(vo.getComplaintContent());
//								cbnVo.setComplaintCreateDt2(vo.getComplaintCreateDt());
//							}
						}
					}
					//setting info
					settingProcessData(procVo, speechTaskRecsDirPath);
					logger.info("procVo:{}", procVo);
					//cvt TC wav
					WavUtil.cvtChWav(procVo.getSrcWavPath(), speechTaskRecsDirPath, cvtWavWorkDirPath);

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
	
	private void settingProcessData(ChIbProcessVo processVo, String speechTaskRecsDirPath){
		ChIbCallResultRecordInfoVo infoVo = processVo.getResultRecordInfoVo();
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
			Map<String, ChIbProcessVo> processVoMap, 
			FileObject dimensionTaskDimFo,
			FileObject speechTaskDimFo){
		String spliter = "|";
		StringBuilder speechText = new StringBuilder();
		speechText.append("电话流水号|FilePath");
		StringBuilder dimensionText = new StringBuilder();
		//OLD
//		dimensionText
//		.append("任务流水号")
//		.append("|录音列表")
//		.append("|DATA_LOCATION")
//		.append("|小結清單第二層")
//		.append("|小結清單")
//		.append("|小結")
//		.append("|部")
//		.append("|單位")
//		.append("|組別")
//		.append("|接洽人員")
//		.append("|通話秒數")
//		.append("|聯繫時間")
//		.append("|結束時間")
//		.append("|完成時間")
//		.append("|CTI位置")
//		.append("|客戶代號")
//		.append("|關聯單號")
//		.append("|服務通路")
//		.append("|ConnectionID")
//		.append("|客訴客代")
//		.append("|客訴編號")
//		.append("|關聯編號")
//		.append("|銷售編號")
//		.append("|商品名稱")
//		.append("|主因分類")
//		.append("|客訴主因")
//		.append("|客訴內容")
//		.append("|建立者單位")
//		.append("|建立者組別")
//		.append("|客訴建立者")
//		.append("|客訴建立日")
//		.append("|開始時間")
//		.append("|下一通開始時間")
//		.append("|客訴客代2")
//		.append("|客訴編號2")
//		.append("|關聯編號2")
//		.append("|銷售編號2")
//		.append("|商品名稱2")
//		.append("|主因分類2")
//		.append("|客訴主因2")
//		.append("|客訴內容2")
//		.append("|客訴建立日2")
//		;
		dimensionText
		.append("任务流水号").append(spliter)
		.append("录音列表").append(spliter)
		.append("DATA_LOCATION").append(spliter)
		.append("CWCNAME1").append(spliter)
		.append("CWCNAME2").append(spliter)
		.append("CWCNAME3").append(spliter)
		.append("DEPTNAME1").append(spliter)
		.append("DEPTNAME2").append(spliter)
		.append("DEPTNAME3").append(spliter)
		.append("USERNAME").append(spliter)
		.append("TALKTIME").append(spliter)
		.append("CONTACTDATE").append(spliter)
		.append("ENDDATE").append(spliter)
		.append("DONEDATE").append(spliter)
		.append("CTIPLACE").append(spliter)
		.append("CUSTOMERID").append(spliter)
		.append("REFERENCEID").append(spliter)
		.append("CHANNELNAME").append(spliter)
		.append("CONNID").append(spliter)
		.append("CSCUSTOMERID").append(spliter)
		.append("COMPLAINTID").append(spliter)
		.append("CSREFERENCENO").append(spliter)
		.append("CSSALENO").append(spliter)
		.append("CSSALENAME").append(spliter)
		.append("COMPLAINTCLASSNAME").append(spliter)
		.append("COMPLAINTREASONNAME").append(spliter)
		.append("COMPLAINTDESC").append(spliter)
		.append("CSDEPTNAME").append(spliter)
		.append("CSDEPTNAME3").append(spliter)
		.append("CSCREATEDBY").append(spliter)
		.append("CSCREATEDDATE").append(spliter)
		.append("RETURNDATE").append(spliter)
		.append("RETURNID").append(spliter)
		.append("ORDERTYPE").append(spliter)
		.append("RETURNTYPE").append(spliter)
		.append("RTNSALENO").append(spliter)
		.append("RTNSALENAME").append(spliter)
		.append("RETURNCATEGORY").append(spliter)
		.append("returnReason").append(spliter)
		.append("returnRemark").append(spliter)
		.append("RTNLCLASSNAME").append(spliter)
		.append("RTNMCLASSNAME").append(spliter)
		.append("RTNSCLASSNAME").append(spliter)
		.append("RTNDEPTNAME").append(spliter)
		.append("RTNDEPTNAME3").append(spliter)
		.append("RTNCREATEDBYID").append(spliter)
		.append("RTNCREATEDBY").append(spliter)
		.append("RTNSUPPLIERNAME").append(spliter)
		.append("RTNSALEPRICE").append(spliter)
		;
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
			
			for(Entry<String, ChIbProcessVo> entry : processVoMap.entrySet()){
				ChIbProcessVo processVo = entry.getValue();
				//ex:3241@@#.wav
				String baseFileName = processVo.getBaseFileName();
				ChIbCallResultRecordInfoVo vo = processVo.getResultRecordInfoVo();
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
				.append((vo.getCwcname1()==null)?"":vo.getCwcname1())
				.append("|")
				.append((vo.getCwcname2()==null)?"":vo.getCwcname2())
				.append("|")
				.append((vo.getCwcname3()==null)?"":vo.getCwcname3())
				.append("|")
				.append((vo.getDeptname1()==null)?"":vo.getDeptname1())
				.append("|")
				.append((vo.getDeptname2()==null)?"":vo.getDeptname2())
				.append("|")
				.append((vo.getDeptname3()==null)?"":vo.getDeptname3())
				.append("|")
				.append((vo.getUsername()==null)?"":vo.getUsername())
				.append("|")
				.append((vo.getTalktime()==null)?"":vo.getTalktime())
				.append("|")
				.append((vo.getContactdate()==null)?"":vo.getContactdate())
				.append("|")
				.append((vo.getEnddate()==null)?"":vo.getEnddate())
				.append("|")
				.append((vo.getDonedate()==null)?"":vo.getDonedate())
				.append("|")
				.append((vo.getCtiplace()==null)?"":vo.getCtiplace())
				.append("|")
				.append((vo.getCustomerid()==null)?"":vo.getCustomerid())
				.append("|")
				.append((vo.getReferenceid()==null)?"":vo.getReferenceid())
				.append("|")
				.append((vo.getChannelname()==null)?"":vo.getChannelname())
				.append("|")
				.append((vo.getConnid()==null)?"":vo.getConnid())
				.append("|")
				.append((vo.getCscustomerid()==null)?"":vo.getCscustomerid())
				.append("|")
				.append((vo.getComplaintid()==null)?"":vo.getComplaintid())
				.append("|")
				.append((vo.getCsreferenceno()==null)?"":vo.getCsreferenceno())
				.append("|")
				.append((vo.getCssaleno()==null)?"":vo.getCssaleno())
				.append("|")
				.append((vo.getCssalename()==null)?"":vo.getCssalename())
				.append("|")
				.append((vo.getComplaintclassname()==null)?"":vo.getComplaintclassname())
				.append("|")
				.append((vo.getComplaintreasonname()==null)?"":vo.getComplaintreasonname())
				.append("|")
				.append((vo.getComplaintdesc()==null)?"":vo.getComplaintdesc())
				.append("|")
				.append((vo.getCsdeptname()==null)?"":vo.getCsdeptname())
				.append("|")
				.append((vo.getCsdeptname3()==null)?"":vo.getCsdeptname3())
				.append("|")
				.append((vo.getCscreatedby()==null)?"":vo.getCscreatedby())
				.append("|")
				.append((vo.getCscreateddate()==null)?"":vo.getCscreateddate())
				.append("|")
				.append((vo.getReturndate()==null)?"":vo.getReturndate())
				.append("|")
				.append((vo.getReturnid()==null)?"":vo.getReturnid())
				.append("|")
				.append((vo.getOrdertype()==null)?"":vo.getOrdertype())
				.append("|")
				.append((vo.getReturntype()==null)?"":vo.getReturntype())
				.append("|")
				.append((vo.getRtnsaleno()==null)?"":vo.getRtnsaleno())
				.append("|")
				.append((vo.getRtnsalename()==null)?"":vo.getRtnsalename())
				.append("|")
				.append((vo.getReturncategory()==null)?"":vo.getReturncategory())
				.append("|")
				.append((vo.getReturnreason()==null)?"":vo.getReturnreason())
				.append("|")
				.append((vo.getReturnremark()==null)?"":vo.getReturnremark())
				.append("|")
				.append((vo.getRtnlclassName()==null)?"":vo.getRtnlclassName())
				.append("|")
				.append((vo.getRtnmclassName()==null)?"":vo.getRtnmclassName())
				.append("|")
				.append((vo.getRtnsclassName()==null)?"":vo.getRtnsclassName())
				.append("|")
				.append((vo.getRtndeptname()==null)?"":vo.getRtndeptname())
				.append("|")
				.append((vo.getRtndeptname3()==null)?"":vo.getRtndeptname3())
				.append("|")
				.append((vo.getRtncreatedbyid()==null)?"":vo.getRtncreatedbyid())
				.append("|")
				.append((vo.getRtncreatedby()==null)?"":vo.getRtncreatedby())
				.append("|")
				.append((vo.getRtnsuppliername()==null)?"":vo.getRtnsuppliername())
				.append("|")
				.append((vo.getRtnsaleprice()==null)?"":vo.getRtnsaleprice())
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
					AppConst.chIbPsttFtpUsername, 
					AppConst.chIbPsttFtpPassword, 
					AppConst.chIbPsttFtpHost, 
					AppConst.chIbPsttFtpPort, 
					destFilePath);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new RuntimeException("upload failed");
		}
	}
	
	private void upload(String srcDirPath, String destDirPath){
		try {
			FtpUtil.uploadDir(
					AppConst.chIbPsttFtpUsername, 
					AppConst.chIbPsttFtpPassword, 
					AppConst.chIbPsttFtpHost, 
					AppConst.chIbPsttFtpPort, 
					destDirPath, 
					srcDirPath);
		} catch (Exception e) {
			logger.error("{}", e);
			throw new RuntimeException("upload failed");
		}
	}
}
