package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class TransactionManager {

    private final MongoCollection<Document> collection;

    public TransactionManager() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        collection = db.getCollection("transactions");
    }


    public void addTransaction(Transaction t) {
        collection.insertOne(t.toDocument());
    }


    public ArrayList<Transaction> getAllTransactions() {
        ArrayList<Transaction> list = new ArrayList<>();
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document d = cursor.next();
            list.add(new Transaction(
                    d.getObjectId("_id"),
                    d.getString("type"),
                    d.getDouble("amount"),
                    d.getString("description"),
                    d.getString("category")
            ));
        }
        return list;
    }


    public double getTotalIncome() {
        double total = 0;
        for (Transaction t : getAllTransactions()) {
            if ("Prihod".equalsIgnoreCase(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }


    public double getTotalExpense() {
        double total = 0;
        for (Transaction t : getAllTransactions()) {
            if ("Rashod".equalsIgnoreCase(t.getType())) {
                total += t.getAmount();
            }
        }
        return total;
    }


    public void updateTransaction(Transaction t) {
        Document updated = new Document("type", t.getType())
                .append("amount", t.getAmount())
                .append("description", t.getDescription())
                .append("category", t.getCategory());

        collection.updateOne(
                new Document("_id", t.getId()),
                new Document("$set", updated)
        );
    }


    public void updateTransaction(String selectedTransactionId, String type, double amount, String description) {
        ObjectId objectId = new ObjectId(selectedTransactionId);
        Document updated = new Document("type", type)
                .append("amount", amount)
                .append("description", description);

        collection.updateOne(
                new Document("_id", objectId),
                new Document("$set", updated)
        );
    }


    public void updateTransaction(String selectedTransactionId, String type, double amount, String description, String category) {
        ObjectId objectId = new ObjectId(selectedTransactionId);
        Document updated = new Document("type", type)
                .append("amount", amount)
                .append("description", description)
                .append("category", category);

        collection.updateOne(
                new Document("_id", objectId),
                new Document("$set", updated)
        );
    }

    public void deleteTransaction(String transactionId) {
        ObjectId objectId = new ObjectId(transactionId);
        collection.deleteOne(new Document("_id", objectId));
    }
}
