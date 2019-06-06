package com.qq183311108.state;

import java.util.Date;

public interface IOrderMsg {
    public long getOrderId();

	public void setOrderId(long orderId);

	public Date getStartDate();

	public void setStartDate(Date startDate);
	
	public Date getEndDate();

	public void setEndDate(Date endDate);	
}
