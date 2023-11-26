package com.mastering.lambdas.misc;

import javax.validation.*;
import java.lang.annotation.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

public class ClassLevelConstraintExample {

    //using our custom constraint on class level
    @ValidOrder
    public static class Order {
        private final BigDecimal price;
        private final BigDecimal quantity;

        public Order (BigDecimal price, BigDecimal quantity) {
            this.price = price;
            this.quantity = quantity;
        }

        public BigDecimal getPrice () {
            return price;
        }

        public BigDecimal getQuantity () {
            return quantity;
        }

        public BigDecimal getTotalPrice () {
            return (price != null && quantity != null ?
                    price.multiply(quantity) : BigDecimal.ZERO)
                    .setScale(2, RoundingMode.CEILING);
        }

        @Override
        public String toString() {
            return "Order{" +
                    "price=" + price +
                    ", quantity=" + quantity +
                    '}';
        }
    }

    //Our constraint definition
    @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = OrderValidator.class)
    @Documented
    public static @interface ValidOrder {
        String message() default "total price must be 50 or greater for online order. " +
                "Found: ${validatedValue.totalPrice}";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    //our validator
    public static class OrderValidator implements ConstraintValidator<ValidOrder, Order> {
        @Override
        public void initialize (ValidOrder constraintAnnotation) {
        }

        @Override
        public boolean isValid (Order order, ConstraintValidatorContext context) {
            if (order.getPrice() == null || order.getQuantity() == null) {
                return false;
            }
            return order.getTotalPrice()
                    .compareTo(new BigDecimal(50)) >= 0;
        }
    }

    //performing validation
    public static void main (String[] args)  {
        Order order = new Order(new BigDecimal(4.5), new BigDecimal(9));

        Validator validator = getValidator();
        Set<ConstraintViolation<Order>> constraintViolations = validator.validate(order);

        if (constraintViolations.size() > 0) {
            constraintViolations.stream().forEach(
                    ClassLevelConstraintExample::printError);
        } else {
            //proceed using order
            System.out.println(order);
        }
    }

    private static Validator getValidator(){
        Configuration<?> config = Validation.byDefaultProvider().configure();
        ValidatorFactory factory = config.buildValidatorFactory();
        Validator validator = factory.getValidator();
        factory.close();
        return validator;
    }

    private static void printError (
            ConstraintViolation<Order> violation) {
        System.out.println(violation.getPropertyPath() + " " + violation.getMessage());
    }
}

