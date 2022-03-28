package jp.co.example.ecommerce_b.repository;

import java.util.List;

import javax.swing.ListCellRenderer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;

@Repository
public class OrderRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Order> ORDER_ROW_MAPPER=(rs,i)->{
		Order order = new Order();
		order.setId(rs.getInt("id"));
		order.setUserId(rs.getInt("user_id"));
		order.setStatus(rs.getInt("status"));
		order.setTotalPrice(rs.getInt("total_price"));
		return order;
		
	};
	
	public Order checkOrder(Integer userId) {
		String sql="select id,user_id,status,total_price from orders where user_id=:userId and status=0;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Order>list=template.query(sql, param, ORDER_ROW_MAPPER);
		if(list.isEmpty()) {
			return null;
		}else {
			return list.get(0);
		}
	}
	
	public Integer intoCart(Order order) {
		String sql="insert into orders(user_id,status,total_price)values(:userId,:status,:totalPrice) returning id;";
		SqlParameterSource param=new MapSqlParameterSource().addValue("userId",order.getUserId()).addValue("status", order.getStatus()).addValue("totalPrice",order.getTotalPrice());
		return template.queryForObject(sql, param, Integer.class);
	}
	
	public Integer updateOrder(Order order) {
		String sql="update orders set total_price=:totalPrice where user_id=:userId and status=0 returning id;";
		SqlParameterSource param= new MapSqlParameterSource().addValue("totalPrice", order.getTotalPrice()).addValue("userId", order.getUserId());
		List<Integer> list= template.queryForList(sql, param,Integer.class);
		return list.get(0);
	}
	
	public Integer insertItem(OrderItem item) {
		String sql ="insert into order_items(item_id,order_id,quantity,size)values(:itemId,:orderId,:quantity,:size) returning id;";
		SqlParameterSource param=new BeanPropertySqlParameterSource(item);
		return template.queryForObject(sql, param,Integer.class);
		
	}
	
	public void insertTopping(List<OrderTopping> list) {
		String sql="insert into order_toppings(topping_id,order_item_id)values(:toppingId,:orderItemId);";
		
		for(OrderTopping topping:list) {
			SqlParameterSource param=new MapSqlParameterSource().addValue("toppingId", topping.getToppingId()).addValue("orderItemId",topping.getOrderItemId());
			template.update(sql, param);
			
		}
	
	}

}
