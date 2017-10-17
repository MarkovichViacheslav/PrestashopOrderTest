package myprojects.automation.assignment5;


import myprojects.automation.assignment5.model.ProductData;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.Properties;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.Random;

/**
 * Contains main script actions that may be used in scripts.
 */
public class GeneralActions {
    private WebDriver driver;
    private WebDriverWait wait;
    private By allProductsLink = By.partialLinkText("Все товары");
    private By productsImages = By.xpath("//h1[@class='h3 product-title']/a");
    private By productField = By.cssSelector(".h1[itemprop='name']");
    private By priceField = By.xpath("//span[@itemprop='price']");
    private By aboutProductTab = By.cssSelector("a[href='#product-details']");
    private By quantityField = By.cssSelector(".product-quantities>span");
    private By addToCartButton = By.className("add-to-cart");
    private By goToCheckoutButton = By.xpath("//a[contains(text(), 'перейти к оформлению')]");
    private By continueCheckoutButton = By.xpath("//a[contains(text(), 'Оформление заказа')]");
    private By personalInformationForm = By.id("customer-form");
    private By firstNameField = By.cssSelector("input[name='firstname']");
    private By lastNameField = By.cssSelector("input[name='lastname']");
    private By emailField = By.cssSelector("input[name='email']");
    private By addressField = By.cssSelector("input[name='address1']");
    private By indexField = By.cssSelector("input[name='postcode']");
    private By cityField = By.cssSelector("input[name='city']");
    private By continueButton = By.xpath("//button[contains(text(), 'Продолжить')]");
    private By typeOfDeliverytForm = By.id("checkout-delivery-step");
    private By paymentForm = By.id("checkout-payment-step");
    private By paymentTypeBankTransfer = By.xpath("//span[contains(text(), 'Оплата банковским переводом')]");
    private By approveServiceConditions = By.cssSelector("#conditions-to-approve  .custom-checkbox");
    private By confirmOrderButton = By.cssSelector("#payment-confirmation button[type='submit']");
    private String productName;
    private int quantity;
    private String productUrl;
    private float price;

    public GeneralActions(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 30);
    }

    public void open() {
        driver.navigate().to(Properties.getBaseUrl());
        waitForContentLoad("prestashop-automation");
    }

    public void openRandomProduct() {
        // TODO implement logic to open random product before purchase
        try {
            driver.navigate().to(Properties.getBaseUrl());
            waitForContentLoad("prestashop-automation");
            scrollPageDown();
            clickWithJS(allProductsLink);
            waitForContentLoad("Главная");
            clickRandomProduct();
        } catch (UnsupportedOperationException exc) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Extracts product information from opened product details page.
     *
     * @return
     */
    public ProductData getOpenedProductInfo() {
        CustomReporter.logAction("Get information about currently opened product");
        // TODO extract data from opened page
        try {
            productName = driver.findElement(productField).getText();
            clickWithJS(aboutProductTab);
            waitForElementClickability(quantityField);
            quantity = DataConverter.parseStockValue(driver.findElement(quantityField).getText());
            productUrl = driver.getCurrentUrl();
            price = DataConverter.parsePriceValue(driver.findElement(priceField).getText());
            ProductData newProduct = new ProductData(productName, quantity, price);
            return newProduct;
        } catch (UnsupportedOperationException exc) {
            throw new UnsupportedOperationException();
        }
    }

    public void waitForElementClickability(By element) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForContentLoad(String contentName) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.titleContains(contentName));
    }

    public void clickWithJS(By by){
        WebElement element = driver.findElement(by);
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click()", element);
    }

    public boolean scrollPageDown(){
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        boolean scrollResult = (boolean) executor.executeScript(
                "var scrollBefore = $(window).scrollTop();" +
                        "window.scrollTo(scrollBefore, document.body.scrollHeight);" +
                        "return $(window).scrollTop() > scrollBefore;");
        return scrollResult;
    }

    public void clickRandomProduct() {
        List<WebElement> allProductsNamesLinks = driver.findElements(productsImages);
        Random r = new Random();
        int randomValue = r.nextInt(allProductsNamesLinks.size());
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click()", allProductsNamesLinks.get(randomValue));
    }

        public void addToCart(){
        clickWithJS(addToCartButton);
        waitForElementClickability(goToCheckoutButton);
        clickWithJS(goToCheckoutButton);
        waitForContentLoad("Торговые точки");

    }
        public void continueCheckout(String firstname, String lastname, String email, String address, String index, String city) throws InterruptedException {
        clickWithJS(continueCheckoutButton);
        waitForElementClickability(personalInformationForm);

        //first step
        driver.findElement(firstNameField).sendKeys(firstname);
        driver.findElement(lastNameField).sendKeys(lastname);
        driver.findElement(emailField).sendKeys(email);
        clickWithJS(continueButton);

        //second step
        driver.findElement(addressField).sendKeys(address);
        driver.findElement(indexField).sendKeys(index);
        driver.findElement(cityField).sendKeys(city);

        //third step
        clickWithJS(typeOfDeliverytForm);
        Thread.sleep(5000);

        //fourth step
        clickWithJS(paymentForm);
        waitForElementClickability(paymentTypeBankTransfer);
        clickWithJS(paymentTypeBankTransfer);
        scrollPageDown();
        driver.findElement(approveServiceConditions).click();
        driver.findElement(confirmOrderButton).click();
        Thread.sleep(5000);
    }
    public void checkQuantityAfterOrder(){
        driver.navigate().to(getProductUrl());
        clickWithJS(aboutProductTab);
        waitForElementClickability(quantityField);

    }
    public String getProductName() { return productName; }

    public int getRecordQty() { return quantity; }

    public float getPriceRecord()
    {
        return price;
    }

    public String getProductUrl() { return productUrl; }


    }


