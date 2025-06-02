package com.iut;

import com.iut.account.model.Account;
import com.iut.account.service.AccountService;
import com.iut.user.model.User;
import com.iut.user.service.UserService;

import java.util.List;

public class BankService {
    private final UserService userService;
    private final AccountService accountService;

    public BankService(final UserService userService, final AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    // متد ثبت کاربر جدید و ایجاد حساب پیش‌فرض با موجودی 0
    public boolean registerNewUser(User user) {
        boolean created = userService.createUser(user);
        if (created) {
            // ایجاد حساب پیش فرض با شناسه ترکیبی userId + "_default"
            String defaultAccountId = user.getId() + "_default";
            accountService.createAccount(defaultAccountId, 0, user.getId());
            return true;
        }
        return false;
    }

    // دریافت حساب‌های یک کاربر
    public List<Account> getUserAccounts(String userId) {
        return accountService.getUserAccounts(userId);
    }

    // گرفتن کاربر با شناسه
    public User getUser(String id) {
        return userService.getUser(id);
    }

    // افزودن حساب جدید به یک کاربر
    public boolean addAccountToUser(String userId, Account account) {
        if (userService.getUser(userId) == null) {
            return false; // کاربر وجود ندارد
        }
        account.setUserId(userId);
        return accountService.createAccount(account.getId(), account.getBalance(), userId);
    }

    // گرفتن حساب بر اساس شناسه حساب
    public Account getAccount(String accountId) {
        return accountService.getAccount(accountId);
    }

    // حذف حساب بر اساس شناسه
    public boolean deleteAccount(String accountId) {
        Account account = accountService.getAccount(accountId);
        if (account != null) {
            return accountService.deleteAccount(accountId);
        }
        return false;
    }


}
