package com.example.xddd.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDate date;
    private String region;
    private String cityStreetHouse;
    private int apartment;
    private int fl00r;
    private boolean lift;
    private String phoneNumber;
    private boolean terminalPayment;
    private boolean furnitureAssembly;

    public Order() {
    }

    public Order(LocalDate date,
                 String region,
                 String cityStreetHouse,
                 int apartment,
                 int fl00r,
                 boolean lift,
                 String phoneNumber,
                 boolean terminalPayment,
                 boolean furnitureAssembly) {
        this.date = date;
        this.region = region;
        this.cityStreetHouse = cityStreetHouse;
        this.apartment = apartment;
        this.fl00r = fl00r;
        this.lift = lift;
        this.phoneNumber = phoneNumber;
        this.terminalPayment = terminalPayment;
        this.furnitureAssembly = furnitureAssembly;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCityStreetHouse() {
        return cityStreetHouse;
    }

    public void setCityStreetHouse(String cityStreetHouse) {
        this.cityStreetHouse = cityStreetHouse;
    }

    public int getApartment() {
        return apartment;
    }

    public void setApartment(int apartment) {
        this.apartment = apartment;
    }

    public int getFl00r() {
        return fl00r;
    }

    public void setFl00r(int fl00r) {
        this.fl00r = fl00r;
    }

    public boolean isLift() {
        return lift;
    }

    public void setLift(boolean lift) {
        this.lift = lift;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isTerminalPayment() {
        return terminalPayment;
    }

    public void setTerminalPayment(boolean terminalPayment) {
        this.terminalPayment = terminalPayment;
    }

    public boolean isFurnitureAssembly() {
        return furnitureAssembly;
    }

    public void setFurnitureAssembly(boolean furnitureAssembly) {
        this.furnitureAssembly = furnitureAssembly;
    }
}
