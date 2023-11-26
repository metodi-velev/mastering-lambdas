package com.mastering.lambdas.designpatterns.factorymethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlanFactory {
    //use getPlan method to get object of type Plan
    public Plan getPlan(String planType) {
        if (planType == null) {
            return null;
        }
        String uppercasePlanType = planType.toUpperCase();
        switch (uppercasePlanType) {
            case "DOMESTICPLAN":
                return new DomesticPlan();
            case "COMMERCIALPLAN":
                return new CommercialPlan();
            case "INSTITUTIONALPLAN":
                return new InstitutionalPlan();
            default:
                return null;
        }
    }
}

abstract class Plan {
    protected double rate;

    abstract void getRate();

    public void calculateBill(int units) {
        System.out.println(units * rate);
    }
}

class DomesticPlan extends Plan {
    public void getRate() {
        rate = 3.50;
    }
}

class CommercialPlan extends Plan {
    public void getRate() {
        rate = 7.50;
    }
}

class InstitutionalPlan extends Plan {
    public void getRate() {
        rate = 5.50;
    }
}

class GenerateBill {
    public static void main(String args[]) {
        PlanFactory planFactory = new PlanFactory();
        String planName = "";
        int units = 0;
        System.out.print("Enter the name of plan for which the bill will be generated: ");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            planName = br.readLine();
            System.out.print("Enter the number of units for bill will be calculated: ");
            units = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            e.getMessage();
        }
        Plan p = planFactory.getPlan(planName);
        //call getRate() method and calculateBill()method of DomesticPaln.

        System.out.print("Bill amount for " + planName + " of " + units + " units is: ");
        p.getRate();
        p.calculateBill(units);
    }
}

