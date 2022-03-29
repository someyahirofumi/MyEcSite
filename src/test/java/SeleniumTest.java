import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

class SeleniumTest {
	private static WebDriver driver;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		   System.setProperty("webdriver.chrome.driver", "./exe/chromedriver");
	         driver= new ChromeDriver();
	       
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		driver.quit();
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void loginTest() {
		 driver.get("http://localhost:8080/user");
		 assertEquals("http://localhost:8080/user",driver.getCurrentUrl());
		 driver.findElement(By.id("inputEmail")).sendKeys("test@test.co.jp");
		 driver.findElement(By.id("inputPassword")).sendKeys("aaaaaaaa");
		 driver.findElement(By.xpath("/html/body/div/div[1]/div/div/form/fieldset/div[3]/button")).click();
		 assertEquals("http://localhost:8080/user/login",driver.getCurrentUrl(),"ログイン処理ができていません");
		 
		 
	}
	
	@Test
	void registerTest() {
		driver.get("http://localhost:8080/user");
		driver.findElement(By.xpath("/html/body/div/div[2]/div/a")).click();
		assertEquals("http://localhost:8080/user/toRegisterUser",driver.getCurrentUrl());
	}
	
	@Test 
	void searchTest() {
		 driver.get("http://localhost:8080/user");
		 assertEquals("http://localhost:8080/user",driver.getCurrentUrl());
		 driver.findElement(By.id("inputEmail")).sendKeys("test@test.co.jp");
		 driver.findElement(By.id("inputPassword")).sendKeys("aaaaaaaa");
		 driver.findElement(By.xpath("/html/body/div/div[1]/div/div/form/fieldset/div[3]/button")).click();
		 WebElement element= driver.findElement(By.xpath("//*[@id=\"listBox\"]"));
		 Select selectObject = new Select(element);
		 selectObject.selectByIndex(1);
		 driver.findElement(By.xpath("//*[@id=\"select_menu\"]/button")).click();
		 assertEquals("チョコクッキー",driver.findElement(By.xpath("/html/body/div/div[2]/div/table/tbody/tr[1]/th[1]/a[2]")).getText());
		 WebElement element2= driver.findElement(By.xpath("//*[@id=\"listBox\"]"));
		 Select selectObject2 = new Select(element2);
		 selectObject2.selectByIndex(2);
		 driver.findElement(By.xpath("//*[@id=\"select_menu\"]/button")).click();
		 assertEquals("エスプレッソフラペチーノ",driver.findElement(By.xpath("/html/body/div/div[2]/div/table/tbody/tr[1]/th[1]/a[2]")).getText(),"高い順並べ替えができていません");
		
		
		
		
	}

}
