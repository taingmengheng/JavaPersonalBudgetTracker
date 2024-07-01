//Group4 E6
//Heng Taingmeng
//Kim Horlong
//Yen Mom
//Chea Sokchan
//PN ភីអ៊ែន
//Roeun Sophan

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;


public class BudgetTrackerGUI {
    private JFrame frame;
    private Budget budget;
    private JPanel transactionsPanel;
    private JLabel balanceLabel;
    private DateTimeFormatter dateTimeFormatter;

    public BudgetTrackerGUI() {
        budget = new Budget(1900.54);
        dateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a");
        initialize();
        addDefaultExpensesAndIncomes();
    }

    private void initialize() {
        // Frame
        frame = new JFrame("Personal Budget Tracker - TM");
        frame.setBounds(600, 30, 350, 782);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/money.png"));
            frame.setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(50, 10, 40, 10));
        headerPanel.setBackground(new Color(0x01667f));
        headerPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 140));

        // "Available Balance" Label
        JLabel availableBalanceLabel = new JLabel("Available Balance", JLabel.CENTER);
        availableBalanceLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        availableBalanceLabel.setForeground(Color.WHITE);
        headerPanel.add(availableBalanceLabel, BorderLayout.NORTH);

        // Account Balance
        balanceLabel = new JLabel("$" + String.format("%.2f", budget.getRemainingBudget()), JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        balanceLabel.setForeground(Color.WHITE);
        headerPanel.add(balanceLabel, BorderLayout.CENTER);

        frame.getContentPane().add(headerPanel, BorderLayout.NORTH);

        // Transaction History Panel
        transactionsPanel = new JPanel();
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        transactionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(transactionsPanel);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        JButton optionsButton = new JButton("Options");
        optionsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showOptionsPopup();
            }
        });
        optionsButton.setPreferredSize(new Dimension(Integer.MAX_VALUE, 46));
        optionsButton.setBackground(Color.RED);
        optionsButton.setForeground(Color.WHITE);

        Font buttonFont = optionsButton.getFont();
        optionsButton.setFont(new Font(buttonFont.getName(), Font.BOLD, 16));

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(optionsButton, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        optionsButton.setFocusable(false);

        frame.setVisible(true);
    }

    private void showOptionsPopup() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(e -> addExpense());
        addExpenseButton.setFocusable(false);
        addExpenseButton.setPreferredSize(new Dimension(250, 40));
        addExpenseButton.setMaximumSize(new Dimension(250, 40));
        panel.add(addExpenseButton);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton addIncomeButton = new JButton("Add Income");
        addIncomeButton.addActionListener(e -> addIncome());
        addIncomeButton.setFocusable(false);
        addIncomeButton.setPreferredSize(new Dimension(250, 40));
        addIncomeButton.setMaximumSize(new Dimension(250, 40));
        panel.add(addIncomeButton);


        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        JButton generateReportButton = new JButton("Generate Report");
        generateReportButton.addActionListener(e -> generateReport());
        generateReportButton.setFocusable(false);
        generateReportButton.setPreferredSize(new Dimension(250, 40));
        generateReportButton.setMaximumSize(new Dimension(250, 40));
        panel.add(generateReportButton);


        JOptionPane optionsPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{});
        JDialog dialog = optionsPane.createDialog(frame, "Options");
        dialog.setVisible(true);
    }



    private void generateReport() {
        StringBuilder reportText = new StringBuilder();

        // Total amount spent
        reportText.append("Total Amount Spent: $").append(String.format("%.2f", budget.getTotalExpenses())).append("\n\n");

        // Summarize expenses by category
        Map<String, Double> expensesByCategory = new HashMap<>();
        for (Expense expense : budget.getExpenses()) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            expensesByCategory.put(category, expensesByCategory.getOrDefault(category, 0.0) + amount);
        }
        reportText.append("Expenses by Category:\n");
        for (Map.Entry<String, Double> entry : expensesByCategory.entrySet()) {
            reportText.append(entry.getKey()).append(": $").append(String.format("%.2f", entry.getValue())).append("\n");
        }

        // Summarize expenses by date
        Map<LocalDateTime, Double> expensesByDate = new TreeMap<>(Collections.reverseOrder());
        for (Expense expense : budget.getExpenses()) {
            String dateString = expense.getDate();
            LocalDateTime date = LocalDateTime.parse(dateString, dateTimeFormatter);
            double amount = expense.getAmount();
            expensesByDate.put(date, expensesByDate.getOrDefault(date, 0.0) + amount);
        }
        reportText.append("\nExpenses by Date:\n");
        for (Map.Entry<LocalDateTime, Double> entry : expensesByDate.entrySet()) {
            reportText.append(entry.getKey().format(dateTimeFormatter)).append(": $").append(String.format("%.2f", entry.getValue())).append("\n");
        }


        // Display report in a JOptionPane
        JTextArea textArea = new JTextArea(reportText.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300)); // Adjust dimensions as needed

        JOptionPane.showMessageDialog(frame, scrollPane, "Expense Report", JOptionPane.PLAIN_MESSAGE);
    }

    private void addExpense() {
        JTextField amountField = new JTextField();
        JTextField expenseNameField = new JTextField();
        JTextField categoryField = new JTextField("Default");
        JTextArea descriptionArea = new JTextArea();

        CustomDateTimePicker customDateTimePicker = new CustomDateTimePicker();

        Object[] message = {
                "Amount:", amountField,
                "Expense Name:", expenseNameField,
                "Category:", categoryField,
                "Choose Date:", customDateTimePicker.getComponent(),
                "Description (Optional):", new JScrollPane(descriptionArea),
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Expense", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String expenseName = expenseNameField.getText();
                String category = categoryField.getText();
                String description = descriptionArea.getText();

                if (expenseName.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Expense Name and Category must be filled out.");
                    return;
                }

                LocalDateTime dateTime = customDateTimePicker.getDateTime();
                Expense expense = new Expense(budget.getExpenses().size() + 1, expenseName, amount, category, dateTime, description);
                budget.addExpense(expense);
                JOptionPane.showMessageDialog(frame, "Expense added successfully!");
                updateBalance();
                viewAllTransactions();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a numeric value for Amount.");
            }
        }
    }

    private void addIncome() {
        JTextField amountField = new JTextField();
        JTextField incomeNameField = new JTextField();
        JTextField categoryField = new JTextField("Default");
        JTextArea descriptionArea = new JTextArea();

        CustomDateTimePicker customDateTimePicker = new CustomDateTimePicker();

        Object[] message = {
                "Amount:", amountField,
                "Income Name:", incomeNameField,
                "Category:", categoryField,
                "Choose Date:", customDateTimePicker.getComponent(),
                "Description (Optional):", new JScrollPane(descriptionArea),
        };

        int option = JOptionPane.showConfirmDialog(frame, message, "Add Income", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String incomeName = incomeNameField.getText();
                String category = categoryField.getText();
                String description = descriptionArea.getText();

                if (incomeName.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Income Name and Category must be filled out.");
                    return;
                }

                LocalDateTime dateTime = customDateTimePicker.getDateTime();
                Income income = new Income(budget.getIncomes().size() + 1, incomeName, amount, category, dateTime, description);
                budget.addIncome(income);
                JOptionPane.showMessageDialog(frame, "Income added successfully!");
                updateBalance();
                viewAllTransactions();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a numeric value for Amount.");
            }
        }
    }

    private void updateBalance() {
        balanceLabel.setText("$" + String.format("%.2f", budget.getRemainingBudget()));
    }

    private void viewAllTransactions() {
        transactionsPanel.removeAll();

        List<Transaction> transactions = new ArrayList<>();
        transactions.addAll(budget.getExpenses());
        transactions.addAll(budget.getIncomes());

        // Sort transactions by date using Comparator
        Collections.sort(transactions, new Comparator<Transaction>() {
            @Override
            public int compare(Transaction t1, Transaction t2) {
                LocalDateTime date1 = LocalDateTime.parse(t1.getDate(), dateTimeFormatter);
                LocalDateTime date2 = LocalDateTime.parse(t2.getDate(), dateTimeFormatter);
                return date2.compareTo(date1);
            }
        });

        // Display transactions
        for (Transaction transaction : transactions) {
            JPanel transactionPanel = createTransactionPanel(transaction);
            transactionsPanel.add(transactionPanel);
        }

        transactionsPanel.revalidate();
        transactionsPanel.repaint();


        JScrollPane parentScrollPane = (JScrollPane) transactionsPanel.getParent().getParent();
        parentScrollPane.revalidate();
        parentScrollPane.repaint();
    }

    private JPanel createTransactionPanel(Transaction transaction) {
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        transactionPanel.setToolTipText("Right click to delete the transaction");

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 12, 5, 5));

        JLabel transactionNameLabel = new JLabel();
        JLabel transactionDateLabel = new JLabel();
        JLabel transactionAmountLabel = new JLabel();

        if (transaction instanceof Expense) {
            Expense expense = (Expense) transaction;
            transactionNameLabel.setText(expense.getName());
            transactionDateLabel.setText(expense.getDate());
            transactionAmountLabel.setText("- $" + String.format("%.2f", expense.getAmount()));
            transactionAmountLabel.setForeground(Color.RED);
        } else if (transaction instanceof Income) {
            Income income = (Income) transaction;
            transactionNameLabel.setText(income.getName());
            transactionDateLabel.setText(income.getDate());
            transactionAmountLabel.setText("+ $" + String.format("%.2f", income.getAmount()));
            transactionAmountLabel.setForeground(new Color(0x32cd32));
        }

        transactionNameLabel.setFont(transactionNameLabel.getFont().deriveFont(Font.BOLD, 14f));
        transactionDateLabel.setFont(transactionDateLabel.getFont().deriveFont(Font.PLAIN, 10f));
        transactionDateLabel.setPreferredSize(new Dimension(150, 20));
        transactionDateLabel.setForeground(Color.GRAY);

        leftPanel.add(transactionNameLabel);
        leftPanel.add(transactionDateLabel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 8, 12));

        transactionAmountLabel.setFont(transactionAmountLabel.getFont().deriveFont(Font.BOLD, 15f));
        transactionAmountLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        rightPanel.add(transactionAmountLabel, BorderLayout.EAST);

        transactionPanel.add(leftPanel, BorderLayout.WEST);
        transactionPanel.add(rightPanel, BorderLayout.EAST);

        // Add mouse listener for right-click context menu and hover effect
        transactionPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int result = JOptionPane.showConfirmDialog(frame, "Do you want to delete this transaction?", "Delete Transaction", JOptionPane.YES_NO_OPTION);
                    if (result == JOptionPane.YES_OPTION) {
                        if (transaction instanceof Expense) {
                            budget.removeExpense((Expense) transaction);
                        } else if (transaction instanceof Income) {
                            budget.removeIncome((Income) transaction);
                        }
                        updateBalance();
                        viewAllTransactions();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                transactionPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                transactionPanel.setBackground(new Color(0xE6E6E6));
                leftPanel.setBackground(new Color(0xE6E6E6));
                rightPanel.setBackground(new Color(0xE6E6E6));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                transactionPanel.setCursor(Cursor.getDefaultCursor());
                transactionPanel.setBackground(null);
                leftPanel.setBackground(null);
                rightPanel.setBackground(null);
            }
        });

        return transactionPanel;
    }


    private void addDefaultExpensesAndIncomes() {
        budget.addExpense(new Expense(1, "Lunch", 10.0, "Food", LocalDateTime.of(2023, 6, 11, 12, 0), ""));
        budget.addExpense(new Expense(2, "Transportation", 30.0, "Travel", LocalDateTime.of(2023, 6, 11, 13, 0), ""));
        budget.addExpense(new Expense(3, "Movie tickets", 20.0, "Entertainment", LocalDateTime.of(2023, 6, 11, 14, 0), ""));
        budget.addExpense(new Expense(4, "Groceries", 50.0, "Food", LocalDateTime.of(2023, 6, 11, 15, 0), ""));
        budget.addExpense(new Expense(5, "Phone bill", 60.0, "Utilities", LocalDateTime.of(2023, 6, 11, 16, 0), ""));

        budget.addIncome(new Income(1, "Freelance Work", 500.0, "Income", LocalDateTime.of(2023, 6, 11, 9, 0), ""));
        budget.addIncome(new Income(2, "Part-time Job", 300.0, "Income", LocalDateTime.of(2023, 6, 11, 10, 0), ""));
        budget.addIncome(new Income(3, "Investment", 200.0, "Income", LocalDateTime.of(2023, 6, 11, 11, 0), ""));
        budget.addIncome(new Income(4, "Gifts", 50.0, "Income", LocalDateTime.of(2023, 6, 11, 12, 0), ""));
        budget.addIncome(new Income(5, "Side Business", 150.0, "Income", LocalDateTime.of(2023, 6, 11, 13, 0), ""));

        updateBalance();
        viewAllTransactions();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BudgetTrackerGUI window = new BudgetTrackerGUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

