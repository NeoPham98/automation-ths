package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.DriverFactory;

public class Login_Test {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/sign-in");
    }

    @Test
    public void Login() throws InterruptedException {
//        System.out.println("Test login...");

        // Nhập email và mật khẩu
        driver.findElement(By.name("email")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("Nkg@6688");

        // Click nút "Đăng nhập ngay"
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Đợi vài giây để trang chuyển (dùng tạm Thread.sleep)
        Thread.sleep(3000);

        // In tiêu đề trang để kiểm tra
        String title = driver.getTitle();
//        System.out.println("Page Title: " + title);

        // Kiểm tra điều kiện sau đăng nhập (thay đổi nếu cần)
        assert title.contains("Dashboard") || title.contains("Trang chủ") || !title.contains("Đăng nhập");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
