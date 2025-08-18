package testcase;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.DriverFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class Add_Materials_To_Books {
    WebDriver driver;
    WebDriverWait wait;

    // ====== Config / Selectors chung ======
    private static final By BTN_PLUS = By.cssSelector("button:has(svg.lucide-plus)");
    private static final By BTN_CONFIRM_PRIMARY = By.cssSelector("button.text-white.bg-blue-900");
    private static final By INPUT_NAME = By.cssSelector("input[placeholder='Nhập tên học liệu']");
    private static final By DIALOG_OPEN = By.cssSelector("div[role='dialog'][data-state='open']");
    private static final By BTN_STAR = By.cssSelector("button:has(svg.lucide-star)");
    private static final By BTN_SAVE_BOOK = By.xpath("//button[normalize-space()='Lưu sách']");
    private static final By TOAST_SUCCESS = By.xpath("//*[contains(normalize-space(),'Lưu thành công')]");
    private static final By QUIZ_LIST = By.cssSelector("div.bg-white.rounded-lg.shadow-sm.border");
    private static final By QUIZ_RESULTS = By.cssSelector("div.w-full.rounded-lg.border.p-4.flex.items-center.justify-between.gap-5");
    private static final By BTN_X = By.cssSelector("svg.lucide-x.cursor-pointer");
    // item đầu tiên trong kho học liệu
    private static final By WAREHOUSE_FIRST_ITEM_TEXT = By.xpath(
            "(//div[contains(@class,'px-4') and contains(@class,'py-3') and contains(@class,'flex')]//span[contains(@class,'text-base')])[1]"
    );

    // ====== Kiểu học liệu ======
    private enum MaterialType {
        LECTURE("Bài giảng", "Học liệu bài giảng", "D:\\Data\\samplepptx.pptx", 3000),
        SCORM("Scrom / xAPI", "Học liệu Scrom", "D:\\Data\\Que_diem.zip", 3000),
        DOCUMENT("Tài liệu", "Tài liệu", "D:\\Data\\DOCX_sample.docx", 5000),
        AUDIO("Âm thanh", "Học liệu âm thanh", "D:\\Data\\AUDIO_sample.mp3", 5000),
        VIDEO("Video", "Học liệu video", "D:\\Data\\Video.mp4", 15000),
        IMAGE("Hình ảnh", "Học liệu hình ảnh", "D:\\Data\\sampleIMG.jpg", 5000),
        THREE_D("3D / VR", "Học liệu 3D", "D:\\Data\\3D_sample.glb", 5000),
        INTERACTIVE("Học liệu nâng cao", "Học liệu nâng cao", "D:\\Data\\Robot_Finding_Treasure.zip", 5000),
        QUIZ("Câu hỏi", "Bộ đề", null, 3000);

        final String categoryLabel;  // label trong dialog chọn loại
        final String title;          // tên hiển thị mong muốn
        final String filePath;       // đường dẫn file upload (null với quiz)
        final long postWaitMs;       // delay ổn định UI

        MaterialType(String categoryLabel, String title, String filePath, long postWaitMs) {
            this.categoryLabel = categoryLabel;
            this.title = title;
            this.filePath = filePath;
            this.postWaitMs = postWaitMs;
        }
    }

    // Thứ tự các pin từ trên xuống dưới (theo offset đã dùng)
    private static final List<MaterialType> PIN_TYPES = Arrays.asList(
            MaterialType.LECTURE,
            MaterialType.SCORM,
            MaterialType.DOCUMENT,
            MaterialType.AUDIO,
            MaterialType.VIDEO,
            MaterialType.IMAGE,
            MaterialType.THREE_D,
            MaterialType.INTERACTIVE,
            MaterialType.QUIZ
    );

    // ====== Helpers UI ======
    private WebElement waitClickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    private void click(By by) {
        waitClickable(by).click();
    }

    private void jsClick(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    private void scrollIntoView(WebElement el) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
    }

    private void typeReplace(By locator, String text) {
        WebElement input = waitClickable(locator);
        new Actions(driver)
                .moveToElement(input)
                .click()
                .keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .sendKeys(text)
                .perform();
    }

    private void clickInOpenDialog(By relativeBy) {
        WebElement dialog = wait.until(ExpectedConditions.visibilityOfElementLocated(DIALOG_OPEN));
        WebElement el = dialog.findElement(relativeBy);
        scrollIntoView(el);
        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (Exception ignore) {
            jsClick(el);
        }
    }

    private void selectOption(String label, String option) {
        click(By.xpath("//button[@role='combobox' and .//span[text()='" + label + "']]"));
        click(By.xpath("//div[contains(@role,'option') or contains(@role,'menuitem')][normalize-space()='" + option + "']"));
    }

    private void uploadFileToHiddenInput(String filePath) {
        WebElement input = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", input);
        input.sendKeys(filePath);
    }

    private void expectToastSuccess() {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(8));
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_SUCCESS));
        } catch (TimeoutException e) {
            Assert.fail("Edit Books: Failed - Không thấy toast 'Thao tác thành công'.");
        }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    // ====== Điều hướng ======
    private void hoverToElement(By selector) {
        WebElement element = driver.findElement(selector);
        new Actions(driver).moveToElement(element).pause(Duration.ofMillis(800)).perform();
    }

    private void login(String email, String password) throws InterruptedException {
        driver.findElement(By.name("email")).sendKeys(email);
        Thread.sleep(800);
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(800);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        Thread.sleep(800);
    }

    private void goToBookAndEdit() {
        hoverToElement(By.xpath("//div[span[normalize-space()='Bộ sách']]"));
        click(By.xpath("//button[normalize-space()='Lớp 3']"));
        sleep(500);
        click(By.xpath("//a[.//h2[normalize-space()='Công Nghệ 3']]"));
        sleep(500);
        click(By.cssSelector("button:has(svg.lucide-square-pen)"));
        sleep(300);
    }

    private void nextPage(int times) {
        for (int i = 0; i < times; i++) {
            click(By.cssSelector("button:has(svg.lucide-move-right)"));
            sleep(200);
        }
    }

    // ====== Quy trình thêm học liệu ======
    private void addByUpload(MaterialType type) {
        if (type == MaterialType.QUIZ) {
            uploadQuiz();
            return;
        }
        typeReplace(INPUT_NAME, type.title);
        click(BTN_PLUS);
        clickInOpenDialog(By.xpath(".//p[normalize-space()='" + type.categoryLabel + "']/ancestor::figure[1]"));
        sleep(300);
        uploadFileToHiddenInput(type.filePath);
        sleep(300);
        click(BTN_CONFIRM_PRIMARY);
        sleep(type.postWaitMs);
    }

    private void addFromWarehouse(MaterialType type) {
        click(BTN_PLUS);
        clickInOpenDialog(By.xpath(".//p[normalize-space()='" + type.categoryLabel + "']/ancestor::figure[1]"));
        click(By.xpath("//button[normalize-space()='Kho học liệu']"));

        WebElement el = waitClickable(WAREHOUSE_FIRST_ITEM_TEXT);
        scrollIntoView(el);
        try {
            el.click();
        } catch (ElementClickInterceptedException e) {
            jsClick(el);
        }
        click(BTN_CONFIRM_PRIMARY);
        sleep(type.postWaitMs);
    }

    private void placePinsAndRun(WebElement canvas, int[][] offsets, Runnable[] actions) {
        for (int i = 0; i < offsets.length; i++) {
            int x = offsets[i][0], y = offsets[i][1];
            new Actions(driver)
                    .moveToElement(canvas)
                    .moveByOffset(x, y)
                    .click()
                    .pause(Duration.ofMillis(200))
                    .perform();
            actions[i].run();
        }
    }

    private WebElement getBookImageArea() {
        return driver.findElement(By.xpath("//div[contains(@class,'relative') and .//img[@alt]]"));
    }

    private int[][] defaultPinOffsets() {
        return new int[][]{
                {760, -250},{760, -200},{760, -150},{760, -100},{760, -50},
                {760, 0},{760, 50},{760, 100},{760, 150}
        };
    }

    private void addMaterialsByUpload() {
        click(BTN_STAR);
        sleep(200);

        Runnable[] actions = new Runnable[PIN_TYPES.size()];
        for (int i = 0; i < PIN_TYPES.size(); i++) {
            final MaterialType type = PIN_TYPES.get(i);
            actions[i] = () -> addByUpload(type);
        }
        placePinsAndRun(getBookImageArea(), defaultPinOffsets(), actions);
        sleep(2000);
    }

    private void addMaterialsByWarehouse() {
        click(BTN_STAR);
        sleep(200);

        Runnable[] actions = new Runnable[PIN_TYPES.size()];
        for (int i = 0; i < PIN_TYPES.size(); i++) {
            final int idx = i;
            actions[i] = () -> {
                // 0..7 chọn từ kho; ô thứ 9 (idx=8) theo yêu cầu cũ: upload bài giảng
                if (idx < 8) {
                    addFromWarehouse(PIN_TYPES.get(idx));
                } else {
                    addByUpload(MaterialType.LECTURE);
                }
            };
        }
        placePinsAndRun(getBookImageArea(), defaultPinOffsets(), actions);
        sleep(1000);
    }

