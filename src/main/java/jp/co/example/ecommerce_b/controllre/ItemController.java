package jp.co.example.ecommerce_b.controllre;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.Users;
import jp.co.example.ecommerce_b.service.ItemService;

@Controller
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private HttpSession session;
	
	
	
	@RequestMapping("/showCart")
	public String showCart(Model model) {
		
		if(session.getAttribute("userId") == null && session.getAttribute("preId") == null) {
			Integer uuID= UUID.randomUUID().hashCode();
			
			
			session.setAttribute("preId", uuID);
		}
		System .out .println (session.getAttribute("preId"));
		Integer userId =0;
		
		if(session.getAttribute("userId") ==null) {
			userId=(Integer)session.getAttribute("preId");
			
			
		}else {
			userId=(Integer)session.getAttribute("userId");
		}
		
		Order orderList=itemService.getCartList(userId);
		
		if(orderList == null) {
			model.addAttribute("cartNullMessage","カートに商品がありません");
		}
			
		session.setAttribute("cart", orderList);
		return"cart_list";
	}
	
	
}
