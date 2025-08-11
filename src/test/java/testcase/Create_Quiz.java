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

public class Create_Quiz {
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
        Thread.sleep(7000);
        login("test@email.com", "Nkg@6688");
    }

    private void login(String email, String password) throws InterruptedException {
        driver.findElement(By.name("email")).sendKeys(email);
        Thread.sleep(800);
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(800);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(800);
    }

    // ========== Reusable Actions ==========
    private void clickElement(By by) throws InterruptedException {
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    private void clickButton(String buttonText) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='" + buttonText + "']")));
        button.click();
    }

    private void selectDropdown(By comboBox, String optionText) throws InterruptedException {
        clickElement(comboBox);
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option' or @role='menuitem'][normalize-space()='" + optionText + "']")));
        option.click();
    }

    private void fillInput(By by, String text) throws InterruptedException {
        Thread.sleep(1000);
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        input.clear();
        input.sendKeys(text);
    }

    private void inputTextLength(By by, String text) throws InterruptedException {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        Thread.sleep(500);
        input.sendKeys(text);
    }

    private void addEditableContents(String[] contents) {
        List<WebElement> editors = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("div[contenteditable='true'].ProseMirror"), contents.length - 1));
        Actions actions = new Actions(driver);
        for (int i = 0; i < contents.length; i++) {
            WebElement editor = editors.get(i);
            actions.moveToElement(editor).click().pause(Duration.ofMillis(500))
                    .sendKeys(Keys.chord(Keys.CONTROL, ""), Keys.DELETE)
                    .sendKeys(contents[i])
                    .pause(Duration.ofSeconds(1)).perform();
        }
    }

    private void addEssayQuestion(String questionText, String answerSample) {
        WebElement editor = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='quiz-studio-editor']//div[@contenteditable='true']")));
        Actions actions = new Actions(driver).moveToElement(editor).click().pause(Duration.ofMillis(300));
        for (String line : questionText.split("\n")) {
            actions.sendKeys(line).sendKeys(Keys.ENTER).pause(Duration.ofMillis(200));
        }
        actions.perform();

        clickButton("Thiết lập");

        WebElement inputArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div#latex-editor-content div.ProseMirror[contenteditable='true']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", inputArea);

        new Actions(driver).moveToElement(inputArea)
                .click().pause(Duration.ofMillis(200))
                .sendKeys(answerSample)
                .pause(Duration.ofMillis(200)).perform();
    }

    private void inputBlankBox(String[] texts) {
        List<WebElement> editors = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("div#latex-editor-content div.ProseMirror[contenteditable='true']"),
                texts.length - 1));
        for (int i = 0; i < texts.length; i++) {
            WebElement editor = editors.get(i);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", editor);
            WebElement pTag = editor.findElement(By.tagName("p"));
            new Actions(driver).moveToElement(pTag)
                    .click().pause(Duration.ofMillis(200))
                    .sendKeys(Keys.chord(Keys.CONTROL, "a"))
                    .sendKeys(Keys.BACK_SPACE)
                    .sendKeys(texts[i])
                    .pause(Duration.ofMillis(300)).perform();
        }
    }

    private void inputTextBoxWrongAnswer(String[] texts) throws InterruptedException {
        List<WebElement> inputs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("div.flex input")));
        int[] indexes = {1, 2};
        for (int i = 0; i < indexes.length; i++) {
            WebElement input = inputs.get(indexes[i]);
            input.click();
            input.clear();
            input.sendKeys(texts[i]);
            input.sendKeys(Keys.ENTER);
            Thread.sleep(300);
        }
    }

    private void uploadFile(String filePath) {
        WebElement uploadInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='file']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", uploadInput);
        uploadInput.sendKeys(filePath);
    }

    private void uploadImage(String filePath) throws InterruptedException {
        clickElement(By.cssSelector("button:has(svg.lucide-image)"));
        Thread.sleep(1000);
        uploadFile(filePath);
        Thread.sleep(500);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[role='dialog']")));
        clickElement(By.xpath("//div[@role='dialog']//button[normalize-space()='Lưu']"));
        Thread.sleep(500);
    }

    private void clickAddImageButton(int index, String filePath) throws InterruptedException {
        List<WebElement> buttons = driver.findElements(By.cssSelector("button:has(svg.lucide-image)"));
        wait.until(ExpectedConditions.elementToBeClickable(buttons.get(index))).click();
        Thread.sleep(1000);
        uploadFile(filePath);
        Thread.sleep(500);
        clickButton("Lưu");
        Thread.sleep(500);
    }

    private void inputWrongLabel(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Thêm nhãn']")));
        input.click();
        input.sendKeys(text);
        input.sendKeys(Keys.ENTER);
    }

    private void clickCenterOfImageLabeling() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("image-labeling")));
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        new Actions(driver).moveToElement(element, centerX - width / 2, centerY - height / 2).click().perform();
    }

    private void addQuestion(String type) throws InterruptedException {
        clickElement(By.xpath("//button[contains(., 'Thêm câu hỏi')]"));
        clickElement(By.xpath("//h3[normalize-space()='Tạo thủ công']"));
        clickElement(By.xpath("//span[normalize-space()='" + type + "']"));
    }

    private void handleError(String type, Exception e) {
        String msg = e.getMessage().split("\n")[0];
        System.out.println("Tạo quiz " + type + ": Failed - " + msg);
        Assert.fail(msg);
    }

    // ========== Main Test ==========
    @Test
    public void CreateQuiz_Check() throws InterruptedException {
        clickElement(By.cssSelector("a[href='/kho-hoc-lieu']"));
        clickElement(By.xpath("//button[.//span[normalize-space()='Tạo']]"));
        clickElement(By.xpath("//div[p[text()='Bộ đề']]"));

        fillInput(By.xpath("//input[@placeholder='Nhập tiêu đề']"), "Bộ đề Smoke Test");
        fillInput(By.xpath("//textarea[@placeholder='Nhập mô tả']"), "Lorem Ipsum...");

        selectDropdown(By.xpath("//button[@role='combobox']"), "Công khai");
        clickElement(By.xpath("//button[normalize-space()='Tiếp theo']"));
        selectDropdown(By.xpath("//button[.//span[text()='Chọn bộ môn']]"), "Toán");
        selectDropdown(By.xpath("//button[.//span[text()='Chọn khối lớp']]"), "Lớp 12");
        selectDropdown(By.xpath("//button[.//span[text()='Chọn cấp độ']]"), "Cơ bản");
        selectDropdown(By.xpath("//button[.//span[text()='Chọn ngôn ngữ']]"), "Tiếng Việt");
        fillInput(By.xpath("//input[@placeholder='Nhập tác giả']"), "Hải Tú" + Keys.ENTER);
        fillInput(By.xpath("//input[@placeholder='Nhập Tổng chủ biên/chủ biên']"), "Sơn Tùng MTP" + Keys.ENTER);
        fillInput(By.xpath("//input[@placeholder='Nhập Biên soạn/biên dịch']"), "Trịnh Trần Phương Tuấn" + Keys.ENTER);
        fillInput(By.xpath("//input[@placeholder='Nguồn sở hữu']"), "Thiên An" + Keys.ENTER);
        clickButton("Lưu");

        // === Các loại câu hỏi ===
//        try {
//            addQuestion("Trắc nghiệm");
//            uploadImage("D:\\Data\\sampleIMG.jpg");
//            addEditableContents(new String[]{"Trắc nghiệm smoke test", "A. Đáp án A", "B. Đáp án B"});
//            clickElement(By.cssSelector("button[role='checkbox']"));
//            selectDropdown(By.xpath("//button[.//span[text()='Không tính']]"), "1 điểm");
//            fillInput(By.xpath("//input[@value='15']"), "60");
//            clickButton("Lưu");
//            System.out.println("Tạo quiz Trắc nghiệm: Passed");
//        } catch (Exception e) {
//            handleError("Trắc nghiệm", e);
//        }

        // === TỰ LUẬN ===
        try {
            addQuestion("Tự luận");
            addEssayQuestion("Smoke test câu hỏi Tự luận", "abc");
            clickButton("Lưu thông tin");
            inputTextLength(
                    By.xpath("(//input[@type='text' and contains(@class,'text-center')])[2]"),
                    "100"
            );
            selectDropdown(By.xpath("//button[.//span[text()='Không tính']]"), "1 điểm");
            fillInput(By.xpath("//input[contains(@class, 'text-center')]"), "60");
            clickButton("Lưu");
            System.out.println("Tạo quiz Tự luận: Passed");
        } catch (Exception e) {
            handleError("Tự luận", e);
        }

        // === ĐIỀN TỪ ===
        try {
            addQuestion("Điền vào chỗ trống");
            addEditableContents(new String[]{"Chào mừng đến với __ và __"});
            inputBlankBox(new String[]{"NKG", "Techainer"});
            selectDropdown(By.xpath("//button[.//span[text()='Không tính']]"), "1 điểm");
            fillInput(By.xpath("//input[contains(@class, 'text-center')]"), "60");
            clickButton("Lưu");
            System.out.println("Tạo quiz Điền từ: Passed");
        } catch (Exception e) {
            handleError("Điền từ", e);
        }

        // === GHÉP ĐÔI ===
        try {
            addQuestion("Ghép đôi");
            uploadImage("D:\\Data\\sampleIMG.jpg");

            Thread.sleep(1500);
            addEditableContents(new String[]{
                    "Ghép đôi smoke test", "1", "1", "2", "2", "3", "3"
            });
            clickAddImageButton(0, "D:\\Data\\sampleIMG.jpg");

            clickAddImageButton(2, "D:\\Data\\sampleIMG.jpg");

            clickAddImageButton(4, "D:\\Data\\sampleIMG.jpg");

            selectDropdown(By.xpath("//button[.//span[text()='Không tính']]"), "1 điểm");
            fillInput(By.xpath("//input[contains(@class, 'text-center')]"), "60");
            clickButton("Lưu");
            System.out.println("Tạo quiz Ghép đôi: Passed");
        } catch (Exception e) {
            handleError("Ghép đôi", e);
        }

        // === HỘP THẢ ===
        try {
            addQuestion("Hộp thả");
            addEditableContents(new String[]{"__ và __ là hai công ty hàng đầu thế giới"});
            uploadImage("D:\\Data\\sampleIMG.jpg");

            for (int i = 0; i < 4; i++) {
                clickElement(By.xpath("(//button[span[normalize-space()='Thêm lựa chọn']])[last()]"));
            }
            inputBlankBox(new String[]{
                    "Google", "Facebook", "NKG", "Techainer", "FPT", "Vietel"
            });
            selectDropdown(By.xpath("//button[.//span[text()='Không tính']]"), "1 điểm");
            fillInput(By.xpath("//input[contains(@class, 'text-center')]"), "60");
            clickButton("Lưu");
            System.out.println("Tạo quiz Hộp thả: Passed");
        } catch (Exception e) {
            handleError("Hộp thả", e);
        }

        // === KÉO THẢ ===
        try {
            addQuestion("Kéo thả");
            addEditableContents(new String[]{"__ và __ là hai công ty hàng đầu thế giới"});
            uploadImage("D:\\Data\\sampleIMG.jpg");

            for (int i = 0; i < 2; i++) {
                clickElement(By.xpath("(//button[span[normalize-space()='Thêm']])[last()]"));
            }
            inputBlankBox(new String[]{"NKG", "Techainer"});
            inputTextBoxWrongAnswer(new String[]{"Facebook", "Google"});
            selectDropdown(By.xpath("//button[.//span[text()='Không tính']]"), "1 điểm");
            fillInput(By.xpath("//input[contains(@class, 'text-center')]"), "60");

            System.out.println("Tạo quiz Kéo thả: Passed");
        } catch (Exception e) {
            handleError("Kéo thả", e);
        }

        // === DÁN NHÃN ===
        try {
            addQuestion("Dán nhãn");
            uploadFile("D:\\Data\\sampleIMG.jpg");
            clickButton("Lưu");
            Thread.sleep(500);
            addEditableContents(new String[]{"Dán nhãn cho đúng"});
            for (int i = 0; i < 2; i++) {
                clickElement(By.xpath("(//button[span[normalize-space()='Thêm']])[last()]"));
                inputWrongLabel("Nhãn sai " + (i + 1));
            }
            clickCenterOfImageLabeling();
            inputWrongLabel("Nhãn đúng 1");
            selectDropdown(By.xpath("//button[.//span[text()='Không tính']]"), "1 điểm");
            fillInput(By.xpath("//input[contains(@class, 'text-center')]"), "60");
            clickButton("Lưu");
            System.out.println("Tạo quiz Dán nhãn: Passed");
        } catch (Exception e) {
            handleError("Dán nhãn", e);
        }

    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
