package lk.ijse.AutoCareCenter.Util;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Paint;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static boolean isTextFiledValid(TextField textField, String text) {
        String field = "";

        switch (textField) {
            case ID:
                field = "^([A-Z][0-9]{3})$";
                break;
            case CONTACT:
                field = "^([+]94{1,3}|[0])([1-9]{2})([0-9]){7}$";
                break;
            case NAME:
                field = "^[A-z|\\s]{3,}$";
                break;
            case ADDRESS:
                field = "^([A-z0-9]|[-/,.@+]|\\s){4,}$";
                break;
            case SERVICECHARGE:
                field = "^[0-9][0-9 -]*$";
                break;
            case UNITPRICE:
                field = "^[1-9]\\d{0,7}(?:\\.\\d{1,4})?|\\.\\d{1,4}$";
                break;
            case QTY:
                field = "^[1-9][1-9 -]*$";
                break;
            case USERNAME:
                field = "[A-z]{3,}";
                break;
            case PASSWORD:
                field = "[A-Z a-z 0-9]*$";
                break;

        }

        Pattern pattern = Pattern.compile(field);

        if (text != null) {
            if (text.trim().isEmpty()) {
                return false;
            }
        } else {
            return false;
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean setTextColor(TextField location, JFXTextField field) {
        if (Regex.isTextFiledValid(location, field.getText())) {
            field.setFocusColor(Paint.valueOf("Green"));
            field.setUnFocusColor(Paint.valueOf("Green"));
            return true;
        } else {
            field.setFocusColor(Paint.valueOf("Red"));
            field.setUnFocusColor(Paint.valueOf("Red"));
            return false;
        }
    }
}

