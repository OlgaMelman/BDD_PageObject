package ru.netology.web.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPageV2;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.*;

public class MoneyTransferTest {
    LoginPageV2 loginPageV2;
    DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        var loginPage = open("http://localhost:9999", LoginPageV2.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);
    }

    @Test
    void shouldTransferFromFirstToSecondCard() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateValidAmount(firstCardBalance);
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = dashboardPage.getCardBalance(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(secondCardInfo);
        assertEquals (expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals (expectedBalanceSecondCard, actualBalanceSecondCard);

    }
    @Test
    void shouldTransferFromFirstToSecondCardWithKopeck() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalanceDouble(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalanceDouble(secondCardInfo);
        var amount = 10.50;
        var expectedBalanceFirstCard = firstCardBalance - amount;
        var expectedBalanceSecondCard = secondCardBalance + amount;
        var transferPage = dashboardPage.selectCardToTransfer(secondCardInfo);
        dashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
        var actualBalanceFirstCard = dashboardPage.getCardBalanceDouble(firstCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalanceDouble(secondCardInfo);
        assertEquals (expectedBalanceFirstCard, actualBalanceFirstCard);
        assertEquals (expectedBalanceSecondCard, actualBalanceSecondCard);

    }
    @Test
    void shouldGetErrorMsgIfAmountUnderBalance() {
        var firstCardInfo = getFirstCardInfo();
        var secondCardInfo = getSecondCardInfo();
        var firstCardBalance = dashboardPage.getCardBalance(firstCardInfo);
        var secondCardBalance = dashboardPage.getCardBalance(secondCardInfo);
        var amount = generateInvalidAmount(secondCardBalance);
        var transferPage = dashboardPage.selectCardToTransfer(firstCardInfo);
        transferPage.makeTransfer(String.valueOf(amount), secondCardInfo);
        transferPage.findErrorMsg("Выполнена попытка переводы суммы, превышающая остаток на карте списания");
        var actualBalanceFirstCard = dashboardPage.getCardBalance(secondCardInfo);
        var actualBalanceSecondCard = dashboardPage.getCardBalance(firstCardInfo);
        assertEquals (firstCardBalance, actualBalanceFirstCard);
        assertEquals (secondCardBalance, actualBalanceSecondCard);

    }

}
