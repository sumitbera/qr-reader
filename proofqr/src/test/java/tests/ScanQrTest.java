package tests;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utility.AppConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

public class ScanQrTest implements AppConstants {

    AndroidDriver androidDriver;

    @BeforeClass
    public void setup() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, ANDROID_APP_CONFIG.AUTOMATION_NAME);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, ANDROID_APP_CONFIG.PLATFORM_NAME);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, ANDROID_APP_CONFIG.PLATFORM_OS);
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, ANDROID_APP_CONFIG.DEVICE_NAME);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, ANDROID_APP_CONFIG.APP_PACKAGE);
        capabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, ANDROID_APP_CONFIG.MAIN_ACTIVITY);
        capabilities.setCapability(AndroidMobileCapabilityType.AUTO_GRANT_PERMISSIONS, true);
        androidDriver = new AndroidDriver(new URL(AppConstants.COMMON_APP_CONFIG.APPIUM_HUB_URL), capabilities);
    }

    @Test(priority = 1, testName = "Capture QR code from web login page")
    public void captureQRCode() throws InterruptedException, IOException, NotFoundException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);
        driver.get(AppConstants.COMMON_APP_CONFIG.WEB_URL);
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
        File dest = new File(System.getProperty("user.dir") + COMMON_APP_CONFIG.IMAGE_PATH);
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
    }

    @Test(priority = 2, testName = "Test to click on Get started button")
    public void clickGetStartedBtn() {
        WebDriverWait wait = new WebDriverWait(androidDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text = 'GET STARTED']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text = 'GET STARTED']")));
        WebElement getStartedBtn = androidDriver.findElement(By.xpath("//android.widget.TextView[@text = 'GET STARTED']"));
        getStartedBtn.click();
    }

    @Test(priority = 3, testName = "Test to accept terms and conditions")
    public void acceptTerms() {
        WebDriverWait wait = new WebDriverWait(androidDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text = 'Terms of use']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text = 'AGREE']")));
        WebElement agreeBtn = androidDriver.findElement(By.xpath("//android.widget.TextView[@text = 'AGREE']"));
        agreeBtn.click();
    }

    @Test(priority = 4, testName = "Test to accept biometric")
    public void acceptBiometric() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(androidDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text = 'Biometric security']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.TextView[@text = 'CONTINUE']")));
        WebElement continueBtn = androidDriver.findElement(By.xpath("//android.widget.TextView[@text = 'CONTINUE']"));
        continueBtn.click();
        Thread.sleep(5000);
    }

    @Test(priority = 5, testName = "Test to enter device passcode")
    public void enterDevicePasscode() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(androidDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("com.android.systemui:id/subtitle")));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("com.android.systemui:id/lockPassword")));
        WebElement passcodeField = androidDriver.findElement(By.id("com.android.systemui:id/lockPassword"));
        passcodeField.sendKeys("123456");
        Thread.sleep(2000);
        androidDriver.pressKey(new KeyEvent(AndroidKey.ENTER));
        Thread.sleep(5000);
    }

    @Test(priority = 6, testName = "Test to scan QR code")
    public void scanQrCode() {
        WebDriverWait wait = new WebDriverWait(androidDriver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.Button/android.widget.ImageView")));
        WebElement backBtn = androidDriver.findElement(By.xpath("//android.widget.Button/android.widget.ImageView"));
        backBtn.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text = 'Scan']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.widget.Button/android.view.ViewGroup/android.widget.ImageView")));
        WebElement scanBtn = androidDriver.findElement(By.xpath("//android.widget.Button/android.view.ViewGroup/android.widget.ImageView"));
        scanBtn.click();
    }

    //@AfterClass
    public void tearDown() {
        androidDriver.quit();
    }
}
