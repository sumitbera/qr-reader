import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class Launcher {

    public static void main(String[] args) throws InterruptedException, IOException, NotFoundException {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("https://test-studio.oliu.id/login");
        Thread.sleep(2000);

        //Delete QR screenshot if exists in screenshots folder
        try {
            Files.deleteIfExists(Paths.get("screenshots/qr.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Screenshot deleted...");

        //Wait for QR code to Load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.MuiBox-root.css-194mb91")));
        System.out.println("-------QR Code Found on web page-------");

        //Find QR code URL
        WebElement qrCode = driver.findElement(By.cssSelector("img[src*='data']"));

        //Store QR code in screenshot directory
        File source = qrCode.getScreenshotAs(OutputType.FILE);
        File dest = new File(System.getProperty("user.dir") + "/screenshots/qr.png");
        try {
            FileHandler.copy(source, dest);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        //Decode QR code captured from the login screen
        System.out.println("############################## QR DECODING STARTED #################################################");
        BufferedImage bfImage = ImageIO.read(new File("screenshots/qr.png"));
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bfImage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        Result resultUrl = new MultiFormatReader().decode(binaryBitmap);
        System.out.println("Invitation URL: " + resultUrl.getText());
        System.out.println("############################## QR DECODING COMPLETED ###############################################");
        driver.quit();
    }

}