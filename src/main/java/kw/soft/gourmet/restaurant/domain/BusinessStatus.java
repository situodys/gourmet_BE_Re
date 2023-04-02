package kw.soft.gourmet.restaurant.domain;

public enum BusinessStatus {
    OPEN,
    CLOSE,
    BREAK;

    public BusinessStatus determineStatus(BusinessStatus another) {
        if (this == BREAK || another == BREAK) {
            return BREAK;
        }
        if (this == OPEN || another == OPEN) {
            return OPEN;
        }
        return CLOSE;
    }
}
