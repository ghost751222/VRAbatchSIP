package com.macaron.vra.vo;

import java.io.Serializable;

public class GqObProcessVo implements Serializable {

	private GqObCallResultRecordInfoVo resultRecordInfoVo;
	
	private String subFilePath;
	private String baseFileName;
	private String srcWavPath;
	private String destWavPath;
	
	private String msg;

	@Override
	public String toString() {
		return "GqObProcessVo [resultRecordInfoVo=" + resultRecordInfoVo + ", subFilePath=" + subFilePath
				+ ", baseFileName=" + baseFileName + ", srcWavPath=" + srcWavPath + ", destWavPath=" + destWavPath
				+ ", msg=" + msg + "]";
	}

	public GqObCallResultRecordInfoVo getResultRecordInfoVo() {
		return resultRecordInfoVo;
	}

	public void setResultRecordInfoVo(GqObCallResultRecordInfoVo resultRecordInfoVo) {
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
