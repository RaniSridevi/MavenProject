package dataprovider;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Ecommerceprogram {
	
	WebDriver driver;
    
	@BeforeClass
	public void redirect_url() {
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		driver.get("https://demo.nopcommerce.com/");
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		//Redirect to user registration page
	}
	
	@Test(dataProvider = "read_data",priority = 1)
	public void user_register(String gender,String first_name,String last_name,String day,String month,String year,
			String email,String company_name,String password,
			String confirm_password) throws InterruptedException {
		driver.findElement(By.xpath("//a[text()='Register']")).click();

		
		WebElement radio_male=driver.findElement(By.id("gender-male"));
		WebElement radio_female=driver.findElement(By.id("gender-female"));
		//Here we click on Radio_Button According to data provided in Excel
		if(gender.equals(radio_male.getAttribute("value"))) {
			radio_male.click();
		}
		else if(gender.equals(radio_female.getAttribute("value"))){
			radio_female.click();
		}
		
		
		WebElement firstname=driver.findElement(By.id("FirstName"));
		firstname.sendKeys(first_name);
		Thread.sleep(2000);

		WebElement lastname=driver.findElement(By.id("LastName"));
		lastname.sendKeys(last_name);
		
		WebElement Day=driver.findElement(By.name("DateOfBirthDay"));
		Select select=new Select(Day);
		select.selectByVisibleText(day);
		Thread.sleep(2000);
		
		WebElement mon=driver.findElement(By.name("DateOfBirthMonth"));
		Select select1=new Select(mon);
		select1.selectByVisibleText(month);
		
		WebElement Year=driver.findElement(By.name("DateOfBirthYear"));
		Select select2=new Select(Year);
		select2.selectByVisibleText(year);

		WebElement usermail=driver.findElement(By.id("Email"));
		usermail.sendKeys(email);
        //Thread.sleep(2000);
		
		WebElement company=driver.findElement(By.id("Company"));
		company.sendKeys(company_name);

		
		WebElement pass_word=driver.findElement(By.id("Password"));
		pass_word.sendKeys(password);

		WebElement confirmpassword=driver.findElement(By.id("ConfirmPassword"));
		confirmpassword.sendKeys(confirm_password);
		
		driver.findElement(By.id("register-button")).click();
//		Thread.sleep(2000);
		
	}
	
	@DataProvider(name="read_data")
	public Object[][] read_data() throws EncryptedDocumentException, IOException {
		User_Data data=new User_Data();
		Object [][]output=data.readfile();
		
		return output;
	}
	
	@Test(priority = 2)
	public void account_action() throws EncryptedDocumentException, IOException, InterruptedException {
		//Here user will login initially and perform account action like select a product and add to cart
		driver.findElement(By.xpath("//a[@class='ico-login']")).click();
		WebElement email_id=driver.findElement(By.id("Email"));
		WebElement password=driver.findElement(By.id("Password"));
		
		//Creating an object of File Input Stream class to read/recognize the Excel file provided in this
		FileInputStream file=new FileInputStream("D:\\Eclipse\\workspace\\Demo\\src\\main\\resources\\Project_Data.xlsx");
		
		Workbook book=WorkbookFactory.create(file);
		//From cell no 6  here fetched 
		String user=book.getSheet("TestData").getRow(3).getCell(6).getStringCellValue();
		String pass=book.getSheet("TestData").getRow(3).getCell(8).getStringCellValue();
		
		email_id.sendKeys(user);
		password.sendKeys(pass);
		
		//Clicking check box of remember me
		driver.findElement(By.id("RememberMe")).click();
		
		book.close();
		//Perform login operations
		driver.findElement(By.cssSelector("button[class='button-1 login-button']")).click();
		//Refresh the page
		try {
		//driver.navigate().refresh(); 
		WebDriverWait wait=new WebDriverWait(driver,Duration.ofSeconds(10));
		//Once user log in successfully now we go to Apparel Section by hover over the Apparel Section
		//Hover over the Apparel by using action class
		WebElement apparel=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//a[@href='/apparel'])[1]")));
		Actions hover=new Actions(driver);
		
		hover.moveToElement(apparel).build().perform();
		WebElement shoes=driver.findElement(By.linkText("Shoes"));
		shoes.click();
		//Product added to wish list
		driver.findElement(By.xpath("(//button[@title='Add to wishlist'])[2]")).click();

		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//After that select particular section
		
		
		/*Now click on particular shoes and perform operations like 
		  Add to wish list and checkout and other operations */
		driver.findElement(By.linkText("Nike Floral Roshe Customized Running Shoes")).click();
		JavascriptExecutor js= (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,450)", "");
		
		
		//now select size of shoes
		WebElement shoe_size=driver.findElement(By.id("product_attribute_6"));
		Select size=new Select(shoe_size);
		size.selectByValue("15");
		
		//Color of shoes
		WebElement colour=driver.findElement(By.id("product_attribute_7"));
		Select col=new Select(colour);
		col.selectByValue("17");
		
		Thread.sleep(2000);
		driver.findElement(By.xpath("/html/body/div[6]/div[3]/div/div[3]/div/div[2]/div[2]/div[2]/div/div/div[3]/div/div[1]/a/img")).click();
		
		//driver.findElement(By.xpath("(//span[@class='attribute-square'])[1]")).click();
		
//		//Add to wish list
//		driver.findElement(By.xpath("//button[@id='add-to-wishlist-button-24']")).click();
		
		//Add to wish list
		
		//Add to cart
		driver.findElement(By.xpath("//button[text()='Add to cart']")).click();
		
		//Email a friend
		driver.findElement(By.xpath("//button[text()='Email a friend']")).click();
		
		//Scroll Up
		//js.executeScript("window.scrollBy(0,-550)", "");
		
		//Send mail to friend friends id
		driver.findElement(By.id("FriendEmail")).sendKeys("sridevi25711@gmail.com");
		
		
		//Optional message
		driver.findElement(By.id("PersonalMessage")).sendKeys("Please check the product");
		//Click on submit to send mail
		driver.findElement(By.xpath("//button[@name='send-email']")).click();
		
		js.executeScript("window.scrollBy(0,-550)", "");
		Thread.sleep(2000);
		driver.findElement(By.linkText("Shopping cart")).click();
		
		//updating the quantity of product
		WebElement qty=driver.findElement(By.className("qty-input"));
		qty.clear();
		Thread.sleep(1000);
		qty.sendKeys("1");
		
		driver.findElement(By.id("updatecart")).click();
		//clicking checkout and complete the shopping
		driver.findElement(By.id("termsofservice")).click();
		Thread.sleep(1500);
		driver.findElement(By.id("checkout")).click();
		
	}
	
	@AfterClass
	public void teardown() throws InterruptedException {
		
		driver.quit();
	}
	
}


