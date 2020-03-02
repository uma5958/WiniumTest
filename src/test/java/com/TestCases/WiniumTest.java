package com.TestCases;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.winium.DesktopOptions;
import org.openqa.selenium.winium.WiniumDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.Util.ActionUtils;


public class WiniumTest {
	
	@Test
	public void Test1() throws Exception {
		DesktopOptions options = new DesktopOptions();
		options.setApplicationPath("C:\\Users\\Mahesh\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\RemotePC\\RemotePC.lnk");
		WiniumDriver driver = new WiniumDriver(new URL("http://localhost:9999"), options);
		
		driver.findElement(By.id("username")).sendKeys("naveenvarma.poranki+8@idrive.com");
		driver.findElement(By.id("password")).sendKeys("test22");
		
		ActionUtils.checkbox_Unchecking(driver, By.id("Remember_CheckBox"));
		driver.findElement(By.id("LoginButton")).click();
		
		Assert.assertTrue(driver.findElement(By.id("LogoutArrow")).isDisplayed());
		Reporter.log("Login successful", true);
		
		driver.findElement(By.id("LogoutArrow")).click();
		driver.findElement(By.name("Logout")).click();
		
		Assert.assertTrue(driver.findElement(By.id("username")).isDisplayed());
		Reporter.log("Logout successful", true);
		
		driver.quit();
	}
	
	
	

}
