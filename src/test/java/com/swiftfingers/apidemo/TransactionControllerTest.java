package com.swiftfingers.apidemo;

import com.swiftfingers.apidemo.controller.TransactionController;
import com.swiftfingers.apidemo.model.TransactionRequest;
import com.swiftfingers.apidemo.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


import java.time.Instant;

@ExtendWith(SpringExtension.class)
public class TransactionControllerTest {
    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @Test
    public void createTransaction_ValidTransaction_ReturnsCreated() {
        // Arrange
        TransactionRequest validTransactionRequest = new TransactionRequest("10.50", Instant.now().toString());

        // Act
        ResponseEntity responseEntity = transactionController.createTransaction(validTransactionRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        verify(transactionService, times(1)).addTransaction(eq(validTransactionRequest), any(Instant.class));
    }

    @Test
    public void createTransaction_FutureTransaction_ReturnsUnprocessableEntity() {
        // Arrange
        TransactionRequest futureTransactionRequest = new TransactionRequest("10.50", Instant.now().plusSeconds(60).toString());

        // Act
        ResponseEntity responseEntity = transactionController.createTransaction(futureTransactionRequest);

        // Assert
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
        verify(transactionService, never()).addTransaction(any(), any());
    }

    @Test
    public void createTransaction_OlderTransaction_ReturnsNoContent() {
        // Arrange
        TransactionRequest olderTransactionRequest = new TransactionRequest("10.50", Instant.now().minusSeconds(31).toString());

        // Act
        ResponseEntity responseEntity = transactionController.createTransaction(olderTransactionRequest);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(transactionService, never()).addTransaction(any(), any());
    }

    @Test
    public void createTransaction_InvalidTransaction_ReturnsBadRequest() {
        // Arrange
        TransactionRequest invalidTransactionRequest = new TransactionRequest("invalidAmount", "invalidTimestamp");

        // Act
        ResponseEntity responseEntity = transactionController.createTransaction(invalidTransactionRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verify(transactionService, never()).addTransaction(any(), any());
    }

    @Test
    public void getStatistics_ReturnsOk() {
        // Act
        ResponseEntity responseEntity = transactionController.getStatistics();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(transactionService, times(1)).getStatistics();
    }

    @Test
    public void deleteTransactions_ReturnsNoContent() {
        // Act
        ResponseEntity responseEntity = transactionController.deleteTransactions();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(transactionService, times(1)).deleteAllTransactions();
    }

}
