package com.iut.account.service;

import com.iut.account.model.Account;
import com.iut.account.repo.MockAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountServiceTest {

    private AccountService accountService;
    private MockAccountRepository mockRepository;

    @BeforeEach
    void setUp() {
        mockRepository = new MockAccountRepository();
        accountService = new AccountService(mockRepository);
    }

    @Test
    void testCreateAccount_Success() {
        boolean result = accountService.createAccount("acc1", 1000, "user1");
        assertTrue(result);
        Account account = accountService.getAccount("acc1");
        assertNotNull(account);
        assertEquals("acc1", account.getId());
        assertEquals(1000, account.getBalance());
        assertEquals("user1", account.getUserId());
    }

    @Test
    void testCreateAccount_AccountAlreadyExists() {
        accountService.createAccount("acc1", 1000, "user1");
        boolean result = accountService.createAccount("acc1", 2000, "user2");
        assertFalse(result);
    }

    @Test
    void testDeposit_Success() {
        accountService.createAccount("acc1", 1000, "user1");
        boolean result = accountService.deposit("acc1", 500);
        assertTrue(result);
        Account account = accountService.getAccount("acc1");
        assertEquals(1500, account.getBalance());
    }

    @Test
    void testDeposit_AccountNotFound() {
        boolean result = accountService.deposit("acc1", 500);
        assertFalse(result);
    }

    @Test
    void testWithdraw_Success() {
        accountService.createAccount("acc1", 1000, "user1");
        boolean result = accountService.withdraw("acc1", 300);
        assertTrue(result);
        Account account = accountService.getAccount("acc1");
        assertEquals(700, account.getBalance());
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        accountService.createAccount("acc1", 1000, "user1");
        assertThrows(IllegalArgumentException.class, () -> accountService.withdraw("acc1", 1500));
    }

    @Test
    void testWithdraw_AccountNotFound() {
        boolean result = accountService.withdraw("acc1", 300);
        assertFalse(result);
    }

    @Test
    void testTransfer_Success() {
        accountService.createAccount("acc1", 1000, "user1");
        accountService.createAccount("acc2", 500, "user2");
        boolean result = accountService.transfer("acc1", "acc2", 300);
        assertTrue(result);
        assertEquals(700, accountService.getAccount("acc1").getBalance());
        assertEquals(800, accountService.getAccount("acc2").getBalance());
    }

    @Test
    void testTransfer_InsufficientFunds() {
        accountService.createAccount("acc1", 200, "user1");
        accountService.createAccount("acc2", 500, "user2");
        assertThrows(IllegalArgumentException.class, () -> accountService.transfer("acc1", "acc2", 300));
    }

    @Test
    void testTransfer_AccountNotFound() {
        accountService.createAccount("acc1", 1000, "user1");
        boolean result = accountService.transfer("acc1", "acc2", 300);
        assertFalse(result);
    }

    @Test
    void testGetBalance_Success() {
        accountService.createAccount("acc1", 1000, "user1");
        int balance = accountService.getBalance("acc1");
        assertEquals(1000, balance);
    }

    @Test
    void testGetBalance_AccountNotFound() {
        assertThrows(IllegalArgumentException.class, () -> accountService.getBalance("acc1"));
    }

    @Test
    void testDeleteAccount_Success() {
        accountService.createAccount("acc1", 1000, "user1");
        boolean result = accountService.deleteAccount("acc1");
        assertTrue(result);
        assertNull(accountService.getAccount("acc1"));
    }

    @Test
    void testGetUserAccounts_Success() {
        accountService.createAccount("acc1", 1000, "user1");
        accountService.createAccount("acc2", 2000, "user1");
        accountService.createAccount("acc3", 3000, "user2");
        List<Account> accounts = accountService.getUserAccounts("user1");
        assertEquals(2, accounts.size());
        assertEquals("acc1", accounts.get(0).getId());
        assertEquals("acc2", accounts.get(1).getId());
    }
}