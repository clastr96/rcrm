package com.example.raynetcrm.dto;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientCsvBean {
    @CsvBindByPosition(position = 0)
    private String regNumber;
    @CsvBindByPosition(position = 1)
    private String title;
    @CsvBindByPosition(position = 2)
    private String email;
    @CsvBindByPosition(position = 3)
    private String phone;

    public boolean isValid() {
        return isValidEmail(email) && isValidPhoneNumber(phone) &&
                isValidStringLength(regNumber, 255) &&
                isValidStringLength(title, 255) &&
                isValidStringLength(email, 255) &&
                isValidStringLength(phone, 255);
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("\\+\\d{12}");
    }

    private boolean isValidStringLength(String str, int maxLength) {
        return str == null || str.length() <= maxLength;
    }
}
