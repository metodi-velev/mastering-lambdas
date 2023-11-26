package com.mastering.lambdas.designpatterns.templatemethod;

/**
 * To prevent code duplication we use the template method
 */
abstract class Sub {

    public void make() {
        this.layBread()
                .addLettuce()
                .addPrimaryToppings()
                .addSauces();
    }

    protected abstract Sub addPrimaryToppings();

    protected Sub layBread() {
        System.out.println("Laying down the bread");
        return this;
    }

    protected Sub addLettuce() {
        System.out.println("Add some lettuce");
        return this;
    }

    protected Sub addSauces() {
        System.out.println("Add oil and vinegar");
        return this;
    }
}
