package com.hospital.backend.Models;

import java.util.List;
import java.util.Random;

public enum ShiftTypes {
    MORNING("1"),
    AFTERNOON("2"),
    NIGHT("3"),
    SHORT("8"),
    SICK_LEAVE("CM"),
    REST_LEAVE("CO"),
    FREE("L");

    private final String value;

    ShiftTypes(final String newValue) {
        value = newValue;
    }


    public String getValue() { return value; }

    public static ShiftTypes randomShift()  {
        Random random = new Random();
        List<ShiftTypes> shiftTypes=List.of(ShiftTypes.MORNING,ShiftTypes.AFTERNOON);
        int index= random.nextInt(shiftTypes.size());
        return shiftTypes.get(index);
    }
}
