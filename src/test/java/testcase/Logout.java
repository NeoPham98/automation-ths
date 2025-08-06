package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.DriverFactory;

import java.time.Duration;

public class Logout {
    WebDriver driver;
    WebDriverWait wait;


    @BeforeMethod
    public void setUp() throws InterruptedException{
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled"); // giảm phát hiện tự động
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("start-maximized");

        driver = DriverFactory.getDriver(options);
        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/dang-nhap");
        // Đăng nhập trước khi logout
        Thread.sleep(5000);
        driver.findElement(By.name("email")).sendKeys("test@email.com");
        Thread.sleep(800);
        driver.findElement(By.name("password")).sendKeys("Nkg@6688");
        Thread.sleep(800);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

    }


    @Test
    public void Logout_Check() throws InterruptedException {
        Thread.sleep(3000); // đợi trang load sau login

        // Click vào avatar
        driver.findElement(By.xpath("//button[.//img[@alt='user-image']]")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[text()='Đăng xuất']")).click();
        Thread.sleep(3000); // đợi load lại trang login
        String title = driver.getTitle();
        System.out.println("Logout thành công: " + title);


    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}