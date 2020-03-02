package com.Util;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

public class ActionUtils {

	// Action Utils ==============================================================================
	// ScreenShot code
	public static void takeScreenshotAtEndOfTest(WebDriver driver) throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String currentDir = System.getProperty("user.dir");
		FileUtils.copyFile(scrFile, new File(currentDir + "/screenshots/" + System.currentTimeMillis() + ".png"));
	}

	// Wait for element present
	public static void waitForElementPresent(WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 10);
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			Reporter.log("Some exception/error occured while waiting for element: "+locator.toString(), true);
			e.printStackTrace();
		}
	}

	// Returns element
	public static WebElement getElement(WebDriver driver, By locator) {
		WebElement element = null;
		try {
			waitForElementPresent(driver, locator);
			element = driver.findElement(locator);
		} catch (Exception e) {
			Reporter.log("Some exception occured while creating element: "+locator.toString(), true);
			e.printStackTrace();
		}
		return element;
	}

	// Returns elements
	public static List<WebElement> getElements(WebDriver driver, By locator) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		List<WebElement> list = null;
		try {
			wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
			waitForElementPresent(driver, locator);
			list = driver.findElements(locator);
		} catch (Exception e) {
			Reporter.log("Some exception occured while creating element: "+locator.toString(), true);
			e.printStackTrace();
		}
		return list;
	}

	// Returns the title of the page
	public static String getPageTitle(WebDriver driver) {
		return driver.getTitle();
	}

	// Click Method by using JAVA Generics (You can use both By or Webelement)
	public static <T> void click(WebDriver driver, T elementAttr) {
		if (elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			driver.findElement((By) elementAttr).click();
		} else {
			((WebElement) elementAttr).click();
		}
	}

	public static <T> void writeText(WebDriver driver, T elementAttr, String text) {
		if (elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			driver.findElement((By) elementAttr).sendKeys(text);
		} else {
			((WebElement) elementAttr).sendKeys(text);
		}
	}

	public static <T> String readText(WebDriver driver, T elementAttr) {
		if (elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			return driver.findElement((By) elementAttr).getText();
		} else {
			return ((WebElement) elementAttr).getText();
		}
	}

	// Close popup if exists
	public static void handlePopup(WebDriver driver, By by) throws Exception {
		List<WebElement> popups = driver.findElements(by);
		if (!popups.isEmpty()) {
			popups.get(0).click();
			Thread.sleep(500);
		}
	}

	// Wait for element to be clickable then click
	public static <T> void waitForClickableThenClick (WebDriver driver, T elementAttr) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		if(elementAttr.getClass().getName().contains("By")) {
			WebElement ele = driver.findElement((By)elementAttr);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			ele.click();
		} else {
			WebElement ele = ((WebElement)elementAttr);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
			ele.click();
		}
	}

	// Wait for element to be visible then click
	public static <T> void waitForVisibleThenClick (WebDriver driver, T elementAttr) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		if(elementAttr.getClass().getName().contains("By")) {
			WebElement ele = driver.findElement((By)elementAttr);
			wait.until(ExpectedConditions.visibilityOf(ele));
			ele.click();
		} else {
			WebElement ele = ((WebElement)elementAttr);
			wait.until(ExpectedConditions.visibilityOf(ele));
			ele.click();
		}
	}

	// Wait for element to be visible then type
	public static <T> void waitForVisibleThenType(WebDriver driver, T elementAttr, String text) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		if(elementAttr.getClass().getName().contains("By")) {
			WebElement ele = driver.findElement((By)elementAttr);
			wait.until(ExpectedConditions.visibilityOf(ele));
			ele.sendKeys(text);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			wait.until(ExpectedConditions.visibilityOf(ele));
			ele.sendKeys(text);
		}
	}

	// JavaScript click
	public static <T> void jsClick(WebDriver driver, T elementAttr){
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
		}
	}

	// JavaScript type
	public static <T> void jsType(WebDriver driver, T elementAttr, String value){
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'", ele);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'", ele);
		}
	}

	// Clear & type
	public static <T> void clearAndType(WebDriver driver, T elementAttr, String value) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.clear();
			ele.sendKeys(value);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.clear();
			ele.sendKeys(value);
		}
	}

	// Click, clear & type - same textbox
	public static <T> void clickClearAndType(WebDriver driver, T elementAttr, String value) throws Exception {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.click();
			Thread.sleep(500);
			ele.clear();
			ele.sendKeys(value);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.click();
			Thread.sleep(500);
			ele.clear();
			ele.sendKeys(value);
		}
	}

	// Click, clear & type - Hidden textbox
	public static <T> void clickClearAndType(WebDriver driver, T elementAttr1, T elementAttr2, String value) throws Exception {
		if(elementAttr1.getClass().getName().contains("By") && elementAttr2.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr1);
			WebElement ele1 = driver.findElement((By)elementAttr1);
			WebElement ele2 = driver.findElement((By)elementAttr2);
			ele1.click();
			Thread.sleep(500);
			ele2.clear();
			ele2.sendKeys(value);
		} else {
			WebElement ele1 = ((WebElement)elementAttr1);
			WebElement ele2 = ((WebElement)elementAttr2);
			ele1.click();
			Thread.sleep(500);
			ele2.clear();
			ele2.sendKeys(value);
		}
	}

	// JavaScript - click, clear & type
	public static <T> void jsClickClearAndType(WebDriver driver, T elementAttr, String value) throws Exception {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
			Thread.sleep(500);
			ele.clear();
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
			Thread.sleep(500);
			ele.clear();
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele);
		}
	}

	// JavaScript - click, clear & type - Hidden textbox 
	public static <T> void jsClickClearAndType(WebDriver driver, T elementAttr1, T elementAttr2, String value) throws Exception {
		if(elementAttr1.getClass().getName().contains("By") && elementAttr2.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr1);
			WebElement ele1 = driver.findElement((By)elementAttr1);
			WebElement ele2 = driver.findElement((By)elementAttr2);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele1);
			Thread.sleep(500);
			ele2.clear();
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele2);
		} else {
			WebElement ele1 = ((WebElement)elementAttr1);
			WebElement ele2 = ((WebElement)elementAttr2);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele1);
			Thread.sleep(500);
			ele2.clear();
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele2);
		}
	}

	// Actions click
	public static <T> void actionClick(WebDriver driver, T elementAttr) {
		Actions act = new Actions(driver);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			act.moveToElement(ele).click().build().perform();
		} else {
			WebElement ele = ((WebElement)elementAttr);
			act.moveToElement(ele).click().build().perform();
		}
	}

	// Actions click - click at part of element wrt xy coordinates
	public static <T> void actionClickAtPartOfElementWrtXY(WebDriver driver, T elementAttr, int x, int y) {
		Actions action= new Actions(driver);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			action.moveToElement(ele).moveByOffset(x, y).click().build().perform();
		} else {
			WebElement ele = ((WebElement)elementAttr);
			action.moveToElement(ele).moveByOffset(x, y).click().build().perform();
		}
	}

	// Stale element click
	public static <T> void staleElementClick(WebDriver driver, T elementAttr) {
		for(int i=0; i<=3;i++){
			try{
				if(elementAttr.getClass().getName().contains("By")) {
					waitForElementPresent(driver, (By) elementAttr);
					WebElement ele = driver.findElement((By)elementAttr);
					ele.click();
				} else {
					WebElement ele = ((WebElement)elementAttr);
					ele.click();
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
	}

	// Click checkbox 
	public static <T> void checkbox_Checking(WebDriver driver, T elementAttr) {
		boolean checkstatus;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				Reporter.log("Checkbox is already checked", true);
			} else {
				ele.click();
				Reporter.log("Checked the checkbox", true);
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				Reporter.log("Checkbox is already checked", true);
			} else {
				ele.click();
				Reporter.log("Checked the checkbox", true);
			}
		}
	}

	// Select radio button
	public static <T> void radiobutton_Select(WebDriver driver, T elementAttr) {
		boolean checkstatus;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				Reporter.log("RadioButton is already checked", true);
			} else {
				ele.click();
				Reporter.log("Selected the Radiobutton", true);
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				Reporter.log("RadioButton is already checked", true);
			} else {
				ele.click();
				Reporter.log("Selected the Radiobutton", true);
			}
		}
	}

	// Uncheck radio button
	public static <T> void checkbox_Unchecking(WebDriver driver, T elementAttr) {
		boolean checkstatus;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				ele.click();
				Reporter.log("Checkbox is unchecked", true);
			} else {
				Reporter.log("Checkbox is already unchecked", true);
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				ele.click();
				Reporter.log("Checkbox is unchecked", true);
			} else {
				Reporter.log("Checkbox is already unchecked", true);
			}
		}
	}

	// Deselect radio button
	public static <T> void radioButton_Deselect(WebDriver driver, T elementAttr) {
		boolean checkstatus;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				ele.click();
				Reporter.log("Radio Button is deselected", true);
			} else {
				Reporter.log("Radio Button was already Deselected", true);
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				ele.click();
				Reporter.log("Radio Button is deselected", true);
			} else {
				Reporter.log("Radio Button was already Deselected", true);
			}
		}
	}

	// Drag & Drop - 1
	public static <T> void dragAndDrop(WebDriver driver, T fromWebElementAttr, T toWebElementAttr) {
		Actions builder = new Actions(driver);
		if(fromWebElementAttr.getClass().getName().contains("By") && toWebElementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) fromWebElementAttr);
			WebElement ele1 = driver.findElement((By)fromWebElementAttr);
			WebElement ele2 = driver.findElement((By)toWebElementAttr);
			builder.dragAndDrop(ele1, ele2).perform();
		} else {
			WebElement ele1 = ((WebElement)fromWebElementAttr);
			WebElement ele2 = ((WebElement)toWebElementAttr);
			builder.dragAndDrop(ele1, ele2).perform();
		}
	}

	// Drag & Drop 2
	public static <T> void dragAndDrop_Method2(WebDriver driver, T fromWebElementAttr, T toWebElementAttr) {
		Actions builder = new Actions(driver);
		if(fromWebElementAttr.getClass().getName().contains("By") && toWebElementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) fromWebElementAttr);
			WebElement ele1 = driver.findElement((By)fromWebElementAttr);
			WebElement ele2 = driver.findElement((By)toWebElementAttr);
			Action dragAndDrop = builder.clickAndHold(ele1).moveToElement(ele2).release(ele2).build();
			dragAndDrop.perform();
		} else {
			WebElement ele1 = ((WebElement)fromWebElementAttr);
			WebElement ele2 = ((WebElement)toWebElementAttr);
			Action dragAndDrop = builder.clickAndHold(ele1).moveToElement(ele2).release(ele2).build();
			dragAndDrop.perform();
		}
	}

	// Drag & drop 3
	public static <T> void dragAndDrop_Method3(WebDriver driver, T fromWebElementAttr, T toWebElementAttr) throws InterruptedException {
		Actions builder = new Actions(driver);
		if(fromWebElementAttr.getClass().getName().contains("By") && toWebElementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) fromWebElementAttr);
			WebElement ele1 = driver.findElement((By)fromWebElementAttr);
			WebElement ele2 = driver.findElement((By)toWebElementAttr);
			builder.clickAndHold(ele1).moveToElement(ele2).perform();
			Thread.sleep(2000);
			builder.release(ele2).build().perform();
		} else {
			WebElement ele1 = ((WebElement)fromWebElementAttr);
			WebElement ele2 = ((WebElement)toWebElementAttr);
			builder.clickAndHold(ele1).moveToElement(ele2).perform();
			Thread.sleep(2000);
			builder.release(ele2).build().perform();
		}
	}

	// Double click
	public static <T> void doubleClickWebelement(WebDriver driver, T elementAttr)throws InterruptedException {
		Actions builder = new Actions(driver);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			builder.doubleClick(ele).perform();
			Thread.sleep(2000);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			builder.doubleClick(ele).perform();
			Thread.sleep(2000);
		}
	}

	// Select by visible text
	public static <T> void selectElementByVisibleText(WebDriver driver, T elementAttr, String Name) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByVisibleText(Name);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByVisibleText(Name);
		}
	}

	// Select by value
	public static <T> void selectElementByValue(WebDriver driver, T elementAttr, String value) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByValue(value);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByValue(value);
		}
	}

	// Select by index
	public static <T> void selectElementByIndex(WebDriver driver, T elementAttr, int index) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByIndex(index);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByIndex(index);
		}
	}

	// Upload file
	public static <T> void uploadFile(WebDriver driver, T browseButton, String filePath) throws Exception {
		if(browseButton.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) browseButton);
			WebElement ele = driver.findElement((By)browseButton);
			StringSelection sel2 = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel2,null);
			Reporter.log("Selection: "+sel2, true);
			Thread.sleep(1000);
			ele.click();
			Reporter.log("Browse button clicked", true);
			Robot robot2 = new Robot();
			Thread.sleep(2000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			robot2.keyPress(KeyEvent.VK_CONTROL);
			robot2.keyPress(KeyEvent.VK_V);
			robot2.keyRelease(KeyEvent.VK_CONTROL);
			robot2.keyRelease(KeyEvent.VK_V);
			Thread.sleep(1000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		} else {
			WebElement ele = ((WebElement)browseButton);
			StringSelection sel2 = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel2,null);
			Reporter.log("Selection: "+sel2, true);
			Thread.sleep(1000);
			ele.click();
			Reporter.log("Browse button clicked", true);
			Robot robot2 = new Robot();
			Thread.sleep(2000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			robot2.keyPress(KeyEvent.VK_CONTROL);
			robot2.keyPress(KeyEvent.VK_V);
			robot2.keyRelease(KeyEvent.VK_CONTROL);
			robot2.keyRelease(KeyEvent.VK_V);
			Thread.sleep(1000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		}
	}

	// Upload file 2
	public static <T> void uploadFile2(WebDriver driver, T browseButton, String filePath) throws Exception {
		if(browseButton.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) browseButton);
			WebElement ele = driver.findElement((By)browseButton);
			StringSelection sel2 = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel2,null);
			Reporter.log("Selection: "+sel2, true);
			Thread.sleep(1000);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
			Reporter.log("Browse button clicked", true);
			Robot robot2 = new Robot();
			Thread.sleep(2000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			robot2.keyPress(KeyEvent.VK_CONTROL);
			robot2.keyPress(KeyEvent.VK_V);
			robot2.keyRelease(KeyEvent.VK_CONTROL);
			robot2.keyRelease(KeyEvent.VK_V);
			Thread.sleep(1000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		} else {
			WebElement ele = ((WebElement)browseButton);
			StringSelection sel2 = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel2,null);
			Reporter.log("Selection: "+sel2, true);
			Thread.sleep(1000);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
			Reporter.log("Browse button clicked", true);
			Robot robot2 = new Robot();
			Thread.sleep(2000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			robot2.keyPress(KeyEvent.VK_CONTROL);
			robot2.keyPress(KeyEvent.VK_V);
			robot2.keyRelease(KeyEvent.VK_CONTROL);
			robot2.keyRelease(KeyEvent.VK_V);
			Thread.sleep(1000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		}
	}

	// Scroll into view
	public static <T> void scrollIntoView(WebDriver driver, T elementAttr) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			JavascriptExecutor js=(JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", ele);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			JavascriptExecutor js=(JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", ele);
		}
	}

	// Scroll page wrt xy coordinates
	public static void scrollPageWrtXY(WebDriver driver, int x, int y) {
		((JavascriptExecutor)driver).executeScript("scroll(" +x+ "," +y+ ")");
	}

	// Check checkbox
	public static <T> void check(WebDriver driver, T checkBox) {
		if(checkBox.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) checkBox);
			WebElement ele = driver.findElement((By)checkBox);
			if (!ele.isSelected())
				ele.click();
		} else {
			WebElement ele = ((WebElement)checkBox);
			if (!ele.isSelected())
				ele.click();
		}
	}	

	// Switch to new window
	public static void switchToNewWindow(WebDriver driver) {
		Set s = driver.getWindowHandles();
		Iterator itr = s.iterator();
		String w1 = (String) itr.next();
		String w2 = (String) itr.next();
		driver.switchTo().window(w2);
	}

	// Switch to new window 2
	public static void switchToNewWindow_new(WebDriver driver) {
		List<String> browserTabs = new ArrayList<String> (driver.getWindowHandles());
		driver.switchTo().window(browserTabs .get(1));
	}

	// Switch to old window
	public static void switchToOldWindow(WebDriver driver) {
		Set s = driver.getWindowHandles();
		Iterator itr = s.iterator();
		String w1 = (String) itr.next();
		String w2 = (String) itr.next();
		driver.switchTo().window(w1);
	}

	// Switch to parent window 
	public static void switchToParentWindow(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	// Wait for element to be clickable
	public static <T> void waitForElementToBeClickable(WebDriver driver, T elementAttr) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
		} else {
			WebElement ele = ((WebElement)elementAttr);
			wait.until(ExpectedConditions.elementToBeClickable(ele));
		}
	}

	// Wait for element to be visible
	public static <T> void waitForElementVisibility(WebDriver driver, T elementAttr) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			wait.until(ExpectedConditions.visibilityOf(ele));
		} else {
			WebElement ele = ((WebElement)elementAttr);
			wait.until(ExpectedConditions.visibilityOf(ele));
		}
	}

	// Wait for alert present
	public static void waitForAlertPresent(WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.alertIsPresent());
	}

	// Wait for list of elements visibility
	public static void waitForListOfElementsVisibility(WebDriver driver, List<WebElement> list) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfAllElements(list));
	}

	// Set window size
	public static void setWindowSize(WebDriver driver, int Dimension1, int dimension2) {
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.manage().window().setSize(new Dimension(Dimension1, dimension2));
	}

	// Press key down
	public static <T> void pressKeyDown(WebDriver driver, T elementAttr) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.sendKeys(Keys.DOWN);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.sendKeys(Keys.DOWN);
		}
	}

	// Press key down
	public static <T> void pressKeyEnter(WebDriver driver, T elementAttr) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.sendKeys(Keys.ENTER);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.sendKeys(Keys.ENTER);
		}
	}

	// Press key up
	public static <T> void pressKeyUp(WebDriver driver, T elementAttr) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.sendKeys(Keys.UP);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.sendKeys(Keys.UP);
		}
	}

	// Switch to tab
	public static <T> void moveToTab(WebDriver driver, T elementAttr) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.sendKeys(Keys.chord(Keys.ALT, Keys.TAB));
			ele.sendKeys(Keys.chord(Keys.CONTROL+"a", Keys.BACK_SPACE, Keys.ENTER));
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.sendKeys(Keys.chord(Keys.ALT, Keys.TAB));
		}
	}

	// Keyboard events
	public static <T> void keyboardEvents(WebDriver driver, T elementAttr, Keys key, String alphabet) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.sendKeys(Keys.chord(key, alphabet));
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.sendKeys(Keys.chord(key, alphabet));
		}
	}

	// Click multiple elements
	public static <T> void clickMultipleElements(WebDriver driver, T elementAttr1, T elementAttr2) {
		Actions act = new Actions(driver);
		if(elementAttr1.getClass().getName().contains("By") && elementAttr2.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr1);
			WebElement ele1 = driver.findElement((By)elementAttr1);
			WebElement ele2 = driver.findElement((By)elementAttr2);
			act.keyDown(Keys.CONTROL).click(ele1).click(ele2).keyUp(Keys.CONTROL).build().perform();
		} else {
			WebElement ele1 = ((WebElement)elementAttr1);
			WebElement ele2 = ((WebElement)elementAttr2);
			act.keyDown(Keys.CONTROL).click(ele1).click(ele2).keyUp(Keys.CONTROL).build().perform();
		}
	}

	// Hover to element
	public static <T> void hoverToElement(WebDriver driver, T elementAttr) throws Exception {
		Actions act = new Actions(driver);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			act.moveToElement(ele).build().perform();
			Thread.sleep(2000);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			act.moveToElement(ele).build().perform();
			Thread.sleep(2000);
		}
	}

	// Slider pointer drag & drop
	public static <T> void sliderDragAndDrap(WebDriver driver, T sliderBar, T slider, String dragChoicePercentage) {
		Actions act = new Actions(driver);
		if(sliderBar.getClass().getName().contains("By") && slider.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) sliderBar);
			WebElement bar = driver.findElement((By)sliderBar);
			WebElement pointer = driver.findElement((By)slider);
			int size = bar.getSize().getWidth();
			Reporter.log("Size of slider bar in pixels is: "+size, true);
			// Using drag and drop
			int userOption = Integer.parseInt(dragChoicePercentage);
			act.dragAndDropBy(pointer, (userOption*size)/100, 0).release().build().perform();
			// Using click and Hold then drop
			//act.moveToElement(slider).clickAndHold().moveByOffset((dragChoice*size)/100, 0).release().build().perform();
		} else {
			WebElement bar = ((WebElement)sliderBar);
			WebElement pointer = ((WebElement)slider);
			int size = bar.getSize().getWidth();
			Reporter.log("Size of slider bar in pixels is: "+size, true);
			// Using drag and drop
			int userOption = Integer.parseInt(dragChoicePercentage);
			act.dragAndDropBy(pointer, (userOption*size)/100, 0).release().build().perform();
			// Using click and Hold then drop
			//act.moveToElement(slider).clickAndHold().moveByOffset((dragChoice*size)/100, 0).release().build().perform();
		}
	}

	// Returns tool tip of the element
	public static <T> String getToolTip(WebDriver driver, T elementAttr)	{
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			String tooltip = ele.getAttribute("title");
			Reporter.log("Tool text : " +tooltip, true);
			return tooltip;
		} else {
			WebElement ele = ((WebElement)elementAttr);
			String tooltip = ele.getAttribute("title");
			Reporter.log("Tool text : " +tooltip, true);
			return tooltip;
		}
	}

	// Returns tool tip of the element
	public static <T> String getToolTip2(WebDriver driver, T elementAttr, String attribute) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			String tooltip = ele.getAttribute(attribute);
			Reporter.log("Tool text : " +tooltip, true);
			return tooltip;
		} else {
			WebElement ele = ((WebElement)elementAttr);
			String tooltip = ele.getAttribute(attribute);
			Reporter.log("Tool text : " +tooltip, true);
			return tooltip;
		}
	}
	
	// Returns attribute value
	public static <T> String getAttributeValue(WebDriver driver, T elementAttr, String attribute) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			String value = ele.getAttribute(attribute);
			return value;
		} else {
			WebElement ele = ((WebElement)elementAttr);
			String value = ele.getAttribute(attribute);
			return value;
		}
	}

	// Highlights the element before action
	public static <T> void highlightElement(WebDriver driver, T elementAttr) throws Exception {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", ele); // highlight
			Thread.sleep(300); // delay between highlight and unhighlight 
			((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", ele); // unhighlight
		} else {
			WebElement ele = ((WebElement)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", ele); // highlight
			Thread.sleep(300); // delay between highlight and unhighlight 
			((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", ele); // unhighlight
		}
	}

	public static void highlightElement(WebDriver driver, WebElement element) throws Exception {
		((JavascriptExecutor)driver).executeScript("arguments[0].style.border='3px solid yellow'", element); // highlight
		Thread.sleep(300); // delay between highlight and unhighlight 
		((JavascriptExecutor)driver).executeScript("arguments[0].style.border=''", element); // unhighlight
	}


	// Returns first selected option
	public static <T> String getFirstSelectedOption(WebDriver driver, T elementAttr) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			Select select = new Select(ele);
			WebElement option = select.getFirstSelectedOption();
			return option.getAttribute("text");
		} else {
			WebElement ele = ((WebElement)elementAttr);
			Select select = new Select(ele);
			WebElement option = select.getFirstSelectedOption();
			return option.getAttribute("text");
		}
	}

	// Click one check box from list
	public static void clickCheckboxFromList(WebDriver driver, String xpathOfElement, String valueToSelect) {
		List<WebElement> lst = driver.findElements(By.xpath(xpathOfElement));
		for (int i = 0; i < lst.size(); i++) {
			List<WebElement> dr = lst.get(i).findElements(By.tagName("label"));
			for (WebElement f : dr) {
				Reporter.log("value in the list : " + f.getText(),true);
				if (valueToSelect.equals(f.getText())) {
					f.click();
					break;
				}
			}
		}
	}

	// Download file
	public static <T> void downoadFile(WebDriver driver, T downloadButton) throws Exception {
		WebElement ele = null;
		if(downloadButton.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) downloadButton);
			ele = driver.findElement((By)downloadButton);
		} else {
			ele = ((WebElement)downloadButton);
		}
		ele.click();   
		Thread.sleep(2000);   
		Robot robot = new Robot();   
		//For clicking on the Ok button on the dialog box   
		robot.keyPress(KeyEvent.VK_ENTER);    
		robot.keyRelease(KeyEvent.VK_ENTER);    
		Thread.sleep(2000);   
		//For clicking on Ok button on the dialog box which appears(not necessary)   
		//while saving file in a specific location.   
		robot.keyPress(KeyEvent.VK_ENTER);    
		robot.keyRelease(KeyEvent.VK_ENTER);    
		Thread.sleep(2000);   
		//For navigating to Yes button,if a prompt says that the file already   
		//exists in the location   
		robot.keyPress(KeyEvent.VK_LEFT);    
		robot.keyRelease(KeyEvent.VK_LEFT);    
		Thread.sleep(2000);   
		//For clicking on Ok button   
		robot.keyPress(KeyEvent.VK_ENTER);    
		robot.keyRelease(KeyEvent.VK_ENTER);    
	}

	// Download file 2
	public static void downloadFile(String href, String fileName)	throws Exception {
		URL url = null;
		URLConnection con = null;
		int i;
		url = new URL(href);
		con = url.openConnection();
		File file = new File(".//OutputData//" + fileName);
		BufferedInputStream bis = new BufferedInputStream(con.getInputStream());
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(file));
		while ((i = bis.read()) != -1) {
			bos.write(i);
		}
		bos.flush();
		bis.close();
	}

	public static void navigateToEveryLinkInPage(WebDriver driver) throws InterruptedException {
		List<WebElement> linksize = driver.findElements(By.tagName("a"));
		int linksCount = linksize.size();
		Reporter.log("Total no of links Available: " + linksCount, true);
		String[] links = new String[linksCount];
		Reporter.log("List of links Available: ", true);
		// print all the links from WebPage
		for (int i = 0; i < linksCount; i++) {
			links[i] = linksize.get(i).getAttribute("href");
			Reporter.log(linksize.get(i).getAttribute("href"), true);
		}
		// navigate to each Link on the WebPage
		for (int i = 0; i < linksCount; i++) {
			driver.navigate().to(links[i]);
			Thread.sleep(3000);
			Reporter.log(driver.getTitle(), true);
		}
	}

	// AjaxCall wait
	public static void waitForAjax(WebDriver driver) {
		new WebDriverWait(driver, 180).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				JavascriptExecutor js = (JavascriptExecutor) driver;
				return (Boolean) js.executeScript("return jQuery.active == 0");
			}
		});
	}

	// Fluent wait
	@SuppressWarnings("deprecation")
	public static <T> void fluentWait(WebDriver driver, T elementAttr, int timeoutInSeconds){
		WebElement ele = null;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			ele = driver.findElement((By)elementAttr);
		} else {
			ele = ((WebElement)elementAttr);
		}
		new FluentWait<WebDriver>(driver)
		.withTimeout(30, TimeUnit.SECONDS)
		.pollingEvery(1, TimeUnit.SECONDS)
		.ignoring(NoSuchElementException.class)
		.ignoring(StaleElementReferenceException.class)
		.until(ExpectedConditions.visibilityOf(ele));
	}

	// Returns all options from dropdown
	public static <T> List<WebElement> getAllOptionsFromTheDropdown(WebDriver driver, T elementAttr) {
		WebElement ele = null;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			ele = driver.findElement((By)elementAttr);
		} else {
			ele = ((WebElement)elementAttr);
		}
		Select s = new Select(ele);
		List<WebElement> options = s.getOptions();
		return options;
	}

	// Returns current time
	public static long getTime() {
		return System.currentTimeMillis();
	}

	// Returns popup windows 
	public static String getWindowHandle(WebDriver driver) {
		return driver.getWindowHandle();
	}

	// Returns set of windows
	public static Set<String> getWindowHandles(WebDriver driver) {
		return driver.getWindowHandles();
	}

	// Scroll to bottom of the page
	public static void scrollBottom(WebDriver driver) {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(0,document.body.scrollHeight); return true");
		try { Thread.sleep(1000); }catch(Exception e){}
	}

	// Scroll down the page as per the given number
	public static void scrollBottomByChoice(WebDriver driver, int noOfTimes) throws Exception {
		for (int i = 1; i <= noOfTimes; i++) {
			scrollBottom(driver);
			Thread.sleep(1000);
		}
	}

	// Scroll to top of page
	public static void scrollTop(WebDriver driver) {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(document.body.scrollHeight,0); return true");
		try {Thread.sleep(1000); }catch(Exception e){}
	}

	// Scroll to center of the page
	public static void scrollCenter(WebDriver driver) {
		JavascriptExecutor js = ((JavascriptExecutor) driver);
		js.executeScript("window.scrollTo(0,document.body.scrollHeight/2); return true");
		try { Thread.sleep(1000); }catch(Exception e){}
	}

	// Scroll page wrt xy coordinates
	public static void scrollPage(WebDriver driver, int xValue, int yValue) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("window.scrollBy(" + xValue + "," + yValue + ")", "");
		try { Thread.sleep(1000); }catch(Exception e){}
	}

	// Select from given list of option
	public static void selectFromListBox(List<WebElement> elementList, String selection) {
		try {
			for (WebElement element : elementList) {
				if (element.getText().trim().equalsIgnoreCase(selection)) {
					element.click();
					break;
				}
			}
		} catch (Exception e) {
			Reporter.log("Error : [" + selection + "] is not present in the list box or list box contains no value : "
					+ e.getLocalizedMessage(), true);
		}
	}

	// Adjust Browser Zoom
	public static void adjustBrowserZoom(WebDriver driver, int zoom) {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("document.body.style.zoom = '"+zoom+"%';");
	}

	// Ajax Utils================================================================================
	public static <T> void ajaxJsClick(WebDriver driver, T elementAttr){
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxJsType(WebDriver driver, T elementAttr, String value){
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'", ele);
			ele.sendKeys(Keys.TAB);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'", ele);
			ele.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxSendKeys(WebDriver driver, T elementAttr, int timeout, String value) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(ele));
			ele.sendKeys(value);
			ele.sendKeys(Keys.TAB);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOf(ele));
			ele.sendKeys(value);
			ele.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxType(WebDriver driver, T elementAttr, String value) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.sendKeys(value);
			ele.sendKeys(Keys.TAB);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.sendKeys(value);
			ele.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxClearAndType(WebDriver driver, T elementAttr, String value) throws Exception {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.clear();
			ele.sendKeys(value);
			ele.sendKeys(Keys.TAB);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.clear();
			ele.sendKeys(value);
			ele.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxClickClearAndType(WebDriver driver, T elementAttr1, T elementAttr2, String value) throws Exception {
		if(elementAttr1.getClass().getName().contains("By") && elementAttr2.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr1);
			WebElement ele1 = driver.findElement((By)elementAttr1);
			WebElement ele2 = driver.findElement((By)elementAttr2);
			ele1.click();
			Thread.sleep(500);
			ele2.clear();
			ele2.sendKeys(value);
			ele2.sendKeys(Keys.TAB);
		} else {
			WebElement ele1 = ((WebElement)elementAttr1);
			WebElement ele2 = ((WebElement)elementAttr2);
			ele1.click();
			Thread.sleep(500);
			ele2.clear();
			ele2.sendKeys(value);
			ele2.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	// Click, select inner text & type - Hidden textbox
	public static <T> void ajaxClickSelectAndType(WebDriver driver, T eleAttr1, T eleAttr2, String value) throws Exception {
		if(eleAttr1.getClass().getName().contains("By") && eleAttr2.getClass().getName().contains("By")) {
			WebElement ele1 = driver.findElement((By)eleAttr1);
			WebElement ele2 = driver.findElement((By)eleAttr2);
			ele1.click();
			ele2.sendKeys(Keys.CONTROL+"a");
			ele2.sendKeys(value);
			ele2.sendKeys(Keys.TAB);
		} else {
			WebElement ele1 = ((WebElement)eleAttr1);
			WebElement ele2 = ((WebElement)eleAttr2);
			ele1.click();
			ele2.sendKeys(Keys.CONTROL+"a");
			ele2.sendKeys(value);
			ele2.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxClickClearAndType(WebDriver driver, T elementAttr, String value) throws Exception {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			ele.click();
			Thread.sleep(500);
			ele.clear();
			ele.sendKeys(value);
			ele.sendKeys(Keys.TAB);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			ele.click();
			Thread.sleep(500);
			ele.clear();
			ele.sendKeys(value);
			ele.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxJsClickClearAndType(WebDriver driver, T elementAttr1, T elementAttr2, String value) throws Exception {
		if(elementAttr1.getClass().getName().contains("By") && elementAttr2.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr1);
			WebElement ele1 = driver.findElement((By)elementAttr1);
			WebElement ele2 = driver.findElement((By)elementAttr2);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele1);
			Thread.sleep(500);
			ele2.clear();
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele2);
			ele2.sendKeys(Keys.TAB);
		} else {
			WebElement ele1 = ((WebElement)elementAttr1);
			WebElement ele2 = ((WebElement)elementAttr2);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele1);
			Thread.sleep(500);
			ele2.clear();
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele2);
			ele2.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	// JavaScript - click, select inner text & type - Hidden textbox 
	public static <T> void ajaxJsClickSelectAndType(WebDriver driver, T eleAttr1, T eleAttr2, String value) throws Exception {
		if(eleAttr1.getClass().getName().contains("By") && eleAttr2.getClass().getName().contains("By")) {
			WebElement ele1 = driver.findElement((By)eleAttr1);
			WebElement ele2 = driver.findElement((By)eleAttr2);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele1);
			ele2.sendKeys(Keys.CONTROL+"a");
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele2);
			ele2.sendKeys(Keys.TAB);
		} else {
			WebElement ele1 = ((WebElement)eleAttr1);
			WebElement ele2 = ((WebElement)eleAttr2);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele1);
			ele2.sendKeys(Keys.CONTROL+"a");
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele2);
			ele2.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxJsClickClearAndType(WebDriver driver, T elementAttr, String value) throws Exception {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
			Thread.sleep(500);
			ele.clear();
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele);
			ele.sendKeys(Keys.TAB);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
			Thread.sleep(500);
			ele.clear();
			((JavascriptExecutor)driver).executeScript("arguments[0].value='"+value+"'",ele);
			ele.sendKeys(Keys.TAB);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxClickOn(WebDriver driver, T elementAttr, int timeout) {
		if (elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			new WebDriverWait(driver, timeout).
			until(ExpectedConditions.elementToBeClickable(ele));
			ele.click();
		} else {
			WebElement ele = ((WebElement)elementAttr);
			new WebDriverWait(driver, timeout).
			until(ExpectedConditions.elementToBeClickable(ele));
			ele.click();
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxClick(WebDriver driver, T elementAttr) {
		if (elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			try {
				(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(ele));
				ele.click();
			}
			catch (StaleElementReferenceException  e) {
				// simply retry finding the element in the refreshed DOM
				ele.click();
			}
			catch (WebDriverException e) {
				ele.click();
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			try {
				(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(ele));
				ele.click();
			}
			catch (StaleElementReferenceException  e) {
				// simply retry finding the element in the refreshed DOM
				ele.click();
			}
			catch (WebDriverException e) {
				ele.click();
			}
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxActionClick(WebDriver driver, T elementAttr) {
		Actions act = new Actions(driver);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			act.moveToElement(ele).click().build().perform();
		} else {
			WebElement ele = ((WebElement)elementAttr);
			act.moveToElement(ele).click().build().perform();
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxActionClickAtPartOfElementWrtXY(WebDriver driver, T elementAttr, int x, int y) {
		Actions action= new Actions(driver);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			action.moveToElement(ele).moveByOffset(x, y).click().build().perform();
		} else {
			WebElement ele = ((WebElement)elementAttr);
			action.moveToElement(ele).moveByOffset(x, y).click().build().perform();
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxStaleElementClick(WebDriver driver, T elementAttr) {
		for(int i=0; i<=3;i++){
			try{
				if(elementAttr.getClass().getName().contains("By")) {
					waitForElementPresent(driver, (By) elementAttr);
					WebElement ele = driver.findElement((By)elementAttr);
					ele.click();
				} else {
					WebElement ele = ((WebElement)elementAttr);
					ele.click();
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxClickMultipleElements(WebDriver driver, T someElement, T someOtherElement) {
		Actions builder = new Actions(driver);
		if(someElement.getClass().getName().contains("By") && someOtherElement.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) someElement);
			WebElement ele1 = driver.findElement((By)someElement);
			WebElement ele2 = driver.findElement((By)someOtherElement);
			builder.keyDown(Keys.CONTROL).click(ele1)
			.click(ele2).keyUp(Keys.CONTROL).build().perform();
		} else {
			WebElement ele1 = ((WebElement)someElement);
			WebElement ele2 = ((WebElement)someOtherElement);
			builder.keyDown(Keys.CONTROL).click(ele1)
			.click(ele2).keyUp(Keys.CONTROL).build().perform();
		}
		waitForAjax(driver);
	}

	public static boolean ajaxCheckAlert_Accept(WebDriver driver) {
		try {
			Alert a = driver.switchTo().alert();
			String str = a.getText();
			Reporter.log("Alert message is: "+str, true);
			a.accept();
			waitForAjax(driver);
			return true;
		} catch (Exception e) {
			Reporter.log("No alert present", true);
			return false;
		}
	}

	public static boolean ajaxCheckAlert_Dismiss(WebDriver driver) {
		try {
			Alert a = driver.switchTo().alert();
			String str = a.getText();
			Reporter.log("Alert message is: "+str, true);
			a.dismiss();
			waitForAjax(driver);
			return true;
		} catch (Exception e) {
			Reporter.log("No alert present", true);
			return false;
		}
	}

	public static <T> void ajaxCheckbox_Checking(WebDriver driver, T elementAttr) {
		boolean checkstatus;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				Reporter.log("Checkbox is already checked", true);
			} else {
				ele.click();
				Reporter.log("Checked the checkbox", true);
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				Reporter.log("Checkbox is already checked", true);
			} else {
				ele.click();
				Reporter.log("Checked the checkbox", true);
			}
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxRadiobutton_Select(WebDriver driver, T elementAttr) {
		boolean checkstatus;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				Reporter.log("RadioButton is already checked", true);
			} else {
				ele.click();
				Reporter.log("Selected the Radiobutton", true);
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				Reporter.log("RadioButton is already checked", true);
			} else {
				ele.click();
				Reporter.log("Selected the Radiobutton", true);
			}
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxCheckbox_Unchecking(WebDriver driver, T elementAttr) {
		boolean checkstatus;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				ele.click();
				Reporter.log("Checkbox is unchecked", true);
			} else {
				Reporter.log("Checkbox is already unchecked", true);
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				ele.click();
				Reporter.log("Checkbox is unchecked", true);
			} else {
				Reporter.log("Checkbox is already unchecked", true);
			}
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxRadioButton_Deselect(WebDriver driver, T elementAttr) {
		boolean checkstatus;
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				ele.click();
				Reporter.log("Radio Button is deselected", true);
			} else {
				Reporter.log("Radio Button was already Deselected", true);
			}
		} else {
			WebElement ele = ((WebElement)elementAttr);
			checkstatus = ele.isSelected();
			if (checkstatus == true) {
				ele.click();
				Reporter.log("Radio Button is deselected", true);
			} else {
				Reporter.log("Radio Button was already Deselected", true);
			}
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxDragAndDrop(WebDriver driver, T fromWebElementAttr, T toWebElementAttr) {
		Actions builder = new Actions(driver);
		if(fromWebElementAttr.getClass().getName().contains("By") && toWebElementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) fromWebElementAttr);
			WebElement ele1 = driver.findElement((By)fromWebElementAttr);
			WebElement ele2 = driver.findElement((By)toWebElementAttr);
			builder.dragAndDrop(ele1, ele2).perform();
		} else {
			WebElement ele1 = ((WebElement)fromWebElementAttr);
			WebElement ele2 = ((WebElement)toWebElementAttr);
			builder.dragAndDrop(ele1, ele2).perform();
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxDragAndDrop_Method2(WebDriver driver, T fromWebElementAttr, T toWebElementAttr) {
		Actions builder = new Actions(driver);
		if(fromWebElementAttr.getClass().getName().contains("By") && toWebElementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) fromWebElementAttr);
			WebElement ele1 = driver.findElement((By)fromWebElementAttr);
			WebElement ele2 = driver.findElement((By)toWebElementAttr);
			Action dragAndDrop = builder.clickAndHold(ele1).moveToElement(ele2).release(ele2).build();
			dragAndDrop.perform();
		} else {
			WebElement ele1 = ((WebElement)fromWebElementAttr);
			WebElement ele2 = ((WebElement)toWebElementAttr);
			Action dragAndDrop = builder.clickAndHold(ele1).moveToElement(ele2).release(ele2).build();
			dragAndDrop.perform();
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxDragAndDrop_Method3(WebDriver driver, T fromWebElementAttr, T toWebElementAttr) throws InterruptedException {
		Actions builder = new Actions(driver);
		if(fromWebElementAttr.getClass().getName().contains("By") && toWebElementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) fromWebElementAttr);
			WebElement ele1 = driver.findElement((By)fromWebElementAttr);
			WebElement ele2 = driver.findElement((By)toWebElementAttr);
			builder.clickAndHold(ele1).moveToElement(ele2).perform();
			Thread.sleep(2000);
			builder.release(ele2).build().perform();
		} else {
			WebElement ele1 = ((WebElement)fromWebElementAttr);
			WebElement ele2 = ((WebElement)toWebElementAttr);
			builder.clickAndHold(ele1).moveToElement(ele2).perform();
			Thread.sleep(2000);
			builder.release(ele2).build().perform();
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxDoubleClickWebelement(WebDriver driver, T elementAttr)throws InterruptedException {
		Actions builder = new Actions(driver);
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			builder.doubleClick(ele).perform();
			Thread.sleep(2000);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			builder.doubleClick(ele).perform();
			Thread.sleep(2000);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxSelectElementByVisibleText(WebDriver driver, T elementAttr, String Name) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByVisibleText(Name);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByVisibleText(Name);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxSelectElementByValue(WebDriver driver, T elementAttr, String value) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByValue(value);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByValue(value);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxSelectElementByIndex(WebDriver driver, T elementAttr, int index) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByIndex(index);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			Select selectitem = new Select(ele);
			selectitem.selectByIndex(index);
		}
		waitForAjax(driver);
	}

	public static void ajaxClickCheckboxFromList(WebDriver driver, String xpathOfElement, String valueToSelect) {
		List<WebElement> lst = driver.findElements(By.xpath(xpathOfElement));
		for (int i = 0; i < lst.size(); i++) {
			List<WebElement> dr = lst.get(i).findElements(By.tagName("label"));
			for (WebElement f : dr) {
				Reporter.log("value in the list : " + f.getText(),true);
				if (valueToSelect.equals(f.getText())) {
					f.click();
					waitForAjax(driver);
					break;
				}
			}
		}
	}

	public static <T> void ajaxUploadFile(WebDriver driver, T browseButton, String filePath) throws Exception {
		if(browseButton.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) browseButton);
			WebElement ele = driver.findElement((By)browseButton);
			StringSelection sel2 = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel2,null);
			Reporter.log("Selection: "+sel2, true);
			Thread.sleep(1000);
			ele.click();
			Reporter.log("Browse button clicked", true);
			Robot robot2 = new Robot();
			Thread.sleep(2000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			robot2.keyPress(KeyEvent.VK_CONTROL);
			robot2.keyPress(KeyEvent.VK_V);
			robot2.keyRelease(KeyEvent.VK_CONTROL);
			robot2.keyRelease(KeyEvent.VK_V);
			Thread.sleep(1000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		} else {
			WebElement ele = ((WebElement)browseButton);
			StringSelection sel2 = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel2,null);
			Reporter.log("Selection: "+sel2, true);
			Thread.sleep(1000);
			ele.click();
			Reporter.log("Browse button clicked", true);
			Robot robot2 = new Robot();
			Thread.sleep(2000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			robot2.keyPress(KeyEvent.VK_CONTROL);
			robot2.keyPress(KeyEvent.VK_V);
			robot2.keyRelease(KeyEvent.VK_CONTROL);
			robot2.keyRelease(KeyEvent.VK_V);
			Thread.sleep(1000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxUploadFile2(WebDriver driver, T browseButton, String filePath) throws Exception {
		if(browseButton.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) browseButton);
			WebElement ele = driver.findElement((By)browseButton);
			StringSelection sel2 = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel2,null);
			Reporter.log("Selection: "+sel2, true);
			Thread.sleep(1000);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
			Reporter.log("Browse button clicked", true);
			Robot robot2 = new Robot();
			Thread.sleep(2000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			robot2.keyPress(KeyEvent.VK_CONTROL);
			robot2.keyPress(KeyEvent.VK_V);
			robot2.keyRelease(KeyEvent.VK_CONTROL);
			robot2.keyRelease(KeyEvent.VK_V);
			Thread.sleep(1000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		} else {
			WebElement ele = ((WebElement)browseButton);
			StringSelection sel2 = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel2,null);
			Reporter.log("Selection: "+sel2, true);
			Thread.sleep(1000);
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", ele);
			Reporter.log("Browse button clicked", true);
			Robot robot2 = new Robot();
			Thread.sleep(2000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			robot2.keyPress(KeyEvent.VK_CONTROL);
			robot2.keyPress(KeyEvent.VK_V);
			robot2.keyRelease(KeyEvent.VK_CONTROL);
			robot2.keyRelease(KeyEvent.VK_V);
			Thread.sleep(1000);
			robot2.keyPress(KeyEvent.VK_ENTER);
			robot2.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(1000);
		}
		waitForAjax(driver);
	}

	public static <T> void ajaxScrollIntoView(WebDriver driver, T elementAttr) {
		if(elementAttr.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) elementAttr);
			WebElement ele = driver.findElement((By)elementAttr);
			JavascriptExecutor js=(JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", ele);
		} else {
			WebElement ele = ((WebElement)elementAttr);
			JavascriptExecutor js=(JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", ele);
		}
		waitForAjax(driver);
	}

	public static void ajaxScrollPageWrtXY(WebDriver driver, int x, int y) {
		((JavascriptExecutor)driver).executeScript("scroll(" +x+ "," +y+ ")");
		waitForAjax(driver);
	}

	public static <T> void ajaxCheck(WebDriver driver, T checkBox) {
		if(checkBox.getClass().getName().contains("By")) {
			waitForElementPresent(driver, (By) checkBox);
			WebElement ele = driver.findElement((By)checkBox);
			if (!ele.isSelected())
				ele.click();
		} else {
			WebElement ele = ((WebElement)checkBox);
			if (!ele.isSelected())
				ele.click();
		}
		waitForAjax(driver);
	}	































}
