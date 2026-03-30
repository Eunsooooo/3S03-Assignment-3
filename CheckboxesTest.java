import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CheckboxesTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void initialCheckboxStates() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));

        assertEquals(2, checkboxes.size());
        assertFalse(checkboxes.get(0).isSelected());
        assertTrue(checkboxes.get(1).isSelected());
    }

    @Test
    void checkFirstCheckbox() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        WebElement firstCheckbox = checkboxes.get(0);

        assertFalse(firstCheckbox.isSelected());

        firstCheckbox.click();

        assertTrue(firstCheckbox.isSelected());
    }

    @Test
    void uncheckSecondCheckbox() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        WebElement secondCheckbox = checkboxes.get(1);

        assertTrue(secondCheckbox.isSelected());

        secondCheckbox.click();

        assertFalse(secondCheckbox.isSelected());
    }

    @Test
    void toggleBothCheckboxes() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        WebElement firstCheckbox = checkboxes.get(0);
        WebElement secondCheckbox = checkboxes.get(1);

        firstCheckbox.click();
        secondCheckbox.click();

        assertTrue(firstCheckbox.isSelected());
        assertFalse(secondCheckbox.isSelected());
    }

    @Test
    void clickAgainToConfirmStateChangesPersistCorrectly() {
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        List<WebElement> checkboxes = driver.findElements(By.cssSelector("input[type='checkbox']"));
        WebElement firstCheckbox = checkboxes.get(0);
        WebElement secondCheckbox = checkboxes.get(1);

        firstCheckbox.click();
        secondCheckbox.click();

        assertTrue(firstCheckbox.isSelected());
        assertFalse(secondCheckbox.isSelected());

        firstCheckbox.click();
        secondCheckbox.click();

        assertFalse(firstCheckbox.isSelected());
        assertTrue(secondCheckbox.isSelected());
    }
}