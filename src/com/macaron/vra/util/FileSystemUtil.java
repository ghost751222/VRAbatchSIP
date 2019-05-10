package com.macaron.vra.util;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileSystemUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileSystemUtil.class);

	public static FileObject createFolder(String uri) throws FileSystemException{
		FileSystemManager fsm = VFS.getManager();
		
		FileObject fo = fsm.resolveFile(uri);
//		logger.info("{}", fo.exists());
		if(fo==null || !fo.exists()){
			fo.createFolder();
		}
		if(!fo.isFolder()){
			throw new RuntimeException("File is not a folder:" + uri);
		}
		
		return fo;
	}

	public static FileObject createFile(String uri) throws FileSystemException{
		FileSystemManager fsm = VFS.getManager();
		
		FileObject fo = fsm.resolveFile(uri);
		logger.info("{}", fo);
		if(fo==null || !fo.exists()){
			fo.createFile();
		}
		if(!fo.isFile()){
			throw new RuntimeException("File is not a file:" + uri);
		}
		
		return fo;
	}
}
