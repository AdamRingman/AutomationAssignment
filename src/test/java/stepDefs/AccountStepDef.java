package stepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountStepDef {

    private WebDriver driver;
    private WebDriverWait wait;

    private String randomOutput(int lengthOfOutput) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        String Output = "";
        Random rnd = new Random();
        for (int i = 0; i < lengthOfOutput; i++) {
            int c = (int) (rnd.nextFloat() * chars.length());
            Output += chars.charAt(c);
        }
        return Output;
    }

    @Given("i have chosen chrome")
    public void iHaveChosenChrome() {
        System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        driver.get("https://login.mailchimp.com/signup/");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("i have chosen edge")
    public void iHaveChosenEdge() {
        System.setProperty("webdriver.edge.driver", "C:\\Selenium\\msedgedriver.exe");
        EdgeOptions option = new EdgeOptions();
        option.setCapability("ms:inPrivate", true);
        driver = new EdgeDriver(option);

        driver.get("https://login.mailchimp.com/signup/");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Given("i have entered a email address {string}")
    public void iHaveEnteredAEmailAddress(String email) {
        String emailInput = "";
        if (email.equalsIgnoreCase("random")) {
            emailInput = randomOutput(20) + "@gmail.com";
        } else if (email.equalsIgnoreCase("blank")) {
            emailInput = "";
        }
        WebElement emailBox = driver.findElement(By.name("email"));
        emailBox.sendKeys(emailInput);
    }

    @Given("i have entered a username {string}")
    public void iHaveEnteredAUsername(String username) {
        String usernameInput = "";
        if (username.equalsIgnoreCase("Random")) {
            usernameInput = randomOutput(20);
        } else if (username.equalsIgnoreCase("TooLong")) {
            usernameInput = randomOutput(105);
        } else if (username.equalsIgnoreCase("Used")) {
            usernameInput = "Username";
        }
        WebElement usernameBox = driver.findElement(By.id("new_username"));
        usernameBox.click();
        usernameBox.clear();
        usernameBox.sendKeys(usernameInput);
    }

    @Given("i have entered a password {string}")
    public void iHaveEnteredAPassword(String password) {
        WebElement passwordBox = driver.findElement(By.id("new_password"));
        passwordBox.sendKeys(password);
    }

    @When("i click the sign up button")
    public void iClickTheSignUpButton() {
        Actions action = new Actions(driver);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("create-account-enabled")));
        WebElement signUpButton = driver.findElement(By.id("create-account-enabled"));
        action.moveToElement(signUpButton).perform();
        signUpButton.click();
    }

    @Then("i create an account {string}")
    public void iCreateAnAccount(String result) {
        String actual = "";
        String expected = result;

        if (result.equalsIgnoreCase("Pass")) {
            wait.until(ExpectedConditions.urlContains("success"));
            if (driver.getCurrentUrl().contains("success")) {
                actual = "Pass";
            }
        } else {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("invalid-error")));
            WebElement errorText = driver.findElement(By.className("invalid-error"));
            if (errorText.getText().contains("An email address must contain a single @.")) {
                actual = "BlankEmail";
            } else if (errorText.getText().contains("Enter a value less than 100 characters long")) {
                actual = "CharLimit";
            } else if (errorText.getText().contains("Great minds think alike - someone already has this username.")) {
                actual = "AlreadyInUse";
            }
        }
        assertEquals(actual, expected);
        driver.close();
    }
}