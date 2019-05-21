package ui;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class Stepdefs {

    @Before
    public void beforeScenario() {
        Configuration.baseUrl = "http://test-app.d6.dev.devcaz.com";
    }

    @After
    public void afterScenario() {
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    @Given("I am on page {string}")
    public void i_am_on_page(String string) {
        open(string);
    }

    @Given("I fill Login with {string}")
    public void i_fill_Login_with(String string) {
        $("[placeholder='Login']").setValue(string);
    }

    @Given("I fill Password {string}")
    public void i_fill_Password(String string) {
        $("[placeholder='Password']").setValue(string);
    }

    @When("I click {string}")
    public void i_click(String string) {
        $("[value='Sign in']").click();
    }

    @When("I click Sign in")
    public void i_click_Sign_in() {
        $("[value='Sign in']").click();
    }

    @Then("I am logged in")
    public void i_am_logged_in() {
        assertThat(url(), endsWith("/configurator/dashboard/index"));
    }

    @Then("I see Admin panel")
    public void i_see_Admin_panel() {
        $(".nav-profile").shouldHave(text("admin1"));
    }

    @Given("I am logged with {string} and {string}")
    public void i_am_logged_with_and(String string, String string2) {
        open("/admin/login");
        i_fill_Login_with(string);
        i_fill_Password(string2);
        i_click_Sign_in();
        i_am_logged_in();
    }

    @When("I click by text {string}")
    public void i_click_by_text(String string) {
        $x("//*[text()='" + string + "']").click();
    }

    @Then("I see url is {string}")
    public void i_see_url_is(String string) {
        assertThat(url(), endsWith(string));
    }

    @Then("I see {string}")
    public void i_see(String string) {
        $(string).shouldBe(visible);
    }

    @When("I click column header {string}")
    public void i_click_column_header(String string) {
        $x("//table/thead/tr/th[.='" + string + "']/a").click();
        sleep(3000);
    }

    @Then("column {string} is sorted by asc")
    public void column_is_sorted_by_asc(String string) {
        List<String> actual = $$x("//table/tbody/tr/td[count(preceding-sibling::td)+1 = count(ancestor::table/thead/tr/th[.='" + string + "']/preceding-sibling::th)+1]").texts();
        List<String> sorted = new ArrayList<>(actual);
        sorted.sort(String.CASE_INSENSITIVE_ORDER);
        assertThat(actual, equalTo(sorted));
    }
}
