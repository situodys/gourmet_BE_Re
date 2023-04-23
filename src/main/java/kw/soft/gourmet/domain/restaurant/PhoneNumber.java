package kw.soft.gourmet.domain.restaurant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;
import kw.soft.gourmet.domain.restaurant.exception.Code;
import kw.soft.gourmet.domain.restaurant.exception.RestaurantException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class PhoneNumber {
    private static final Pattern pattern = Pattern.compile(
            "^(0(1[0-9]|2|3[1-3]|4[1-4]|5[1-5]|6[1-4]))-(\\d{3,4})-(\\d{4})$");

    @Column(name = "phone_number", length = 50)
    private String phoneNumber;

    public PhoneNumber(final String phoneNumber) {
        checkValidPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    private void checkValidPhoneNumber(final String phoneNumber) {
        if (phoneNumber == null || !pattern.matcher(phoneNumber).matches()) {
            throw new RestaurantException(Code.INVALID_PHONE_NUMBER);
        }
    }
}
