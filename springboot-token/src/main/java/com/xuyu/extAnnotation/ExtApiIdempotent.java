package com.xuyu.extAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//����ӿ��ݵ��� ֧�������ӳٺͱ��ظ��ύ
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExtApiIdempotent {
	String type();
}
