package com.simplelife.pages.base;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class BasePage {

    public AppiumDriver appiumDriver;
    AndroidDriver androidDriver;
    WebDriverWait wait;
    private int timeout = 20;
    private int pollingTime = 100;

    public BasePage(AppiumDriver appiumDriver) {
        this.appiumDriver = appiumDriver;
        wait = new WebDriverWait(appiumDriver, 20);
        init();
    }

    public void init() {
        PageFactory.initElements(this.appiumDriver, this);
    }


    public boolean waitForElement(WebElement element) {
        Wait wait = new FluentWait(appiumDriver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(pollingTime))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);
        wait.until((Function<WebDriver, WebElement>) appiumDriver -> element);
        return element.isDisplayed();
    }


    boolean isExists(WebElement element) {
        try {
            waitForElement(element);
            return element.isDisplayed();
        } catch (Throwable throwable) {
        }
        return false;
    }

    public void waitVisibilityOf(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }


    public void waitElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitVisibilityOfAllElements(List<WebElement> elements) {
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public void sendKeys(WebElement elem, String text) {
        waitElementToBeClickable(elem);
        elem.click();
        if (text != null) {
            if (!elem.getText().isEmpty()) {
                elem.clear();
            }
            elem.sendKeys(text);
        } else {
            Assert.assertNotNull(elem.getText());
        }
    }

    public void hideKeyboard() {
        try {
            appiumDriver.hideKeyboard();
        } catch (WebDriverException e) {
        }
    }

    public void clickOnElement(WebElement element) {
        waitElementToBeClickable(element);
        try {
            element.click();
        } catch (Exception e) {
            JavascriptExecutor executer = (JavascriptExecutor) appiumDriver;
            executer.executeScript("arguments[0].click();", element);
        }
    }

    public void clickOnDoneButtonOnKeypad()
    {
        appiumDriver.executeScript("mobile:performEditorAction", ImmutableMap.of("action","done"));

    }

}