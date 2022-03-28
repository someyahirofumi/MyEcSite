package jp.co.example.ecommerce_b.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.repository.OrderRepository;

@Service
@Transactional
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	public Order checkOrder(Integer userId) {
		return orderRepository.checkOrder(userId);
	}
	
	public Integer intoCart(Order order) {
		return orderRepository.intoCart(order);
	}
	
	public Integer updateOrder(Order order) {
		return orderRepository.updateOrder(order);
	}
	
	public Integer insertItem(OrderItem item) {
		return orderRepository.insertItem(item);
	}
	
	public void insertTopping(List<OrderTopping> list) {
		orderRepository.insertTopping(list);
		
	}

}
