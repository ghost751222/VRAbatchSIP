package com.macaron.vra.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.VFS;
import org.apache.commons.vfs2.provider.local.LocalFile;
import org.apache.commons.vfs2.provider.local.LocalFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Files;

public class WavUtil {

	private static final Logger logger = LoggerFactory.getLogger(WavUtil.class);
	
	public static String cvtGqWav(String srcFilePath, String destDirPath, String workDirPath) throws IOException, InterruptedException{
		logger.info("{}", destDirPath);
		FileObject destDirFo = FileSystemUtil.createFolder(destDirPath);
		logger.info("{}", workDirPath);
		FileObject workDirFo = FileSystemUtil.createFolder(workDirPath);
		//clean before start		
		workDirFo.delete(Selectors.SELECT_CHILDREN);
		
		FileSystemManager fsm = VFS.getManager();
		FileObject srcFo = fsm.resolveFile(Paths.get(srcFilePath).toUri());
		if(srcFo==null || !srcFo.exists()){
			throw new RuntimeException("File doesn`t exist:" + srcFilePath);
		}
//		String baseName = srcFo.getName().getBaseName();
		String baseName = Paths.get(srcFilePath).getFileName().toString();
		logger.info("{}", baseName);
		//copy src to workDir
		String workFilePath = workDirPath + "/" + baseName;
		logger.info("{}", workFilePath);
		FileObject workFo = fsm.resolveFile(Paths.get(workFilePath).toUri());
		workFo.copyFrom(srcFo, Selectors.SELECT_SELF);

		String fileName = baseName.substring(0, baseName.lastIndexOf("."));
		logger.info("{}", fileName);
		
		String g7231Suffix = ".g723_1";
		String wavSuffix = ".wav";
		String g7231LeftPath = workDirPath + "/" + fileName + "_L" + g7231Suffix;
		String g7231RightPath = workDirPath + "/" + fileName + "_R" + g7231Suffix;
		String g7231LeftWavPath = workDirPath + "/" + fileName + "_L" + wavSuffix;
		String g7231RightWavPath = workDirPath + "/" + fileName + "_R" + wavSuffix;
		
		String destFilePath = destDirPath + "/" + baseName;
		logger.info("{}", destFilePath);
		
		StringBuilder cmd = new StringBuilder();
		//g723 split
		cmd
		.append("G72xStereoSplit.exe ")
		.append("\"")
		.append(workFilePath)
		.append("\" ")
		.append("2 244 ")
		.append("\"")
		.append(g7231LeftPath)
		.append("\" ")
		.append("\"")
		.append(g7231RightPath)
		.append("\" ")
		;
		execCmd(new String(cmd.toString().getBytes("UTF-8"), "MS950"));
		cmd.delete(0, cmd.length());
		//convert to wav
		cmd
		.append("ffmpeg -y -acodec g723_1 -f g723_1 -i ")
		.append("\"")
		.append(g7231LeftPath)
		.append("\" ")
		.append("\"")
		.append(g7231LeftWavPath)
		.append("\" ")
		;
		execCmd(new String(cmd.toString().getBytes("UTF-8"), "MS950"));
		cmd.delete(0, cmd.length());
		//convert to wav
		cmd
		.append("ffmpeg -y -acodec g723_1 -f g723_1 -i ")
		.append("\"")
		.append(g7231RightPath)
		.append("\" ")
		.append("\"")
		.append(g7231RightWavPath)
		.append("\" ")
		;
		execCmd(new String(cmd.toString().getBytes("UTF-8"), "MS950"));
		cmd.delete(0, cmd.length());
		//combine two to one wav
		cmd
		.append("ffmpeg -y ")
		.append("-i \"")
		.append(g7231LeftWavPath)
		.append("\" ")
		.append("-i \"")
		.append(g7231RightPath)
		.append("\" ")
		.append("-filter_complex \"[0]apad[a];[a][1]amerge[aout]\" -map \"[aout]\" ")
		.append("\"")
		.append(destFilePath)
		.append("\" ")
		;
		execCmd(new String(cmd.toString().getBytes("UTF-8"), "MS950"));
		cmd.delete(0, cmd.length());
		//clean after finished
		workDirFo.delete(Selectors.SELECT_SELF_AND_CHILDREN);
		
		return destFilePath;
	}
	
