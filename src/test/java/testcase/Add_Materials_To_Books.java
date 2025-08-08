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

public class Add_Materials_To_Books {
    WebDriver driver;
    WebDriverWait wait;

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
    private void goToBookAndEdit() throws InterruptedException {
        hoverToElement(By.xpath("//div[span[normalize-space()='Bộ sách']]"));
        clickElement(By.xpath("//button[normalize-space()='Lớp 3']"));
        Thread.sleep(2000);
        clickElement(By.xpath("//a[.//h2[normalize-space()='Công Nghệ 3']]"));
        Thread.sleep(1000);
        clickElement(By.cssSelector("button:has(svg.lucide-square-pen)"));
        Thread.sleep(500);
    }

    private void nextPage() throws InterruptedException{
        for (int i = 0; i < 3; i++) {
            clickElement(By.cssSelector("button:has(svg.lucide-move-right)"));
            Thread.sleep(300);
        }

    }
    private void inputText(By locator, String text) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(locator));

        Actions actions = new Actions(driver);
        actions.moveToElement(input)
                .click()
                .pause(Duration.ofMillis(500))
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .sendKeys(text)
                .pause(Duration.ofMillis(500))
                .perform();
    }

    private void addMaterials() throws InterruptedException{
        clickElement(By.cssSelector("button:has(svg.lucide-star)"));
        Thread.sleep(300);

        WebElement imgBookArea = driver.findElement(By.xpath("//div[contains(@class, 'relative') and .//img[@alt]]"));
        int[][] clickPoints = {
                {760, -250},
                {760, -200},
                {760, -150},
                {760, -100},
                {760, -50},
                {760, 0},
                {760, 50},
                {760, 100},
                {760, 150}
        };
        for (int i = 0; i < clickPoints.length; i++) {
            int[] point = clickPoints[i];
            new Actions(driver)
                    .moveToElement(imgBookArea)
                    .moveByOffset(point[0], point[1])  // di chuyển tới vị trí cần click
                    .click()
                    .pause(Duration.ofMillis(300))     // pause nhỏ giữa các click
                    .perform();

            if (i == 0){
                uploadQuiz();
//                uploadLecture();
            }
            else if (i == 1){
                uploadScrom();
            }
            else if (i == 2){
                uploadDocument();
            }
            else if (i == 3){
                uploadAudio();
            }
            else if (i == 4){
                uploadVideo();
            }
            else if (i == 5){
                uploadImage();
            }
            else if (i == 6){
                upload3D();
            }
            else if (i == 7){
                uploadInteractive();
            }
            else if (i == 8){
                uploadQuiz();
            }
        }
        Thread.sleep(3000);
    }

    private void uploadFile(String filePath) {
        WebElement uploadInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='file']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", uploadInput);
        uploadInput.sendKeys(filePath);
    }

    private void clickInOpenDialog(By relativeBy) {
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[role='dialog'][data-state='open']"))
        );
        WebElement el = dialog.findElement(relativeBy);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (Exception ignore) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }
    }

    private void selectOption(String label, String option) throws InterruptedException {
        clickElement(By.xpath("//button[@role='combobox' and .//span[text()='" + label + "']]"));
        clickElement(By.xpath("//div[contains(@role,'option') or contains(@role,'menuitem')][normalize-space()='" + option + "']"));
    }

    public void uploadLecture() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Học liệu bài giảng");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='Bài giảng']/ancestor::figure[1]"));
        Thread.sleep(500);
        uploadFile("D:\\Data\\samplepptx.pptx");
        Thread.sleep(500);
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

    public void uploadScrom() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Học liệu Scrom");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='Scrom / xAPI']/ancestor::figure[1]"));
        Thread.sleep(500);
        uploadFile("D:\\Data\\Que_diem.zip");
        Thread.sleep(500);
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

    public void uploadDocument() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Tài liệu");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='Tài liệu']/ancestor::figure[1]"));
        Thread.sleep(500);
        uploadFile("D:\\Data\\DOCX_sample.docx");
        Thread.sleep(500);
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

    public void uploadAudio() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Học liệu âm thanh");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='Âm thanh']/ancestor::figure[1]"));
        Thread.sleep(500);
        uploadFile("D:\\Data\\AUDIO_sample.mp3");
        Thread.sleep(500);
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

    public void uploadVideo() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Học liệu video");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='Video']/ancestor::figure[1]"));
        Thread.sleep(500);
        uploadFile("D:\\Data\\Video.mp4");
        Thread.sleep(500);
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

    public void uploadImage() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Học liệu hình ảnh");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='Hình ảnh']/ancestor::figure[1]"));
        Thread.sleep(500);
        uploadFile("D:\\Data\\sampleIMG.jpg");
        Thread.sleep(500);
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

    public void upload3D() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Học liệu 3D");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='3D / VR']/ancestor::figure[1]"));
        Thread.sleep(500);
        uploadFile("D:\\Data\\3D_sample.glb");
        Thread.sleep(500);
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

    public void uploadInteractive() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Học liệu nâng cao");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='Học liệu nâng cao']/ancestor::figure[1]"));
        Thread.sleep(500);
        uploadFile("D:\\Data\\Robot_Finding_Treasure.zip");
        Thread.sleep(500);
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

    public void uploadQuiz() throws InterruptedException{
        inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Bộ đề");
        clickElement(By.cssSelector("button:has(svg.lucide-plus)"));
        clickInOpenDialog(By.xpath(".//p[normalize-space()='Câu hỏi']/ancestor::figure[1]"));
        Thread.sleep(500);
        selectOption("Chọn khối lớp", "Lớp 3");
        Thread.sleep(500);
        selectOption("Chọn bộ đề", "Công nghệ 3 - Bài 2: Sử dụng đèn học");
        clickElement(By.cssSelector("button.text-white.bg-blue-900"));
        Thread.sleep(3000);
    }

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
    public void AddMaterialsToBook_Check() throws InterruptedException {
        goToBookAndEdit();
        nextPage();
        addMaterials();

    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }


}
