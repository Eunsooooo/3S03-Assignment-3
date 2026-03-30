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

public class AddRemoveElementsTest {

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
    void initialPageState() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        WebElement addButton = driver.findElement(By.xpath("//button[text()='Add Element']"));
        List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[text()='Delete']"));

        assertTrue(addButton.isDisplayed());
        assertEquals(0, deleteButtons.size());
    }

    @Test
    void addOneElement() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        WebElement addButton = driver.findElement(By.xpath("//button[text()='Add Element']"));
        addButton.click();

        List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[text()='Delete']"));
        assertEquals(1, deleteButtons.size());
    }

    @Test
    void addMultipleElements() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        WebElement addButton = driver.findElement(By.xpath("//button[text()='Add Element']"));

        for (int i = 0; i < 3; i++) {
            addButton.click();
        }

        List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[text()='Delete']"));
        assertEquals(3, deleteButtons.size());
    }

    @Test
    void removeOneElement() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        WebElement addButton = driver.findElement(By.xpath("//button[text()='Add Element']"));
        addButton.click();
        addButton.click();
        addButton.click();

        List<WebElement> deleteButtonsBefore = driver.findElements(By.xpath("//button[text()='Delete']"));
        assertEquals(3, deleteButtonsBefore.size());

        deleteButtonsBefore.get(0).click();

        List<WebElement> deleteButtonsAfter = driver.findElements(By.xpath("//button[text()='Delete']"));
        assertEquals(2, deleteButtonsAfter.size());
    }

    @Test
    void removeAllAddedElements() {
        driver.get("https://the-internet.herokuapp.com/add_remove_elements/");

        WebElement addButton = driver.findElement(By.xpath("//button[text()='Add Element']"));

        for (int i = 0; i < 4; i++) {
            addButton.click();
        }

        List<WebElement> deleteButtons = driver.findElements(By.xpath("//button[text()='Delete']"));
        assertEquals(4, deleteButtons.size());

        while (!driver.findElements(By.xpath("//button[text()='Delete']")).isEmpty()) {
            driver.findElements(By.xpath("//button[text()='Delete']")).get(0).click();
        }

        List<WebElement> deleteButtonsAfter = driver.findElements(By.xpath("//button[text()='Delete']"));
        assertEquals(0, deleteButtonsAfter.size());
    }
}