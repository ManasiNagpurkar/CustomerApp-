package customer_app.launch;

import static org.testng.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;

public class YourBooking {

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
        dcap.setCapability("noReset", true); // Keep user logged in
        dcap.setCapability("ignoreHiddenApiPolicyError", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), dcap);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ✅ Navigate to "Your Booking" page
    @Test(priority = 1)
    public void navigateToYourBooking() {
        try {
            WebElement yourBookingTab = wait.until(
                    ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Tab 2 of 4"))
            );
            yourBookingTab.click();
            System.out.println("✅ Navigated to Your Booking page!");
        } catch (Exception e) {
            System.err.println("❌ Error navigating to Your Booking: " + e.getMessage());
            assertTrue(false, "Could not navigate to Your Booking page");
        }
    }

    // 🔹 Scroll and check all tabs: Ongoing, Upcoming, Cancelled, then return Home
    @Test(priority = 2, dependsOnMethods = "navigateToYourBooking")
    public void checkBookingTabs() {
        try {
            String[] tabs = {"Ongoing", "Upcoming", "Cancelled"};

            for (String tab : tabs) {
                WebElement tabElement = wait.until(
                        ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId(tab))
                );
                tabElement.click();
                System.out.println("➡️ Clicked '" + tab + "' tab.");

                // Safely get all services/cards in this tab
                List<WebElement> services = driver.findElements(
                        By.xpath("//android.widget.ScrollView//android.widget.ImageView")
                );

                if (services.isEmpty()) {
                    System.out.println("📄 No services found in '" + tab + "' tab.");
                } else {
                    System.out.println("📄 " + services.size() + " services found in '" + tab + "' tab.");
                }
            }

            // 🔹 Navigate back to Home (Tab 1 of 4)
            WebElement homeTab = wait.until(
                    ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("Tab 1 of 4"))
            );
            homeTab.click();
            System.out.println("🏠 Navigated back to Home page successfully!");

            System.out.println("🎉 Checked all booking tabs and returned to Home successfully!");

        } catch (Exception e) {
            System.err.println("❌ Error checking booking tabs or navigating Home: " + e.getMessage());
            assertTrue(false, "Failed during booking tab check or Home navigation");
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
