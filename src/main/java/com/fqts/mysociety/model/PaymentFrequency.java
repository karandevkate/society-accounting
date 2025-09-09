package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentFrequency {
    MONTHLY,
    QUARTERLY,
    YEARLY,
    ONE_TIME;

    @JsonCreator
    public static PaymentFrequency fromValue(String value) {
        return PaymentFrequency.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }




}
