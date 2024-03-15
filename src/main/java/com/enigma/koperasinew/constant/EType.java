package com.enigma.koperasinew.constant;

public enum EType {
    SILVER(0.03),
    GOLD(0.05),
    PLATINUM(0.05);

    private final double interestRate;

    EType(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }
}