	public static String cvtTcWav(String srcFilePath, String destDirPath, String workDirPath) throws IOException, InterruptedException{
		logger.info("{}", destDirPath);
		FileObject destDirFo = FileSystemUtil.createFolder(destDirPath);
		logger.info("{}", workDirPath);
		FileObject workDirFo = FileSystemUtil.createFolder(workDirPath);
		//clean before start		
		workDirFo.delete(Selectors.SELECT_CHILDREN);
		
		FileSystemManager fsm = VFS.getManager();
		FileObject srcFo = fsm.resolveFile(Paths.get(srcFilePath).toUri());
		if(srcFo==null || !srcFo.exists()){
			throw new RuntimeException("File doesn`t exist:" + srcFilePath);
		}
//		String baseName = srcFo.getName().getBaseName();
		String baseName = Paths.get(srcFilePath).getFileName().toString();
		logger.info("{}", baseName);
		//copy src to workDir
		String workFilePath = workDirPath + "/" + baseName;
		logger.info("{}", workFilePath);
		FileObject workFo = fsm.resolveFile(Paths.get(workFilePath).toUri());
		workFo.copyFrom(srcFo, Selectors.SELECT_SELF);

		String fileName = baseName.substring(0, baseName.lastIndexOf("."));
		logger.info("{}", fileName);

		String destFilePath = destDirPath + "/" + baseName;
		logger.info("{}", destFilePath);
		
		StringBuilder cmd = new StringBuilder();
		//convert to wav
		cmd
		.append("ffmpeg -y -i ")
		.append("\"")
		.append(workFilePath)
		.append("\" ")
		.append("\"")
		.append(destFilePath)
		.append("\" ")
		;
		execCmd(cmd.toString());
		//clean after finished
		workDirFo.delete(Selectors.SELECT_SELF_AND_CHILDREN);
		
		return destFilePath;
	}
	
	public static String cvtChWav(String srcFilePath, String destDirPath, String workDirPath) throws IOException, InterruptedException{
		logger.info("{}", destDirPath);
		FileObject destDirFo = FileSystemUtil.createFolder(destDirPath);
		logger.info("{}", workDirPath);
		FileObject workDirFo = FileSystemUtil.createFolder(workDirPath);
		//clean before start		
		workDirFo.delete(Selectors.SELECT_CHILDREN);
		
		FileSystemManager fsm = VFS.getManager();
		FileObject srcFo = fsm.resolveFile(Paths.get(srcFilePath).toUri());
		if(srcFo==null || !srcFo.exists()){
			throw new RuntimeException("File doesn`t exist:" + srcFilePath);
		}
//		String baseName = srcFo.getName().getBaseName();
		String baseName = Paths.get(srcFilePath).getFileName().toString();
		logger.info("{}", baseName);
		//copy src to workDir
		String workFilePath = workDirPath + "/" + baseName;
		logger.info("{}", workFilePath);
		FileObject workFo = fsm.resolveFile(Paths.get(workFilePath).toUri());
		workFo.copyFrom(srcFo, Selectors.SELECT_SELF);

		String fileName = baseName.substring(0, baseName.lastIndexOf("."));
		logger.info("{}", fileName);

		String destFilePath = destDirPath + "/" + baseName;
		logger.info("{}", destFilePath);
		
		StringBuilder cmd = new StringBuilder();
		//convert to wav
		cmd
		.append("ffmpeg -y -i ")
		.append("\"")
		.append(workFilePath)
		.append("\" -ar 8000 ")
		.append("\"")
		.append(destFilePath)
		.append("\" ")
		;
		execCmd(cmd.toString());
		//clean after finished
		workDirFo.delete(Selectors.SELECT_SELF_AND_CHILDREN);
		
		return destFilePath;
	}
	
