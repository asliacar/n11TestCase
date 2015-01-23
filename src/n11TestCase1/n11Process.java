package n11TestCase1;

import com.thoughtworks.selenium.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class n11Process {
	private Selenium selenium;

	@Before
	public void setUp() throws Exception {
		selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://www.n11.com/");
		selenium.start();
	}

	@Test
	public void testN11() throws Exception {
		//Turkish special characters are important for the test case
		//On first import charaters might not be suitable for unicode, we must always check and correct them
		
		selenium.open("/");
		selenium.click("link=Giriş Yap");
		selenium.waitForPageToLoad("30000");
		
		verifyEquals("Üye Girişi", selenium.getText("css=h2"));
		
		//A sample account currently working with a simple password
		selenium.type("id=email", "asliaktugacar@gmail.com");
		selenium.type("id=password", "sufle123");
		selenium.click("id=loginButton");
		selenium.waitForPageToLoad("30000");
		
		verifyTrue(selenium.isElementPresent("link=Giriş Yap"));
		
		selenium.type("id=searchData", "samsung");
		selenium.click("css=span.icon.iconSearch");
		selenium.waitForPageToLoad("30000");
		
		//In n11.com, "Samsung için" text seems to be a logical text choice to search and verify
		verifyTrue(selenium.getText("css=div.resultText").matches("^Samsung için[\\s\\S]*$"));
		
		selenium.click("link=2");
		selenium.waitForPageToLoad("30000");
		
		//Search results may vary so as the 3rd result of this test case
		//But, we are assuming the result do not change considering the case
		verifyEquals("2", selenium.getValue("name=currentPage"));
		for (int second = 0;; second++) {
			if (second >= 60) fail("timeout");
			try { if ("".equals(selenium.getText("//div[@id='p-13631913']/div[2]/span[2]"))) break; } catch (Exception e) {}
			Thread.sleep(1000);
		}
		selenium.click("//div[@id='p-13631913']/div[2]/span[2]");
		
		selenium.click("link=Hesabım");
		selenium.waitForPageToLoad("30000");
		
		//It appears only when logged in this text (Hesabým {Name}) appears on screen.
		//Therefore, we could verify it by searching this text
		verifyEquals("Merhaba,\n Aslı Aktuğ", selenium.getText("css=div.profile > span"));
		
		selenium.click("xpath=(//a[contains(text(),'Favorilerim')])[2]");
		selenium.waitForPageToLoad("30000");
		
		verifyEquals("Favorilerim", selenium.getText("id=buyerProductWatchLegend"));
		verifyEquals("Samsung i8200 Galaxy S3 Mini Cep Telefonu", selenium.getText("link=Samsung i8200 Galaxy S3 Mini Cep Telefonu"));
		
		selenium.click("link=Kaldır");
		selenium.waitForPageToLoad("30000");
		
		//We could have search for the Id of the product that has been added to the link's Url,
		//but, it seems like searching the name of the product works 
		verifyFalse(selenium.isTextPresent("Samsung i8200 Galaxy S3 Mini Cep Telefonu"));
	}

	private boolean verifyFalse(boolean isPresent) {
		return isPresent;
	}

	private boolean verifyTrue(boolean elementPresent) {
		return elementPresent;
	}

	private boolean verifyEquals(String val1, String val2) {
		return val1 == val2;
	}

	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
