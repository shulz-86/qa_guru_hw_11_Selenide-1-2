package ru.shulz86.githubtest;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

public class GithubTest {
    @Test
    void shouldFindSolutions() {
        step("Открыть браузер на странице гитхаб", () -> {
            open("https://github.com/");
        });
        step("В верхнем меню навести курсор на Solutions и выбрать Enterprise", () -> {
            $(byText("Solutions")).hover();
            $(byText("Enterprise")).click();
        });
        step("Убедиться что загрузилась нужная страница", () -> {
            $("h1.h1-mktg").shouldHave(text("Build like the best"));
        });
    }
}
