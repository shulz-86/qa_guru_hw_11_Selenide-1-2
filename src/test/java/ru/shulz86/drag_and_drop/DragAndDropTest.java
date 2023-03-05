package ru.shulz86.drag_and_drop;

import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

public class DragAndDropTest {
    @Test
    void dragAndDropTest() {
        step("Открыть браузер на странице https://the-internet.herokuapp.com/drag_and_drop", () -> {
            open("https://the-internet.herokuapp.com/drag_and_drop");
        });
        step("Перенести прямоугольник А на место В", () -> {
            $("#column-a").dragAndDropTo("#column-b");
        });
        step("Проверить, что прямоугольники действительно поменялись", () -> {
            $("#column-a").shouldHave(text("B"));
            $("#column-b").shouldHave(text("A"));
        });
    }
}
