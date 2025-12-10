package financeapp;

import org.bson.Document;
import org.bson.types.ObjectId;

public class Transaction
{
    private ObjectId id;
    private String type;
    private double amount;
    private String description;


    public Transaction(ObjectId id, String type, double amount, String description) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }


    public Transaction(String type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
    }


    public ObjectId getId() {
        return id;
    }


    public Document toDocument () {
        Document doc = new Document("type", type)
                .append("amount", amount)
                .append("description", description);


        if (id != null) {
            doc.append("_id", id);
        }

        return doc;
    }

    public String getType () {
        return type;
    }
    public double getAmount () {
        return amount;
    }
    public String getDescription () {
        return description;
    }

}
