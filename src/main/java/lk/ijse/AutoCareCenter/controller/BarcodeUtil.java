package lk.ijse.AutoCareCenter.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BarcodeUtil {

    public static Path generateBarcodeImage(String barcodeText) throws Exception {

        File dir = new File("barcodes");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        BitMatrix matrix = new MultiFormatWriter().encode(
                barcodeText,
                BarcodeFormat.CODE_128,
                300,
                100
        );

        Path path = Paths.get("barcodes/" + barcodeText + ".png");
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
        System.out.println("Barcode saved to: " + path.toAbsolutePath());
        return path;
    }
}
