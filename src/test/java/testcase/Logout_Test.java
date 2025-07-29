package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import utils.DriverFactory;

public class Logout_Test {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/sign-in");

        // Đăng nhập trước khi logout
        driver.findElement(By.name("email")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("Nkg@6688");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Test
    public void Logout() throws InterruptedException {
        Thread.sleep(3000); // đợi trang load sau login

        // Click vào avatar
        driver.findElement(By.xpath("//button[@id='radix-:r2:']")).click();
        driver.findElement(By.xpath("//div[text()='Đăng xuất']")).click();
        Thread.sleep(3000); // đợi load lại trang login
        String title = driver.getTitle();

        // Kiểm tra đã quay lại trang đăng nhập
        assert title.contains("Đăng nhập");
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}