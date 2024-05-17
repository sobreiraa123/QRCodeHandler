import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper {
    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/QRCodeInfo", "postgres", "admin");
    }

    public void insertInvoice(Map<String, String> qrData) throws SQLException {
        String sql = "INSERT INTO invoices (nif_emitente, nif_adquirente, pais_adquirente, tipo_documento, estado_documento, data_documento, identificacao_documento, atcud, espaco_fiscal, base_tributavel_iva_taxa_normal, total_iva_taxa_normal, total_impostos, total_documento, hash, numero_certificado, outras_informacoes, custo, tipo_despesa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, qrData.getOrDefault("A", ""));
            pstmt.setString(2, qrData.getOrDefault("B", ""));
            pstmt.setString(3, qrData.getOrDefault("C", ""));
            pstmt.setString(4, qrData.getOrDefault("D", ""));
            pstmt.setString(5, qrData.getOrDefault("E", ""));

            String dateStr = qrData.getOrDefault("F", "");
            if (!dateStr.isEmpty() && dateStr.length() == 8) {
                String formattedDate = dateStr.substring(0, 4) + "-" + dateStr.substring(4, 6) + "-" + dateStr.substring(6, 8);
                pstmt.setDate(6, Date.valueOf(formattedDate));
            } else {
                pstmt.setDate(6, null);
            }

            pstmt.setString(7, qrData.getOrDefault("G", ""));
            pstmt.setString(8, qrData.getOrDefault("H", ""));
            pstmt.setString(9, qrData.getOrDefault("I1", ""));

            pstmt.setBigDecimal(10, getBigDecimal(qrData.get("I7")));
            pstmt.setBigDecimal(11, getBigDecimal(qrData.get("I8")));
            pstmt.setBigDecimal(12, getBigDecimal(qrData.get("N")));
            pstmt.setBigDecimal(13, getBigDecimal(qrData.get("O")));

            pstmt.setString(14, qrData.getOrDefault("Q", ""));
            pstmt.setString(15, qrData.getOrDefault("R", ""));
            pstmt.setString(16, qrData.getOrDefault("S", ""));
            pstmt.setString(17, qrData.getOrDefault("Custo", ""));
            pstmt.setString(18, qrData.getOrDefault("Tipo de Despesa", ""));
            
            pstmt.executeUpdate();
        }
    }

    public Map<String, Double> getExpenses() throws SQLException {
        String sql = "SELECT tipo_despesa, SUM(CAST(custo AS DOUBLE PRECISION)) AS total FROM invoices GROUP BY tipo_despesa";
        Map<String, Double> expenses = new HashMap<>();

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String tipoDespesa = rs.getString("tipo_despesa");
                Double total = rs.getDouble("total");
                expenses.put(tipoDespesa, total);
            }
        }
        return expenses;
    }

    private java.math.BigDecimal getBigDecimal(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return new java.math.BigDecimal(value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid number format for value: " + value);
            }
        }
        return null;
    }
}
