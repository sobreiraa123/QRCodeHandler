import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class MainApp extends JFrame {
    private JTextArea textArea;
    private JButton btnLoad;
    private DatabaseHelper dbHelper = new DatabaseHelper();
    private CSVExporter csvExporter = new CSVExporter();

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

        this.add(btnLoad, BorderLayout.NORTH);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainApp();
            }
        });
    }
}
