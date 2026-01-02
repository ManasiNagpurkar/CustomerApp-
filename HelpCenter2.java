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

public class HelpCenter2 {

    AndroidDriver driver;
    WebDriverWait wait;

    @BeforeTest
    public void setUp() throws MalformedURLException {
        DesiredCapabilities dcap = new DesiredCapabilities();
        dcap.setCapability("platformName", "Android");
        dcap.setCapability("deviceName", "vivo Y21A");
        dcap.setCapability("appium:automationName", "UiAutomator2");
        dcap.setCapability("appium:appPackage", "com.hausvalley.customer");
        dcap.setCapability("appium:appActivity", "com.hausvalley.customer.MainActivity");
        dcap.setCapability("noReset", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), dcap);
        wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    @Test
    public void performBookingHelpCenterFlow() throws InterruptedException {
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

            // Step 3: Open Booking & Scheduling
            WebElement bookingScheduling = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.xpath("//android.widget.Button[@content-desc=\"Booking & Scheduling\"]")));
            bookingScheduling.click();
            System.out.println("📅 Opened Booking & Scheduling.");
            Thread.sleep(1500);

            // Step 4: Perform open-close for all FAQ one by one
            clickInfo("How do I book a service on Hausvalley.com?", "Book Service");
            clickInfo("Can I schedule a service for a future date?", "Schedule Service");
            clickInfo("Can I reschedule or cancel a service?", "Reschedule/Cancel Service");
            clickInfo("What happens if a provider is late or doesn’t show up?", "Provider Late");
            clickInfo("How do I view my booking history?", "Booking History");

            // Step 5: Open Customer Care
            WebElement customerCare = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.xpath("//android.view.View[@content-desc=\"Customer Care\"]")));
            customerCare.click();
            System.out.println("📞 Opened Customer Care.");
            Thread.sleep(1500);

            // Step 6: Type message
            WebElement messageBox = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.xpath("//android.widget.EditText")));
            messageBox.sendKeys("Hello, I need some help regarding my booking.");
            System.out.println("💬 Message typed successfully.");

            // Step 7: Send message
            WebElement sendBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View[4]")));
            sendBtn.click();
            System.out.println("✅ Message sent successfully.");

        } catch (Exception e) {
            System.err.println("❌ Error occurred: " + e.getMessage());
        }
    }

    // 🔹 Scroll + Click helper method (works for hidden elements also)
    public void clickInfo(String contentDesc, String title) throws InterruptedException {
        try {
            // Scroll until visible
            driver.findElement(MobileBy.AndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().description(\"" + contentDesc + "\"))"));

            WebElement info = wait.until(ExpectedConditions.elementToBeClickable(
                    MobileBy.xpath("//android.view.View[@content-desc=\"" + contentDesc + "\"]")));

            // open
            info.click();
            System.out.println("📖 Opened: " + title);
            Thread.sleep(2000);

            // close
            info.click();
            System.out.println("❎ Closed: " + title);
            Thread.sleep(1500);

        } catch (Exception e) {
            System.err.println("⚠️ Could not click on " + title + ": " + e.getMessage());
        }
    }

    @AfterTest
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
