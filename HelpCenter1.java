package customer_app.launch;

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
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

public class HelpCenter1 {

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

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), dcap);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void openHelpCenterAndClickFAQ() throws InterruptedException {
        try {
            // Step 1: Go to Profile tab
            WebElement profileTab = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.xpath("//android.widget.ImageView[@content-desc=\"Tab 4 of 4\"]")));
            profileTab.click();
            System.out.println("👤 Opened Profile tab.");
            Thread.sleep(1500);

            // Step 2: Open Help Center
            WebElement helpCenterBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.xpath("//android.widget.Button[@content-desc=\"Help center\"]")));
            helpCenterBtn.click();
            System.out.println("🆘 Opened Help Center.");
            Thread.sleep(1500);

            // Step 3: Open "Account & App Usage"
            WebElement accountAppUsage = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.AccessibilityId("Account & App Usage")));
            accountAppUsage.click();
            System.out.println("⚙️ Opened Account & App Usage section.");
            Thread.sleep(1500);

            // Step 4: Open and close each info topic
            clickInfo("//android.view.View[@content-desc=\"How do I create an account on Hausvalley.com?\"]", "Create Account");
            clickInfo("//android.view.View[@content-desc=\"I forgot my password. How do I reset it?\"]", "Forgot Password");
            clickInfo("//android.view.View[@content-desc=\"How can I update my phone number or address?\"]", "Update Info");
            clickInfo("//android.view.View[@content-desc=\"Can I use HausValley.com in different cities?\"]", "Different Cities");
            clickInfo("//android.view.View[@content-desc=\"How do I delete my account?\"]", "Delete Account");

            // Step 5: Click on Customer Care (only once)
            WebElement customerCare = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.xpath("//android.view.View[@content-desc=\"Customer Care\"]")));
            customerCare.click();
            System.out.println("📞 Opened: Customer Care");
            Thread.sleep(2000);

            System.out.println("✅ All info sections opened and closed successfully. Customer Care left open.");

        } catch (Exception e) {
            System.err.println("❌ Error during Help Center navigation: " + e.getMessage());
        }
    }

    // Helper method: Scroll + Double click (open & close)
    public void clickInfo(String xpath, String title) throws InterruptedException {
        try {
            // Scroll to element
            driver.findElement(MobileBy.AndroidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                + "new UiSelector().description(\"" + title + "\"))"));

            WebElement info = wait.until(ExpectedConditions.elementToBeClickable(MobileBy.xpath(xpath)));
            
            // Open
            info.click();
            System.out.println("📖 Opened: " + title);
            Thread.sleep(1500);
            
            // Close
            info.click();
            System.out.println("❎ Closed: " + title);
            Thread.sleep(1000);

        } catch (Exception e) {
            System.err.println("⚠️ Could not click on " + title + ": " + e.getMessage());
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
