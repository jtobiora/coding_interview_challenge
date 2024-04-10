Transaction Statistics API
This is a RESTful API for calculating real-time statistics based on transactions. The main use case for this API is to provide statistics for the last 30 seconds of transactions.
The API supports three main endpoints: POST /transaction, GET /transaction and DELETE /transaction.

1. Usage
   a. POST /transaction
   i. Description: This endpoint should be called every time a new transaction is made. The request payload should contain information about the transaction, including amount and timestamp.
   ii. Request:
     - Payload: JSON object representing a transaction (e.g amount and timestamp).
     - Response:
       * 201 Created: Transaction successfully created
       * 204 No Content: Transaction older than 30 seconds
         
   b. GET /transaction
   i. Description: This endpoint returns the statistics based on transactions in the last 30 seconds.
   ii. Request:
     No additional parameters required.
   iii. Response:
     200 OK. Returns a JSON object containing real-time statistics
   
   c. DELETE /transaction
   i. Description: This endpoint deletes all transactions.
   ii. Request:
     No additional parameters required.
   iii. Response:
     204 No Content: All transactions successfully deleted.
   
