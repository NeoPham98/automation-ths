package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.DriverFactory;

import java.time.Duration;
import java.util.List;

public class Read_Books_With_Anonymus {
    WebDriver driver;
    WebDriverWait wait;


    private void clickElement(By by) throws InterruptedException {
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }
    public void hoverToElement(By selector) {
            WebElement element = driver.findElement(selector);
            // Hover với Actions + delay nhỏ để hỗ trợ hover menu hiệu ứng
            Actions actions = new Actions(driver);
            actions.moveToElement(element).pause(Duration.ofMillis(800)).perform();
            System.out.println("✅ Đã hover vào phần tử: " + selector.toString());

    }

    public void clickStartButtonReadBook(int index) {


            // 1. Lấy danh sách các phần tử <a> chính (vùng hover)
            List<WebElement> hoverTargets = driver.findElements(By.cssSelector("a[href*='/books/']"));



            // 2. Hover vào sách tại index
            WebElement hoverTarget = hoverTargets.get(index);
            new Actions(driver).moveToElement(hoverTarget).pause(Duration.ofMillis(500)).perform();

            // 3. Click vào button nằm trong vùng đó
            WebElement button = hoverTarget.findElement(By.xpath(".//button[contains(text(), 'Bắt đầu ngay')]"));
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(button)).click();

            System.out.println("✅ Click thành công vào sách thứ " + (index + 1));


    }
    @BeforeMethod
    public void setUp() throws InterruptedException{
        // Thiết lập driver và các tùy chọn nếu cần;
         driver = DriverFactory.getDriver();
         driver.manage().window().maximize();
         driver.get("https://dev.gkebooks.click/");
         wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }


    @Test
    public void ReadBooksWithAnonymus_Check() throws InterruptedException {

        hoverToElement(By.cssSelector("div.group.lg\\:relative"));
        Thread.sleep(2000);
        clickElement(By.xpath("//button[normalize-space()='Lớp 3']"));
        Thread.sleep(2000);
        clickStartButtonReadBook(2);
        Thread.sleep(5000);




        // In ra thông báo kiểm tra
        System.out.println("Đọc sách thành công mà không cần đăng nhập.");

        // Thêm các bước kiểm tra khác nếu cần
    }
    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}

