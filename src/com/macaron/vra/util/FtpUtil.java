package com.macaron.vra.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.Selectors;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;

public class FtpUtil {

	private static String createSftpConnectionStr(
			String userName, String password, String hostName, int port, String destPath) throws URISyntaxException{
		String uri = new URI(
				"sftp",
				userName + ":" + password,
				hostName,
				port,
				destPath,
				null,
				null
				).toString();
		
		return uri;
	}
	
	private static FileSystemOptions createSftpOpts() throws FileSystemException{
		FileSystemOptions opts = new FileSystemOptions();
		SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
		SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
		SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 1000000);
		return opts;
	}
	
	public static boolean exists(
			String userName, String password, String hostName, int port, String destPath){
		StandardFileSystemManager manager = new StandardFileSystemManager();
		FileObject remoteFile = null;
		try {
			manager.init();
			
			remoteFile = manager.resolveFile(
					createSftpConnectionStr(userName, password, hostName, port, destPath),
					createSftpOpts()
					);
			if(remoteFile!=null && remoteFile.exists()){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void upload(
			String userName, String password, String hostName, int port, String destPath, InputStream is){
		if(is==null){
			throw new RuntimeException("no input ");
		}
		StandardFileSystemManager manager = new StandardFileSystemManager();
		FileObject remoteFile = null;
		try {
			manager.init();
			
			remoteFile = manager.resolveFile(
					createSftpConnectionStr(userName, password, hostName, port, destPath),
					createSftpOpts()
					);
//			try(OutputStream os = remoteFile.getContent().getOutputStream()){
//				IOUtils.copyLarge(is, os);
//				os.flush();
//			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void upload(
			String userName, String password, String hostName, int port, String destPath, String localPath){
		StandardFileSystemManager manager = new StandardFileSystemManager();
		FileObject remoteFile = null;
		try {
			manager.init();
			
			FileObject localFile = manager.resolveFile(localPath);
			
			remoteFile = manager.resolveFile(
					createSftpConnectionStr(userName, password, hostName, port, destPath),
					createSftpOpts()
					);
			
			remoteFile.copyFrom(localFile, Selectors.SELECT_SELF);
//			try(OutputStream os = remoteFile.getContent().getOutputStream()){
//				IOUtils.copyLarge(is, os);
//				os.flush();
//			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			manager.close();
		}
	}
	
	public static void uploadDir(
			String userName, String password, String hostName, int port, String destPath, String localDirPath){
		StandardFileSystemManager manager = new StandardFileSystemManager();
		FileObject remoteFile = null;
		try {
			manager.init();
			
			FileObject localDirFile = manager.resolveFile(localDirPath);
			
			remoteFile = manager.resolveFile(
					createSftpConnectionStr(userName, password, hostName, port, destPath),
					createSftpOpts()
					);
			
			remoteFile.copyFrom(localDirFile, Selectors.SELECT_ALL);
//			try(OutputStream os = remoteFile.getContent().getOutputStream()){
//				IOUtils.copyLarge(is, os);
//				os.flush();
//			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}finally{
			manager.close();
		}
	}
	
	public static void main(String[] args) {
		try {
			FtpUtil.uploadDir(
					"root", 
					"1qaz@WSX", 
					"192.168.202.11", 
					22,
					"/root/PSAE2837/DataSource", 
					"C:/installdata/sam/workspace/FugoDataSyncBatch/uploadPrepareOb");
		} catch (Exception e) {
			throw new RuntimeException("upload failed");
		}
	}
}
