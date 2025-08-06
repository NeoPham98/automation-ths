package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import utils.DriverFactory;

import java.time.Duration;

public class Login_Google {
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
    public void LoginGoogle_Check() throws InterruptedException {

        Thread.sleep(1000);
        // Click Đăng nhập bằng google
        driver.findElement(By.xpath("//button[.//span[text()='Đăng nhập với Google']]")).click();
        Thread.sleep(3000);

        // Thao tác cửa số tab google
        String mainWindow = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        // Điền email và Next
        driver.findElement(By.id("identifierId")).sendKeys("datpq1.rok20798@gmail.com");
        driver.findElement(By.xpath("//span[text()='Next']")).click();

        // Đợi và nhập mật khẩu
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd")));
        Thread.sleep(1000);
        driver.findElement(By.name("Passwd")).sendKeys("Aptx4869#@!?");
        driver.findElement(By.xpath("//span[text()='Next']")).click();
        Thread.sleep(10000);
        String title = driver.getTitle();
        System.out.println("Login bằng Google thành công: " + title);
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }

}
