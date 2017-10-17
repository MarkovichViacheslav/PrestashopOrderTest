package myprojects.automation.assignment5.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
//import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class DriverFactory {
    /**
     *
     * @param browser Driver type to use in tests.
     * @return New instance of {@link WebDriver} object.
     */
    public static WebDriver initDriver(String browser) {
        switch (browser) {
            case "firefox":
                System.setProperty(
                        "webdriver.gecko.driver",
                        new File(DriverFactory.class.getResource("/geckodriver.exe").getFile()).getPath());
                return new FirefoxDriver();
            case "ie":
            case "internet explorer":
              System.setProperty(
                        "webdriver.ie.driver",
                        new File(DriverFactory.class.getResource("/IEDriverServer.exe").getFile()).getPath());
                InternetExplorerOptions options = new InternetExplorerOptions();
                options.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
                options.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
                return new InternetExplorerDriver(options);

            case "chrome":
            default:
                System.setProperty(
                        "webdriver.chrome.driver", new File(DriverFactory.class.getResource("/chromedriver.exe").getFile()).getPath());
                Map<String, String> mobileEmulation = new HashMap<String, String>();
                mobileEmulation.put("deviceName", "Galaxy S5");
                Map<String, Object> chromeOptions = new HashMap<String, Object>();
                chromeOptions.put("mobileEmulation", mobileEmulation);
                ChromeOptions element = new ChromeOptions();
                element.setCapability("mobileEmulation",chromeOptions);
                return new ChromeDriver(element);
        }
    }

    /**
     *
     * @param browser Remote driver type to use in tests.
     * @param gridUrl URL to Grid.
     * @return New instance of {@link RemoteWebDriver} object.
     */
    public static WebDriver initDriver(String browser, String gridUrl) throws MalformedURLException {
        // TODO prepare capabilities for required browser and return RemoteWebDriver instance
        try {
            DesiredCapabilities capabilities;
            switch (browser) {
                case "firefox":
                    capabilities = DesiredCapabilities.firefox();
                    break;
                case "ie":
                case "internet explorer":
                   capabilities = DesiredCapabilities.internetExplorer();
                    break;
                default:
                capabilities = DesiredCapabilities.firefox();
                break;
            }

            if (browser.equals("chrome")) {
                Map<String, String> mobileEmulation = new HashMap<String, String>();
                mobileEmulation.put("deviceName", "Galaxy S5");
                Map<String, Object> chromeOptions = new HashMap<String, Object>();
                chromeOptions.put("mobileEmulation", mobileEmulation);
                DesiredCapabilities capabilities1 = DesiredCapabilities.chrome();
                capabilities1.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                return new RemoteWebDriver(new URL(gridUrl), capabilities1);
            }
            else return new RemoteWebDriver(new URL(gridUrl), capabilities);

        }
        catch (UnsupportedOperationException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException();
        }
    }
}
