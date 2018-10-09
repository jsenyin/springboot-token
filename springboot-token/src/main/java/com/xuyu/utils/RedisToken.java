package com.xuyu.utils;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedisToken {

	@Autowired
	private BaseRedisService baseRedisService;
	private static final long TOKENTIMEOUT=60*60;
	//1.�ڵ��ýӿ�֮ǰ���ɶ�Ӧ������token�������redis
	public String getToken() {
		//����token ����֤ ��ʱΨһ ��֧�ֲַ�ʽ���� �ֲ�ʽȫ��id���ɹ���
		String token="token"+UUID.randomUUID();
		//��α�֤token��ʱ�����棩ʹ��redis ʵ�ֻ���
		baseRedisService.setString(token,token,TOKENTIMEOUT);
		return token;
		
	}
	
	
	//�ڵ��ýӿڵ�ʱ�򣬽������Ʒ��뵽����ͷ��
	
	//�ӿڻ�ȡ��Ӧ�����ƣ���ȡ������Ӧ�����ƣ�ֱ�ӷ�����ʾ�����ظ��ύ
	public synchronized boolean findToken(String tokenKey) {
		//2.�ӿڻ�ȡ����Ӧ�����ơ���ȡ����ɾ�����ƣ�ִ��ҵ���߼�
		String tokenValue = (String)baseRedisService.getString(tokenKey);
		if(StringUtils.isEmpty(tokenValue)) {
			return false;
		}
		//��֤ÿ���ӿڶ�Ӧ��token��ֻ�ܷ���һ�Σ���֤�ӿ��ݵ���
		baseRedisService.delKey(tokenValue);
		return true;
	}
}
