package testcase;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.DriverFactory;

import java.time.Duration;

public class Create_Other_Material {
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
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/sign-in");

        Thread.sleep(5000); // Đợi xử lý CAPTCHA
        driver.findElement(By.name("email")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("Nkg@6688");
        Thread.sleep(800);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }
    private void clickElement(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    private void type(By locator, String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

        Actions actions = new Actions(driver);
        actions.moveToElement(input)
                .click()
                .pause(Duration.ofMillis(300))
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .sendKeys(text)
                .pause(Duration.ofMillis(500))
                .perform();
    }


    private void selectDropdown(String visibleText) {
        clickElement(By.xpath("//button[@role='combobox']"));
        clickElement(By.xpath("//div[@role='option' or @role='menuitem'][normalize-space()='" + visibleText + "']"));
    }

    private void selectOption(String label, String option) {
        clickElement(By.xpath("//button[@role='combobox' and .//span[text()='" + label + "']]"));
        clickElement(By.xpath("//div[contains(@role,'option') or contains(@role,'menuitem')][normalize-space()='" + option + "']"));
    }

    private void inputMetadata(String placeholder, String value) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='" + placeholder + "']")));
        input.sendKeys(value, Keys.ENTER);
    }

    public void createMaterials(
            String title,
            String description,
            String visibility,
            String subject,
            String grade,
            String level,
            String language,
            String year,
            String author,
            String chiefEditor,
            String compiler,
            String ownership
    ) throws InterruptedException {
        // Tiêu đề & mô tả
        Thread.sleep(5000);
        type(By.xpath("//input[@placeholder='Nhập tiêu đề']"), title);
        Thread.sleep(800);
        type(By.xpath("//textarea[@placeholder='Nhập mô tả']"), description);
        Thread.sleep(800);
        // Chọn công khai/riêng tư
        selectDropdown(visibility);
        clickElement(By.xpath("//button[normalize-space()='Tiếp theo']"));
        Thread.sleep(800);
        // Các combo box lựa chọn
        selectOption("Chọn bộ môn", subject);
        Thread.sleep(800);
        selectOption("Chọn khối lớp", grade);
        Thread.sleep(800);
        selectOption("Chọn cấp độ", level);
        Thread.sleep(800);
        selectOption("Chọn ngôn ngữ", language);
        Thread.sleep(800);
        selectOption("Chọn năm sản xuất", year);
        Thread.sleep(800);

        // Metadata
        inputMetadata("Nhập tác giả", author);
        Thread.sleep(800);
        inputMetadata("Nhập Tổng chủ biên/chủ biên", chiefEditor);
        Thread.sleep(800);
        inputMetadata("Nhập Biên soạn/biên dịch", compiler);
        Thread.sleep(800);
        inputMetadata("Nhập nội dung", ownership);
        Thread.sleep(800);

        // Hoàn tất
        clickElement(By.xpath("//button[normalize-space()='Tiếp theo']"));
        Thread.sleep(800);
        clickElement(By.xpath("//button[normalize-space()='Tạo học liệu khác']"));
        Thread.sleep(1000);
    }


    @Test
    public void CreateOtherMaterial_Check() throws InterruptedException {
        clickElement(By.cssSelector("a[href='/kho-hoc-lieu']"));
        Thread.sleep(3000);
        clickElement(By.xpath("//button[span[text()='Tạo']]"));
        Thread.sleep(2000);
        clickElement(By.xpath("//div[p[text()='Học liệu']]"));
        Thread.sleep(2000);

//==============================================BÀI GIẢNG===============================================================
        clickElement(By.xpath("//figure[h3[normalize-space()='Bài giảng']]"));
        // Upload file
        Thread.sleep(2000);
        WebElement uploadInput = driver.findElement(By.xpath("//input[@type='file']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", uploadInput);
        uploadInput.sendKeys("C:\\Users\\NCPC\\Downloads\\DataTest\\samplePPTX.pptx");

        createMaterials(
                "Học liệu smoke test",
                "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                "Công khai",
                "Đạo đức",
                "Lớp 12",
                "Cơ bản",
                "Tiếng Việt",
                "2025",
                "Hải Tú",
                "Sơn Tùng MTP",
                "Trịnh Trần Phương Tuấn",
                "Thiên An"
        );
    }



    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
