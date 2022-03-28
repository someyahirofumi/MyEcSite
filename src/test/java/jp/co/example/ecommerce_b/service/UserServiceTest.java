package jp.co.example.ecommerce_b.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import jp.co.example.ecommerce_b.domain.Users;
import jp.co.example.ecommerce_b.repository.UserRepository;
@SpringBootTest
class UserServiceTest {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	
	@Mock
	private UserRepository userRepository;
	
	@InjectMocks
	private UserService userService;
	
	

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		
		
		template.update("insert into users(name,email,password,zipcode,address,telephone)values('test','test@test.com','test','0000000','test','000000000');",new MapSqlParameterSource());
		System.out.println("test用データ追加");
	}

	@AfterEach
	void tearDown() throws Exception {
		template.update("delete from users where id=(select MAX(id)from users);",new MapSqlParameterSource());
		System.out.println("test用データ削除");
	
	}

	@Test
	void test() {
		when(userService.searchByEmail("test@test.com")).thenReturn(template.queryForObject("select Max(id)from users;",new MapSqlParameterSource(),Integer.class));
		System.out.println("searchByEmailメソッドテスト完了");
	}
	
	@Test
	void registerUserTest() {
		Users user = new Users();
		user.setName("testさん");
		user.setEmail("test@test.co.jp");
		user.setPassword("tttttttt");
		user.setZipcode("11111111");
		user.setAddress("test県test市");
		user.setTelephone("01111111111");
		userService.registerUser(user);
		
		assertEquals("testさん",template.queryForObject("select name from users where id=(select MAX(id)from users);", new MapSqlParameterSource(), String.class),"nameが登録されていません");
		assertEquals("test@test.co.jo",template.queryForObject("select email from users where id=(select MAX(id)from users);", new MapSqlParameterSource(), String.class),"emailが登録されていません");
		assertEquals(passwordEncoder.encode("tttttttt"),template.queryForObject("select password from users where id=(select MAX(id)from users);", new MapSqlParameterSource(), String.class),"パスワードが登録されていません");
		assertEquals("11111111",template.queryForObject("select zipcode from users where id=(select MAX(id)from users);", new MapSqlParameterSource(), String.class),"郵便番号が登録されていません");
		assertEquals("test県test市",template.queryForObject("select address from users where id=(select MAX(id)from users);", new MapSqlParameterSource(), String.class),"住所が登録されていません");
		assertEquals("01111111111",template.queryForObject("select telephone from users where id=(select MAX(id)from users);", new MapSqlParameterSource(), String.class),"電話番号が登録されていません");
		
		System .out .println("registerUserメソッドテスト完了");
		template.update("delete from users where id=(select MAX(id)from uses);", new MapSqlParameterSource());
		System.out.println("registerUserメソッドテストようデータ削除");
		
	}

}
