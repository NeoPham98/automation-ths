package testcase;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import utils.DriverFactory;

import java.time.Duration;

public class Create_Other_Material {
    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/sign-in");

        // Login
        driver.findElement(By.name("email")).sendKeys("test@email.com");
        driver.findElement(By.name("password")).sendKeys("Nkg@6688");
        driver.findElement(By.cssSelector("button[type='submit']")).click();


    }
    @Test
    public void CreateOtherMaterial_Check() throws InterruptedException {

        // Click kho học liệu trên header
        WebDriverWait loading = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement material = loading.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("a[href='/materials']")
        ));
        material.click();

        // Click tạo
        Thread.sleep(3000);
        WebElement buttonCreate = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//button[span[text()='Tạo']]")
                ));
        buttonCreate.click();

        // Chọn Tạo học liệu khác
        Thread.sleep(2000);
        driver.findElement(By.xpath("//div[p[text()='Học liệu']]")).click();

        // Chọn học liệu bài giảng
        Thread.sleep(2000);
        WebElement lesson = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//figure[h3[normalize-space()='Bài giảng']]")
                ));
        lesson.click();

        //Upload file pptx
        Thread.sleep(2000);
        WebElement uploadInput = driver.findElement(By.xpath("//input[@type='file']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", uploadInput);
        uploadInput.sendKeys("C:\\Users\\GHC\\Downloads\\Data\\PPTX_sample.pptx");

        //Nhập tên file
        Thread.sleep(5000);
        WebElement inputTitle = driver.findElement(By.xpath("//input[@placeholder='Nhập tiêu đề']"));
        inputTitle.sendKeys("Bài giảng Smoke Test");
        Thread.sleep(1000);

        WebElement inputDescription = driver.findElement(By.xpath("//textarea[@placeholder='Nhập mô tả']"));
        inputDescription.sendKeys("Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
        Thread.sleep(1000);

        // Chọn Công khai hoặc riêng tư
        WebElement comboBoxBtn = driver.findElement(By.xpath("//button[@role='combobox']"));
        comboBoxBtn.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option' or @role='menuitem'][normalize-space()='Công khai']") // Hoặc 'Riêng tư'
        ));
        option.click();

        // Click tiếp theo
        Thread.sleep(1000);
        WebElement nextButton = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Tiếp theo']")));
        nextButton.click();

        // Chọn bộ môn
        Thread.sleep(1000);
        WebDriverWait waitChoiceSubject = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement optionSubject = waitChoiceSubject.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@role='combobox' and .//span[text()='Chọn bộ môn']]")
        ));
        optionSubject.click();
        // Chờ mục "Đạo đức" hiển thị và click
        WebElement optionDaoDuc = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@role,'option') or contains(@role,'menuitem')][normalize-space()='Đạo đức']")
        ));
        optionDaoDuc.click();



        // Chọn khối lớp
        Thread.sleep(1000);
        WebDriverWait waitChoiceClass = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement optionClass = waitChoiceClass.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@role='combobox' and .//span[text()='Chọn khối lớp']]")
        ));
        optionClass.click();
        // Chờ mục "Lớp 12" hiển thị và click
        WebElement optionClass1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@role,'option') or contains(@role,'menuitem')][normalize-space()='Lớp 12']")
        ));
        optionClass1.click();



        // Chọn cấp độ
        Thread.sleep(1000);
        WebDriverWait waitChoiceLevel = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement optionLevel = waitChoiceLevel.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@role='combobox' and .//span[text()='Chọn cấp độ']]")
        ));
        optionLevel.click();
        // Chờ mục "Lớp 12" hiển thị và click
        WebElement optionLevel1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@role,'option') or contains(@role,'menuitem')][normalize-space()='Cơ bản']")
        ));
        optionLevel1.click();


        // Chọn ngôn ngữ
        Thread.sleep(1000);
        WebDriverWait waitChoiceLanguage = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement optionLanguage = waitChoiceLanguage.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@role='combobox' and .//span[text()='Chọn ngôn ngữ']]")
        ));
        optionLanguage.click();
        // Chờ mục "Lớp 12" hiển thị và click
        WebElement optionLanguage1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@role,'option') or contains(@role,'menuitem')][normalize-space()='Tiếng Việt']")
        ));
        optionLanguage1.click();


        // Chọn năm sản xuất
        Thread.sleep(1000);
        WebDriverWait waitChoiceYear = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement optionYear = waitChoiceYear.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@role='combobox' and .//span[text()='Chọn năm sản xuất']]")
        ));
        optionYear.click();
        // Chờ mục "Năm" hiển thị và click
        WebElement optionYear1 = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@role,'option') or contains(@role,'menuitem')][normalize-space()='2025']")
        ));
        optionYear1.click();

        //Nhập tác giả
        Thread.sleep(1000);
        WebElement inputAuthor = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='Nhập tác giả']")
                ));

        inputAuthor.sendKeys("Hải Tú", Keys.ENTER);



        // Nhập Tổng chủ biên/chủ biên
        Thread.sleep(1000);
        WebElement inputEditor = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='Nhập Tổng chủ biên/chủ biên']")
                ));

        inputEditor.sendKeys("Sơn Tùng MTP", Keys.ENTER);


        // Nhập Biên soạn/biên dịch
        Thread.sleep(1000);
        WebElement inputCompilation = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='Nhập Biên soạn/biên dịch']")
                ));

        inputCompilation.sendKeys("Trịnh Trần Phương Tuấn", Keys.ENTER);


        // Nhập Nguồn sở hữu
        Thread.sleep(1000);
        WebElement inputOnner = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='Nhập nội dung']")
                ));

        inputOnner.sendKeys("Thiên An", Keys.ENTER);


        // Click tiếp theo
        Thread.sleep(1000);
        WebElement nextButton2 = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Tiếp theo']")));
        nextButton2.click();


        Thread.sleep(1000);
        WebElement goToDetail = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Xem chi tiết']")));
        goToDetail.click();


    }
    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
