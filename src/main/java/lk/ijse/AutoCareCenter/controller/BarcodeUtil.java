package lk.ijse.AutoCareCenter.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.imageio.ImageIO;
import java.io.File;


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


    public static void printBarcode(String barcodeText) throws Exception {

        File file = new File("barcodes/" + barcodeText + ".png");

        BufferedImage image = ImageIO.read(file);

        PrinterJob printerJob = PrinterJob.getPrinterJob();

        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {

            if (pageIndex > 0) {
                return java.awt.print.Printable.NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

            g2d.drawImage(image, 50, 50, null);

            return java.awt.print.Printable.PAGE_EXISTS;
        });

        if (printerJob.printDialog()) {
            System.out.println("Printing...");
            printerJob.print();
        }
    }
}