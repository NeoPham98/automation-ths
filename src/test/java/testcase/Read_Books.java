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
            Thread.sleep(1000);

            //Nhớ xoá cái này
            clickElement(By.xpath(".//button[normalize-space()='Bắt đầu ngay']"));

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
            clickElement(By.xpath("//button[normalize-space()='Kiểm tra kết quả']"));
            Thread.sleep(1000); // hoặc thay bằng wait nếu muốn

        // Kiểm tra có div thông báo xuất hiện
            By resultMessage = By.cssSelector("div.text-xl.flex.items-center.font-semibold.flex-1");

             try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(resultMessage));

                } catch (TimeoutException e) {
                Assert.fail("Đọc sách: Failed - Lỗi làm quiz trong sách " + e.getMessage());
            }
            Thread.sleep(1000);
                new Actions(driver).sendKeys(Keys.ESCAPE).perform();

    }
    private void filterMaterials() throws InterruptedException {
        clickElement(By.xpath("//button[@role='combobox' and .//span[text()='Tất cả học liệu']]"));
        Thread.sleep(300);
        clickElement(By.xpath("//*[text()='Câu hỏi']"));
        By resultSelector = By.cssSelector("div.flex.border.rounded-lg.transition-all.cursor-pointer");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(resultSelector));

        } catch (TimeoutException e) {
            Assert.fail("Đọc sách: Failed - Lọc không tìm thấy học liệu  'Câu hỏi'- " + e.getMessage());

        }



    }
    private void searchMaterials() throws InterruptedException {
        inputText(By.cssSelector("input[placeholder='Tìm kiếm']"), "quiz");
        Thread.sleep(1000);

        By resultSelector = By.cssSelector("div.flex.border.rounded-lg.transition-all.cursor-pointer");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(resultSelector));
            inputText(By.cssSelector("input[placeholder='Tìm kiếm']"), "");
        } catch (TimeoutException e) {
            Assert.fail("Đọc sách: Failed - Không tìm thấy học liệu tên 'quiz'- " + e.getMessage());

        }

    }



    private void toggleSidebarStrictCheck(String svgIconName, By targetDivSelector) throws InterruptedException {
        By buttonSelector = By.cssSelector("button:has(svg.lucide-" + svgIconName + ")");

        // Click lần 1 — kiểm tra xem sidebar có đang ẩn (phải có translate-x-full)
        clickElement(buttonSelector);
        Thread.sleep(300);

        WebElement sidebar = wait.until(ExpectedConditions.presenceOfElementLocated(targetDivSelector));
        String classAfterClick1 = sidebar.getAttribute("class");

        if (!classAfterClick1.contains("translate-x-full")) {
            Assert.fail("Đọc sách: Failed - Sidebar không click đóng được");
        }

        // Click lần 2 — kiểm tra sidebar đã mở (không còn translate-x-full)
        clickElement(buttonSelector);
        Thread.sleep(300);

        String classAfterClick2 = sidebar.getAttribute("class");

        if (classAfterClick2.contains("translate-x-full")) {
            Assert.fail("Đọc sách: Failed - Sidebar không click mở được");
        }
    }


    private void openMaterialsListAndCrop() throws InterruptedException {
        // Sidebar list (bên trái)
        By listSidebarSelector = By.cssSelector("div.border-r.bg-white");

        // Sidebar file-video (bên phải)
        By videoSidebarSelector = By.cssSelector("div.border-l.bg-white.right-0");

        toggleSidebarStrictCheck("list", listSidebarSelector);
        toggleSidebarStrictCheck("file-video", videoSidebarSelector);


        clickElement(By.cssSelector("button:has(svg.lucide-crop)"));
        Thread.sleep(1000);
        // Click nút "Kiểm tra kết quả"

    }

    private void inputText(By locator, String text) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(locator));

        Actions actions = new Actions(driver);
        actions.moveToElement(input)
                .click()
                .pause(Duration.ofMillis(300))
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .sendKeys(text)
                .pause(Duration.ofMillis(500))
                .perform();
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
            searchMaterials();
            filterMaterials();
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
