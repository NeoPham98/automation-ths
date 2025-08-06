package testcase;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.DriverFactory;

import java.time.Duration;
import java.util.List;

public class Read_Books {
    WebDriver driver;
    WebDriverWait wait;

    // ==== Utility Methods ====

    private void clickElement(By by) throws InterruptedException{
            Thread.sleep(1000);
            wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    public void hoverToElement(By selector) throws InterruptedException{
            WebElement element = driver.findElement(selector);
            new Actions(driver).moveToElement(element).pause(Duration.ofMillis(800)).perform();
    }

    private void login(String email, String password) throws InterruptedException{
            driver.findElement(By.name("email")).sendKeys(email);
            Thread.sleep(800);
            driver.findElement(By.name("password")).sendKeys(password);
            Thread.sleep(800);
            driver.findElement(By.cssSelector("button[type='submit']")).click();
            Thread.sleep(800);
    }

    private void goToBookAndStart() throws InterruptedException{
            hoverToElement(By.xpath("//div[span[normalize-space()='Bộ sách']]"));
            clickElement(By.xpath("//button[normalize-space()='Lớp 3']"));
            Thread.sleep(2000);

            hoverToElement(By.cssSelector("div.group:nth-of-type(3)"));
            Thread.sleep(500);

            List<WebElement> bookDivs = driver.findElements(By.cssSelector("div.group"));
            WebElement thirdDiv = bookDivs.get(3);
            thirdDiv.findElement(By.xpath(".//button[normalize-space()='Bắt đầu ngay']")).click();

    }

    private void nextPagesAndZoom() throws InterruptedException {
            for (int i = 0; i < 5; i++) {
                clickElement(By.cssSelector("button:has(svg.lucide-move-right)"));
                Thread.sleep(500);
            }
            clickElement(By.cssSelector("button:has(svg.lucide-zoom-in)"));
            Thread.sleep(500);
            clickElement(By.cssSelector("button:has(svg.lucide-zoom-out)"));
            Thread.sleep(800);

    }

    private void selectMaterialButton() throws InterruptedException{

            List<WebElement> buttons = driver.findElements(By.cssSelector("button.absolute.object-contain.cursor-pointer"));
            WebElement selected = buttons.get(3);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selected);
            Thread.sleep(3000);
            new Actions(driver).sendKeys(Keys.ESCAPE).perform();

    }

    private void openMaterialsListAndCrop() throws InterruptedException{

            for (int i = 0; i < 2; i++) {
                clickElement(By.cssSelector("button:has(svg.lucide-list)"));
                Thread.sleep(200);
            }
            for (int i = 0; i < 2; i++) {
                clickElement(By.cssSelector("button:has(svg.lucide-file-video)"));
                Thread.sleep(200);
            }
            clickElement(By.cssSelector("button:has(svg.lucide-crop)"));
            Thread.sleep(1000);

    }

    public void checkChatbotReply() throws InterruptedException{

            List<WebElement> repliesBefore = driver.findElements(By.cssSelector("div.markdown-body"));
            int countBefore = repliesBefore.size();

            WebElement cropArea = driver.findElement(By.xpath("//div[contains(@class, 'relative') and .//img[@alt]]"));
            new Actions(driver)
                    .moveToElement(cropArea)
                    .pause(Duration.ofMillis(200))
                    .clickAndHold()
                    .pause(Duration.ofMillis(300))
                    .moveByOffset(500, 200)
                    .pause(Duration.ofMillis(300))
                    .release()
                    .perform();

            Thread.sleep(1000);
            clickElement(By.cssSelector("button:has(svg.lucide-check)"));
            Thread.sleep(1000);
            clickElement(By.cssSelector("button:has(svg.lucide-send-horizontal)"));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            wait.until(d -> d.findElements(By.cssSelector("div.markdown-body")).size() > countBefore);
            List<WebElement> repliesAfter = driver.findElements(By.cssSelector("div.markdown-body"));
            WebElement latestReply = repliesAfter.get(repliesAfter.size() - 1);
            String replyText = latestReply.getText();

            if (replyText.trim().isEmpty()) {
                Assert.fail("Đọc sách: Failed - Chatbot không phản hồi nội dung");
            }

    }

    // ==== Test Setup & Teardown ====

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
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            Thread.sleep(7000);
            login("test@email.com", "Nkg@6688");
            Thread.sleep(3000);
}


    @Test
    public void ReadBook_Check() {
        try {
            goToBookAndStart();
            nextPagesAndZoom();
            selectMaterialButton();
            openMaterialsListAndCrop();
            checkChatbotReply();
            Thread.sleep(2000);
            System.out.println("Đọc sách: Passed");
        } catch (Exception e) {
            Assert.fail("Đọc sách: Failed - " + e.getMessage());
        }
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
