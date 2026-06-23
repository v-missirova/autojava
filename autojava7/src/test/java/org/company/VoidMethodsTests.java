package org.company;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoidMethodsTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FeeService feeService;

    @Mock
    private AuditLog auditLog;

    @InjectMocks
    private TransferService transferService;

    @Test
    void shouldLogTransferExactlyOnce_whenTransferIsSuccessful() {
        Account accountFrom = new Account("1", new BigDecimal("100.00"));
        Account accountTo = new Account("2", new BigDecimal("50.00"));
        BigDecimal transferAmount = new BigDecimal("20.00");

        when(accountRepository.findById("1")).thenReturn(accountFrom);
        when(accountRepository.findById("2")).thenReturn(accountTo);
        when(feeService.calculateFee(transferAmount)).thenReturn(new BigDecimal("2.00"));

        transferService.transfer("1", "2", transferAmount);

        verify(auditLog, times(1)).logTransfer("1", "2", transferAmount);
        verify(auditLog, never()).logError(anyString());
    }

    @Test
    void shouldLogErrorAndNeverLogTransfer_whenAccountNotFound() {
        when(accountRepository.findById("1")).thenReturn(null);

        transferService.transfer("1", "2", new BigDecimal("20.00"));

        verify(auditLog).logError("Account not found");
        verify(auditLog, never()).logTransfer(anyString(), anyString(), any());
    }

    @Test
    void shouldLogError_whenInsufficientFunds() {
        Account accountFrom = new Account("1", new BigDecimal("10.00"));
        Account accountTo = new Account("2", new BigDecimal("50.00"));
        BigDecimal transferAmount = new BigDecimal("20.00");

        when(accountRepository.findById("1")).thenReturn(accountFrom);
        when(accountRepository.findById("2")).thenReturn(accountTo);
        when(feeService.calculateFee(transferAmount)).thenReturn(new BigDecimal("2.00"));

        transferService.transfer("1", "2", transferAmount);

        verify(auditLog).logError("Insufficient funds");
        verify(auditLog, never()).logTransfer(anyString(), anyString(), any());
    }
}