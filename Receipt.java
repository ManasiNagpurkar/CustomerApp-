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

public class Receipt {

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        System.out.println("🚀 App launched successfully!");
    }

    @Test(priority = 1)
    public void completeLoginFlow() throws InterruptedException {
        if (driver.findElements(By.xpath("//android.widget.ImageView[@content-desc='Tab 2 of 4']")).size() > 0) {
            System.out.println("✅ Already logged in, skipping login steps.");
            return;
        }

        System.out.println("🔐 Performing login flow...");

        try {
            WebElement skip = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("Skip")));
            skip.click();
            System.out.println("⏭️ Intro skipped successfully!");
        } catch (Exception e) {
            System.out.println("ℹ️ No skip button found, continuing...");
        }

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

        WebElement otpField = wait.until(ExpectedConditions.elementToBeClickable(
                MobileBy.AndroidUIAutomator("new UiSelector().className(\"android.widget.EditText\")")));
        otpField.sendKeys("123456");

        WebElement verifyBtn = wait.until(ExpectedConditions.elementToBeClickable(
                MobileBy.AccessibilityId("Verify")));
        verifyBtn.click();

        try {
            WebElement allowLocation = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_allow_foreground_only_button']")));
            allowLocation.click();
            System.out.println("📍 Location allowed.");
        } catch (Exception e) {
            System.out.println("ℹ️ No location popup appeared.");
        }

        Thread.sleep(3000);
    }

    // ✅ Navigate to "Your Booking" → "Past" → service → Receipt → Download
    @Test(priority = 2, dependsOnMethods = "completeLoginFlow")
    public void openPastBookingAndDownloadReceipt() throws InterruptedException {
        try {
            // Open "Your Booking" tab
            WebElement yourBookingTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.ImageView[@content-desc='Tab 2 of 4']")));
            yourBookingTab.click();
            System.out.println("📘 Opened 'Your Booking' tab.");
            Thread.sleep(2000);

            // Click "Past" tab
            WebElement pastTab = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("Past")));
            pastTab.click();
            System.out.println("📜 Switched to 'Past' tab.");
            Thread.sleep(2000);

            // Click on specific service (Special occasion)
            WebElement service = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("Special occasion\nServices: 1\nManaai\nDate: 18 October 2025\nPaid: \n ₹250.00")));
            service.click();
            System.out.println("✅ Opened the service details.");

            // Click on "Receipt"
            WebElement receiptBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@content-desc='Receipt']")));
            receiptBtn.click();
            System.out.println("🧾 Opened Receipt.");

            // Click "More options" (3 dots)
            WebElement moreOptions = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.ImageView[@content-desc='More options']")));
            moreOptions.click();
            System.out.println("⚙️ Opened more options.");

            // Select "Download"
            WebElement downloadOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.TextView[@resource-id='com.google.android.apps.docs:id/title' and @text='Download']")));
            downloadOption.click();
            System.out.println("⬇️ Selected 'Download' option.");

            // Choose Google Drive (2nd icon)
            WebElement driveIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//android.widget.ImageView[@resource-id='android:id/icon'])[2]")));
            driveIcon.click();
            System.out.println("☁️ Selected Google Drive for download.");

            Thread.sleep(3000);
            System.out.println("🎉 Receipt downloaded successfully to Google Drive!");

        } catch (Exception e) {
            System.err.println("❌ Error in Past booking download flow: " + e.getMessage());
            assertTrue(false, "Failed during Past booking flow");
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
