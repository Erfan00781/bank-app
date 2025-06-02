package com.iut.account.repo;

import com.iut.Repository;
import com.iut.account.model.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockAccountRepository implements Repository<Account, String> {

    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public boolean save(Account input) {
        if (accounts.containsKey(input.getId())) {
            return false;
        }
        accounts.put(input.getId(), input);
        return true;
    }

    @Override
    public boolean update(Account input) {
        if (!accounts.containsKey(input.getId())) {
            return false;
        }
        accounts.put(input.getId(), input);
        return true;
    }

    @Override
    public boolean delete(String id) {
        if (!accounts.containsKey(id)) {
            return false;
        }
        accounts.remove(id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        return accounts.containsKey(id);
    }

    @Override
    public Account findById(String id) {
        return accounts.get(id);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public List<Account> findByUserId(String userId) {
        List<Account> result = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getUserId() != null && account.getUserId().equals(userId)) {
                result.add(account);
            }
        }
        return result;
    }
}