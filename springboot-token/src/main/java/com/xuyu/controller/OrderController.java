package com.xuyu.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xuyu.entity.OrderEntity;
import com.xuyu.extAnnotation.ExtApiIdempotent;
import com.xuyu.mapper.OrderMapper;
import com.xuyu.utils.ConstantUtils;
import com.xuyu.utils.RedisToken;


@RestController
public class OrderController {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private RedisToken redisToken;

	// @Autowired
	// private RedisTokenUtils redisTokenUtils;
	//
	// ��redis�л�ȡToken
	@RequestMapping("/redisToken")
	public String RedisToken() {
		return redisToken.getToken();
	}

	// @RequestMapping(value = "/addOrderExtApiIdempotent", produces =
	// "application/json; charset=utf-8")
	// @ExtApiIdempotent(type = ConstantUtils.EXTAPIHEAD)
	// public String addOrderExtApiIdempotent(@RequestBody OrderEntity
	// orderEntity, HttpServletRequest request) {
	// // ���ʹ��Token ����ݵ���
	// // ���裺
	// // 2.���ýӿڵ�ʱ�򣬽������Ʒ��������ͷ��(��ȡ����ͷ�е�����)
	// String token = request.getHeader("token");
	// if (StringUtils.isEmpty(token)) {
	// return "��������";
	// }
	// // 3.�ӿڻ�ȡ��Ӧ������,����ܹ���ȡ��(��redis��ȡ����)����(����ǰ����ɾ����) ��ֱ��ִ�и÷��ʵ�ҵ���߼�
	// boolean isToken = redisToken.findToken(token);
	// // 4.�ӿڻ�ȡ��Ӧ������,�����ȡ���������� ֱ�ӷ��������ظ��ύ
	// if (!isToken) {
	// return "�����ظ��ύ!";
	// }
	// int result = orderMapper.addOrder(orderEntity);
	// return result > 0 ? "��ӳɹ�" : "���ʧ��" + "";
	// }

	@RequestMapping(value = "/addOrderExtApiIdempotent", produces = "application/json; charset=utf-8")
	@ExtApiIdempotent(type = ConstantUtils.EXTAPIHEAD)
	public String addOrderExtApiIdempotent(@RequestBody OrderEntity orderEntity, HttpServletRequest request) {
		int result = orderMapper.addOrder(orderEntity);
		return result > 0 ? "��ӳɹ�" : "���ʧ��" + "";
	}

}
