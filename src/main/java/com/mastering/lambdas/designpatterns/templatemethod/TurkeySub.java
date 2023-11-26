package com.mastering.lambdas.designpatterns.templatemethod;

public class TurkeySub extends Sub {

    public static void main(String[] args) {
        (new TurkeySub()).make();
    }

    @Override
    protected Sub addPrimaryToppings() {
        System.out.println("Add some turkey");
        return this;
    }
}

class VeggieSub extends Sub {

    public static void main(String[] args) {
        (new VeggieSub()).make();
    }

    @Override
    protected Sub addPrimaryToppings() {
        System.out.println("Add some veggies");
        return this;
    }
}
