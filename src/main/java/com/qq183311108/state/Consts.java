package com.qq183311108.state;

import java.util.Date;

public interface Consts {
	
	public enum OrderEvents implements IOrderMsg {
		E1, E2, E3, E4
        ;

        private long orderId;
        private Date startDate;
        private Date endDate;
        
        private OrderEvents(){};
        
		@Override
		public long getOrderId() {
			return this.orderId;
		}
		
		@Override
		public void setOrderId(long orderId) {
			this.orderId = orderId;
		}
		
		@Override
		public Date getStartDate() {
			return this.startDate;
		}
		@Override
		public void setStartDate(Date startDate) {
			this.startDate = startDate;
			
		}
		
		@Override
		public Date getEndDate() {
			return endDate;
		}
		
		@Override
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
		}
    }	
	
	
/*	public enum States {
	    STATES_0,STATES_1,STATES_2,STATES_3,STATES_4
	}*/

}
