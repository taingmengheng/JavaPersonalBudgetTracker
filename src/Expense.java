import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Expense implements Transaction {
    private int id;
    private String name;
    private double amount;
    private String category;
    private LocalDateTime dateTime;
    private String description;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");

    public Expense(int id, String name, double amount, String category, LocalDateTime dateTime, String description) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.dateTime = dateTime;
        this.description = description;
    }

    @Override
    public String getDate() {
        return dateTime.format(dateTimeFormatter);
    }

    // Other getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s: -$%.2f\n%s", name, amount, dateTime.format(dateTimeFormatter));
    }
}
