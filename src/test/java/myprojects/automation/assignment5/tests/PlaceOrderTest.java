package myprojects.automation.assignment5.tests;

import myprojects.automation.assignment5.BaseTest;
import myprojects.automation.assignment5.utils.DataConverter;
import myprojects.automation.assignment5.utils.logging.CustomReporter;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;



public class PlaceOrderTest extends BaseTest {

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version
        CustomReporter.logAction("If test failed, because driver cannot find slider with images on the page - it's a mobile emulation");
        actions.open();
        SoftAssert websiteVersion = new SoftAssert();
        websiteVersion.assertTrue(driver.findElement(By.xpath("//div[@id='carousel']")).isDisplayed());
        }


    @Test
    public void createNewOrder() throws InterruptedException {
        // TODO implement order creation test

        // open random product
        actions.openRandomProduct();

        // save product parameters
        actions.getOpenedProductInfo();

        // add product to Cart and validate product information in the Cart
        actions.addToCart();
        CustomReporter.logAction("Checking that in cart displayed only one product position");
        Assert.assertTrue(driver.findElements(By.className("cart-item")).size()==1);
        CustomReporter.logAction("Checking that product's name corresponds to the product's name on the product's page");
        Assert.assertEquals(driver.findElement(By.cssSelector(".product-line-info:nth-child(1)")).getText().toUpperCase(), actions.getProductName().toUpperCase());
        CustomReporter.logAction("Checking that product's price corresponds to the product's price on the product's page");
        Assert.assertEquals(DataConverter.parsePriceValue(driver.findElement(By.className("product-price")).getText()), actions.getPriceRecord());

        // proceed to order creation, fill required information
        actions.continueCheckout("QaTestLab", "BestTestingCourses", "Webinar2017@gmail.com", "Lenina, 10/app.45", "23456", "Charkiv");

        // place new order and validate order summary
        CustomReporter.logAction("Checking that correct message about order's confirmation is displayed");
        Assert.assertTrue(driver.findElement(By.xpath("//h3[@class='h1 card-title']")).getText().contains("Ваш заказ подтверждён".toUpperCase()));
        CustomReporter.logAction("Checking that in order's details displayed only one product position");
        Assert.assertEquals(Integer.parseInt(driver.findElement(By.className("col-xs-2")).getText()),1);
        CustomReporter.logAction("Checking that in order's details product's name corresponds to the product's name on the product's page");
        Assert.assertTrue(driver.findElement(By.className("col-sm-4")).getText().toUpperCase().contains(actions.getProductName().toUpperCase()));
        CustomReporter.logAction("Checking that in order's details product's price corresponds to the product's price on the product's page");
        Assert.assertEquals(DataConverter.parsePriceValue(driver.findElement(By.className("col-xs-5")).getText()), actions.getPriceRecord());

        // check updated In Stock value
        CustomReporter.logAction("Checking that quantity of product on product's page became less by 1 after checkout");
        actions.checkQuantityAfterOrder();
        Assert.assertEquals(DataConverter.parseStockValue(driver.findElement(By.cssSelector(".product-quantities>span")).getText()) + 1, actions.getRecordQty());
    }

}
