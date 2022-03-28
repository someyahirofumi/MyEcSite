package jp.co.example.ecommerce_b.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderConfirmForm {
	@NotBlank(message="名前を入力してください")
	private String destinationName;
	@NotBlank(message="メールアドレスを入力してください")
	@Email(message="メールアドレスの形式で入力してください")
	private String destinationEmail;
	@NotBlank(message="郵便番号を入力してください")
	private String destinationZipcode;
	private String destinationAddress;
	@NotBlank(message="電話番号を入力してください")
	private String destinationTel;
	@NotBlank(message="時間を選択してください")
	private String time;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private String date;
	@NotBlank(message="支払い方法を選択してください")
	private Integer paymentMethod;
	
	public Date getDeliveryDateTime() {
		SimpleDateFormat sdt=new SimpleDateFormat("yyyy-MM-dd HH");
		Date deliveryDateTime=new Date();
		try {
			deliveryDateTime=sdt.parse(date+" "+time);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		return deliveryDateTime;
	
		
		
		
	}
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getDestinationEmail() {
		return destinationEmail;
	}
	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}
	public String getDestinationZipcode() {
		return destinationZipcode;
	}
	public void setDestinationZipcode(String destinationZipcode) {
		this.destinationZipcode = destinationZipcode;
	}
	public String getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	public String getDestinationTel() {
		return destinationTel;
	}
	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}
	
	public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	@Override
	public String toString() {
		return "OrderConfirmForm [destinationName=" + destinationName + ", destinationEmail=" + destinationEmail
				+ ", destinationZipcode=" + destinationZipcode + ", destinationAddress=" + destinationAddress
				+ ", destinationTel=" + destinationTel + ", time=" + time + ", date=" + date + ", paymentMethod="
				+ paymentMethod + "]";
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	

}
