package com.macaron.vra.vo;

import java.util.Date;

public class ChIbCallResultRecordInfoVo {

	private Date dataDt;
	private String dataLocation; // 資料區域
	private String cwcname1; // 小結清單第二層
	private String cwcname2; // 小結清單
	private String cwcname3; // 小結
	private String deptname1; // 部
	private String deptname2; // 單位
	private String deptname3; // 組別
	private String username; // 接洽人員
	private String talktime; // 通話秒數
	private String contactdate; // 聯繫時間
	private String enddate; // 結束時間
	private String donedate; // 完成時間
	private String ctiplace; // CTI位置
	private String customerid; // 客戶代號
	private String referenceid; // 關聯單號
	private String channelname; // 服務通路
	private String connid; // ConnectionID
	private String cscustomerid; // 客訴客代
	private String complaintid; // 客訴編號
	private String csreferenceno; // 關聯編號
	private String cssaleno; // 銷售編號
	private String cssalename; // 商品名稱
	private String complaintclassname; // 主因分類
	private String complaintreasonname; // 客訴主因
	private String complaintdesc; // 客訴內容
	private String csdeptname; // 建立者單位
	private String csdeptname3; // 建立者組別
	private String cscreatedby; // 客訴建立者
	private String cscreateddate; // 客訴建立日
	// NEW APPEND
	private String returndate;// 銷退日期
	private String returnid;// 銷退單號
	private String ordertype;// 訂單類別
	private String returntype;// 銷退類別
	private String rtnsaleno;// 銷售編號
	private String rtnsalename;// 商品名稱
	private String returncategory;// 銷退主因
	private String returnreason;// 銷退原因
	private String returnremark;// 備註
	private String rtnlclassName;// 商品大分類
	private String rtnmclassName;// 商品中分類
	private String rtnsclassName;// 商品小分類
	private String rtndeptname;// 建單者單位
	private String rtndeptname3;// 建單者組別
	private String rtncreatedbyid;// 建單者代碼
	private String rtncreatedby;// 建單者姓名
	private String rtnsuppliername;// 廠商名
	private String rtnsaleprice;// 售價

	// record
	private String fileName;

