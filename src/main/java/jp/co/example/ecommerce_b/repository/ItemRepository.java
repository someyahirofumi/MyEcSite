package jp.co.example.ecommerce_b.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.domain.Topping;

@Repository
public class ItemRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Item> ITEM_ROW_MAPPER=(rs,i)->{
		
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setImagePath(rs.getString("image_path"));
		item.setDescription(rs.getString("description"));
		item.setPriceM(rs.getInt("price_m"));
		item.setPriceL(rs.getInt("price_l"));
		return item;
		
		
		
	};
	
	public static final ResultSetExtractor<List<Order>> ORDER_ORDERITEM_ORDERTOPPING_EXTRACTOR=(rs)->{
		List<Order> orderList= new ArrayList<>();
		List<OrderItem>orderItemList = null;
		List<OrderTopping> orderToppingList=null;
		int beforeId= 0;
		int beforeOrderItemId = 0;
		
		while(rs.next()) {
			int nowId=rs.getInt("order_id");
			int nowOrderItemId=rs.getInt("order_item_id");
			
			if(beforeId != nowId) {
				Order order=new Order();
				order.setId(nowId);
				order.setUserId(rs.getInt("user_id"));
				order.setTotalPrice(rs.getInt("total_price"));
				order.setStatus(rs.getInt("status"));
				order.setDeliveryTime(rs.getTimestamp("delivery_time"));
				orderItemList= new ArrayList<>();
				order.setOrderItemList(orderItemList);
				orderList.add(order);
			
		}
			if(beforeOrderItemId != nowOrderItemId) {
				OrderItem orderItem= new OrderItem();
				orderItem.setId(rs.getInt("order_item_id"));
				orderItem.setItemId(rs.getInt("item_id"));
				orderItem.setQuantity(rs.getInt("quantity"));
				orderItem.setSize(rs.getString("size").toCharArray()[0]);
				Item item = new Item();
				item.setName(rs.getString("item_name"));
				item.setPriceM(rs.getInt("item_price_M"));
				item.setPriceL(rs.getInt("item_price_L"));
				item.setImagePath(rs.getString("image_path"));
				orderItem.setItem(item);
				orderToppingList = new ArrayList<>();
				orderItem.setOrderToppingList(orderToppingList);
				orderItemList.add(orderItem);
			}

			if (rs.getInt("topping_id") > 0) {
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setToppingId(rs.getInt("topping_id"));
				Topping topping = new Topping();
				topping.setName(rs.getString("topping_name"));
				topping.setPriceM(rs.getInt("topping_price_M"));
				topping.setPriceL(rs.getInt("topping_price_L"));
				orderTopping.setTopping(topping);
				orderToppingList.add(orderTopping);
			}

			beforeId = nowId;
			beforeOrderItemId = nowOrderItemId;
		}

		return orderList;
	
				
			
		
		
	};
	
	private static final RowMapper<Topping> TOPPING_ROW_MAPPER=new BeanPropertyRowMapper<>(Topping.class);
	
	public List<Item> selectAll(){
		String sql="select*from items ;";
		List<Item> items = template.query(sql, ITEM_ROW_MAPPER);
		return items;
	}
	
	
	public List<Topping> getAllTopping(){
		String sql="select * from toppings;";
		return template.query(sql, TOPPING_ROW_MAPPER);
	}
	
	public Order getCartList(Integer userId){
		String sql = 
				"SELECT o.id as order_id,o.user_id,o.total_price,oi.id as order_item_id,oi.quantity,size,i.name as item_name,"
				        + "	ot.id as topping_id,i.price_m as item_price_M,i.price_l as item_price_L,o.status as status,o.order_date,o.delivery_time,"
						+ " i.id as item_id,i.image_path,t.name as topping_name,t.price_m as topping_price_M,t.price_l as topping_price_L"
						+ " FROM "
						+ " orders as o"
						+ " LEFT OUTER JOIN order_items as oi ON o.id=oi.order_id"
						+ " LEFT OUTER JOIN order_toppings as ot ON oi.id=ot.order_item_id"
						+ " LEFT OUTER JOIN items as i ON oi.item_id= i.id"
						+ " LEFT OUTER JOIN toppings as t ON ot.topping_id=t.id"
						+ " WHERE"
						+ " o.user_id=:userId AND o.status=0;";
		SqlParameterSource param= new MapSqlParameterSource().addValue("userId", userId);
		List<Order> cartList=new ArrayList<>();
		cartList= template.query(sql, param,ORDER_ORDERITEM_ORDERTOPPING_EXTRACTOR);
		
		 if (cartList.isEmpty()) {
				return null;
			} else {
				return cartList.get(0);
			}
	}
	
	public void deleteCart(Integer orderId) {
		String sql="BEGIN;\n"
				+ "delete from order_items\n"
				+ "where id=:id;\n"
				+ "delete from order_toppings\n"
				+ "where order_item_id=:id;\n"
				+ "COMMIT;";
		template.update(sql, new MapSqlParameterSource().addValue("id", orderId));
		
		
		
	}
	

}

