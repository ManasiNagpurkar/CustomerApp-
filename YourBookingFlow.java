package customer_app.launch;

import static org.testng.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class YourBookingFlow {

    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dcap = new DesiredCapabilities();
        dcap.setCapability("platformName", "Android");
        dcap.setCapability("deviceName", "emulator-5554"); // Change if using USB
        dcap.setCapability("appium:automationName", "UiAutomator2");
        dcap.setCapability("appium:appPackage", "com.hausvalley.customer");
        dcap.setCapability("appium:appActivity", "com.hausvalley.customer.MainActivity");
        dcap.setCapability("noReset", true);
        dcap.setCapability("ignoreHiddenApiPolicyError", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), dcap);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        System.out.println("🚀 App launched successfully!");
    }

    @Test(priority = 1)
    public void completeLoginFlow() throws InterruptedException {
        // Skip login if already logged in
        if (driver.findElements(By.xpath("//android.widget.ImageView[@content-desc='Tab 2 of 4']")).size() > 0) {
            System.out.println("✅ Already logged in, skipping login steps.");
            return;
        }

        System.out.println("🔐 Performing login flow...");

        // Skip intro
        try {
            WebElement skip = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("Skip")));
            skip.click();
            System.out.println("⏭️ Intro skipped successfully!");
        } catch (Exception e) {
            System.out.println("ℹ️ No skip button found, continuing...");
        }

        // Enter phone
        WebElement phoneLabel = wait.until(ExpectedConditions.elementToBeClickable(
                MobileBy.AccessibilityId("Phone Number")));
        phoneLabel.click();
        Thread.sleep(1000);

        WebElement phoneField = wait.until(ExpectedConditions.elementToBeClickable(
                MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\")")));
        phoneField.clear();
        phoneField.sendKeys("7387984104");
        System.out.println("📲 Entered mobile number.");

        WebElement signInBtn = wait.until(ExpectedConditions.elementToBeClickable(
                MobileBy.AccessibilityId("Sign In")));
        signInBtn.click();

        // Enter OTP
        WebElement otpField = wait.until(ExpectedConditions.elementToBeClickable(
                MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\")")));
        otpField.sendKeys("123456");

        WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(
                MobileBy.AccessibilityId("Verify")));
        verifyBtn.click();

        // Handle Location Permission
        try {
            WebElement allowLocation = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']")));
            allowLocation.click();
            System.out.println("📍 Location allowed.");
        } catch (Exception e) {
            System.out.println("ℹ️ No location popup appeared.");
        }

        Thread.sleep(3000); // Wait for homepage
    }

    @Test(priority = 2, dependsOnMethods = "completeLoginFlow")
    public void bookFirstServiceAgain() throws InterruptedException {
        try {
            // Open "Your Booking" tab
            WebElement yourBookingTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.ImageView[@content-desc='Tab 2 of 4']")));
            yourBookingTab.click();
            System.out.println("📘 Opened 'Your Booking' tab.");
            Thread.sleep(2000);

            // Click first "Book Again"
            WebElement bookAgainBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//android.widget.Button[@content-desc='Book Again'])[1]")));
            bookAgainBtn.click();
            System.out.println("🔁 Clicked first 'Book Again'.");

            // Click "Add Address & Time Slot"
            WebElement addAddressBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("Add Address & Time Slot")));
            addAddressBtn.click();
            System.out.println("📍 Clicked 'Add Address & Time Slot'.");

            // Select 2nd address
            WebElement addressOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.ScrollView/android.widget.RadioButton[2]")));
            addressOption.click();
            System.out.println("🏠 Selected 2nd address.");

            // Click "Proceed"
            WebElement proceedBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("Proceed")));
            proceedBtn.click();
            System.out.println("➡️ Clicked 'Proceed'.");

            // Select Time Slot 09:30 PM
            WebElement timeSlot = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.view.View[@content-desc='09:30 PM']")));
            timeSlot.click();
            System.out.println("⏰ Selected time slot 09:30 PM.");

            // Click "Search Partner"
            WebElement searchPartnerBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@content-desc='Search Partner']")));
            searchPartnerBtn.click();
            System.out.println("🔎 Clicked 'Search Partner'.");

            Thread.sleep(3000);
            System.out.println("🎉 Service rebooking completed successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error during booking flow: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("🧹 Driver session ended successfully.");
        }
    }
}
