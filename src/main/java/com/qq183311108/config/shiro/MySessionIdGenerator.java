package com.qq183311108.config.shiro;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;

public class MySessionIdGenerator implements SessionIdGenerator{

	private static final String SESSION_SIGN_KEY="183311108@qq.com";
	
	@Override
	public  Serializable generateId(Session session) {
		String uuid = UUID.randomUUID().toString();
		String pwdSalt = RandomStringUtils.randomAlphabetic(6);
		
		return new StringBuilder(pwdSalt).append(uuid).append(new Md5Hash(uuid.toUpperCase(),SESSION_SIGN_KEY+pwdSalt,2).toHex()).toString();
	}

}
