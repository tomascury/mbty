package com.mobiquityinc.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class Package implements Serializable {

    private BigDecimal maxWeight;

    private List<PackageItem> items;

    public Package() {
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    public List<PackageItem> getItems() {
        return items;
    }

    public void setItems(List<PackageItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Package aPackage = (Package) o;

        if (maxWeight != null ? !maxWeight.equals(aPackage.maxWeight) : aPackage.maxWeight != null) return false;
        return items != null ? items.equals(aPackage.items) : aPackage.items == null;
    }

    @Override
    public int hashCode() {
        int result = maxWeight != null ? maxWeight.hashCode() : 0;
        result = 31 * result + (items != null ? items.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Package{" +
                "maxWeight=" + maxWeight +
                ", items=" + items +
                '}';
    }
}
