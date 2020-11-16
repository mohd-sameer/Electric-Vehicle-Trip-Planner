package com.example.seccharge;

public class Vehicle {

    private Long id;
    private String vehicleVin;
    private String plateNumber ;
    private String make;
    private String model;
    private String year;
    private String vechicleType;
    private String vechicleColor ;
    private Boolean primaryVehicle;
    private Long userid;

    Vehicle(Long id, String vehicleVin, String plateNumber, String make, String model, String year, String vechicleType, String vechicleColor, Boolean primaryVehicle, Long userid) {
        this.id = id;
        this.vehicleVin = vehicleVin;
        this.plateNumber = plateNumber;
        this.make = make;
        this.model = model;
        this.year = year;
        this.vechicleType = vechicleType;
        this.vechicleColor = vechicleColor;
        this.primaryVehicle = primaryVehicle;
        this.userid = userid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVechicleType() {
        return vechicleType;
    }

    public void setVechicleType(String vechicleType) {
        this.vechicleType = vechicleType;
    }

    public String getVechicleColor() {
        return vechicleColor;
    }

    public void setVechicleColor(String vechicleColor) {
        this.vechicleColor = vechicleColor;
    }

    public Boolean getPrimaryVehicle() {
        return primaryVehicle;
    }

    public void setPrimaryVehicle(Boolean primaryVehicle) {
        this.primaryVehicle = primaryVehicle;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }


    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
