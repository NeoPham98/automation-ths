package testcase;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

            if (i == 2){
                inputText(By.cssSelector("input[placeholder='Nhập tên học liệu']"), "Học liệu bài giảng");

            }
        }



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
