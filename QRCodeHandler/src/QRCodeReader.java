import com.google.zxing.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class QRCodeReader {
    public static Map<String, String> decodeQRCode(File file) throws IOException {
        try {
            BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);
            return parseQRData(result.getText());
        } catch (NotFoundException e) {
            throw new IOException("QR Code not found or could not be decoded", e);
        }
    }

    private static Map<String, String> parseQRData(String qrData) {
        Map<String, String> dataMap = new HashMap<>();
        String[] parts = qrData.split("\\*");
        for (String part : parts) {
            String[] keyValue = part.split(":");
            if (keyValue.length == 2) {
                dataMap.put(keyValue[0], keyValue[1]);
            }
        }
        return dataMap;
    }
}
