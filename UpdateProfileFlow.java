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

public class UpdateProfileFlow {

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
    public void updateProfileDetails() throws InterruptedException {
        try {
            // Navigate to Profile tab
            WebElement profileTab = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.ImageView[@content-desc='Tab 4 of 4']")));
            profileTab.click();
            System.out.println("👤 Opened Profile tab.");
            Thread.sleep(2000);

            // Try locating Edit button with fallback
            WebElement editProfileBtn = null;
            try {
                editProfileBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.Button[@content-desc=\"Mana nagp\r\n"+ "7387984104\"]")));
            } catch (Exception e) {
                // fallback if the above doesn't work
                editProfileBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        MobileBy.AccessibilityId("Edit Profile")));
            }
            editProfileBtn.click();
            System.out.println("✏️ Opened Edit Profile section.");

            // Enter First Name
            WebElement firstName = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.view.View[@content-desc='First Name Last Name Phone Number Email Gender Date of Birth']/android.widget.EditText[1]")));
            firstName.clear();
            firstName.sendKeys("Manasi");
            System.out.println("📝 Entered First Name: Manasi");

            // Enter Last Name
            WebElement lastName = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.view.View[@content-desc='First Name Last Name Phone Number Email Gender Date of Birth']/android.widget.EditText[2]")));
            lastName.clear();
            lastName.sendKeys("Nagpurkar");
            System.out.println("📝 Entered Last Name: Nagpurkar");

            // Select Gender Male
            WebElement maleOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.ImageView[@content-desc='male']")));
            maleOption.click();
            System.out.println("🚹 Selected Gender: Male");

            // Open Date Picker
            WebElement datePicker = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.view.View[@content-desc='First Name Last Name Phone Number Email Gender Date of Birth']/android.widget.ImageView[5]")));
            datePicker.click();
            System.out.println("📅 Opened Date Picker.");

            // Select year 2002
            WebElement yearSelector = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("2002")));
            yearSelector.click();
            System.out.println("📆 Selected Year: 2002");

            // Dismiss button to go to month selector
            WebElement dismissBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.view.View[@content-desc='Dismiss']/android.view.View/android.view.View/android.view.View[2]/android.widget.Button[1]")));
            dismissBtn.click();
            System.out.println("🗓️ Navigated to Month selector.");

            // Select July 27, 2002
            WebElement julyDate = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@content-desc='27, Saturday, July 27, 2002']")));
            julyDate.click();
            System.out.println("📅 Selected Date: 27 July 2002");

            // Click OK
            WebElement okBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("OK")));
            okBtn.click();
            System.out.println("✅ Confirmed Date selection.");

            // Upload Profile Image
            WebElement uploadImage = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.view.View[@content-desc='Manasi Nagpurkar ']/android.widget.ImageView[2]")));
            uploadImage.click();
            System.out.println("🖼️ Clicked Upload Profile Image.");

            // Allow permission (if popup appears)
            try {
                WebElement allowOnce = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//android.widget.Button[@resource-id='com.android.permissioncontroller:id/permission_allow_one_time_button']")));
                allowOnce.click();
                System.out.println("✅ Granted one-time media access permission.");
            } catch (Exception e) {
                System.out.println("ℹ️ No permission popup appeared.");
            }

            // Select "Gallery"
            WebElement galleryOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@content-desc='Gallery']")));
            galleryOption.click();
            System.out.println("🖼️ Selected 'Gallery' option.");

            // Select a photo
            WebElement photo = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.View/android.view.View[5]/android.view.View[2]/android.view.View[2]/android.view.View")));
            photo.click();
            System.out.println("📸 Selected a photo from gallery.");

            // Click Update Now
            WebElement updateNow = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//android.widget.Button[@content-desc='Update Now']")));
            updateNow.click();
            System.out.println("💾 Profile updated successfully with image!");

            Thread.sleep(3000);

        } catch (Exception e) {
            System.err.println("❌ Error during profile update: " + e.getMessage());
            assertTrue(false, "Profile update failed");
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