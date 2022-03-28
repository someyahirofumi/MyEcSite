package jp.co.example.ecommerce_b.controllre;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkPassword")
public class CheckPasswordApiController {
	
	@RequestMapping(value="/check",method=RequestMethod.POST)
	public Map<String,String>check(String password,String confirmPass){
		System .out .println("処理開始");
		Map<String,String>map=new HashMap<>();
		if(!(password.equals(confirmPass))) {
			map.put("checkMessage", "パスワードと確認用パスワードが一致していません");
		}if(password.equals(confirmPass)) {
			map.put("checkMessage", "OK!");
		}
		return map;
		
	}

}