	@Override
	public String toString() {
		return "ChIbCallResultRecordInfoVo [dataDt=" + dataDt + ", dataLocation=" + dataLocation + ", cwcname1="
				+ cwcname1 + ", cwcname2=" + cwcname2 + ", cwcname3=" + cwcname3 + ", deptname1=" + deptname1
				+ ", deptname2=" + deptname2 + ", deptname3=" + deptname3 + ", username=" + username + ", talktime="
				+ talktime + ", contactdate=" + contactdate + ", enddate=" + enddate + ", doneate=" + donedate
				+ ", ctiplace=" + ctiplace + ", customerid=" + customerid + ", referenceid=" + referenceid
				+ ", channelname=" + channelname + ", connid=" + connid + ", cscustomerid=" + cscustomerid
				+ ", complaintid=" + complaintid + ", csreferenceno=" + csreferenceno + ", cssaleno=" + cssaleno
				+ ", cssalename=" + cssalename + ", complaintclassname=" + complaintclassname + ", complaintreasonname="
				+ complaintreasonname + ", complaintdesc=" + complaintdesc + ", csdeptname=" + csdeptname
				+ ", csdeptname3=" + csdeptname3 + ", cscreatedby=" + cscreatedby + ", cscreateddate=" + cscreateddate
				+ ", returnDate=" + returndate + ", returnId=" + returnid + ", orderType=" + ordertype + ", returnType="
				+ returntype + ", rtnSaleNo=" + rtnsaleno + ", rtnSaleName=" + rtnsalename + ", returnCategory="
				+ returncategory + ", returnReason=" + returnreason + ", returnRemark=" + returnremark
				+ ", rtnLClassName=" + rtnlclassName + ", rtnMClassName=" + rtnmclassName + ", rtnSClassName="
				+ rtnsclassName + ", rtnDeptName=" + rtndeptname + ", rtnDeptName3=" + rtndeptname3
				+ ", rtnCreatedById=" + rtncreatedbyid + ", rtnCreatedBy=" + rtncreatedby + ", rtnSupplierName="
				+ rtnsuppliername + ", rtnSalePrice=" + rtnsaleprice + ", fileName=" + fileName + "]";
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

	public String getCwcname1() {
		return cwcname1;
	}

	public void setCwcname1(String cwcname1) {
		this.cwcname1 = cwcname1;
	}

	public String getCwcname2() {
		return cwcname2;
	}

	public void setCwcname2(String cwcname2) {
		this.cwcname2 = cwcname2;
	}

	public String getCwcname3() {
		return cwcname3;
	}

	public void setCwcname3(String cwcname3) {
		this.cwcname3 = cwcname3;
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

	public String getTalktime() {
		return talktime;
	}

	public void setTalktime(String talktime) {
		this.talktime = talktime;
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

	public String getDonedate() {
		return donedate;
	}

	public void setDonedate(String donedate) {
		this.donedate = donedate;
	}

	public String getCtiplace() {
		return ctiplace;
	}

	public void setCtiplace(String ctiplace) {
		this.ctiplace = ctiplace;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getReferenceid() {
		return referenceid;
	}

	public void setReferenceid(String referenceid) {
		this.referenceid = referenceid;
	}

	public String getChannelname() {
		return channelname;
	}

	public void setChannelname(String channelname) {
		this.channelname = channelname;
	}

	public String getConnid() {
		return connid;
	}

	public void setConnid(String connid) {
		this.connid = connid;
	}

	public String getCscustomerid() {
		return cscustomerid;
	}

	public void setCscustomerid(String cscustomerid) {
		this.cscustomerid = cscustomerid;
	}

	public String getComplaintid() {
		return complaintid;
	}

	public void setComplaintid(String complaintid) {
		this.complaintid = complaintid;
	}

	public String getCsreferenceno() {
		return csreferenceno;
	}

	public void setCsreferenceno(String csreferenceno) {
		this.csreferenceno = csreferenceno;
	}

	public String getCssaleno() {
		return cssaleno;
	}

	public void setCssaleno(String cssaleno) {
		this.cssaleno = cssaleno;
	}

	public String getCssalename() {
		return cssalename;
	}

	public void setCssalename(String cssalename) {
		this.cssalename = cssalename;
	}

	public String getComplaintclassname() {
		return complaintclassname;
	}

	public void setComplaintclassname(String complaintclassname) {
		this.complaintclassname = complaintclassname;
	}

	public String getComplaintreasonname() {
		return complaintreasonname;
	}

	public void setComplaintreasonname(String complaintreasonname) {
		this.complaintreasonname = complaintreasonname;
	}

	public String getComplaintdesc() {
		return complaintdesc;
	}

	public void setComplaintdesc(String complaintdesc) {
		this.complaintdesc = complaintdesc;
	}

	public String getCsdeptname() {
		return csdeptname;
	}

	public void setCsdeptname(String csdeptname) {
		this.csdeptname = csdeptname;
	}

	public String getCsdeptname3() {
		return csdeptname3;
	}

	public void setCsdeptname3(String csdeptname3) {
		this.csdeptname3 = csdeptname3;
	}

	public String getCscreatedby() {
		return cscreatedby;
	}

	public void setCscreatedby(String cscreatedby) {
		this.cscreatedby = cscreatedby;
	}

	public String getCscreateddate() {
		return cscreateddate;
	}

	public void setCscreateddate(String cscreateddate) {
		this.cscreateddate = cscreateddate;
	}

	public String getReturndate() {
		return returndate;
	}

	public void setReturndate(String returndate) {
		this.returndate = returndate;
	}

	public String getReturnid() {
		return returnid;
	}

	public void setReturnid(String returnid) {
		this.returnid = returnid;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public void setOrdertype(String ordertype) {
		this.ordertype = ordertype;
	}

	public String getReturntype() {
		return returntype;
	}

	public void setReturntype(String returntype) {
		this.returntype = returntype;
	}

	public String getRtnsaleno() {
		return rtnsaleno;
	}

	public void setRtnsaleno(String rtnsaleno) {
		this.rtnsaleno = rtnsaleno;
	}

	public String getRtnsalename() {
		return rtnsalename;
	}

	public void setRtnsalename(String rtnsalename) {
		this.rtnsalename = rtnsalename;
	}

	public String getReturncategory() {
		return returncategory;
	}

	public void setReturncategory(String returncategory) {
		this.returncategory = returncategory;
	}

	public String getReturnreason() {
		return returnreason;
	}

	public void setReturnreason(String returnreason) {
		this.returnreason = returnreason;
	}

	public String getReturnremark() {
		return returnremark;
	}

	public void setReturnremark(String returnremark) {
		this.returnremark = returnremark;
	}

	public String getRtnlclassName() {
		return rtnlclassName;
	}

	public void setRtnlclassName(String rtnlclassName) {
		this.rtnlclassName = rtnlclassName;
	}

	public String getRtnmclassName() {
		return rtnmclassName;
	}

	public void setRtnmclassName(String rtnmclassName) {
		this.rtnmclassName = rtnmclassName;
	}

	public String getRtnsclassName() {
		return rtnsclassName;
	}

	public void setRtnsclassName(String rtnsclassName) {
		this.rtnsclassName = rtnsclassName;
	}

	public String getRtndeptname() {
		return rtndeptname;
	}

	public void setRtndeptname(String rtndeptname) {
		this.rtndeptname = rtndeptname;
	}

	public String getRtndeptname3() {
		return rtndeptname3;
	}

	public void setRtndeptname3(String rtndeptname3) {
		this.rtndeptname3 = rtndeptname3;
	}

	public String getRtncreatedbyid() {
		return rtncreatedbyid;
	}

	public void setRtncreatedbyid(String rtncreatedbyid) {
		this.rtncreatedbyid = rtncreatedbyid;
	}

	public String getRtncreatedby() {
		return rtncreatedby;
	}

	public void setRtncreatedby(String rtncreatedby) {
		this.rtncreatedby = rtncreatedby;
	}

	public String getRtnsuppliername() {
		return rtnsuppliername;
	}

	public void setRtnsuppliername(String rtnsuppliername) {
		this.rtnsuppliername = rtnsuppliername;
	}

	public String getRtnsaleprice() {
		return rtnsaleprice;
	}

	public void setRtnsaleprice(String rtnsaleprice) {
		this.rtnsaleprice = rtnsaleprice;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
