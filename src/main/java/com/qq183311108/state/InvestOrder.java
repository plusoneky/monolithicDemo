package com.qq183311108.state;

public class InvestOrder {

	public long orderId;
	public States orderStatus;
	public String orderName;
	
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public States getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(States orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	
}
