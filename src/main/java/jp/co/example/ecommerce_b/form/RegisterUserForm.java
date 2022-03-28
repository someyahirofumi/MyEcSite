package jp.co.example.ecommerce_b.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;



public class RegisterUserForm {
	@NotBlank(message="氏名を入力してください")
	private String name;
	@Email(message="Eメールの形式で入力してください")
	@NotBlank(message="メールアドレスを入力してください")	
	private String email;
	@Pattern(regexp="^[0-9]{7}$",message="ハイフンを抜いて入力してください")
	@NotBlank(message="郵便番号を入力してください")
	private String zipcode;
	@NotBlank(message="住所を入力してください")
	private String address;
	@Pattern(regexp="^\\d{1,4}-?\\d{1,4}-?\\d{4}$", message="ハイフンを含めた電話番号を入力してください")
	
	private String telephone;
	@Length(min=8, max=16,message="パスワードは8文字以上16文字以内で入力してください")
	private String password;
	private String confirmationPassword;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmationPassword() {
		return confirmationPassword;
	}
	public void setConfirmationPassword(String confirmationPassword) {
		this.confirmationPassword = confirmationPassword;
	}
	@Override
	public String toString() {
		return "RegisterUser [name=" + name + ", email=" + email + ", zipcode=" + zipcode + ", address=" + address
				+ ", telephone=" + telephone + ", password=" + password + ", confirmationPassword="
				+ confirmationPassword + "]";
	}
	

}
