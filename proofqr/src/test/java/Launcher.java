import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class Launcher {

    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://test-studio.oliu.id/login");
        Thread.sleep(3000);
        //Delete QR screenshot
        try {
            Files.deleteIfExists(Paths.get("screenshots/qr.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Screenshot deleted...");

        //Capture screenshot of QR
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.MuiBox-root.css-194mb91")));
        System.out.println("QR Code Found on web page");
        WebElement qrCode = driver.findElement(By.cssSelector("div.MuiBox-root.css-194mb91"));
        File source = qrCode.getScreenshotAs(OutputType.FILE);
        File dest = new File(System.getProperty("user.dir") + "/screenshots/qr.png");
        try {
            FileHandler.copy(source, dest);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        driver.quit();
    }
}