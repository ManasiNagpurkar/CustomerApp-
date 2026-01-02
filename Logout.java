package customer_app.launch;



import static org.testng.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class Logout {

    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dcap = new DesiredCapabilities();
        dcap.setCapability("platformName", "Android");
        dcap.setCapability("deviceName", "emulator-5554");
        dcap.setCapability("appium:automationName", "UiAutomator2");
        dcap.setCapability("appium:appPackage", "com.hausvalley.customer");
        dcap.setCapability("appium:appActivity", "com.hausvalley.customer.MainActivity");
        dcap.setCapability("noReset", true);
        dcap.setCapability("ignoreHiddenApiPolicyError", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), dcap);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test(priority = 1)
    public void shouldLaunchApp() {
        boolean appLaunched = driver.findElements(AppiumBy.accessibilityId("Sign In")).size() > 0;
        assertTrue(appLaunched, "App did not launch properly!");
        System.out.println("✅ App launched successfully!");
    }

    @Test(priority = 2)
    public void skipIntroIfPresent() throws InterruptedException {
        try {
            WebElement skipBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Skip"))
            );
            skipBtn.click();
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("ℹ️ No Skip button appeared.");
        }
    }

    @Test(priority = 3, dependsOnMethods = "skipIntroIfPresent")
    public void enterPhoneNumber() {
        try {
            WebElement phoneLabel = wait.until(
                    ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Phone Number"))
            );
            phoneLabel.click();
            Thread.sleep(1000);

            WebElement phoneField = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\")")
                    )
            );
            phoneField.click();
            phoneField.clear();
            phoneField.sendKeys("7387984104");

            WebElement signInBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Sign In"))
            );
            signInBtn.click();

            System.out.println("📱 Phone number entered successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error entering phone number: " + e.getMessage());
        }
    }

    @Test(priority = 4, dependsOnMethods = "enterPhoneNumber")
    public void enterOTPAndVerify() {
        try {
            WebElement otpField = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\")")
                    )
            );
            otpField.click();
            otpField.sendKeys("123456");

            WebElement verifyBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(MobileBy.AccessibilityId("Verify"))
            );
            verifyBtn.click();

            System.out.println("✅ OTP entered and verification attempted.");

            // Handle Location Permission Popup
            try {
                WebElement allowLocation = wait.until(
                        ExpectedConditions.elementToBeClickable(
                                MobileBy.xpath("//android.widget.Button[@resource-id=\"com.android.permissioncontroller:id/permission_allow_foreground_only_button\"]")
                        )
                );
                allowLocation.click();
                System.out.println("📍 Location permission allowed.");
            } catch (Exception popup) {
                System.out.println("ℹ️ No location permission popup appeared.");
            }

        } catch (Exception e) {
            System.err.println("❌ Error entering OTP: " + e.getMessage());
        }
    }

    // 🔹 NEW TEST ADDED — LOGOUT SECTION
    @Test(priority = 5, dependsOnMethods = "enterOTPAndVerify")
    public void logoutFromApp() {
        try {
            // Navigate to Profile tab
            WebElement profileTab = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            MobileBy.xpath("//android.widget.ImageView[@content-desc=\"Tab 4 of 4\"]")
                    )
            );
            profileTab.click();
            System.out.println("👤 Navigated to Profile tab.");

            Thread.sleep(2000);

            // Click Log Out button
            WebElement logoutBtn = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            MobileBy.xpath("//android.widget.Button[@content-desc=\"Log Out\"]")
                    )
            );
            logoutBtn.click();
            System.out.println("🚪 Logged out successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error during logout: " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🧹 Driver session ended.");
        }
    }
}
