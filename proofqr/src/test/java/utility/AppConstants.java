package utility;

public interface AppConstants {
    interface ANDROID_APP_CONFIG {
        String AUTOMATION_NAME = "UiAutomator2";
        String PLATFORM_OS = "11";
        String PLATFORM_NAME = "android";
        String APP_PACKAGE = "com.atb.myproof.debug";
        String MAIN_ACTIVITY = "com.atb.myproof.MainActivity";
        String DEVICE_NAME = "e27ac0c2";
    }

    interface COMMON_APP_CONFIG {
        String APPIUM_HUB_URL = "http://127.0.0.1:4723/wd/hub";
        String WEB_URL = "https://test-studio.oliu.id/login";
        String IMAGE_PATH = "/screenshots/qr.png";
    }
}
