package kw.soft.gourmet.domain.restaurant;

import java.time.LocalTime;

public interface TimeChecker {
    public boolean isWithinBusinessHour(final BusinessHour businessHour, final LocalTime now);
}
