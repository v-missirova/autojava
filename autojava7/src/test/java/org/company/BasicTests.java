package org.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasicTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FeeService feeService;

    @Mock
    private AuditLog auditLog;

    @InjectMocks
    private TransferService transferService;

    @Test
    void shouldReturnTrue_whenTransferIsSuccessful() {
        Account accountFrom = new Account("1", new BigDecimal("100.00"));
        Account accountTo = new Account("2", new BigDecimal("50.00"));
        BigDecimal transferAmount = new BigDecimal("20.00");
        BigDecimal fee = new BigDecimal("2.00");

        when(accountRepository.findById("1")).thenReturn(accountFrom);
        when(accountRepository.findById("2")).thenReturn(accountTo);
        when(feeService.calculateFee(transferAmount)).thenReturn(fee);

        boolean result = transferService.transfer("1", "2", transferAmount);

        assertTrue(result);
        assertEquals(new BigDecimal("78.00"), accountFrom.getBalance());
        assertEquals(new BigDecimal("70.00"), accountTo.getBalance());
    }

    @Test
    void shouldReturnFalse_whenInsufficientFunds() {
        Account accountFrom = new Account("1", new BigDecimal("20.00"));
        Account accountTo = new Account("2", new BigDecimal("50.00"));
        BigDecimal transferAmount = new BigDecimal("20.00");
        BigDecimal fee = new BigDecimal("5.00");

        when(accountRepository.findById("1")).thenReturn(accountFrom);
        when(accountRepository.findById("2")).thenReturn(accountTo);
        when(feeService.calculateFee(transferAmount)).thenReturn(fee);

        boolean result = transferService.transfer("1", "2", transferAmount);

        assertFalse(result);
        assertEquals(new BigDecimal("20.00"), accountFrom.getBalance());
    }

    @Test
    void shouldReturnFalse_whenAccountNotFound() {
        Account accountFrom = new Account("1", new BigDecimal("100.00"));
        BigDecimal transferAmount = new BigDecimal("20.00");

        when(accountRepository.findById("1")).thenReturn(accountFrom);
        when(accountRepository.findById("2")).thenReturn(null);

        boolean result = transferService.transfer("1", "2", transferAmount);

        assertFalse(result);
    }
}