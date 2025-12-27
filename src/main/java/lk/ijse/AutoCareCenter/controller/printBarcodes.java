package lk.ijse.AutoCareCenter.controller;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.imageio.ImageIO;
import java.io.File;


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

        // barcode image draw
        g2d.drawImage(image, 50, 50, null);

        return java.awt.print.Printable.PAGE_EXISTS;
    });

    if (printerJob.printDialog()) {
        printerJob.print();
    }
}
