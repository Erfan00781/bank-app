package com.iut;

import static org.junit.jupiter.api.Assertions.*;

import com.iut.account.model.Account;
import com.iut.account.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountServiceTest {

    private AccountService accountService;

    @BeforeEach
    void setup() {
        MockAccountRepository mockRepo = new MockAccountRepository();
        accountService = new AccountService(mockRepo);
    }

    @Test
    void createAccountTest() {
        assertTrue(accountService.createAccount("acc1", 1000, "user1"));
        // تلاش برای ساخت حساب با همان id دوباره باید false باشد
        assertFalse(accountService.createAccount("acc1", 2000, "user2"));
    }

    @Test
    void depositTest() {
        accountService.createAccount("acc1", 1000, "user1");
        assertTrue(accountService.deposit("acc1", 500));
        assertEquals(1500, accountService.getBalance("acc1"));
        // واریز به حساب غیر موجود باید false برگرداند
        assertFalse(accountService.deposit("nonexistent", 100));
    }

    @Test
    void withdrawTest() {
        accountService.createAccount("acc1", 1000, "user1");
        assertTrue(accountService.withdraw("acc1", 500));
        assertEquals(500, accountService.getBalance("acc1"));

        // برداشت بیشتر از موجودی باید IllegalArgumentException پرتاب کند
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.withdraw("acc1", 600);
        });
        assertEquals("Insufficient funds", exception.getMessage());

        // برداشت از حساب غیر موجود false برمی‌گرداند
        assertFalse(accountService.withdraw("nonexistent", 100));
    }

    @Test
    void transferTest() {
        accountService.createAccount("acc1", 1000, "user1");
        accountService.createAccount("acc2", 500, "user2");

        assertTrue(accountService.transfer("acc1", "acc2", 300));
        assertEquals(700, accountService.getBalance("acc1"));
        assertEquals(800, accountService.getBalance("acc2"));

        // انتقال با موجودی ناکافی باید Exception پرتاب کند
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.transfer("acc1", "acc2", 1000);
        });
        assertEquals("Insufficient funds in source account", exception.getMessage());

        // انتقال بین حساب‌های ناموجود باید false برگرداند
        assertFalse(accountService.transfer("acc1", "nonexistent", 100));
        assertFalse(accountService.transfer("nonexistent", "acc2", 100));
    }

    @Test
    void getBalanceTest() {
        accountService.createAccount("acc1", 1000, "user1");
        assertEquals(1000, accountService.getBalance("acc1"));

        // فراخوانی موجودی حساب ناموجود باید IllegalArgumentException پرتاب کند
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.getBalance("nonexistent");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteAccountTest() {
        accountService.createAccount("acc1", 1000, "user1");
        assertTrue(accountService.deleteAccount("acc1"));
        assertFalse(accountService.deleteAccount("acc1")); // حذف مجدد false است
    }

    @Test
    void existsAndGetAccountTest() {
        accountService.createAccount("acc1", 1000, "user1");
        assertTrue(accountService.getAccount("acc1") != null);
        assertNull(accountService.getAccount("nonexistent"));
    }
}
