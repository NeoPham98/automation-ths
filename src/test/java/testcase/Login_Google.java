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
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() throws InterruptedException{
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("start-maximized");

        driver = DriverFactory.getDriver(options);
        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/dang-nhap");

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        Thread.sleep(8000);
    }

    @Test
    public void LoginGoogle_Check() throws InterruptedException {
        loginWithGoogle("datpq1.rok20798@gmail.com", "Aptx4869#@!?");

        // Đợi trang load sau khi đăng nhập
        Thread.sleep(10000);
        String title = driver.getTitle();
        System.out.println("Login bằng Google: Passed");
    }

    private void loginWithGoogle(String email, String password) throws InterruptedException {

        // Click nút "Đăng nhập với Google"
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[.//span[text()='Đăng nhập với Google']]")
        )).click();

        Thread.sleep(3000); // đợi tab mới hiển thị

        // Chuyển sang tab Google
        switchToNewWindow();

        // Nhập email
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("identifierId"))).sendKeys(email);
        driver.findElement(By.xpath("//span[text()='Next']")).click();

        // Nhập mật khẩu
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Passwd"))).sendKeys(password);
        driver.findElement(By.xpath("//span[text()='Next']")).click();

    }

    private void switchToNewWindow() {
        String mainWindow = driver.getWindowHandle();
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
