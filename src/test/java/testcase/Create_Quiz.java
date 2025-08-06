package testcase;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.DriverFactory;

import java.time.Duration;
import java.util.List;

public class Create_Quiz {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() throws InterruptedException{
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled"); // giảm phát hiện tự động
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("start-maximized");

        driver = DriverFactory.getDriver(options);
        driver.manage().window().maximize();
        driver.get("https://dev.gkebooks.click/dang-nhap");
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Thread.sleep(7000); // hoặc nhiều hơn nếu CAPTCHA chưa xong
        login("test@email.com", "Nkg@6688");
    }

    private void login(String email, String password)  throws InterruptedException{

        driver.findElement(By.name("email")).sendKeys(email);
        Thread.sleep(800);
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(800);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    private void clickElement(By by) throws InterruptedException {
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(by)).click();
    }

    private void selectDropdown(By comboBox, String optionText) throws InterruptedException {
        clickElement(comboBox);
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='option' or @role='menuitem'][normalize-space()='" + optionText + "']")
        ));
        option.click();
    }

    private void fillInput(By by, String text) throws InterruptedException {
        Thread.sleep(1000);
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        input.clear();
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

    public void input(String text) {
        By editorSelector = By.cssSelector("div.tiptap.ProseMirror[contenteditable='true']");
        WebElement editor = wait.until(ExpectedConditions.visibilityOfElementLocated(editorSelector));

        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", editor);

        Actions actions = new Actions(driver);
        actions.moveToElement(editor)
                .click()
                .pause(Duration.ofMillis(200))
                .sendKeys(text)
                .perform();
    }



    public void addEssayQuestion(WebDriver driver, WebDriverWait wait, String questionText, String answerSample) throws InterruptedException {
        // Nhập phần câu hỏi
        WebElement editor = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@id='quiz-studio-editor']//div[@contenteditable='true']")));

        Actions actions = new Actions(driver);
        actions.moveToElement(editor).click().pause(Duration.ofMillis(300));
        for (String line : questionText.split("\n")) {
            actions.sendKeys(line).sendKeys(Keys.ENTER).pause(Duration.ofMillis(200));
        }
        actions.perform();

        // Click nút "Thiết lập"
        WebElement thietLap = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='Thiết lập']")));
        thietLap.click();

        // Đợi vùng input xuất hiện
        WebElement inputArea = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div#latex-editor-content div.ProseMirror[contenteditable='true']")));

        // Đảm bảo focus bằng JavaScript (bắt buộc với ProseMirror)
        ((JavascriptExecutor) driver).executeScript("arguments[0].focus();", inputArea);

        // Click và gõ "a" bằng Actions
        Actions inputAction = new Actions(driver);
        inputAction.moveToElement(inputArea)
                .click()
                .pause(Duration.ofMillis(200))
                .sendKeys(answerSample)
                .pause(Duration.ofMillis(200))
                .perform();
    }
    public void inputBlankBox(String[] texts) {
        List<WebElement> editors = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("div#latex-editor-content div.ProseMirror[contenteditable='true']"),
                texts.length - 1 // Đảm bảo có ít nhất n phần tử
        ));

        for (int i = 0; i < texts.length; i++) {
            WebElement editor = editors.get(i);

            // Scroll và focus vào ô hiện tại
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", editor);

            // Nếu có thẻ <p> bên trong thì click vào p thay vì div
            WebElement pTag = editor.findElement(By.tagName("p"));

            Actions actions = new Actions(driver);
            actions.moveToElement(pTag)
                    .click()
                    .pause(Duration.ofMillis(200))
                    .sendKeys(Keys.chord(Keys.CONTROL, "a"))
                    .sendKeys(Keys.BACK_SPACE)
                    .sendKeys(texts[i])
                    .pause(Duration.ofMillis(300))
                    .perform();
        }
    }
    public void inputTextBoxWrongAnswer(String[] texts) throws InterruptedException {
        List<WebElement> inputs = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.cssSelector("div.flex input")));

        // Nhập từng text vào từng ô input theo đúng thứ tự
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


    private void inputTextLength(By by, String text) throws InterruptedException {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        Thread.sleep(500);
//        input.clear();
        input.sendKeys(text);
    }


    private void clickButton(String buttonText) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='" + buttonText + "']")
        ));
        button.click();
    }

    private void clickButtonSaveInfo(String buttonText) {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[normalize-space()='" + buttonText + "']")
        ));
        button.click();
    }
    public void scrollDown(int pixels) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, arguments[0]);", pixels);
    }

    public void uploadFile(String filePath) {
        // Tìm phần tử input type="file"
        WebElement uploadInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@type='file']")
        ));

        // Bỏ ẩn phần tử nếu đang bị hidden bằng style
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", uploadInput);

        // Gửi file path để upload
        uploadInput.sendKeys(filePath);
    }


    public void clickAddImageButton(int index) {
        List<WebElement> buttons = driver.findElements(By.cssSelector("button:has(svg.lucide-image)"));
        WebElement btn = buttons.get(index);
        wait.until(ExpectedConditions.elementToBeClickable(btn)).click();
    }

    public void inputWrongLabel (String text) {
        // Tìm input theo placeholder
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("input[placeholder='Thêm nhãn']")));

        // Click, nhập text và Enter
        input.click();
        input.sendKeys(text);
        input.sendKeys(Keys.ENTER);
    }

    public void clickCenterOfImageLabeling() {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("image-labeling")));

        // Lấy kích thước
        int width = element.getSize().getWidth();
        int height = element.getSize().getHeight();

        // Tính tọa độ tương đối từ góc trên trái
        int centerX = width / 2;
        int centerY = height / 2;

        // Di chuyển và click vào trung tâm
        Actions actions = new Actions(driver);
        actions.moveToElement(element, centerX - width / 2, centerY - height / 2).click().perform();
    }

    @Test
    public void CreateQuiz_Check() throws InterruptedException {
        clickElement(By.cssSelector("a[href='/kho-hoc-lieu']"));
        clickElement(By.xpath("//button[.//span[normalize-space()='Tạo']]"));
        clickElement(By.xpath("//div[p[text()='Bộ đề']]"));

        fillInput(By.xpath("//input[@placeholder='Nhập tiêu đề']"), "Bộ đề Smoke Test");
        fillInput(By.xpath("//textarea[@placeholder='Nhập mô tả']"), "Lorem Ipsum is simply dummy text of the printing and typesetting industry.");

        selectDropdown(By.xpath("//button[@role='combobox']"), "Công khai");
        clickElement(By.xpath("//button[normalize-space()='Tiếp theo']"));

        selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Chọn bộ môn']]"), "Toán");
        selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Chọn khối lớp']]"), "Lớp 12");
        selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Chọn cấp độ']]"), "Cơ bản");
        selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Chọn ngôn ngữ']]"), "Tiếng Việt");
        fillInput(By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='Nhập tác giả']"), "Hải Tú" + Keys.ENTER);
        fillInput(By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='Nhập Tổng chủ biên/chủ biên']"), "Sơn Tùng MTP" + Keys.ENTER);
        fillInput(By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='Nhập Biên soạn/biên dịch']"), "Trịnh Trần Phương Tuấn" + Keys.ENTER);
        fillInput(By.xpath("//button[@aria-haspopup='dialog']//input[@placeholder='Nguồn sở hữu']"), "Thiên An" + Keys.ENTER);

        clickElement(By.xpath("//button[normalize-space()='Lưu']"));

//===========================================TRẮC NGHIỆM================================================================
//        try {
//            clickElement(By.xpath("//button[contains(., 'Thêm câu hỏi')]"));
//            clickElement(By.xpath("//h3[normalize-space()='Tạo thủ công']"));
//            clickElement(By.xpath("//span[normalize-space()='Trắc nghiệm']"));
//            clickElement(By.cssSelector("button:has(svg.lucide-image)"));
//            Thread.sleep(1000);
//            uploadFile("C:\\Users\\GHC\\Downloads\\Data\\sampleIMG.jpg");
//            Thread.sleep(500);
//            clickElement(By.xpath("//div[@role='dialog']//button[normalize-space()='Lưu']"));
//            addEditableContents(new String[]{"Trắc nghiệm smoke test", "A. Đáp án A", "B. Đáp án B"});
//            clickElement(By.cssSelector("button[role='checkbox']"));
//            selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Không tính']]"), "1 điểm");
//            fillInput(By.xpath("//input[@type='text' and @value='15' and contains(@class, 'text-center')]"), "60");
//            clickElement(By.xpath("//button[normalize-space()='Lưu']"));
//            System.out.println("Tạo quiz Trắc nghiệm: Passed");
//            Thread.sleep(1000);
//        } catch (Exception e){
//            String fullMessage = e.getMessage();
//            String shortMessage = fullMessage.split("\n")[0];
//            System.out.println("Tạo quiz Trắc nghiệm: Failed - " + shortMessage);
//            Assert.fail(shortMessage); // tránh null
//        }
//===========================================TỰ LUẬN====================================================================
//        try {
//            clickElement(By.xpath("//button[contains(., 'Thêm câu hỏi')]"));
//            clickElement(By.xpath("//h3[normalize-space()='Tạo thủ công']"));
//            clickElement(By.xpath("//span[normalize-space()='Tự luận']"));
//
//            addEssayQuestion(driver, wait,
//                    "Smoke test câu hỏi Tự luận",
//                    "a"
//            );
//            clickButtonSaveInfo("Lưu thông tin");
//            inputTextLength(By.cssSelector("input[type='text'].text-center"), "100");
//            selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Không tính']]"), "1 điểm");
//            fillInput(By.xpath("//input[@type='text' and contains(@class, 'text-center')]"), "60");
//            clickElement(By.xpath("//button[normalize-space()='Lưu']"));
//            System.out.println("Tạo quiz Tự luận: Passed");
//            Thread.sleep(1000);
//        } catch (Exception e){
//            String fullMessage = e.getMessage();
//            String shortMessage = fullMessage.split("\n")[0];
//            System.out.println("Tạo quiz Tự luận: Failed - " + shortMessage);
//            Assert.fail(shortMessage); // tránh null
//        }

//===========================================ĐIỀN TỪ====================================================================
//        try {
//            clickElement(By.xpath("//button[contains(., 'Thêm câu hỏi')]"));
//            clickElement(By.xpath("//h3[normalize-space()='Tạo thủ công']"));
//            clickElement(By.xpath("//span[normalize-space()='Điền vào chỗ trống']"));
//            addEditableContents(new String[]{"Chào mừng đến với __ và __"});
//            inputBlankBox(new String[]{
//                    "NKG",
//                    "Techainer",
//
//            });
//            selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Không tính']]"), "1 điểm");
//            fillInput(By.xpath("//input[@type='text' and contains(@class, 'text-center')]"), "60");
//            clickElement(By.xpath("//button[normalize-space()='Lưu']"));
//            System.out.println("Tạo quiz Điền từ: Passed");
//            Thread.sleep(1000);
//        } catch(Exception e){
//            String fullMessage = e.getMessage();
//            String shortMessage = fullMessage.split("\n")[0];
//            System.out.println("Tạo quiz Điền từ: Failed - " + shortMessage);
//            Assert.fail(shortMessage); // tránh null
//        }

//===========================================GHÉP ĐÔI===================================================================
//        try {
//            clickElement(By.xpath("//button[contains(., 'Thêm câu hỏi')]"));
//            clickElement(By.xpath("//h3[normalize-space()='Tạo thủ công']"));
//            clickElement(By.xpath("//span[normalize-space()='Ghép đôi']"));
//            clickElement(By.cssSelector("button:has(svg.lucide-image)"));
//            Thread.sleep(1000);
//            uploadFile("C:\\Users\\NCPC\\Downloads\\DataTest\\1.png");
//            Thread.sleep(500);
//            clickElement(By.xpath("//div[@role='dialog']//button[normalize-space()='Lưu']"));
//            Thread.sleep(1500);
//            addEditableContents(new String[]
//                    {
//                            "Ghép đôi smoke test",
//                            "1",
//                            "1",
//                            "2",
//                            "2",
//                            "3",
//                            "3",
//
//                    });
//
//            clickAddImageButton(0);
//            Thread.sleep(1000);
//            uploadFile("C:\\Users\\NCPC\\Downloads\\DataTest\\1.png");
//            Thread.sleep(500);
//            clickElement(By.xpath("//div[@role='dialog']//button[normalize-space()='Lưu']"));
//            Thread.sleep(1000);
//            clickAddImageButton(2);
//            Thread.sleep(1000);
//            uploadFile("C:\\Users\\NCPC\\Downloads\\DataTest\\1.png");
//            Thread.sleep(500);
//            clickElement(By.xpath("//div[@role='dialog']//button[normalize-space()='Lưu']"));
//            Thread.sleep(1000);
//            clickAddImageButton(4);
//            Thread.sleep(1000);
//            uploadFile("C:\\Users\\NCPC\\Downloads\\DataTest\\1.png");
//            Thread.sleep(500);
//            clickElement(By.xpath("//div[@role='dialog']//button[normalize-space()='Lưu']"));
//
//            selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Không tính']]"), "1 điểm");
//            fillInput(By.xpath("//input[@type='text' and contains(@class, 'text-center')]"), "60");
//            clickElement(By.xpath("//button[normalize-space()='Lưu']"));
//            System.out.println("Tạo quiz Ghép đôi: Passed");
//            Thread.sleep(1000);
//        }catch (Exception e){
//            String fullMessage = e.getMessage();
//            String shortMessage = fullMessage.split("\n")[0];
//            System.out.println("Tạo quiz Ghép đôi: Failed - " + shortMessage);
//            Assert.fail(shortMessage); // tránh null
//        }


// ===========================================HỘP THẢ===================================================================
//        try {
//            clickElement(By.xpath("//button[contains(., 'Thêm câu hỏi')]"));
//            clickElement(By.xpath("//h3[normalize-space()='Tạo thủ công']"));
//            clickElement(By.xpath("//span[normalize-space()='Hộp thả']"));
//            addEditableContents(new String[]{"__ và __ là hai công ty hàng đầu thế giới"});
//            clickElement(By.cssSelector("button:has(svg.lucide-image)"));
//            Thread.sleep(1000);
//            uploadFile("C:\\Users\\NCPC\\Downloads\\DataTest\\1.png");
//            Thread.sleep(500);
//            clickElement(By.xpath("//div[@role='dialog']//button[normalize-space()='Lưu']"));
//            clickElement(By.xpath("(//button[span[normalize-space()='Thêm lựa chọn']])"));
//            clickElement(By.xpath("(//button[span[normalize-space()='Thêm lựa chọn']])"));
//            clickElement(By.xpath("(//button[span[normalize-space()='Thêm lựa chọn']])[2]"));
//            clickElement(By.xpath("(//button[span[normalize-space()='Thêm lựa chọn']])[2]"));
//            inputBlankBox(new String[]{
//                    "Google",
//                    "Facebook",
//                    "NKG",
//                    "Techainer",
//                    "FPT",
//                    "Vietel",
//            });
//            selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Không tính']]"), "1 điểm");
//            fillInput(By.xpath("//input[@type='text' and contains(@class, 'text-center')]"), "60");
//            clickElement(By.xpath("//button[normalize-space()='Lưu']"));
//            System.out.println("Tạo quiz Hộp thả: Passed");
//            Thread.sleep(1000);
//        } catch (Exception e){
//            String fullMessage = e.getMessage();
//            String shortMessage = fullMessage.split("\n")[0];
//            System.out.println("Tạo quiz Hộp thả: Failed - " + shortMessage);
//            Assert.fail(shortMessage); // tránh null
//        }


// ===========================================KÉO THẢ===================================================================
//        try {
//            clickElement(By.xpath("//button[contains(., 'Thêm câu hỏi')]"));
//            clickElement(By.xpath("//h3[normalize-space()='Tạo thủ công']"));
//            clickElement(By.xpath("//span[normalize-space()='Kéo thả']"));
//
//            addEditableContents(new String[]{"__ và __ là hai công ty hàng đầu thế giới"});
//            clickElement(By.cssSelector("button:has(svg.lucide-image)"));
//            Thread.sleep(1000);
//            uploadFile("C:\\Users\\NCPC\\Downloads\\DataTest\\1.png");
//            Thread.sleep(500);
//            clickElement(By.xpath("//div[@role='dialog']//button[normalize-space()='Lưu']"));
//            clickElement(By.xpath("(//button[span[normalize-space()='Thêm']])"));
//            clickElement(By.xpath("(//button[span[normalize-space()='Thêm']])"));
//
//            inputBlankBox(new String[]{
//                    "NKG",
//                    "Techainer",
//
//            });
//            inputTextBoxWrongAnswer(new String[]{
//                    "Facebook",
//                    "Google",
//            });
//            selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Không tính']]"), "1 điểm");
//            fillInput(By.xpath("//input[@type='text' and contains(@class, 'text-center')]"), "60");
//            clickElement(By.xpath("//button[normalize-space()='Lưu']"));
//            System.out.println("Tạo quiz Kéo thả: Passed");
//            Thread.sleep(1000);
//        } catch (Exception e){
//            String fullMessage = e.getMessage();
//            String shortMessage = fullMessage.split("\n")[0];
//            System.out.println("Tạo quiz Kéo thả: Failed - " + shortMessage);
//            Assert.fail(shortMessage);
//        }


// ===========================================DÁN NHÃN==================================================================
        try {
            clickElement(By.xpath("//button[contains(., 'Thêm câu hỏi')]"));
            clickElement(By.xpath("//h3[normalize-space()='Tạo thủ công']"));
            clickElement(By.xpath("//span[normalize-space()='Dán nhãn']"));

            uploadFile("C:\\Users\\NCPC\\Downloads\\DataTest\\1.png");
            Thread.sleep(500);
            addEditableContents(new String[]{"Dán nhãn cho đúng"});
            clickElement(By.xpath("(//button[span[normalize-space()='Thêm']])"));
            inputWrongLabel("Nhãn sai 1");
            clickElement(By.xpath("(//button[span[normalize-space()='Thêm']])"));
            inputWrongLabel("Nhãn sai 2");
            clickCenterOfImageLabeling();
            inputWrongLabel("Nhãn đúng 1");

            selectDropdown(By.xpath("//button[@role='combobox' and .//span[text()='Không tính']]"), "1 điểm");
            fillInput(By.xpath("//input[@type='text' and contains(@class, 'text-center')]"), "60");
            clickElement(By.xpath("//button[normalize-space()='Lưu']"));
            System.out.println("Tạo quiz Kéo thả: Passed");
            Thread.sleep(1000);
        } catch (Exception e){
            String fullMessage = e.getMessage();
            String shortMessage = fullMessage.split("\n")[0];
            System.out.println("Tạo quiz Kéo thả: Failed - " + shortMessage);
            Assert.fail(shortMessage);
        }
    }



    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
