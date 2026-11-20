package edu.utsa.cs3443.topscout.model.fields;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * A date answer, written MM/dd/yyyy (for example 03/07/2026). An empty answer is allowed.
 */
public class Date extends TextArea {

    /** The format the Scout types. STRICT so 02/30/2026 is rejected. */
    public static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public Date(String id, String label, String hint, boolean required, String value) {
        super(id, label, hint, required, value);
    }

    public String toString() {
        return "Date{" +
                "date='" + getValue() + '\'' +
                "} " + super.toStringWithoutValue();
    }

    /**
     * Saves the date if it is empty or a real date in MM/dd/yyyy form.
     *
     * @return true if the value was accepted, false if it was rejected
     */
    @Override
    public boolean setValue(String date) {
        if (date == null) {
            return false;
        }
        if (!date.isEmpty()) {
            try {
                LocalDate.parse(date, FORMAT);
            } catch (DateTimeParseException e) {
                return false;
            }
        }
        return super.setValue(date);
    }
}