	public static String cvtJbWav(String srcFilePath, String destDirPath, String workDirPath) throws IOException, InterruptedException{
		logger.info("{}", destDirPath);
		FileObject destDirFo = FileSystemUtil.createFolder(destDirPath);
		logger.info("{}", workDirPath);
		FileObject workDirFo = FileSystemUtil.createFolder(workDirPath);
		//clean before start		
		workDirFo.delete(Selectors.SELECT_CHILDREN);
		
		FileSystemManager fsm = VFS.getManager();
		FileObject srcFo = fsm.resolveFile(Paths.get(srcFilePath).toUri());
		if(srcFo==null || !srcFo.exists()){
			throw new RuntimeException("File doesn`t exist:" + srcFilePath);
		}
//		String baseName = srcFo.getName().getBaseName();
		logger.info("{}",srcFilePath);
		String baseName = Paths.get(srcFilePath).getFileName().toString();
		logger.info("{}", baseName);
		//copy src to workDir
		String workFilePath = workDirPath + "/" + baseName;
		logger.info("{}", workFilePath);
		FileObject workFo = fsm.resolveFile(Paths.get(workFilePath).toUri());
		workFo.copyFrom(srcFo, Selectors.SELECT_SELF);

		String fileName = baseName.substring(0, baseName.lastIndexOf("."));
		logger.info("{}", fileName);

		String destFilePath = destDirPath + "/" + baseName;
		logger.info("{}", destFilePath);
		
		StringBuilder cmd = new StringBuilder();
		//convert to wav
		cmd
		.append("ffmpeg -y -i ")
		.append("\"")
		.append(workFilePath)
		.append("\" ")
		.append("\"")
		.append(destFilePath)
		.append("\" ")
		;
		execCmd(cmd.toString());
		//clean after finished
		workDirFo.delete(Selectors.SELECT_SELF_AND_CHILDREN);
		
		return destFilePath;
	}
	
	public static String cvtBqWav(String srcFilePath, String destDirPath, String workDirPath) throws IOException, InterruptedException{
		logger.info("{}", destDirPath);
		FileObject destDirFo = FileSystemUtil.createFolder(destDirPath);
		logger.info("{}", workDirPath);
		FileObject workDirFo = FileSystemUtil.createFolder(workDirPath);
		//clean before start		
		workDirFo.delete(Selectors.SELECT_CHILDREN);
		
		FileSystemManager fsm = VFS.getManager();
		FileObject srcFo = fsm.resolveFile(Paths.get(srcFilePath).toUri());
		if(srcFo==null || !srcFo.exists()){
			throw new RuntimeException("File doesn`t exist:" + srcFilePath);
		}
//		String baseName = srcFo.getName().getBaseName();
		logger.info("{}",srcFilePath);
		String baseName = Paths.get(srcFilePath).getFileName().toString();
		logger.info("{}", baseName);
		//copy src to workDir
		String workFilePath = workDirPath + "/" + baseName;
		logger.info("{}", workFilePath);
		FileObject workFo = fsm.resolveFile(Paths.get(workFilePath).toUri());
		workFo.copyFrom(srcFo, Selectors.SELECT_SELF);

		String fileName = baseName.substring(0, baseName.lastIndexOf("."));
		logger.info("{}", fileName);

		String destFilePath = destDirPath + "/" + baseName;
		logger.info("{}", destFilePath);
		
		StringBuilder cmd = new StringBuilder();
		//convert to wav
		cmd
		.append("ffmpeg -y -i ")
		.append("\"")
		.append(workFilePath)
		.append("\" ")
		.append("\"")
		.append(destFilePath)
		.append("\" ")
		;
		execCmd(cmd.toString());
		//clean after finished
		workDirFo.delete(Selectors.SELECT_SELF_AND_CHILDREN);
		
		return destFilePath;
	}

