package org.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MutationTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FeeService feeService;

    @Mock
    private AuditLog auditLog;

    @InjectMocks
    private TransferService transferService;

    @Test
    void shouldLeaveMutantAlive_whenTestLacksBoundaryCheck() {
        Account accountFrom = new Account("1", new BigDecimal("100.00"));
        Account accountTo = new Account("2", new BigDecimal("50.00"));
        BigDecimal transferAmount = new BigDecimal("20.00");
        BigDecimal fee = new BigDecimal("2.00");

        when(accountRepository.findById("1")).thenReturn(accountFrom);
        when(accountRepository.findById("2")).thenReturn(accountTo);
        when(feeService.calculateFee(transferAmount)).thenReturn(fee);

        boolean result = transferService.transfer("1", "2", transferAmount);
        assertTrue(result);
    }

    @Test
    void shouldKillMutant_whenTestChecksExactBoundary() {
        Account accountFrom = new Account("1", new BigDecimal("22.00"));
        Account accountTo = new Account("2", new BigDecimal("50.00"));
        BigDecimal transferAmount = new BigDecimal("20.00");
        BigDecimal fee = new BigDecimal("2.00");

        when(accountRepository.findById("1")).thenReturn(accountFrom);
        when(accountRepository.findById("2")).thenReturn(accountTo);
        when(feeService.calculateFee(transferAmount)).thenReturn(fee);

        boolean result = transferService.transfer("1", "2", transferAmount);

        assertTrue(result, "success when funds >= amount + fee");
    }
}