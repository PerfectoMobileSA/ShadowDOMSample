### ShadowDOMSample
This sample project is designed to help you interact with Shadow DOM elements.

Begin with installing the dependencies below, and continue with the Getting Started procedure below.

### Dependencies
There are several prerequisite dependencies you should install on your machine prior to starting to work with this project:

* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

* An IDE to write your tests on - [Eclipse](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/marsr) or [IntelliJ](https://www.jetbrains.com/idea/download/#)

* [Maven](https://maven.apache.org/)


Eclipse users should also install:

1. [Maven Plugin](http://marketplace.eclipse.org/content/m2e-connector-maven-dependency-plugin)

2. [TestNG Plugin](http://testng.org/doc/download.html)

IntelliJ IDEA users should also install:

1. [Maven Plugin for IDEA](https://plugins.jetbrains.com/plugin/1166)

TestNG Plugin is built-in in the IntelliJ IDEA, from version 7 onwards.
 
#### Optional Installations
* For source control management, you can install [git](https://git-scm.com/downloads).

## Downloading the Sample Project

* [Clone](https://github.com/PerfectoMobileSA/ShadowDOMSample.git) the repository.

* After downloading and unzipping the project to your computer, open it from your IDE by choosing the folder containing the pom.xml 


## Running sample as is

* Open testng.xml file.</p>

* Add cloud URL in "cloudUrl" parameter.</br>  
		
* Add Security Token in "securityToken" parameter.</br>  

Note: Refer to official documentation on how to execute from eclipse / IntelliJ. </br>
* Run pom.xml with the below maven goals & properties when: </p>
   a. If credentials are hardcoded:
		
		clean
		install
		
</p>

* Maven will automatically kick start the parallel execution of different examples inside perfecto package in parallel. </p>

## Shadow DOM methods:

* We are using [Shop Polymer](https://shop.polymer-project.org/) website as an example in this sample project.

### findElementShadowDOM:
This method finds first web element matching the CSS selector and returns it.

### Example: -
    WebElement parentShadowElement = driver.findElement(By.cssSelector("[page='home']"));
    Map<String, Object> params = new HashMap<>();
    params.put("parentElement", parentShadowElement);
    params.put("innerSelector", "div#tabContainer shop-tabs shop-tab:nth-child(1) a");
    WebElement innerDOMElement1 = findElementShadowDOM(((RemoteWebDriver)driver), params);

### Parameters:
 | Name | Type | Description |
  | --- | --- | ------ |
  | parentElement(Mandatory) | WebElement | The shadow-root web element object |
  | innerSelector (Mandatory) | String | CSS-selector of the shadow DOM inner element to search for |
 
### findElementsShadowDOM:
This method finds all the web elements matching the CSS selector and returns the list.

### Example: -
    WebElement parentShadowElement = driver.findElement(By.cssSelector("[page='home']"));
    Map<String, Object> params = new HashMap<>();
    params.put("parentElement", parentShadowElement);
    params.put("innerSelector", "div#tabContainer shop-tabs shop-tab a");
    List<WebElement> allMatchingLinks = findElementsShadowDOM(((RemoteWebDriver)driver), params);

### Parameters:

  | Name | Type | Description |
  | --- | --- | ------ |
  | parentElement(Mandatory) | WebElement | The shadow-root web element object |
  | innerSelector (Mandatory) | String | CSS-selector of the shadow DOM inner element to search for |

### clickElementShadowDOM:
This method performs click operation on the first matching element of the specified CSS locator.

### Example: -
    WebElement parentShadowElement = driver.findElement(By.cssSelector("[page='home']"));
    Map<String, Object> params = new HashMap<>();
    params.put("parentElement", parentShadowElement);
    params.put("innerSelector", "div#tabContainer shop-tabs shop-tab:nth-child(1) a");
    WebElement innerDOMElement1 = clickElementShadowDOM(((RemoteWebDriver)driver), params);

### Parameters:

  | Name | Type | Description |
  | --- | --- | ------ |
  | parentElement(Mandatory) | WebElement | The shadow-root web element object |
  | innerSelector (Mandatory) | String | CSS-selector of the shadow DOM inner element to search for |

### sendKeysShadowDOM:
This method enters text on the first matching element of the specified CSS locator.

### Example: -
    WebElement parentShadowElement = driver.findElement(By.cssSelector("[page='home']"));
    Map<String, Object> params = new HashMap<>();
    params.put("parentElement", parentShadowElement);
    params.put("innerSelector", "div#tabContainer input#id1");
    params.put("characterSequence", "Text to enter");
    WebElement innerDOMElement1 = clickElementShadowDOM(((RemoteWebDriver)driver), params);

### Parameters:
  | Name | Type | Description |
  | --- | --- | ------ |
  | parentElement(Mandatory) | WebElement | The shadow-root web element object |
  | innerSelector (Mandatory) | String | CSS-selector of the shadow DOM inner element to search for |
  | characterSequence (Mandatory) | String | The text to insert in the edit field |

### getTextShadowDOM:
This method returns the text of the provided CSS locator matching Webelement.

### Example: -
    WebElement homeEleShadowParent = driver.findElement(By.cssSelector("[page='home']"));
    Map<String, Object> params = new HashMap<>();
    params.put("parentElement", homeEleShadowParent);
    params.put("innerSelector", "div#tabContainer shop-tabs shop-tab:nth-child(1) a");
    String elementText = getTextShadowDOM(((RemoteWebDriver)driver), params);

### Parameters:
  | Name | Type | Description |
  | --- | --- | ------ |
  | parentElement(Mandatory) | WebElement | The shadow-root web element object |
  | innerSelector (Mandatory) | String | CSS-selector of the shadow DOM inner element to search for |

### getAttributeShadowDOM:
This method returns the value of the given attribute of the provided CSS locator matching Webelement.

### Example: -
    WebElement homeEleShadowParent = driver.findElement(By.cssSelector("[page='home']"));
    Map<String, Object> params = new HashMap<>();
    params.put("parentElement", homeEleShadowParent);
    params.put("innerSelector", "div#tabContainer shop-tabs shop-tab:nth-child(1) a");
    params.put("attribute", "href");		
    String linkURL = getAttributeShadowDOM(((RemoteWebDriver)driver), params);

### Parameters:
  | Name | Type | Description |
  | --- | --- | ------ |
  | parentElement(Mandatory) | WebElement | The shadow-root web element object |
  | innerSelector (Mandatory) | String | CSS-selector of the shadow DOM inner element to search for |
  | attribute (Mandatory) | String | Name of the attribute |

## Working with Nested Shadow DOM Elements:
* Working with nested shadow DOM elements is similar to working with single level shadow DOM elements.
* First, we will try to get the WebElement of the Shadow-root using findElementShadowDOM method, Now provide this root WebElement as a “parentElement” to next root element, and proceed until we reach the final Shadow Root element.
### Example:
    WebElement parentShadowEle = driver.findElement(By.xpath("//vt-virustotal-app"));	
    Map<String, Object> params = new HashMap<>();
    params.put("parentElement", parentShadowEle);
    params.put("innerSelector", "home-view.iron-selected");
    WebElement innerDOMElement1 = findElementShadowDOM(driver, params);

    Map<String, Object> params2 = new HashMap<>();
    params2.put("parentElement", innerDOMElement1);
    params2.put("innerSelector", "#urlSearchInput");
    WebElement innerDOMElement2 = findElementShadowDOM(driver, params);

    Map<String, Object> params3 = new HashMap<>();
    params3.put("parentElement", innerDOMElement2);
    params3.put("innerSelector", "input#input");
    params3.put("characterSequence", "Text to Enter");
    sendKeyShadowDOM(driver, params);

Kindly reach out to [Professional Services](https://www.perfecto.io/services/professional-services-implementation) team of Perfecto to implement this in your Organization.