	public static String cvtTc2Wav(String srcFilePath, String destDirPath, String workDirPath) throws IOException, InterruptedException{
		logger.info("{}", destDirPath);
		FileObject destDirFo = FileSystemUtil.createFolder(destDirPath);
		logger.info("{}", workDirPath);
		FileObject workDirFo = FileSystemUtil.createFolder(workDirPath);
		//clean before start		
		workDirFo.delete(Selectors.SELECT_CHILDREN);
		
		FileSystemManager fsm = VFS.getManager();
		FileObject srcFo = fsm.resolveFile(Paths.get(srcFilePath).toUri());
		if(srcFo==null || !srcFo.exists()){
			throw new RuntimeException("File doesn`t exist:" + srcFilePath);
		}
//		String baseName = srcFo.getName().getBaseName();
		logger.info("{}",srcFilePath);
		String baseName = Paths.get(srcFilePath).getFileName().toString();
		logger.info("{}", baseName);
		//copy src to workDir
		String workFilePath = workDirPath + "/" + baseName;
		logger.info("{}", workFilePath);
		FileObject workFo = fsm.resolveFile(Paths.get(workFilePath).toUri());
		workFo.copyFrom(srcFo, Selectors.SELECT_SELF);

		String fileName = baseName.substring(0, baseName.lastIndexOf("."));
		logger.info("{}", fileName);

		String destFilePath = destDirPath + "/" + baseName;
		logger.info("{}", destFilePath);
		
		StringBuilder cmd = new StringBuilder();
		//convert to wav
		cmd
		.append("ffmpeg -y -i ")
		.append("\"")
		.append(workFilePath)
		.append("\" ")
		.append("\"")
		.append(destFilePath)
		.append("\" ")
		;
		execCmd(cmd.toString());
		//clean after finished
		workDirFo.delete(Selectors.SELECT_SELF_AND_CHILDREN);
		
		return destFilePath;
	}

	
	private static void execCmd(String cmd) throws IOException, InterruptedException{
		logger.info("cmd:{}", cmd);
		
		List<String> cmdList = new ArrayList<String>();
		cmdList.add("cmd.exe");
		cmdList.add("/C");
		cmdList.add(cmd);
		
		Process proc = new ProcessBuilder(cmdList)
				.redirectErrorStream(true)
				.start();
		
		InputStream stdOut = proc.getInputStream();
		BufferedReader brStdOut = new BufferedReader(new InputStreamReader(stdOut, "MS950"));
		String stdLine = null;
		logger.info("<wavCombineOutput>");
		while((stdLine = brStdOut.readLine()) != null){
			logger.info("{}", stdLine);
		}
		logger.info("</wavCombineOutput>");
		
		InputStream errOut = proc.getErrorStream();
		BufferedReader brErrOut = new BufferedReader(new InputStreamReader(errOut, "MS950"));
		String errLine = null;
		logger.info("<wavCombineOutput error>");
		while((errLine = brErrOut.readLine()) != null){
			logger.info("{}", errLine);
		}
		logger.info("</wavCombineOutput error>");
		
		int exitVal = proc.waitFor();
		logger.info("exitVal:{}", exitVal);
	}
	
