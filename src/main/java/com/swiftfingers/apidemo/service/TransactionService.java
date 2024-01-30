package com.swiftfingers.apidemo.service;

import com.swiftfingers.apidemo.model.Transaction;
import com.swiftfingers.apidemo.model.TransactionRequest;
import com.swiftfingers.apidemo.model.TransactionStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TransactionService {

    //private static final Queue<Transaction> transactions = new ConcurrentLinkedQueue<>();//to ensure thread safety with concurrent requests

    private static final List<Transaction> transactions = new CopyOnWriteArrayList<>();//to ensure thread safety with concurrent requests

    private static final int WINDOW_SIZE_SECONDS = 30; // Define the time window for recent transactions

    public void addTransaction (TransactionRequest request, Instant transactionTime) {
        BigDecimal amount = new BigDecimal(request.getAmount());

        // Remove older transactions outside the time window
        transactions.removeIf(transaction -> transaction.getTimestamp().isBefore(transactionTime.minusSeconds(WINDOW_SIZE_SECONDS)));

        transactions.add(new Transaction(amount, transactionTime));
    }

    public TransactionStatistics getStatistics() {
        Instant currentTime = Instant.now();
        Instant thirtySecondsAgo = currentTime.minusSeconds(30);

        //Filters transactions based on their timestamp, retaining only those that occurred
        // within the last 30 seconds.
        List<Transaction> recentTransactions = transactions.stream()
                .filter(transaction -> !transaction.getTimestamp().isBefore(thirtySecondsAgo))
                .collect(Collectors.toList());

        if (recentTransactions.isEmpty()) {
            return new TransactionStatistics();
        }

        //Calculate various statistics (sum, average, max, min, count) on the amounts of the recent transactions.
        DoubleSummaryStatistics stats = recentTransactions.stream()
                .map(Transaction::getAmount)
                .collect(Collectors.summarizingDouble(BigDecimal::doubleValue));

        //Create TransactionStatistics object and format to two decimal places.
        TransactionStatistics statistics = new TransactionStatistics(
                BigDecimal.valueOf(stats.getSum()).setScale(2, BigDecimal.ROUND_HALF_UP),
                BigDecimal.valueOf(stats.getAverage()).setScale(2, BigDecimal.ROUND_HALF_UP),
                BigDecimal.valueOf(stats.getMax()).setScale(2, BigDecimal.ROUND_HALF_UP),
                BigDecimal.valueOf(stats.getMin()).setScale(2, BigDecimal.ROUND_HALF_UP),
                stats.getCount()
        );

        return statistics;
    }

    public void deleteAllTransactions() {
        transactions.clear();
    }

    public static List<Transaction> getTransactions () {
        return transactions;
    }

//    public static List<Transaction> getTransactions () {
//        return new ArrayList<>(transactions);
//    }
}
