package jp.co.example.ecommerce_b.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import jp.co.example.ecommerce_b.domain.Users;
@SpringBootTest

class UserRepositoryTest {
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	@BeforeAll
	 static void setUpBeforeClass(@Autowired NamedParameterJdbcTemplate template ) throws Exception {
	
		Users user = new Users();
		user.setName("test");
		user.setEmail("test@test.com");
		user.setPassword("test");
		user.setTelephone("00000000000");
		user.setZipcode("1222333");
		user.setAddress("東京都新宿区新宿");
		String sql ="insert into users(name,email,password,zipcode,address,telephone)values(:name,:email,:password,:zipcode,:address,:telephone);";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", user.getName()).addValue("email", user.getEmail()).addValue("password", user.getPassword()).addValue("zipcode", user.getZipcode()).addValue("address", user.getAddress()).addValue("telephone", user.getTelephone());
		template.update(sql, param);
		System.out.println("テスト用仮ユーザー登録");
	}

	@AfterAll
	static void tearDownAfterClass(@Autowired NamedParameterJdbcTemplate template) throws Exception {
		String sql = "delete from users where name='test' and password='test';";
		template.update(sql,new MapSqlParameterSource());
		System.out.println("テスト用ユーザー削除");
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	public void searchByEmailTest() {
		assertEquals(userRepository.searchByEmail("test@test.com"),template.queryForObject("select id from users where email='test@test.com'", new MapSqlParameterSource(), Integer.class),"IDが取得できません");
		System.out.println("searchByEmailメソッドテスト完了");
		
	}
	
	@Test
	void checkEmailTest() {
		assertEquals(userRepository.checkEmail("test@test.com"),"test@test.com","emailが検索できていません");
		System.out.println("checkEmailメソッドテスト完了");
		
	}
	

}
