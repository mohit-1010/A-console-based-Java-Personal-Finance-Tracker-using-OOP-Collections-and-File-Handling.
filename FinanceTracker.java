import java.io.*;
import java.util.*;

class Transaction implements Serializable {
    private String type;
    private double amount;
    private String description;
    private Date date;

    public Transaction(String type, double amount, String description) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = new Date();
    }

    public String toString() {
        return String.format("[%s] %s: ₹%.2f (%s)", date, type, amount, description);
    }
}

public class FinanceTracker {
    private List<Transaction> transactions = new ArrayList<>();
    private double balance = 0;

    public void addIncome(double amount, String description) {
        transactions.add(new Transaction("Income", amount, description));
        balance += amount;
    }

    public void addExpense(double amount, String description) {
        transactions.add(new Transaction("Expense", amount, description));
        balance -= amount;
    }

    public void showTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            transactions.forEach(System.out::println);
        }
        System.out.println("Current Balance: ₹" + balance);
    }

    public void saveData(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(transactions);
            out.writeDouble(balance);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public void loadData(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            transactions = (List<Transaction>) in.readObject();
            balance = in.readDouble();
            System.out.println("Data loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        FinanceTracker tracker = new FinanceTracker();
        Scanner sc = new Scanner(System.in);
        String filename = "finance_data.dat";

        tracker.loadData(filename);

        while (true) {
            System.out.println("\n--- Personal Finance Tracker ---");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Transactions");
            System.out.println("4. Save & Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    double incAmount = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter description: ");
                    String incDesc = sc.nextLine();
                    tracker.addIncome(incAmount, incDesc);
                    break;
                case 2:
                    System.out.print("Enter amount: ");
                    double expAmount = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter description: ");
                    String expDesc = sc.nextLine();
                    tracker.addExpense(expAmount, expDesc);
                    break;
                case 3:
                    tracker.showTransactions();
                    break;
                case 4:
                    tracker.saveData(filename);
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}
