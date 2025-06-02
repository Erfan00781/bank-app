package com.iut;

import com.iut.Repository;
import com.iut.account.model.Account;

import java.util.*;

public class MockAccountRepository implements Repository<Account, String> {
    private Map<String, Account> store = new HashMap<>();

    @Override
    public boolean save(Account input) {
        if (store.containsKey(input.getId())) return false;
        store.put(input.getId(), input);
        return true;
    }

    @Override
    public boolean update(Account input) {
        if (!store.containsKey(input.getId())) return false;
        store.put(input.getId(), input);
        return true;
    }

    @Override
    public boolean delete(String id) {
        return store.remove(id) != null;
    }

    @Override
    public boolean existsById(String id) {
        return store.containsKey(id);
    }

    @Override
    public Account findById(String id) {
        return store.get(id);
    }

    @Override
    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }
}
