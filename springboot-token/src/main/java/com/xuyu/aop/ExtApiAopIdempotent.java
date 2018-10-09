package com.xuyu.aop;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xuyu.extAnnotation.ExtApiIdempotent;
import com.xuyu.extAnnotation.ExtApiToken;
import com.xuyu.utils.ConstantUtils;
import com.xuyu.utils.RedisToken;




public class ExtApiAopIdempotent {

	@Autowired
	private RedisToken redisToken;
	
	public void rlAop() {
	}
	@Pointcut("rlAop()")
	public void before(JoinPoint point) {
		MethodSignature  signature = (MethodSignature) point.getSignature();
		ExtApiToken extApiToken = signature.getMethod().getDeclaredAnnotation(ExtApiToken.class);
		if(extApiToken!=null) {
			// ���Է��뵽AOP���� ǰ��֪ͨ
			getRequest().setAttribute("token", redisToken.getToken());
		}
	}
	// ����֪ͨ
	@Around("rlAop()")
	public Object doBefore(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		//2.�Д෽�����Ƿ����ExtApiIdempotent�]��
		MethodSignature  methodSignature  = (MethodSignature) proceedingJoinPoint.getSignature();
		ExtApiIdempotent declaredAnnotation = methodSignature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
		//3.��������ϼ���ע��
		if(declaredAnnotation!=null) {
			String type = declaredAnnotation.type();
			// ���ʹ��Token ����ݵ���
			// ���裺
			String token=null;
			HttpServletRequest request = getRequest();
			if(type.equals(ConstantUtils.EXTAPIHEAD)) {
				token = request.getHeader("token");
			}else {
				token = request.getParameter("token");
			}
			if(StringUtils.isEmpty(token)) {
				return "��������";
			}
			// 3.�ӿڻ�ȡ��Ӧ������,����ܹ���ȡ��(��redis��ȡ����)����(����ǰ����ɾ����) ��ֱ��ִ�и÷��ʵ�ҵ���߼�
			boolean isToken = redisToken.findToken(token);
			if(!isToken) {
				response("�����ظ��ύ!");
				// ���淽�����ڼ���ִ��
				return null;
			}
		}
		//����
		Object proceed = proceedingJoinPoint.proceed();
		return proceed;
	}

	public HttpServletRequest getRequest() {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		return request;
	}

	public void response(String msg) throws IOException {
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletResponse response = attributes.getResponse();
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		try {
			writer.println(msg);
		} catch (Exception e) {

		} finally {
			writer.close();
		}

	}
}
