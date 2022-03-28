package jp.co.example.ecommerce_b.controllre;



import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Topping;
import jp.co.example.ecommerce_b.domain.Users;
import jp.co.example.ecommerce_b.form.RegisterUserForm;
import jp.co.example.ecommerce_b.service.ItemService;
import jp.co.example.ecommerce_b.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@ModelAttribute
	private RegisterUserForm setUpForm() {
		return new RegisterUserForm();
	}
	
	
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private HttpSession session;
	
	
	@RequestMapping("/toRegisterUser")
	public String toRegisterUser() {
		return "register_user";
	}
	
	@RequestMapping("/registerUser")
	public String registerUser(@Validated RegisterUserForm form,BindingResult result,Model model) {
		if(result.hasErrors()){
			return "register_user";
		}
		String email = userService.checkEmail(form.getEmail());
		if(email != null) {
			model.addAttribute("duplicateMessage","そのメールアドレスは登録できません");
			return "register_user";
			
		}
		Users user = new Users();
		BeanUtils.copyProperties(form, user);
		
		userService.registerUser(user);
		
		return "login";
		
		
	}
	

		
	
	
	@RequestMapping("/toItemList")
	public String toItemList(Model model) {
	
	
		List<Item>itemList=itemService.selectAll();
		application.setAttribute("itemList", itemList);
		@SuppressWarnings("unchecked")
		List<Item>itemLists=(List<Item>)application.getAttribute("itemList");
	
		model.addAttribute("itemList",itemLists);
		return "item_list_coffee";
		
		
	}
	
	@RequestMapping("/login")
	public String login(Model model,String inputEmail,String inputPassword) {
	
		 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
			//ログイン済みの場合
			if(session.getAttribute("userId") != null) {
				
				return toItemList(model);
			}
			
			//以下、Spring Security にて実装のためコメントアウト
			//入力されたメールアドレス、パスワードからユーザー情報を検索
			//検索結果が０件のとき、エラーメッセージを表示
			//if(userservice.Login(email, password) == null) {
			//System.out.println("失敗");
			//model.addAttribute("errorMessage","メールアドレス、またはパスワードが間違っています");
			//return "/user/";
			//}
			
			//sessionにユーザー情報を格納(idだけで良い？)
			
			session.setAttribute("userId",userService.searchByEmail(auth.getName()));
			
			//遷移先の判定
			//(sessionスコープにuuidが存在するかの確認)
			if(session.getAttribute("preID") != null) {
				return "forword:/item_detail";
				
			}
			return toItemList(model);
		
		}
	
	@RequestMapping("/search")
	public String search(Model model,String code,String listBox) {
		@SuppressWarnings("unchecked")
		List<Item>itemList=(List<Item>)application.getAttribute("itemList");
		if(!(code ==  null)) {
			
			itemList=itemList.stream()
			.filter(s -> s.getName().contains(code))
			.collect(Collectors.toList());
		
			if(itemList.isEmpty()) {
				model.addAttribute("notfound","商品が見つかりませんでした");
			}
		}
		if(!(listBox == null)) {
			
		if (listBox.equals("low")) {
			itemList=itemList.stream()
					.sorted(Comparator.comparing(Item::getPriceM))
					.collect(Collectors.toList());
					
			
		} else if (listBox.equals("high")) {
			itemList=itemList.stream()
					.sorted(Comparator.comparing(Item::getPriceM).reversed())
					.collect(Collectors.toList());
		}
		
		}
		model.addAttribute("itemList",itemList);
		return"item_list_coffee";
	}
	
	@RequestMapping("/toItemDetail")
	public String toItemDetail(Integer itemId,Model model) {
		List<Topping> toppingList= itemService.getAllTopping();
		
		@SuppressWarnings("unchecked")
		List<Item>itemList=(List<Item>)application.getAttribute("itemList");
		List<Item>item1=itemList.stream()
					.filter(s -> s.getId()==itemId)
					.collect(Collectors.toList());
		model.addAttribute("Item",item1.get(0));
		model.addAttribute("toppingList",toppingList);
		return "item_detail";
		
		
	}
	@RequestMapping("")
	public String toLogin(Model model,@RequestParam(required = false) String error) {
		if(error != null) {
			System.out.println("失敗");
			model.addAttribute("error","メールアドレス、またはパスワードが間違っています");
		}
	
		//ログイン済みの場合
				if(session.getAttribute("userInfo") != null) {
					System.out.println("ログイン済みの場合の処理通過");
					return toItemList(model);
				}
				System.out.println(session.getAttribute("preId"));
		//未ログインの場合
		return "login";
	
	}
	
	@RequestMapping("/logout")
	public String Logout() {
	session.removeAttribute("useId");
	session.removeAttribute("preId");
	return "forword:/toItemList";
}
	

}
