package com.drakov.lending.model;

public class Lender {

    private final Long id;
    private String name;
    private double rate;
    private double available;

    Lender(Long id, String name, double rate, double available) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.available = available;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

        Lender lender = (Lender) o;

        return id.equals(lender.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Lender{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rate=" + rate +
                ", available=" + available +
                '}';
    }
}