	public static void cvtGq(){
		try {
			logger.info(System.getProperty("user.dir"));
			String srcFilePath = "C:/installdata/sam/workspace/VRAbatch/batchWorkTmp/test/gq/2FQ}!))A00420337.WAV";
//			String srcFilePath = "D:/20180712亞梅/2C@E${OQ05320337-OK.WAV";
//			String srcFilePath = "C:/installdata/sam/workspace/VRAbatch/GQ.wav";
			String destFilePath = "C:/installdata/sam/workspace/VRAbatch/cvtWavDest";
//			String destFilePath = "D:/20180712亞梅-GQ";
			String workDirPath = "C:/installdata/sam/workspace/VRAbatch//cvtWavTmp";

			WavUtil.cvtGqWav(
					srcFilePath,
					destFilePath,
					workDirPath);
			if(srcFilePath!=null){
				return;
			}
			
			String srcDirPath = "D:/20180712yamay";
			String destDirPath = "D:/20180712yamay-ok";
			logger.info("{}", destDirPath);
			FileSystemManager fsm = VFS.getManager();
			FileObject srcFo = fsm.resolveFile(Paths.get(srcDirPath).toUri());
			if(srcFo==null || !srcFo.exists()){
				throw new RuntimeException("File doesn`t exist:" + srcDirPath);
			}
			
			FileObject[] fos = srcFo.findFiles(Selectors.SELECT_FILES);
			for (FileObject fo : fos) {
//				logger.info("{}", fo);
//				logger.info("{}", fo.getURL().getFile());
//				logger.info("{}", new File(fo.getURL().getFile()).getAbsoluteFile());
//				String subFilePath = fo.getName().getPath();
//				logger.info("{}", subFilePath);
				String fullSrcFilePath = new File(URLDecoder.decode(fo.getURL().getFile(), "UTF-8")).getAbsolutePath().replaceAll("\\\\", "/");
				logger.info("{}", fullSrcFilePath);
				WavUtil.cvtGqWav(
						fullSrcFilePath,
						destDirPath,
						workDirPath);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void cvtTc(){
		try {
			logger.info(System.getProperty("user.dir"));
			String srcFilePath = "C:/installdata/sam/workspace/VRAbatch/TC.wav";
			String destFilePath = "C:/installdata/sam/workspace/VRAbatch/cvtWavDest";
			String workDirPath = "C:/installdata/sam/workspace/VRAbatch/cvtWavTmp";
			WavUtil.cvtTcWav(
					srcFilePath,
					destFilePath,
					workDirPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void cvtCh(){
		try {
			logger.info(System.getProperty("user.dir"));
			
//			String srcFilePath = "C:/installdata/sam/workspace/VRAbatch/batchWorkTmp/2E#C(S%25Q00720337.WAV";
			String srcFilePath = "\\\\192.168.252.1/Converter/Recording/TC/73201/2018-05-03/010a02bd16b9a2c1_73201_496910983765968.wav";
			String destFilePath = "C:/installdata/sam/workspace/VRAbatch/cvtWavDest";
			String workDirPath = "C:/installdata/sam/workspace/VRAbatch/cvtWavTmp";
			WavUtil.cvtChWav(
					srcFilePath,
					destFilePath,
					workDirPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void cvtJb(){
		try {
			logger.info(System.getProperty("user.dir"));
			String srcFilePath = "C:/installdata/sam/workspace/VRAbatch/JB.wav";
			String destFilePath = "C:/installdata/sam/workspace/VRAbatch/cvtWavDest";
			String workDirPath = "C:/installdata/sam/workspace/VRAbatch/cvtWavTmp";
			WavUtil.cvtJbWav(
					srcFilePath,
					destFilePath,
					workDirPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void mergeWav(File srcFile1, File srcFile2, File destFile){
		logger.info("mergeWav - src1:{}, src2:{}, dest:{}",srcFile1,srcFile2,destFile);
		try{
		  AudioFileFormat aff = AudioSystem.getAudioFileFormat(srcFile1);
		  AudioInputStream ais1 = AudioSystem.getAudioInputStream(srcFile1); 
		  AudioInputStream ais2 = AudioSystem.getAudioInputStream(srcFile2);

		  SequenceInputStream sis = new SequenceInputStream(ais1, ais2); 

		  AudioSystem.write(new AudioInputStream(sis, aff.getFormat(), ais1.getFrameLength() 
		    + ais2.getFrameLength()), aff.getType(), destFile);
		  
		  if (ais1 != null) 
		   ais1.close(); 
		  if (ais2 != null) 
		   ais2.close(); 
		  if (sis != null) 
		   sis.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void copyWav(File src, File dest){
		logger.info("copyWav - src:{}, dest:{}",src,dest);
		try{
			Files.copy(src, dest);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		cvtGq();
//		cvtTc();
//		cvtCh();
	}
}
