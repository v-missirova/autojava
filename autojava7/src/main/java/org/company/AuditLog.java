package org.company;

import java.math.BigDecimal;

public interface AuditLog {
    void logTransfer(String fromId, String toId, BigDecimal amount);
    void logError(String message);
}