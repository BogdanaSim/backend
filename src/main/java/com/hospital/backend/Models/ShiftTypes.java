package com.hospital.backend.Models;

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
}
