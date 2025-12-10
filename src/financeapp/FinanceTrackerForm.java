package financeapp;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class FinanceTrackerForm {
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> typeCombo;
    private JButton addButton;
    private JButton updateButton;
    private JTable transactionTable;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;
    private JPanel mainPanel;

    private TransactionManager manager;


    private String selectedTransactionId = null;

    public FinanceTrackerForm() {
        manager = new TransactionManager();

        loadDataIntoTable();
        updateSummary();


        addButton.addActionListener(e -> {
            try {
                String type = (String) typeCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionField.getText();
                if (description.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Description cannot be empty.");
                    return;
                }

                Transaction t = new Transaction(type, amount, description);
                manager.addTransaction(t);
                loadDataIntoTable();
                updateSummary();
                amountField.setText("");
                descriptionField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Amount must be a number");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error saving transaction: " + ex.getMessage());
            }
        });


        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        transactionTable.getSelectionModel().addListSelectionListener((ListSelectionListener) e -> {
            int selectedRow = transactionTable.getSelectedRow();
            if (selectedRow >= 0) {

                selectedTransactionId = String.valueOf(manager.getAllTransactions().get(selectedRow).getId());


                typeCombo.setSelectedItem(transactionTable.getValueAt(selectedRow, 0).toString());
                amountField.setText(transactionTable.getValueAt(selectedRow, 1).toString());
                descriptionField.setText(transactionTable.getValueAt(selectedRow, 2).toString());
            }
        });


        updateButton.addActionListener(e -> {
            if (selectedTransactionId != null) {
                try {
                    String type = (String) typeCombo.getSelectedItem();
                    double amount = Double.parseDouble(amountField.getText());
                    String description = descriptionField.getText();
                    if (description.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Description cannot be empty.");
                        return;
                    }


                    manager.updateTransaction(selectedTransactionId, type, amount, description);


                    loadDataIntoTable();
                    updateSummary();


                    amountField.setText("");
                    descriptionField.setText("");
                    selectedTransactionId = null;

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Amount must be a number");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error updating transaction: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Odaberite transakciju za ažuriranje");
            }
        });
    }

    private void loadDataIntoTable() {
        ArrayList<Transaction> list = manager.getAllTransactions();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Type");
        model.addColumn("Amount");
        model.addColumn("Description");

        for (Transaction t : list) {
            model.addRow(new Object[]{
                    t.getType(),
                    t.getAmount(),
                    t.getDescription()
            });
        }
        transactionTable.setModel(model);
    }

    private void updateSummary() {
        double income = manager.getTotalIncome();
        double expense = manager.getTotalExpense();
        double balance = income - expense;

        incomeLabel.setText("Income: " + income);
        expenseLabel.setText("Expense: " + expense);
        balanceLabel.setText("Balance: " + balance);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