//    private void addMaterialsByWarehouse() {
//        click(BTN_STAR);
//        sleep(200);
//
//        int[][] allOffsets = defaultPinOffsets();
//        int[][] offsets = java.util.Arrays.copyOfRange(allOffsets, 1, allOffsets.length); // bỏ index 0
//
//        Runnable[] actions = new Runnable[PIN_TYPES.size() - 1];
//        for (int i = 1; i < PIN_TYPES.size(); i++) {
//            final int originalIdx = i;          // 1..8
//            final int targetIdx = i - 1;        // 0..7 trong mảng actions/offsets sau khi cắt
//            actions[targetIdx] = () -> {
//                if (originalIdx < 8) {
//                    addFromWarehouse(PIN_TYPES.get(originalIdx));
//                } else {
//                    // phần tử thứ 9 theo logic cũ: upload bài giảng
//                    addByUpload(MaterialType.LECTURE);
//                }
//            };
//        }
//
//        placePinsAndRun(getBookImageArea(), offsets, actions);
//        sleep(1000);
//    }

    // ====== Sửa/Xoá/Lưu ======
    private void clickMaterialCardByTitle(String title) {
        By card = By.xpath("//div[contains(@class,'cursor-pointer') and .//span[normalize-space()='" + title + "']]");
        WebElement el = waitClickable(card);
        scrollIntoView(el);
        el.click();
    }

    private void deleteMaterialsUploaded() {
        // Xoá các học liệu đã upload theo tên cố định (chu kỳ đầu)
        String[] titles = {
                MaterialType.LECTURE.title, MaterialType.IMAGE.title, MaterialType.VIDEO.title,
                MaterialType.DOCUMENT.title, MaterialType.AUDIO.title, MaterialType.QUIZ.title,
                MaterialType.THREE_D.title, MaterialType.INTERACTIVE.title, MaterialType.SCORM.title
        };

        By btnDelete = By.xpath("//button[normalize-space()='Xoá học liệu']");
        By btnConfirm = By.xpath("//button[normalize-space()='Xác nhận']");

        for (String title : titles) {
            clickMaterialCardByTitle(title);
            click(btnDelete);
            click(btnConfirm);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(btnConfirm));
            click(BTN_STAR);
        }
    }

    private void editMaterials() {
        // Xoá 1 học liệu và thêm lại tài liệu
        clickMaterialCardByTitle(MaterialType.LECTURE.title);
        By firstTrashBtn = By.cssSelector("button:has(svg.lucide-trash[width='20'])");
        click(firstTrashBtn);
        sleep(200);
        click(By.xpath("//button[normalize-space()='Xác nhận']"));
        sleep(300);

        // Thêm "Tài liệu"
        addByUpload(MaterialType.DOCUMENT);

        // Lưu
        click(BTN_SAVE_BOOK);
        expectToastSuccess();
        sleep(1000);
    }

    private void saveMaterials() {
        click(BTN_SAVE_BOOK);
        expectToastSuccess();
//        click(BTN_X);
//        sleep(1000);


    }

    // ====== Quy trình đặc thù: Quiz ======
    private void uploadQuiz() {
        typeReplace(INPUT_NAME, MaterialType.QUIZ.title);
        click(BTN_PLUS);
        clickInOpenDialog(By.xpath(".//p[normalize-space()='" + MaterialType.QUIZ.categoryLabel + "']/ancestor::figure[1]"));
        sleep(300);

        selectOption("Chọn khối lớp", "Lớp 3");
        sleep(200);
        selectOption("Chọn bộ đề", "Công nghệ 3 - Bài 2: Sử dụng đèn học");
        sleep(500);

        List<WebElement> quizzes = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(QUIZ_LIST));
        int clickCount = Math.min(3, quizzes.size());
        for (int i = 0; i < clickCount; i++) {
            wait.until(ExpectedConditions.elementToBeClickable(quizzes.get(i))).click();
        }

        click(BTN_CONFIRM_PRIMARY);
        sleep(300);

        List<WebElement> results = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(QUIZ_RESULTS));
        Assert.assertEquals(results.size(), 3, "Edit Books: Failed - Lỗi không hiển thị quiz");
        sleep(300);
    }

    // ====== Test lifecycle ======
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

        sleep(7000);
        login("test@email.com", "Nkg@6688");
        sleep(3000);
    }

    @Test
    public void AddMaterialsToBook_Check() {
        goToBookAndEdit();
        nextPage(3);

        // 1) Thêm bằng Upload
        addMaterialsByUpload();
        saveMaterials();

        // 2) Xoá các học liệu đã upload
        deleteMaterialsUploaded();

        // 3) Thêm từ Kho học liệu + 1 upload bài giảng cuối
        addMaterialsByWarehouse();
        saveMaterials();

        // 4) Sửa và lưu
        editMaterials();
    }

    @AfterMethod
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
