import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MainApp extends JFrame {
    private JTextArea textArea;
    private JButton btnLoad;
    private JButton btnShowExpenses;
    private DatabaseHelper dbHelper = new DatabaseHelper();
    private CSVExporter csvExporter = new CSVExporter();
    private String[] expenseTypes = {"Médico", "Pessoal"};

    public MainApp() {
        super("QR Code Invoice Reader");
        initializeUI();
    }

    private void initializeUI() {
        this.setLayout(new BorderLayout());
        textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        btnLoad = new JButton("Load QR Code");
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAndProcessImage();
            }
        });

        btnShowExpenses = new JButton("Show Expenses");
        btnShowExpenses.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showExpenses();
            }
        });

        JPanel panel = new JPanel();
        panel.add(btnLoad);
        panel.add(btnShowExpenses);

        this.add(panel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void loadAndProcessImage() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                Map<String, String> dataMap = QRCodeReader.decodeQRCode(file);
                dataMap.putAll(getAdditionalExpenseInfo());
                textArea.setText(dataMap.toString());
                dbHelper.insertInvoice(dataMap);
                csvExporter.export(dataMap, "invoices.csv");
            } catch (IOException e) {
                textArea.setText("Error processing QR Code: " + e.getMessage());
                e.printStackTrace();
            } catch (SQLException e) {
                textArea.setText("Database error: " + e.getMessage());
                e.printStackTrace();
            } catch (Exception e) {
                textArea.setText("Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> getAdditionalExpenseInfo() {
        Map<String, String> additionalInfo = new HashMap<>();
        
        JTextField costField = new JTextField(5);
        JComboBox<String> typeComboBox = new JComboBox<>(expenseTypes);
        JTextField newTypeField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Custo:"));
        panel.add(costField);
        panel.add(new JLabel("Tipo de Despesa:"));
        panel.add(typeComboBox);
        panel.add(new JLabel("Novo Tipo (se aplicável):"));
        panel.add(newTypeField);

        int result = JOptionPane.showConfirmDialog(null, panel, 
                 "Introduza o Custo e o Tipo de Despesa", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String cost = costField.getText().replace(',', '.'); // Convert comma to dot
            String type = newTypeField.getText();
            if (type.isEmpty()) {
                type = (String) typeComboBox.getSelectedItem();
            } else {
                // Add new type to the array
                String[] newExpenseTypes = new String[expenseTypes.length + 1];
                System.arraycopy(expenseTypes, 0, newExpenseTypes, 0, expenseTypes.length);
                newExpenseTypes[expenseTypes.length] = type;
                expenseTypes = newExpenseTypes;
            }
            additionalInfo.put("Custo", cost);
            additionalInfo.put("Tipo de Despesa", type);
        }
        return additionalInfo;
    }

    private void showExpenses() {
        try {
            Map<String, Double> expenses = dbHelper.getExpenses();
            double total = expenses.values().stream().mapToDouble(Double::doubleValue).sum();

            StringBuilder expenseDetails = new StringBuilder();
            for (Map.Entry<String, Double> entry : expenses.entrySet()) {
                expenseDetails.append("Tipo de Despesa: ").append(entry.getKey())
                              .append(", Total Gasto: ").append(entry.getValue()).append("\n");
            }
            expenseDetails.append("\nTotal Gasto: ").append(total);

            JOptionPane.showMessageDialog(this, expenseDetails.toString(), "Despesas", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching expenses: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainApp();
            }
        });
    }
}
