package jp.co.example.ecommerce_b.controllre;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.service.ItemService;
import jp.co.example.ecommerce_b.service.OrderService;

@RestController
@RequestMapping("/delete")
public class DeleteCartApiController {
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HttpSession session;
	
	@RequestMapping(value="/cart",method=RequestMethod.POST)

	public void deleteCart(Integer orderId,Integer subTotal,Integer total) {
		System.out.println("削除処理の開始");
		System.out.println(orderId);
		System.out.println(subTotal);
		System.out.println(total);
		Order order = new Order();
		order.setTotalPrice(total-subTotal);
		if(session.getAttribute("userId") !=null) {
			order.setUserId((Integer)session.getAttribute("userId"));
		}else {
			order.setUserId((Integer)session.getAttribute("preId"));
		}
		
	
		itemService.deleteCart(orderId);
		orderService.updateOrder(order);
		
		
	}
}
