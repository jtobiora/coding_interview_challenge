package com.swiftfingers.apidemo.controller;

import com.swiftfingers.apidemo.model.TransactionRequest;
import com.swiftfingers.apidemo.model.TransactionStatistics;
import com.swiftfingers.apidemo.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api")
@Slf4j
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController (TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /*
    * Creates a new transaction
    * */
    @PostMapping("/transactions")
    public ResponseEntity createTransaction(@RequestBody TransactionRequest transactionRequest) {
        try {
            Instant transactionTime = Instant.parse(transactionRequest.getTimestamp());
            Instant currentTime = Instant.now();

            log.info("Current time {} :: Transaction time {}", currentTime, transactionTime);

            //checks if the transaction is in the future
            if (transactionTime.isAfter(currentTime)) {
                log.info("Transaction time: {} is greater than current time", transactionTime);
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
            }

            //validates for transactions older than 30 seconds
            if (currentTime.minusSeconds(30).isAfter(transactionTime)) {
                log.info("Transaction is older than 30 seconds.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            transactionService.addTransaction(transactionRequest,transactionTime);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); //Invalid JSON or other parsing issues
        }
    }


    /*
     * Returns the statistics based on the transactions that happened in the last 30 seconds.
     *
     * */
    @GetMapping("/statistics")
    public ResponseEntity<TransactionStatistics> getStatistics() {
        return ResponseEntity.ok(transactionService.getStatistics());
    }

    /*
     * Deletes all transactions
     *
     * */
    @DeleteMapping("/transactions")
    public ResponseEntity<Void> deleteTransactions() {
        log.info("Deleting all transactions");
        transactionService.deleteAllTransactions();
        return ResponseEntity.noContent().build();
    }

}
