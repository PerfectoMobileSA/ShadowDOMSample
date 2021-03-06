package com.perfecto.shadowDOM;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.perfecto.reportium.client.ReportiumClient;
import com.perfecto.reportium.client.ReportiumClientFactory;
import com.perfecto.reportium.model.Job;
import com.perfecto.reportium.model.PerfectoExecutionContext;
import com.perfecto.reportium.model.Project;
import com.perfecto.reportium.test.TestContext;
import com.perfecto.reportium.test.result.TestResultFactory;
import com.perfecto.utils.ShadowDomUtils;
import com.perfecto.utils.Utils;

public class TestShadowDOM {

	WebDriver driver = null;
	ReportiumClient report = null;
	ITestContext context = null;
	ShadowDomUtils shadowUtls = new ShadowDomUtils();

	@Test
	public void testShadowDomActions() throws InterruptedException {
		driver.get("https://shop.polymer-project.org/");
		if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
			report.stepStart("Click Men's Fashion option");

		if(context.getCurrentXmlTest().getParameter("deviceType").equalsIgnoreCase("web")) {
			WebElement homeEleShadowParent = driver.findElement(By.cssSelector("html > body:nth-of-type(1) > shop-app:nth-of-type(1)"));
			Map<String, Object> params = new HashMap<>();
			params.put("parentElement", homeEleShadowParent);
			params.put("innerSelector", "div#tabContainer shop-tabs shop-tab:nth-child(1) a");
			WebElement innerDOMElement1 = shadowUtls.findElementShadowDOM(((RemoteWebDriver)driver), params);
			params.put("attribute", "href");
			System.out.println(shadowUtls.getAttributeShadowDOM(((RemoteWebDriver)driver), params));
			((JavascriptExecutor)driver).executeScript("arguments[0].click();", innerDOMElement1);
		}else {
			WebElement homeEleShadowParent = ((RemoteWebDriver)driver).findElement(By.cssSelector("html > body:nth-of-type(1) > shop-app:nth-of-type(1)"));
			Map<String, Object> params = new HashMap<>();
			params.put("parentElement", homeEleShadowParent);
			params.put("innerSelector", "shop-home");
			WebElement innerDOMElement1 = shadowUtls.findElementShadowDOM(((RemoteWebDriver)driver), params);

			Map<String, Object> params2 = new HashMap<>();
			params2.put("parentElement", innerDOMElement1);
			params2.put("innerSelector", "shop-button a");
			shadowUtls.clickElementShadowDOM(((RemoteWebDriver)driver), params2);
		}

		if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
			report.stepStart("Find parent shadow root element");
		WebElement listShadowParent = ((RemoteWebDriver)driver).findElement(By.cssSelector("[page='list']"));
		Map<String, Object> params2 = new HashMap<>();
		params2.put("parentElement", listShadowParent);
		params2.put("innerSelector", "iron-pages shop-list.iron-selected");
		WebElement innerDOMElement2 = shadowUtls.findElementShadowDOM(((RemoteWebDriver)driver), params2);


		if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
			report.stepStart("Verify Inner shadow root element");
		Map<String, Object> params3 = new HashMap<>();
		params3.put("parentElement", innerDOMElement2);
		params3.put("innerSelector", "header span");
		if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
			report.reportiumAssert("Verify Men's wear items", shadowUtls.getTextShadowDOM(((RemoteWebDriver)driver), params3).equals("(16 items)"));

	}

	@Test
	public void testShadowInputText() throws InterruptedException {
		driver.get("https://www.google.com");
		report.reportiumAssert("Google page Validation.", driver.findElement(By.cssSelector("#hplogo")).isDisplayed());
		
		driver.get("https://www.virustotal.com/gui/home/url");

		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement parentShadowEle = wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//vt-virustotal-app"))));

		Map<String, Object> params = new HashMap<>();
		params.put("parentElement", parentShadowEle);
		params.put("innerSelector", "home-view.iron-selected");
		WebElement innerDOMElement1 = shadowUtls.findElementShadowDOM(((RemoteWebDriver)driver), params);

		Map<String, Object> params2 = new HashMap<>();
		params2.put("parentElement", innerDOMElement1);
		params2.put("innerSelector", "#urlSearchInput");
		WebElement innerDOMElement2 = shadowUtls.findElementShadowDOM(((RemoteWebDriver)driver), params2);

		Map<String, Object> params3 = new HashMap<>();
		params3.put("parentElement", innerDOMElement2);
		params3.put("innerSelector", "input#input");
		params3.put("characterSequence", "https://www.google.com");
		shadowUtls.sendKeysShadowDOM((RemoteWebDriver)driver, params3);

		Thread.sleep(2000);
		WebElement urlTextBox = shadowUtls.findElementShadowDOM(((RemoteWebDriver)driver), params3);
		String output = (String)((JavascriptExecutor)driver).executeScript("return arguments[0].value", urlTextBox);
		System.out.println("Output"+output);
		if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
			report.reportiumAssert("Verify entered URL", output.equals("https://www.google.com"));
	}


