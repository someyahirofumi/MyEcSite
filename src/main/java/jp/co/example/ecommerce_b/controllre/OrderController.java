package jp.co.example.ecommerce_b.controllre;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.form.OrderConfirmForm;
import jp.co.example.ecommerce_b.service.OrderService;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@ModelAttribute
	private OrderConfirmForm setUpForm() {
		return new OrderConfirmForm();
	}
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private OrderService orderService;

	
	@RequestMapping("/confirm")
	public String orderConfirm() {
		Order order = (Order)session.getAttribute("cart");
		if(order.getOrderItemList().isEmpty()) {
			return"forward:user/toItemList";
		}
		return"order_confirm";
	}

	
	@RequestMapping("/destination")
	public String destination(@Validated OrderConfirmForm form,BindingResult result,Model model){
		if(result.hasErrors()) {
			
			return"order_confirm";
		}
		
		LocalDateTime todayTime=LocalDateTime.now();
		Timestamp timestamp=new Timestamp(form.getDeliveryDateTime().getTime());
		LocalDateTime deliveryTime= timestamp.toLocalDateTime();
		Duration duration = Duration.between(todayTime,deliveryTime);
		if(duration.toHours()<=3) {
			result.rejectValue("date", null,"今から3時間後以降の日付を入力してください");
		}
		
		Order order = new Order();
		order.setUserId((Integer)session.getAttribute("userId"));
		if(form.getPaymentMethod() ==1) {
			order.setStatus(1);
		}else if(form.getPaymentMethod() == 2) {
			order.setStatus(2);
		}
	
		
		Timestamp today=new Timestamp(System.currentTimeMillis());
	
		
		order.setOrderDate(today);
		order.setDestinationName(form.getDestinationName());
		order.setDestinationEmail(form.getDestinationEmail());
		order.setDestinationZipcode(form.getDestinationZipcode());
		order.setDestinationAddress(form.getDestinationAddress());
		order.setDestinationTel(form.getDestinationTel());
		order.setDeliveryTime(timestamp);
		order.setPaymentMethod(form.getPaymentMethod());
		
		orderService.updateOrder(order);
		
		
		return"redirect:/order/finish";
	}
	
	@RequestMapping("/finish")
	public String finish() {
		return"order_finished";
	}
}
