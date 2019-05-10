package com.macaron.vra.vo;

import java.util.Date;

public class AudiologResultRecordInfoVo {

	private String channel;
	private String channelName;
	private String fileName;
	private String location;
	private String dialedNumber;
	private String callerNumber;
	private String callDirection;
	private String spare04;

	@Override
	public String toString() {
		return "AudiologResultRecordInfoVo [channel=" + channel + "channelName=" + channelName + ", fileName=" + fileName + ", location="
				+ location + ", dialedNumber=" + dialedNumber + ", callerNumber=" + callerNumber + ", callDirection="
				+ callDirection + ", spare04=" + spare04 + "]";
	}
	

	public String getChannel() {
		if(channel.length()==1)
			return"00"+channel;
		else if(channel.length()==2)
			return "0"+channel;
		else
			return channel;
	}


	public void setChannel(String channel) {
		this.channel = channel;
	}


	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDialedNumber() {
		return dialedNumber;
	}

	public void setDialedNumber(String dialedNumber) {
		this.dialedNumber = dialedNumber;
	}

	public String getCallerNumber() {
		return callerNumber;
	}

	public void setCallerNumber(String callerNumber) {
		this.callerNumber = callerNumber;
	}

	public String getCallDirection() {
		return callDirection;
	}

	public void setCallDirection(String callDirection) {
		this.callDirection = callDirection;
	}

	public String getSpare04() {
		return spare04;
	}

	public void setSpare04(String spare04) {
		this.spare04 = spare04;
	}

}