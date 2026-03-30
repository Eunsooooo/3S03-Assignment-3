import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SauceDemoTest {

    private WebDriver driver;
    private WebDriverWait wait;

    private final String BASE_URL = "https://www.saucedemo.com/";

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void openLoginPage() {
        driver.get(BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name")));
    }

    private void login(String username, String password) {
        driver.findElement(By.id("user-name")).clear();
        driver.findElement(By.id("user-name")).sendKeys(username);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("login-button")).click();
    }

    @Test
    void validLogin() {
        openLoginPage();
        login("standard_user", "secret_sauce");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));

        assertEquals("https://www.saucedemo.com/inventory.html", driver.getCurrentUrl());
        assertEquals("Products", driver.findElement(By.className("title")).getText());
    }

    @Test
    void addItemToCart() {
        openLoginPage();
        login("standard_user", "secret_sauce");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-sauce-labs-backpack")));
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();

        WebElement cartBadge = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("shopping_cart_badge"))
        );

        assertEquals("1", cartBadge.getText());
    }

    @Test
    void cartContents() {
        openLoginPage();
        login("standard_user", "secret_sauce");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-sauce-labs-backpack")));
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));

        WebElement itemName = driver.findElement(By.className("inventory_item_name"));
        assertEquals("Sauce Labs Backpack", itemName.getText());
    }

    @Test
    void completeCheckout() {
        openLoginPage();
        login("standard_user", "secret_sauce");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-to-cart-sauce-labs-backpack")));
        driver.findElement(By.id("add-to-cart-sauce-labs-backpack")).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("checkout")));
        driver.findElement(By.id("checkout")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name")));
        driver.findElement(By.id("first-name")).sendKeys("Haekyung");
        driver.findElement(By.id("last-name")).sendKeys("Nam");
        driver.findElement(By.id("postal-code")).sendKeys("L8S4L8");
        driver.findElement(By.id("continue")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));
        assertEquals("Checkout: Overview", driver.findElement(By.className("title")).getText());

        driver.findElement(By.id("finish")).click();

        WebElement completeHeader = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.className("complete-header"))
        );

        assertEquals("Thank you for your order!", completeHeader.getText());
    }

    @Test
    void lockedOutUserCannotLogIn() {
        openLoginPage();
        login("locked_out_user", "secret_sauce");

        WebElement errorMessage = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']"))
        );

        assertTrue(errorMessage.getText().contains("locked out"));
    }

    @Test
    void problemUserShowsAbnormalProductPageBehaviour() {
        openLoginPage();
        login("problem_user", "secret_sauce");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_img")));

        List<WebElement> images = driver.findElements(By.cssSelector(".inventory_item_img img"));
        assertFalse(images.isEmpty());

        String firstImageSrc = images.get(0).getAttribute("src");

        boolean repeatedImageFound = true;
        for (WebElement image : images) {
            String currentSrc = image.getAttribute("src");
            if (!firstImageSrc.equals(currentSrc)) {
                repeatedImageFound = false;
                break;
            }
        }

        assertTrue(repeatedImageFound, "Expected repeated or incorrect product images for problem_user.");
    }

    @Test
    void performanceGlitchUserStillReachesProductsPage() {
        openLoginPage();
        login("performance_glitch_user", "secret_sauce");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("title")));

        assertEquals("Products", driver.findElement(By.className("title")).getText());
        assertTrue(driver.getCurrentUrl().contains("inventory.html"));
    }
}