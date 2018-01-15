package com.qq1833111108.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component  
@ConfigurationProperties(prefix="sysProperties") //接收application.yml中的sysProperties下面的属性
public class SysProperties {

	private long minRequestIntervalTime;
	
	private long maxMaliciousTimes;

	public long getMinRequestIntervalTime() {
		return minRequestIntervalTime;
	}

	public void setMinRequestIntervalTime(long minRequestIntervalTime) {
		this.minRequestIntervalTime = minRequestIntervalTime;
	}

	public long getMaxMaliciousTimes() {
		return maxMaliciousTimes;
	}

	public void setMaxMaliciousTimes(long maxMaliciousTimes) {
		this.maxMaliciousTimes = maxMaliciousTimes;
	}
	
	
	
}
