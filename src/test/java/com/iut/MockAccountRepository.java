package com.iut;

import com.iut.Repository;
import com.iut.account.model.Account;

import java.util.*;

public class MockAccountRepository implements Repository<Account, String> {

    private final Map<String, Account> storage = new HashMap<>();

    @Override
    public boolean save(Account input) {
        if (storage.containsKey(input.getId())) return false;
        storage.put(input.getId(), input);
        return true;
    }

    @Override
    public boolean update(Account input) {
        if (!storage.containsKey(input.getId())) return false;
        storage.put(input.getId(), input);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return storage.remove(id) != null;
    }

    @Override
    public boolean existsById(String id) {
        return storage.containsKey(id);
    }

    @Override
    public Account findById(String id) {
        return storage.get(id);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(storage.values());
    }
}
