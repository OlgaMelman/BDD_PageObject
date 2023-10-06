package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private final SelenideElement headTransfer = $(byText("Пополнение карты"));
    private final SelenideElement fromInput = $("[data-test-id='from'] input");
    private final SelenideElement amountInputNew = $("[data-test-id='amount'] input");
    private final SelenideElement transferButton = $("[data-test-id='action-transfer']");
    private final SelenideElement errorMsg = $("[data-test-id='error-notification']");

    public TransferPage() {
        headTransfer.shouldBe(visible);
    }

    public DashboardPage makeValidTransfer(String amountTransfer, DataHelper.CardInfo cardInfo) {
        makeTransfer(amountTransfer, cardInfo);
        return new DashboardPage();
    }

    public void makeTransfer(String amountTransfer, DataHelper.CardInfo cardInfo) {
        amountInputNew.setValue(amountTransfer);
        fromInput.setValue(cardInfo.getCardNumber());
        transferButton.click();
    }

    public void findErrorMsg(String expectedText) {
        errorMsg.shouldHave(exactText(expectedText), Duration.ofSeconds(15)).shouldBe(visible);

    }
}