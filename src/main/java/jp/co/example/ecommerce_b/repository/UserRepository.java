package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Users;

@Repository
public class UserRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template ;
	
	private static final RowMapper<Users> USERS_ROW_MAPPER=(rs,i)->{
		Users user = new Users();
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setId(rs.getInt("id"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		user.setPassword(rs.getString("password"));
		return user;
		
	};
	
	
	public void registerUser(Users user) {
		String sql ="insert into users(name,email,password,zipcode,address,telephone)values(:name,:email,:password,:zipcode,:address,:telephone);";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", user.getName()).addValue("email", user.getEmail()).addValue("password", user.getPassword()).addValue("zipcode", user.getZipcode()).addValue("address", user.getAddress()).addValue("telephone", user.getTelephone());
		template.update(sql, param);
	}
	
	public Integer searchByEmail(String email) {
		
		String sql = "select id from users where email=:email";
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
		List<Integer> user=template.queryForList(sql,param,Integer.class);
	
		if(user.isEmpty()) {
			return null;
		}
		return user.get(0);
	}
	
	public String checkEmail(String email) {
		List<String> result=template.queryForList("select email from users where email=:email;",new MapSqlParameterSource().addValue("email", email), String.class);
		if(result.isEmpty()) {
			return null;
		}
		return result.get(0);
	} 
	
	public Users searchPassword(String email) {

		String sql = "SELECT id,name,email,zipcode,address,telephone,password FROM users WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource ().addValue("email", email);
		
		List<Users>userList = template.query(sql, param, USERS_ROW_MAPPER);
		if(userList.isEmpty()) {
			return null;
		}
		return userList.get(0);
	}
}
