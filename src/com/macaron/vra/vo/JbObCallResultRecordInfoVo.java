package com.macaron.vra.vo;

import java.util.Date;

public class JbObCallResultRecordInfoVo {

	private Date dataDt;
	private String dataLocation;//資料區域
	private String serialNum; //序號
	
	private String contactgroup; //群組
	private String cwcname; //小結清單
	private String deptname1; //部
	private String deptname2; //單位
	private String deptname3; //組別
	private String username; //姓名
	private String attendbyid; //同仁客服編號
	private String contacttime; //通話秒數
	private String customerid; //客代
	private String contactdate; //聯繫時間
	private String enddate; //結束時間
	private String ctiplace; //CTI位置
	private String agentid; //人員ID
	private String oblevelname; //OB舊客會員等級
	private String salename; //品名
	private String saleno; //品號
	private String subchannelname; //訂單通路
	private String paymenttype; //付款方式
	private String payamount; //付款金額
	private String connid; //Connid
	
	//record
	private String fileName;
	
	@Override
	public String toString() {
		return "JbObCallResultRecordInfoVo [dataDt=" + dataDt + ", dataLocation=" + dataLocation + ", serialNum="
				+ serialNum + ", contactgroup=" + contactgroup + ", cwcname=" + cwcname
				+ ", deptname1=" + deptname1 + ", deptname2=" + deptname2 + ", deptname3=" + deptname3
				+ ", username=" + username + ", attendbyid=" + attendbyid + ", contacttime="
				+ contacttime + ", customerid=" + customerid + ", contactdate=" + contactdate
				+ ", enddate=" + enddate + ", ctiplace=" + ctiplace + ", agentid=" + agentid
				+ ", oblevelname=" + oblevelname + ", salename=" + salename + ", saleno="
				+ saleno + ", subchannelname=" + subchannelname + ", paymenttype=" + paymenttype
				+ ", payamount=" + payamount + ", connid=" + connid	+ "]";
	}
	public Date getDataDt() {
		return dataDt;
	}
	public void setDataDt(Date dataDt) {
		this.dataDt = dataDt;
	}
	public String getDataLocation() {
		return dataLocation;
	}
	public void setDataLocation(String dataLocation) {
		this.dataLocation = dataLocation;
	}
	public String getSerialNum() {
		return serialNum;
	}
	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	public String getContactgroup() {
		return contactgroup;
	}
	public void setContactgroup(String contactgroup) {
		this.contactgroup = contactgroup;
	}
	public String getCwcname() {
		return cwcname;
	}
	public void setCwcname(String cwcname) {
		this.cwcname = cwcname;
	}
	public String getDeptname1() {
		return deptname1;
	}
	public void setDeptname1(String deptname1) {
		this.deptname1 = deptname1;
	}
	public String getDeptname2() {
		return deptname2;
	}
	public void setDeptname2(String deptname2) {
		this.deptname2 = deptname2;
	}
	public String getDeptname3() {
		return deptname3;
	}
	public void setDeptname3(String deptname3) {
		this.deptname3 = deptname3;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAttendbyid() {
		return attendbyid;
	}
	public void setAttendbyid(String attendbyid) {
		this.attendbyid = attendbyid;
	}
	public String getContacttime() {
		return contacttime;
	}
	public void setContacttime(String contacttime) {
		this.contacttime = contacttime;
	}
	public String getCustomerid() {
		return customerid;
	}
	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}
	public String getContactdate() {
		return contactdate;
	}
	public void setContactdate(String contactdate) {
		this.contactdate = contactdate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getCtiplace() {
		return ctiplace;
	}
	public void setCtiplace(String ctiplace) {
		this.ctiplace = ctiplace;
	}
	public String getAgentid() {
		return agentid;
	}
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	public String getOblevelname() {
		return oblevelname;
	}
	public void setOblevelname(String oblevelname) {
		this.oblevelname = oblevelname;
	}
	public String getSalename() {
		return salename;
	}
	public void setSalename(String salename) {
		this.salename = salename;
	}
	public String getSaleno() {
		return saleno;
	}
	public void setSaleno(String saleno) {
		this.saleno = saleno;
	}
	public String getSubchannelname() {
		return subchannelname;
	}
	public void setSubchannelname(String subchannelname) {
		this.subchannelname = subchannelname;
	}
	public String getPaymenttype() {
		return paymenttype;
	}
	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}
	public String getPayamount() {
		return payamount;
	}
	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}
	public String getConnid() {
		return connid;
	}
	public void setConnid(String connid) {
		this.connid = connid;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
