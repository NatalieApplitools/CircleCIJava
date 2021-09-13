import com.applitools.eyes.BatchInfo;
import com.applitools.eyes.RectangleSize;
import com.applitools.eyes.TestResultsSummary;
import com.applitools.eyes.selenium.BrowserType;
import com.applitools.eyes.selenium.Configuration;
import com.applitools.eyes.selenium.Eyes;
import com.applitools.eyes.selenium.fluent.Target;
import com.applitools.eyes.visualgrid.model.DeviceName;
import com.applitools.eyes.visualgrid.model.ScreenOrientation;
import com.applitools.eyes.visualgrid.services.VisualGridRunner;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;




/**
 * Unit test for simple App.
 */
public class TestJava {
    // Create a new chrome web driver
    WebDriver webDriver = new ChromeDriver(new ChromeOptions().setHeadless(getCI()));

    // Create a runner with concurrency of 1
    VisualGridRunner runner = new VisualGridRunner(10);

    // Create Eyes object with the runner, meaning it'll be a Visual Grid eyes.
    Eyes eyes = new Eyes(runner);


    public static boolean getCI() {
        String env = System.getenv("CI");
        return Boolean.parseBoolean(env);
    }

    public static void setUp(Eyes eyes) {

        // Initialize eyes Configuration
        Configuration config = new Configuration();
        # obtain the API key from an environment variable and set it
        api_key = os.environ.get('APPLITOOLS_API_KEY')
        eyes.api_key = api_key

        // create a new batch info instance and set it to the configuration
        config.setBatch(new BatchInfo("CircleCI Java"));

        // Add browsers with different viewports
        config.addBrowser(800, 600, BrowserType.CHROME);
        config.addBrowser(700, 500, BrowserType.FIREFOX);
        config.addBrowser(1600, 1200, BrowserType.IE_11);
        config.addBrowser(1024, 768, BrowserType.EDGE_CHROMIUM);
        config.addBrowser(800, 600, BrowserType.SAFARI);

        // Add mobile emulation devices in Portrait mode
        config.addDeviceEmulation(DeviceName.iPhone_X, ScreenOrientation.PORTRAIT);
        config.addDeviceEmulation(DeviceName.Pixel_2, ScreenOrientation.PORTRAIT);

        // Set the configuration object to eyes
        eyes.setConfiguration(config);

    }


    @Test
    public void succeeded() {
        setUp(this.eyes);
        try {

            this.webDriver.get("http://the-internet.herokuapp.com/login");


            // Call Open on eyes to initialize a test session
            eyes.open(this.webDriver, "CircleCI Java", "Ultrafast", new RectangleSize(800, 600));

            // check the login page with fluent api, see more info here
            // https://applitools.com/docs/topics/sdk/the-eyes-sdk-check-fluent-api.html
            eyes.check(Target.window().fully().withName("Login page"));
            this.webDriver.findElement(By.id("username")).sendKeys("tomsmith");
            this.webDriver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
            this.webDriver.findElement(By.id("login")).submit();

            // Check the app page
            this.eyes.check(Target.window().fully().withName("App page"));

            // Call Close on eyes to let the server know it should display the results
            this.eyes.closeAsync();

        } finally  {
            this.eyes.abortAsync();
            tearDown(this.webDriver, this.runner);
        }

    }

    public static void ultraFastTest(WebDriver webDriver, Eyes eyes) {

        try {


            // Navigate to the url we want to test
            webDriver.get("https://demo.applitools.com");

            // Call Open on eyes to initialize a test session
            eyes.open(webDriver, "CircleCI Java", "CircleCI Java", new RectangleSize(800, 600));

            // check the login page with fluent api, see more info here
            // https://applitools.com/docs/topics/sdk/the-eyes-sdk-check-fluent-api.html
            eyes.check(Target.window().fully().withName("Login page"));

            webDriver.findElement(By.id("log-in")).click();

            // Check the app page
            eyes.check(Target.window().fully().withName("App page"));

            // Call Close on eyes to let the server know it should display the results
            eyes.closeAsync();

        } finally  {
            eyes.abortAsync();
        }

    }

    private static void tearDown(WebDriver webDriver, VisualGridRunner runner) {
        // Close the browser
        webDriver.quit();

        // find visual differences
        TestResultsSummary allTestResults = runner.getAllTestResults(true);
        System.out.println(allTestResults);
    }

}