	@BeforeClass
	public void beforeClass(ITestContext context) throws Exception {
		createDriver(context);
	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
			report.testStart(method.getName(), new TestContext("shadowDom"));
	}

	@AfterMethod
	public void afterMethod(ITestResult result) {
		if(result.getStatus()==ITestResult.SUCCESS) {
			if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
				report.testStop(TestResultFactory.createSuccess());
		}else if(result.getStatus()==ITestResult.FAILURE) {
			if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
				report.testStop(TestResultFactory.createFailure(result.getThrowable()));
		}else if(result.getStatus()==ITestResult.SKIP) {
			if(context.getCurrentXmlTest().getParameter("browser").equalsIgnoreCase("perfecto"))
				report.testStop(TestResultFactory.createFailure(result.getThrowable()));
		}
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
		String reportURL = report.getReportUrl();
		System.out.println(reportURL);
	}

	public void createDriver(ITestContext context) throws Exception {
		//Replace <<cloud name>> with your perfecto cloud name (e.g. demo) or pass it as maven properties: -DcloudName=<<cloud name>>  
		String cloudName = "<<cloud name>>";
		//Replace <<security token>> with your perfecto security token or pass it as maven properties: -DsecurityToken=<<SECURITY TOKEN>>  More info: https://developers.perfectomobile.com/display/PD/Generate+security+tokens
		String securityToken = "<<SECURITY TOKEN>>";
		this.context = context;
		String browser = context.getCurrentXmlTest().getParameter("browser").toUpperCase();
		switch (browser) {
		case "PERFECTO":
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability("platformName", context.getCurrentXmlTest().getParameter("platformName"));
			capabilities.setCapability("platformVersion", context.getCurrentXmlTest().getParameter("platformVersion"));
			capabilities.setCapability("model", context.getCurrentXmlTest().getParameter("model"));
			capabilities.setCapability("deviceName", context.getCurrentXmlTest().getParameter("deviceName"));
			capabilities.setCapability("securityToken",Utils.fetchSecurityToken(securityToken));
			capabilities.setCapability("browserName", context.getCurrentXmlTest().getParameter("browserName"));
			capabilities.setCapability("browserVersion",context.getCurrentXmlTest().getParameter("browserVersion"));
			capabilities.setCapability("location", context.getCurrentXmlTest().getParameter("location"));
			capabilities.setCapability("resolution",context.getCurrentXmlTest().getParameter("resolution"));
			capabilities.setCapability("deviceType",context.getCurrentXmlTest().getParameter("deviceType"));
			capabilities.setCapability("takesScreenshot", Boolean.parseBoolean(context.getCurrentXmlTest().getParameter("takesScreenshot")));
			driver = new RemoteWebDriver(new URL("https://" + Utils.fetchCloudName(cloudName) + ".perfectomobile.com/nexperience/perfectomobile/wd/hub"),capabilities);
			PerfectoExecutionContext perfectoExecutionContext;
			if(System.getProperty("reportium-job-name") != null) {
				perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
						.withProject(new Project("My Project", "1.0"))
						.withJob(new Job(System.getProperty("reportium-job-name") , Integer.parseInt(System.getProperty("reportium-job-number"))))
						.withContextTags("tag1")
						.withWebDriver(driver)
						.build();
			} else {
				perfectoExecutionContext = new PerfectoExecutionContext.PerfectoExecutionContextBuilder()
						.withProject(new Project("My Project", "1.0"))
						.withContextTags("tag1")
						.withWebDriver(driver)
						.build();
			}
			report = new ReportiumClientFactory().createPerfectoReportiumClient(perfectoExecutionContext);
			break;
		case "CHROME":
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");
			driver = new ChromeDriver();
			break;
		default:
			break;
		}
		if(context.getCurrentXmlTest().getParameter("deviceType").equalsIgnoreCase("web"))
			driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
	}

}
