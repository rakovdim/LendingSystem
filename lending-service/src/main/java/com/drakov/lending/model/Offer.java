package com.drakov.lending.model;

public class Offer {

    private final Long id;
    private String lender;
    private double rate;
    private double available;

    Offer(Long id, String lender, double rate, double available) {
        this.id = id;
        this.lender = lender;
        this.rate = rate;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public String getLender() {
        return lender;
    }

    public void setLender(String lender) {
        this.lender = lender;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAvailable() {
        return available;
    }

    public void setAvailable(double available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        return id.equals(offer.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Offer{" +
                "id=" + id +
                ", lender='" + lender + '\'' +
                ", rate=" + rate +
                ", available=" + available +
                '}';
    }
}
