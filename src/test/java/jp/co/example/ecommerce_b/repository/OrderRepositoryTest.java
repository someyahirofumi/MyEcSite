package jp.co.example.ecommerce_b.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
@SpringBootTest
class OrderRepositoryTest {
	
	@Autowired 
	private OrderRepository orderRepository;
	
	@Autowired 
	private NamedParameterJdbcTemplate template;

	@BeforeAll
	static void setUpBeforeClass(@Autowired NamedParameterJdbcTemplate template) throws Exception {
		
		
		template.update("insert into orders(user_id,status,total_price)values(0,0,0);", new MapSqlParameterSource());
		System.out.println("テストデータ挿入");
	}
	
	

	@AfterAll
	static void tearDownAfterClass(@Autowired NamedParameterJdbcTemplate template) throws Exception {
		
		template.update("delete from orders where user_id=0 and status=0 and total_price=0;",new MapSqlParameterSource());
		System.out.println("テストデータ削除");
	}
	



	@Test
	void checkOedertest() {
		Order order=orderRepository.checkOrder(0);
		assertEquals(order.getUserId(),template.queryForObject("select user_id from orders where user_id=0;", new MapSqlParameterSource(), Integer.class),"user_idが一致しません");
		assertEquals(order.getStatus(),template.queryForObject("select status from orders where user_id=0;", new MapSqlParameterSource(), Integer.class),"statusが一致しません");
		assertEquals(order.getTotalPrice(),template.queryForObject("select total_price from orders where user_id=0;", new MapSqlParameterSource(), Integer.class),"total_priceが一致しません");
		System.out.println("checkOrderメソッドテスト完了");
	}
	
	@Test
	void intoCartTest() {
		Order order = new Order();
		order.setUserId(100);
		order.setStatus(100);
		order.setTotalPrice(100);
		orderRepository.intoCart(order);
		assertEquals(100,template.queryForObject("select user_id from orders where id=(select max(id) from orders);",new MapSqlParameterSource(),Integer.class), "user_idがinsertされていません");
		assertEquals(100,template.queryForObject("select status from orders where id=(select max(id) from orders);",new MapSqlParameterSource(),Integer.class), "statusがinsertされていません");
		assertEquals(100,template.queryForObject("select total_price from orders where id=(select max(id) from orders);",new MapSqlParameterSource(),Integer.class), "total_priceがinsertされていません");
		System.out.println("intoCartメソッドテスト完了");
		
		template.update("delete from orders where id=(select Max(id) from orders)", new MapSqlParameterSource());
		System.out.println("intoCartメソッドテスト用データ削除");
	
	}
	
	@Test
	void insertItemTest() {
		OrderItem item = new OrderItem();
		item.setItemId(0);
		item.setOrderId(0);
		item.setQuantity(0);
		item.setSize('M');
		assertEquals(orderRepository.insertItem(item),template.queryForObject("select Max(id) from order_items",new MapSqlParameterSource(),Integer.class), "insertしたデータのIDが取得できていません");
		assertEquals(0,template.queryForObject("select item_id from order_items where id=(select Max(id)from order_items);", new MapSqlParameterSource(), Integer.class),"ItemIdが登録されていません");
		assertEquals(0,template.queryForObject("select order_id from order_items where id=(select Max(id)from order_items);", new MapSqlParameterSource(), Integer.class),"userIdが登録されていません");
		assertEquals(0,template.queryForObject("select quantity from order_items where id=(select Max(id)from order_items);", new MapSqlParameterSource(), Integer.class),"quantityが登録されていません");
		assertEquals('M',template.queryForObject("select size from order_items where id=(select Max(id)from order_items);", new MapSqlParameterSource(), char.class),"sizeが登録されていません");
		System.out.println("insertItemテスト完了");
		
		template.update("delete from order_items where id=(select MAX(id) from order_items);", new MapSqlParameterSource());
		System.out.println("insertItemテスト用データ削除");
	}
	
	@Test
	void insertToppingTest() {
		OrderTopping topping= new OrderTopping();
		List<OrderTopping>list= new ArrayList<>();
		topping.setToppingId(0);
		topping.setOrderItemId(0);
		list.add(topping);
		orderRepository.insertTopping(list);
		assertEquals(0,template.queryForObject("select topping_id from order_toppings where id=(select MAX(id)from order_toppings)",new MapSqlParameterSource(), Integer.class),"toppingIdが登録されておりません");
		assertEquals(0,template.queryForObject("select order_item_id from order_toppings where id=(select MAX(id)from order_toppings)",new MapSqlParameterSource(), Integer.class),"orderItemIdが登録されておりません");
		
		template.update("delete from order_toppings where id=(select Max(id)from order_toppings)",new MapSqlParameterSource());
		System.out.println("insertToppingTest用データの削除");
		
	}
	
	@Test
	void updateOrderTest() {	
		Order order = new Order();
		order.setTotalPrice(10);
		order.setUserId(0);
		assertEquals(orderRepository.updateOrder(order),template.queryForObject("select id from orders where id=(select Max(id)from orders)", new MapSqlParameterSource(), Integer.class),"updateしたデータのIDが取得できません");
		assertEquals(10,template.queryForObject("select total_price from orders where user_id=0", new MapSqlParameterSource(), Integer.class),"updateができていません");
		
	}

}
