package com.macaron.vra.vo;

import java.io.Serializable;
import java.util.List;

public class ChObProcessVo implements Serializable {

	private ChObCallResultRecordInfoVo resultRecordInfoVo;
	
	private String subFilePath;
	private String baseFileName;
	private String srcWavPath;
	private String destWavPath;
	
	private String msg;

	@Override
	public String toString() {
		return "ChObProcessVo [resultRecordInfoVo=" + resultRecordInfoVo + ", subFilePath=" + subFilePath
				+ ", baseFileName=" + baseFileName + ", srcWavPath=" + srcWavPath + ", destWavPath=" + destWavPath
				+ ", msg=" + msg + "]";
	}

	public ChObCallResultRecordInfoVo getResultRecordInfoVo() {
		return resultRecordInfoVo;
	}

	public void setResultRecordInfoVo(ChObCallResultRecordInfoVo resultRecordInfoVo) {
		this.resultRecordInfoVo = resultRecordInfoVo;
	}

	public String getSubFilePath() {
		return subFilePath;
	}

	public void setSubFilePath(String subFilePath) {
		this.subFilePath = subFilePath;
	}

	public String getBaseFileName() {
		return baseFileName;
	}

	public void setBaseFileName(String baseFileName) {
		this.baseFileName = baseFileName;
	}

	public String getSrcWavPath() {
		return srcWavPath;
	}

	public void setSrcWavPath(String srcWavPath) {
		this.srcWavPath = srcWavPath;
	}

	public String getDestWavPath() {
		return destWavPath;
	}

	public void setDestWavPath(String destWavPath) {
		this.destWavPath = destWavPath;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
