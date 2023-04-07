package kw.soft.gourmet.restaurant.domain;

import java.util.regex.Pattern;
import kw.soft.gourmet.restaurant.exception.Code;
import kw.soft.gourmet.restaurant.exception.RestaurantException;
import lombok.Getter;

@Getter
public class PhoneNumber {
    private static final Pattern pattern = Pattern.compile("^(0(2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))-(\\d{3,4})-(\\d{4})$");

    private String phoneNumber;

    public PhoneNumber(String phoneNumber) {
        checkValidPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    private void checkValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !pattern.matcher(phoneNumber).matches()) {
            throw new RestaurantException(Code.INVALID_PHONE_NUMBER);
        }
    }
}
