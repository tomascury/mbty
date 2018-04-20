package com.mobiquityinc.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class PackageItem implements Serializable {

    private int index;

    private BigDecimal weight;

    private BigDecimal cost;

    public PackageItem() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PackageItem that = (PackageItem) o;

        if (weight != null ? !weight.equals(that.weight) : that.weight != null) return false;
        return cost != null ? cost.equals(that.cost) : that.cost == null;
    }

    @Override
    public int hashCode() {
        int result = weight != null ? weight.hashCode() : 0;
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PackageItem{" +
                "index=" + index +
                ", weight=" + weight +
                ", cost=" + cost +
                '}';
    }
}
