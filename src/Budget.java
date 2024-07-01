import java.util.ArrayList;
import java.util.List;

public class Budget {
    private double remainingBudget;
    private List<Expense> expenses;
    private List<Income> incomes;

    public Budget(double initialBudget) {
        this.remainingBudget = initialBudget;
        this.expenses = new ArrayList<>();
        this.incomes = new ArrayList<>();
    }

    public double getTotalExpenses() {
        double totalExpenses = 0.0;
        for (Expense expense : expenses) {
            totalExpenses += expense.getAmount();
        }
        return totalExpenses;
    }


    public void addExpense(Expense expense) {
        expenses.add(expense);
        remainingBudget -= expense.getAmount();
    }

    public void addIncome(Income income) {
        incomes.add(income);
        remainingBudget += income.getAmount();
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
        remainingBudget += expense.getAmount();
    }

    public void removeIncome(Income income) {
        incomes.remove(income);
        remainingBudget -= income.getAmount();
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Income> getIncomes() {
        return incomes;
    }

    public double getRemainingBudget() {
        return remainingBudget;
    }
}
