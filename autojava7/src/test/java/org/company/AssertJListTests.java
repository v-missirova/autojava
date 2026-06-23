package org.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssertJListTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void shouldVerifyAccountsListSize() {
        List<Account> accounts = Arrays.asList(
                new Account("1", new BigDecimal("100.00")),
                new Account("2", new BigDecimal("200.00"))
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = transferService.getAllAccounts();

        assertThat(result).hasSize(2);
    }

    @Test
    void shouldVerifyAccountsListExtractedElements() {
        List<Account> accounts = Arrays.asList(
                new Account("1", new BigDecimal("100.00")),
                new Account("2", new BigDecimal("200.00")),
                new Account("3", new BigDecimal("50.00"))
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = transferService.getAllAccounts();

        assertThat(result)
                .extracting(Account::getId)
                .containsExactly("1", "2", "3");
    }

    @Test
    void shouldFilterAndVerifyAccountsList() {
        List<Account> accounts = Arrays.asList(
                new Account("1", new BigDecimal("100.00")),
                new Account("2", new BigDecimal("200.00")),
                new Account("3", new BigDecimal("50.00"))
        );
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = transferService.getAllAccounts();

        assertThat(result)
                .filteredOn(acc -> acc.getBalance().compareTo(new BigDecimal("100.00")) >= 0)
                .extracting(Account::getId)
                .containsExactlyInAnyOrder("1", "2");
    }
}