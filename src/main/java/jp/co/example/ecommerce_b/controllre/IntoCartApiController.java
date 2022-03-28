package jp.co.example.ecommerce_b.controllre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.co.example.ecommerce_b.domain.LoginUser;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.service.OrderService;

@RestController
@RequestMapping("/intoCart")
@SessionAttributes(value="toppingId")
public class IntoCartApiController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HttpSession session;
	
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public void insert(Integer priceM,Integer priceL,Integer itemId,char size,String toppingId,Integer quantity,@AuthenticationPrincipal LoginUser loginUser) {
		
		System.out.println(session.getAttribute("userId"));
		System.out.println(session.getAttribute("preId"));
		List<String> toppingIds= Arrays.asList(toppingId.split(","));
		List<Integer> toIntToppingIds= new ArrayList<>();
		if(!(toppingId.equals(""))){
		for(String ids:toppingIds) {
			toIntToppingIds.add(Integer.parseInt(ids));
		}
		}
	
//		
//		//動作確認用
//		session.setAttribute("userId", 2);
		
		//仮登録
//		itemId=1;
		//必要なオブジェクト生成
		Order order = new Order();
		OrderItem item = new OrderItem();
		
		List<OrderTopping> toppingList = new ArrayList<>();

		//orderIdを初期化
		Integer orderId=0;
		//小計金額計算(カートの状態、ログイン状態は関係なしの共通処理)
		int total =0;
		if(size =='M') {
			total += priceM;
			//トッピングが選択されている場合の処理
			if(!(toIntToppingIds.isEmpty())) {
			total += toIntToppingIds.size()*200;
			}
			
		}else if(size =='L') {
			total += priceL;
			if(!(toIntToppingIds.isEmpty())) {
			total += toIntToppingIds.size()*300;
			}
		
		}
		item.setSize(size);
		total *= quantity;
		//orderオブジェクトに小計金額とステータスをセット
		order.setTotalPrice(total);
		order.setStatus(0);
		//ログインしているか否か判定
		if(session.getAttribute("userId") !=null) {
			//ログインしている場合の処理
			Integer userId = (Integer)session.getAttribute("userId");
			//userIdをorderオブジェクトにセット
			order.setUserId(userId);
			//カート内に商品があるか否かの判定のためuserIdでordersテーブルを検索
			Order preOrder = orderService.checkOrder(userId);
			if(preOrder ==null) {
				//カートが空の状態時の処理
				//orderテーブルへのinsert処理(カートへの新規追加)
				//同時にinsertしたorderIdを取得
				 orderId=orderService.intoCart(order);
			
			}else {
				//カートが空じゃない場合の処理
				//既にカートに入っている合計金額を取得
				Integer preTotal= preOrder.getTotalPrice();
				//今カートに追加した商品の金額をカートリスト合計と合わせる
				total += preTotal;
				order.setTotalPrice(total);
				orderService.updateOrder(order);
				orderId=preOrder.getId();
			}
			//orderIdをセット
			item.setOrderId(orderId);
			
		}else if(session.getAttribute("userId")==null){
			//ログインしていない状態の処理
			
		
			//preIdが発行されていない＝カートに商品がない状態
			if(session.getAttribute("preId")==null) {
				//カートが空の状態時の処理
				//新規にpreIdを作成オブジェクトにセット
				
				Integer uuID= UUID.randomUUID().hashCode();
			
				
				session.setAttribute("preId", uuID);
				order.setUserId(uuID);
				//orderテーブルへのinsert処理(カートへの新規追加)
				//同時にinsertしたorderIdを取得
				 orderId=orderService.intoCart(order);
				
				 
			}else {
				//カートが空じゃない場合の処理
				//*注意　動作確認時、一度preId発行した後にDBでデータ削除すると正常な動作できなくなる
				//発行済みのpreIdをもとに過去のorderを取得
				System.out.println("未ログイン、カート商品あり動作");
				Order preOrder = orderService.checkOrder((Integer)session.getAttribute("preId"));
				//過去にカート追加した際発行されたpreIdをorderオブジェクトにセット
				order.setUserId((Integer)session.getAttribute("preId"));
				//既にカートに入っている合計金額を取得
				Integer preTotal= preOrder.getTotalPrice();
				//今カートに追加した商品の金額をカートリスト合計と合わせる
				total += preTotal;
				order.setTotalPrice(total);
				//合計金額の更新
				orderId=orderService.updateOrder(order);
			}
			//orderIdをセット
			item.setOrderId(orderId);
		
		}
		
//	　　　itemIdをセット
		item.setItemId(itemId);
		
		
		//個数をセット
		item.setQuantity(quantity);
		//orderテーブルへのinsert処理
		Integer newOrderItemId=orderService.insertItem(item);
		if(!(toIntToppingIds.isEmpty())) {
		//formのトッピングリストから情報を一件ずつ取得し、新たなリストに格納
		
		for(Integer id:toIntToppingIds) {
			OrderTopping topping = new OrderTopping();
			topping.setToppingId(id);
			topping.setOrderItemId(newOrderItemId);
			toppingList.add(topping);
			}
		//トッピングが選択されていた場合にorderToppingテーブルにinsert処理
			
		
		orderService.insertTopping(toppingList);
		}
		
		
	}
	
	

}
