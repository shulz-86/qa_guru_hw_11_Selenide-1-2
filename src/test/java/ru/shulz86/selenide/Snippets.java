package ru.shulz86.selenide;
import com.codeborne.selenide.*;
import org.openqa.selenium.*;

import java.io.*;
import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.*;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class Snippets {
    void browser_command_examples() {
        open("https://google.com");
        open("/customer/orders");     // -Dselenide.baseUrl=http://123.23.23.1
        open("/", AuthenticationType.BASIC,
                new BasicAuthCredentials("", "user", "password"));

        Selenide.back();
        Selenide.refresh();

        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
        executeJavaScript("sessionStorage.clear();"); // no Selenide command for this yet

        Selenide.confirm(); // OK in alert dialogs
        Selenide.dismiss(); // Cancel in alert dialogs

        Selenide.closeWindow(); // close active tab
        Selenide.closeWebDriver(); // close browser completely

        Selenide.switchTo().frame("new");
        Selenide.switchTo().defaultContent(); // return from frame back to the main DOM

        Selenide.switchTo().window("The Internet");

        var cookie = new Cookie("foo", "bar");
        WebDriverRunner.getWebDriver().manage().addCookie(cookie);
    }

    void selectors_examples() {
        $("div").click();
        element("div").click();

        $("div", 2).click(); // the third div

        $x("//h1/div").click();
        $(byXpath("//h1/div")).click();

        $(byText("full text")).click();
        $(withText("ull tex")).click();

        $(byTagAndText("div", "full text"));
        $(withTagAndText("div", "ull text"));

        $("").parent();
        $("").sibling(1);
        $("").preceding(1);
        $("").closest("div");
        $("").ancestor("div"); // the same as closest
        $("div:last-child");

        $("div").$("h1").find(byText("abc")).click();
        // very optional
        $(byAttribute("abc", "x")).click();
        $("[abc=x]").click();

        $(byId("mytext")).click();
        $("#mytext").click();

        $(byClassName("red")).click();
        $(".red").click();
    }

    void actions_examples() {
        $("").click();
        $("").doubleClick();
        $("").contextClick();

        $("").hover();

        $("").setValue("text");
        $("").append("text");
        $("").clear();
        $("").setValue(""); // clear

        $("div").sendKeys("c"); // hotkey c on element
        actions().sendKeys("c").perform(); //hotkey c on whole application
        actions().sendKeys(Keys.chord(Keys.CONTROL, "f")).perform(); // Ctrl + F
        $("html").sendKeys(Keys.chord(Keys.CONTROL, "f"));

        $("").pressEnter();
        $("").pressEscape();
        $("").pressTab();

        // complex actions with keybord and mouse, example
        actions().moveToElement($("div")).clickAndHold().moveByOffset(300, 200).release().perform();

        // old html actions don't work with many modern frameworks
        $("").selectOption("dropdown_option");
        $("").selectRadio("radio_options");
    }

    void assertions_examples() {
        $("").shouldBe(visible);
        $("").shouldNotBe(visible);
        $("").shouldHave(text("abc"));
        $("").shouldNotHave(text("abc"));
        $("").should(appear);
        $("").shouldNot(appear);

        //longer timeouts
        $("").shouldBe(visible, Duration.ofSeconds(30));
    }

    void conditions_examples() {
        $("").shouldBe(visible); // что-то появилось/видимое
        $("").shouldBe(hidden); // исчезло

        $("").shouldHave(text("abc")); // без учета регистра - ищет подстроку
        $("").shouldHave(exactText("abc")); // без учета регистра - ищет точное совпадение
        $("").shouldHave(textCaseSensitive("abc"));
        $("").shouldHave(exactTextCaseSensitive("abc"));
        $("").should(matchText("[0-9]abc$")); // что-то сложное с регексами

        $("").shouldHave(cssClass("red"));  // проверяется есть ли у элемента такой класс
        $("").shouldHave(cssValue("font-size", "12")); // проверка свойств элемента

        $("").shouldHave(value("25")); //ищет подстроку
        $("").shouldHave(exactValue("25")); //ищет точное совпадение
        $("").shouldBe(empty); // проверка что поле пустое

        $("").shouldHave(attribute("disabled")); // проверка наличия атрибута (например класс)
        $("").shouldHave(attribute("name", "example")); // проверка наличия атрибута по имени
        $("").shouldHave(attributeMatching("name", "[0-9]abc$")); // проверка наличия атрибута по имени с исп.регулярки

        $("").shouldBe(checked); // проверка что чек-бокс вкл. Чтоб проверить выкл.ч/б - shouldNotBe()

        // Warning! Only checks if it is in DOM, not if it is visible! You don't need it in most tests!
        $("").should(exist); // находится ли элемент в доме (выдимый или невидимый)

        // Warning! Checks only the "disabled" attribute! Will not work with many modern frameworks
        $("").shouldBe(disabled);
        $("").shouldBe(enabled);
    }

    void collections_examples() {
        $$("div"); // does nothing!

        $$x("//div"); // by XPath

        // selections
        $$("div").filterBy(text("123")).shouldHave(size(1)); // отфильтровывает только 123
        $$("div").excludeWith(text("123")).shouldHave(size(1)); // отфильтровывает все, кроме 123

        $$("div").first().click(); // первый из списка
        elements("div").first().click();
        // $("div").click();
        $$("div").last().click(); // обращение к последнему элементу
        $$("div").get(1).click(); // the second! (start with 0) - обращение к элементу по номеру
        $("div", 1).click(); // same as previous
        $$("div").findBy(text("123")).click(); //  finds first - первый отфильтрованный элемент

        // assertions
        $$("").shouldHave(size(0)); // проверка размера коллекции = пустая
        $$("").shouldBe(CollectionCondition.empty); // the same // проверка размера коллекции = пустая

        $$("").shouldHave(texts("Alfa", "Beta", "Gamma")); // содержит ли коллекция тексты - важен порядок и количество
        $$("").shouldHave(exactTexts("Alfa", "Beta", "Gamma"));

        $$("").shouldHave(textsInAnyOrder("Beta", "Gamma", "Alfa")); // порядок не важен
        $$("").shouldHave(exactTextsCaseSensitiveInAnyOrder("Beta", "Gamma", "Alfa"));

        $$("").shouldHave(itemWithText("Gamma")); // only one text - проверка что в коллекции есть/добавился этот элемент

        $$("").shouldHave(sizeGreaterThan(0)); // проверки на количество элементов в коллекции
        $$("").shouldHave(sizeGreaterThanOrEqual(1));
        $$("").shouldHave(sizeLessThan(3));
        $$("").shouldHave(sizeLessThanOrEqual(2));
    }

    void file_operation_examples() throws FileNotFoundException {
// скачивание файла
        File file1 = $("a.fileLink").download(); // only for <a href=".."> links
        File file2 = $("div").download(DownloadOptions.using(FileDownloadMode.FOLDER)); // more common options, but may have problems with Grid/Selenoid
// получить сначала файл - лучше использовать вариант с file2, а потом по нему искать свои проверки

        // загрузка файла
        File file = new File("src/test/resources/readme.txt");
        $("#file-upload").uploadFile(file);
        $("#file-upload").uploadFromClasspath("readme.txt");
        // don't forget to submit! - не забывать нажать на кнопку загрузки
        $("uploadButton").click();
    }

    void javascript_examples() {
        executeJavaScript("alert('selenide')");
        executeJavaScript("alert(arguments[0]+arguments[1])", "abc", 12);
        long fortytwo = executeJavaScript("return arguments[0]*arguments[1];", 6, 7);
    }
}
