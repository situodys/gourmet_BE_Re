package kw.soft.gourmet.domain.restaurant;

public enum BusinessStatus {
    OPEN,
    CLOSE,
    BREAK;

    public BusinessStatus determineStatus(final BusinessStatus another) {
        if (this == BREAK || another == BREAK) {
            return BREAK;
        }
        if (this == OPEN || another == OPEN) {
            return OPEN;
        }
        return CLOSE;
    }
}
