package jp.co.example.ecommerce_b.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Users;
import jp.co.example.ecommerce_b.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	 @Autowired
	    PasswordEncoder passwordEncoder;
	public void registerUser(Users user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.registerUser(user);
	}
	
	public Integer searchByEmail(String email) {
		return userRepository.searchByEmail(email);
	}
	
	public String checkEmail(String email) {
		return userRepository.checkEmail(email);
	}


}
