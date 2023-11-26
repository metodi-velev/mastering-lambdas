package com.mastering.lambdas.designpatterns.composite;

import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * <b>Composite</b>: A mechanism for treating individual(scalar) objects and
 * composition of objects in a uniform manner. Composite is a structural design pattern that
 * lets you compose objects into tree structures and then work with these structures as if they were individual objects.
 */
interface ValueContainer extends Iterable<Integer> {}

class SingleValue implements ValueContainer {
    public int value;

    // please leave this constructor as-is
    public SingleValue(int value) {
        this.value = value;
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return Collections.singleton(this.value).iterator();
    }

    @Override
    public void forEach(Consumer<? super Integer> action) {
        action.accept(this.value);
    }

    @Override
    public Spliterator<Integer> spliterator() {
        return Collections.singleton(this.value).spliterator();
    }
}

class ManyValues extends ArrayList<Integer> implements ValueContainer {
}


class MyList extends ArrayList<ValueContainer> {
    // please leave this constructor as-is
    public MyList(Collection<? extends ValueContainer> c) {
        super(c);
    }

    SingleValue one = new SingleValue(1);

    public int sum() {
        // todo
        Integer result = 0;
        for (ValueContainer value : this) {
            for (Integer singleValue : value) {
                result += singleValue;
            }
        }
        System.out.println("Result is: " + Objects.requireNonNull(result, "Result is not available."));
        return result;
    }

    public static void main(String[] args) {
        SingleValue one = new SingleValue(1);
        SingleValue two = new SingleValue(2);
        SingleValue three = new SingleValue(3);
        ManyValues manyValues = new ManyValues();
        manyValues.addAll(Arrays.asList(one.value, two.value, three.value));
        SingleValue four = new SingleValue(4);
        SingleValue five = new SingleValue(5);
        SingleValue six = new SingleValue(6);
        SingleValue seven = new SingleValue(7);
        SingleValue eight = new SingleValue(8);
        SingleValue nine = new SingleValue(9);
        ManyValues manyValues2 = new ManyValues();
        manyValues.addAll(Arrays.asList(eight.value, nine.value));
        SingleValue ten = new SingleValue(10);
        MyList myList = new MyList(Arrays.asList(manyValues, four, five, six, seven, manyValues2, ten));
        myList.sum();
    }
}