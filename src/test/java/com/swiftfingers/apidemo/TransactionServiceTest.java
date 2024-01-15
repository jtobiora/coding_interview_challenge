package com.swiftfingers.apidemo;

import com.swiftfingers.apidemo.model.Transaction;
import com.swiftfingers.apidemo.model.TransactionRequest;
import com.swiftfingers.apidemo.model.TransactionStatistics;
import com.swiftfingers.apidemo.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

public class TransactionServiceTest {
    @Mock
    private TransactionRequest transactionRequest;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void addTransaction() {
        Instant transactionTime = Instant.now();
        when(transactionRequest.getAmount()).thenReturn("50.00");
        transactionService.addTransaction(transactionRequest, transactionTime);

        assertEquals(1, TransactionService.getTransactions().size());
        assertEquals(new BigDecimal("50.00"), TransactionService.getTransactions().get(0).getAmount());
        assertEquals(transactionTime, TransactionService.getTransactions().get(0).getTimestamp());
    }

    @Test
    public void getStatisticsNoTransactions() {
        TransactionStatistics statistics = transactionService.getStatistics();

        assertEquals(null, statistics.getSum());
        assertEquals(null, statistics.getAvg());
        assertEquals(null, statistics.getMax());
        assertEquals(null, statistics.getMin());
        assertEquals(0, statistics.getCount());
    }

    @Test
    public void deleteAllTransactions() {
        TransactionService.getTransactions().addAll(Arrays.asList(
                new Transaction(new BigDecimal("10.50"), Instant.now()),
                new Transaction(new BigDecimal("20.75"), Instant.now())
        ));

        transactionService.deleteAllTransactions();

        assertTrue(TransactionService.getTransactions().isEmpty());
    }


}
