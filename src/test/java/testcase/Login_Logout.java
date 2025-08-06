package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import utils.DriverFactory;

import java.time.Duration;

public class Login_Logout {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("start-maximized");

        driver = DriverFactory.getDriver(options);
        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/dang-nhap");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        Thread.sleep(7000); // Đợi CAPTCHA nếu có


    }

    @Test
    public void LoginLogout_Check() throws InterruptedException {
        login("test@email.com", "Nkg@6688");
        clickAvatar();
        logout();

        // Xác minh đã quay lại trang đăng nhập
        String title = driver.getTitle();
        assert title.contains("Đăng nhập") || title.contains("Login");
    }

    private void login(String email, String password) throws InterruptedException {
        driver.findElement(By.name("email")).sendKeys(email);
        Thread.sleep(800);
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(800);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Có thể đợi đến khi avatar xuất hiện để đảm bảo login thành công
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dashboard"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//button[.//img[@alt='user-image']]"))
        ));

        System.out.println("Login bằng Email: Passed");
    }

    private void logout() throws InterruptedException {

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.//img[@alt='user-image']]"))).click();
        Thread.sleep(1000);

        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Đăng xuất']"))).click();

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("dang-nhap"),
                ExpectedConditions.titleContains("Đăng nhập")
        ));

        System.out.println("Logout: Passed");
    }

    private void clickAvatar() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[.//img[@alt='user-image']]")
        ));
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
