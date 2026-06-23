package org.company;

import java.util.List;

public interface AccountRepository {
    Account findById(String id);
    void save(Account account);
    List<Account> findAll();

}