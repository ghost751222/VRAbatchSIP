package com.macaron.vra.vo;

import java.util.Date;

public class TcObCallResultRecordInfoVo {

	private Date dataDt;
	private String dataLocation;//資料區域
	private String serialNum; //序號
	private String callResultGroup; //群組
	private String callResultList; //小結清單
	private String agentDepart; //部
	private String agentUnit; //單位
	private String agentGroup; //組別
	private String agentName; //姓名
	private String agentSerialNum; //同仁客服編號
	private String contactDuration; //通話秒數
	private String customerNo; //客代
	private String contactStartDt; //聯繫時間
	private String contactEndDt; //結束時間
	private String ctiLocation; //CTI位置
	private String agentId; //人員ID
	private String obOldMemberLevel; //OB舊客會員等級
	private String productName; //品名
	private String productSerialNum; //品號
	private String orderChannel; //訂單通路
	private String paymentType; //付款方式
	private String paymentAmount; //付款金額
	private String connectionId; //Connid
	private String productName2; //品名2
	private String productSerialNum2; //品號2
	private String orderChannel2; //訂單通路2
	private String paymentType2; //付款方式2
	private String paymentAmount2; //付款金額2
	
	//record
	private String fileName;
	
	@Override
	public String toString() {
		return "TcObCallResultRecordInfoVo [dataDt=" + dataDt + ", dataLocation=" + dataLocation + ", serialNum="
				+ serialNum + ", callResultGroup=" + callResultGroup + ", callResultList=" + callResultList
				+ ", agentDepart=" + agentDepart + ", agentUnit=" + agentUnit + ", agentGroup=" + agentGroup
				+ ", agentName=" + agentName + ", agentSerialNum=" + agentSerialNum + ", contactDuration="
				+ contactDuration + ", customerNo=" + customerNo + ", contactStartDt=" + contactStartDt
				+ ", contactEndDt=" + contactEndDt + ", ctiLocation=" + ctiLocation + ", agentId=" + agentId
				+ ", obOldMemberLevel=" + obOldMemberLevel + ", productName=" + productName + ", productSerialNum="
				+ productSerialNum + ", orderChannel=" + orderChannel + ", paymentType=" + paymentType
				+ ", paymentAmount=" + paymentAmount + ", connectionId=" + connectionId + ", productName2="
				+ productName2 + ", productSerialNum2=" + productSerialNum2 + ", orderChannel2=" + orderChannel2
				+ ", paymentType2=" + paymentType2 + ", paymentAmount2=" + paymentAmount2 + ", fileName=" + fileName
				+ "]";
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
	public String getCallResultGroup() {
		return callResultGroup;
	}
	public void setCallResultGroup(String callResultGroup) {
		this.callResultGroup = callResultGroup;
	}
	public String getCallResultList() {
		return callResultList;
	}
	public void setCallResultList(String callResultList) {
		this.callResultList = callResultList;
	}
	public String getAgentDepart() {
		return agentDepart;
	}
	public void setAgentDepart(String agentDepart) {
		this.agentDepart = agentDepart;
	}
	public String getAgentUnit() {
		return agentUnit;
	}
	public void setAgentUnit(String agentUnit) {
		this.agentUnit = agentUnit;
	}
	public String getAgentGroup() {
		return agentGroup;
	}
	public void setAgentGroup(String agentGroup) {
		this.agentGroup = agentGroup;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentSerialNum() {
		return agentSerialNum;
	}
	public void setAgentSerialNum(String agentSerialNum) {
		this.agentSerialNum = agentSerialNum;
	}
	public String getContactDuration() {
		return contactDuration;
	}
	public void setContactDuration(String contactDuration) {
		this.contactDuration = contactDuration;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getContactStartDt() {
		return contactStartDt;
	}
	public void setContactStartDt(String contactStartDt) {
		this.contactStartDt = contactStartDt;
	}
	public String getContactEndDt() {
		return contactEndDt;
	}
	public void setContactEndDt(String contactEndDt) {
		this.contactEndDt = contactEndDt;
	}
	public String getCtiLocation() {
		return ctiLocation;
	}
	public void setCtiLocation(String ctiLocation) {
		this.ctiLocation = ctiLocation;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getObOldMemberLevel() {
		return obOldMemberLevel;
	}
	public void setObOldMemberLevel(String obOldMemberLevel) {
		this.obOldMemberLevel = obOldMemberLevel;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductSerialNum() {
		return productSerialNum;
	}
	public void setProductSerialNum(String productSerialNum) {
		this.productSerialNum = productSerialNum;
	}
	public String getOrderChannel() {
		return orderChannel;
	}
	public void setOrderChannel(String orderChannel) {
		this.orderChannel = orderChannel;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public String getConnectionId() {
		return connectionId;
	}
	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}
	public String getProductName2() {
		return productName2;
	}
	public void setProductName2(String productName2) {
		this.productName2 = productName2;
	}
	public String getProductSerialNum2() {
		return productSerialNum2;
	}
	public void setProductSerialNum2(String productSerialNum2) {
		this.productSerialNum2 = productSerialNum2;
	}
	public String getOrderChannel2() {
		return orderChannel2;
	}
	public void setOrderChannel2(String orderChannel2) {
		this.orderChannel2 = orderChannel2;
	}
	public String getPaymentType2() {
		return paymentType2;
	}
	public void setPaymentType2(String paymentType2) {
		this.paymentType2 = paymentType2;
	}
	public String getPaymentAmount2() {
		return paymentAmount2;
	}
	public void setPaymentAmount2(String paymentAmount2) {
		this.paymentAmount2 = paymentAmount2;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
