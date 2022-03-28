package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.Topping;
import jp.co.example.ecommerce_b.repository.ItemRepository;

@Service
@Transactional
public class ItemService {
	
	@Autowired
	private ItemRepository itemRepository;
	
	public List<Item> selectAll(){
		return itemRepository.selectAll(); 
	}
	
	public List<Topping> getAllTopping(){
		return itemRepository.getAllTopping();
	}
	
	public Order getCartList(Integer userId){
		return itemRepository.getCartList(userId);
	}
	
	public void deleteCart(Integer orderId) {
		itemRepository.deleteCart(orderId);
	}
	

}
