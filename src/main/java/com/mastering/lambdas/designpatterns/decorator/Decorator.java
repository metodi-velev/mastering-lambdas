package com.mastering.lambdas.designpatterns.decorator;

public class Decorator {
    public static void main(String[] args) {
        CarService carService = new OilChange(new TireRotation(new BasicInspection()));
        System.out.println(carService.getDescription());
        System.out.println(carService.getCost());
    }
}

class BasicInspection implements CarService {
    public int getCost() {
        return 25;
    }

    @Override
    public String getDescription() {
        return "Basic Inspection";
    }
}

class OilChange implements CarService {
    private CarService carService;

    public OilChange(CarService carService) {
        this.carService = carService;
    }

    public int getCost() {
        return 29 + carService.getCost();
    }

    @Override
    public String getDescription() {
        return carService.getDescription() + ", and oil change";
    }
}

class TireRotation implements CarService {
    private CarService carService;

    public TireRotation(CarService carService) {
        this.carService = carService;
    }

    public int getCost() {
        return 15 + carService.getCost();
    }

    @Override
    public String getDescription() {
        return carService.getDescription() + ", and a tire rotation";
    }
}

