package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.*;
import utils.DriverFactory;

public class Login_Email {
    WebDriver driver;


    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled"); // giảm phát hiện tự động
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("start-maximized");

        driver = DriverFactory.getDriver(options);
        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/sign-in");
    }

    @Test
    public void LoginEmail_Check() throws InterruptedException {
        // Đợi bạn xử lý CAPTCHA Cloudflare thủ công
        Thread.sleep(5000); // hoặc nhiều hơn nếu CAPTCHA chưa xong

        driver.findElement(By.name("email")).sendKeys("test@email.com");
        Thread.sleep(800);
        driver.findElement(By.name("password")).sendKeys("Nkg@6688");
        Thread.sleep(800);
        // Click nút "Đăng nhập ngay"
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Đợi vài giây để trang chuyển (dùng tạm Thread.sleep)
        Thread.sleep(3000);

        // In tiêu đề trang để kiểm tra
        String title = driver.getTitle();
        System.out.println("Login bằng Email thành công: " + title);

        // Kiểm tra điều kiện sau đăng nhập
        assert title.contains("Dashboard") || title.contains("Trang chủ") || !title.contains("Đăng nhập");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
