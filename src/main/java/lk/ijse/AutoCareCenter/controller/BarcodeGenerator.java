package lk.ijse.AutoCareCenter.controller;

import java.util.UUID;

public class BarcodeGenerator {

    public static String generateBarcode() {
        return "GO-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
