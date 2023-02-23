package com.mastering.lambdas.chapter2;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * <code><i>args -> expr</i></code> <br>
 * can be seen as a short form of the corresponding statement lambda <br>
 * <code><i>args -> { return expr; }</i></code>
 */
public class Demo2 {

    private static final Demo2 demo = new Demo2();
    Object i, j;
    IntUnaryOperator iuo = i -> {
        int j = 3;
        return i + j;
    };

    DoubleUnaryOperator sqrt = Math::sqrt;
    //DoubleUnaryOperator sqrt = x -> Math.sqrt(x);

    IntUnaryOperator iuoInt = x -> x * 2;
    DoubleUnaryOperator duoDouble = x -> x * 2;

    /**
     * Using objects
     */
    Consumer<String> consumer = System.out::print;  //s -> System.out.print(s)

    /**
     * Filtering values
     */
    Predicate<String> filter = String::isEmpty;    // s -> s.isEmpty()

    IntPredicate ip = i -> i > 0;

    /**
     * Factory methods, no args
     */
    Supplier<String> supplier = String::new;      //() -> new String()

    /**
     * Transforming or selecting from objects
     */
    Function<Integer, String> function = String::valueOf; //s -> String.valueOf(s)

    public interface IntUnaryOperator {
        int applyAsInt(int operand);
    }

    public interface DoubleUnaryOperator {
        double applyAsDouble(double operand);
    }

    /**
     * Primitive specializations: these interfaces, replace type parameters with primitive
     * types. Examples include lines 63-73:
     */
    interface LongFunction<R> {
        R apply(long value);
    }

    interface ToIntFunction<T> {
        int applyAsInt(T value);
    }

    interface LongToIntFunction {
        int applyAsInt(long value);
    }

    /**
     * The function types of Consumer, Predicate, and Function all take a single
     * argument. There are corresponding interfaces that take two arguments, for
     * example lines 80-90:
     */
    interface BiConsumer<T, U> {
        void accept(T t, U u);
    }

    interface BiFunction<T, U, R> {
        R apply(T t, U u);
    }

    interface ToIntBiFunction<T, U> {
        int apply(T t, U u);
    }

    /**
     * Common use cases for Function require its parameter and result to have the
     * same type. We saw an example of this in the parameter to List.replaceAll.
     * These use cases are met by specializing the variations of Function to corresponding
     * Operators, for example lines 98-104:
     */
    interface UnaryOperator<T> extends Function<T, T> {}

    interface BinaryOperator<T> extends BiFunction<T, T, T> {}

    interface IntBinaryOperator {
        int applyAsInt(int left, int right);
    }

    BiConsumer<String, Integer> biConsumer = (s, i) -> new HashMap<>(Map.of(s, String.valueOf(i)));

    BiFunction<String, String, String> biFunction = (a, b) -> a + b;
    BinaryOperator<Integer> binaryOperator = Integer::sum;

    Comparator<String> cc = String::compareToIgnoreCase; //(String s1, String s2) -> s1.compareToIgnoreCase(s2);

    Comparator<String> cs = Comparator.comparing(String::length);  //s -> s.length()

    //Comparator personComp = Comparator.comparing(p -> p.getLastName());

    //With the unbound method reference
    //Comparator personComp = Comparator.comparing(Person::getLastName);

    IntBinaryOperator[] calculatorOps = new IntBinaryOperator[]{
            Integer::sum, (x, y) -> x - y, (x, y) -> x * y, (x, y) -> x / y
    };

    Callable<Runnable> callable = () -> () -> System.out.println("hi");

    Object s = (Supplier) () -> "hi";
    Object cast = (Callable) () -> "hi";

    Callable c1 = (Callable) s;

    Stream<String> stringStream = Stream.of("a.txt", "b.txt", "c.txt");
    Stream<File> fileStream = stringStream.map(File::new);

    UnaryOperator<Integer> b = Integer::intValue;   //UnaryOperator<Integer> b = x -> x.intValue();

    IntFunction f = (int i) -> i++;

    Consumer<Thread> thr = (Thread t) -> t.start();  //Consumer<Thread> thr = Thread::start;

    UnaryOperator<Integer> unaryOperator = x -> x.intValue(); //x -> x

    interface Foo1 {
        void bar(List<String> arg);
    }

    interface Foo2 {
        void bar(List arg);
    }

    interface Foo extends Foo1, Foo2 {
        public void bar(List arg);
    }

    void bar(IntFunction<String> f) {}

    void bar(DoubleFunction<String> f) {}

    Runnable returnDatePrinter() {
        return () -> System.out.print(new Date());
    }

    public static <T, U extends Comparable<U>>
    Comparator<T> comparing(Function<T, U> keyExtractor) {return null;}

    Comparator<String> css = Comparator.comparing(s -> s.length());

    /**
     * For Comparator.comparing, the Java 8 designers
     * wanted to avoid forcing the user to provide explicit lambda typing in this way,
     * so they replaced the overloads that accepted <b>ToIntFunction</b>, <b>ToLongFunction</b>,
     * and <b>ToDoubleFunction</b> with new methods <b>comparingInt</b>, <b>comparingLong</b>, and
     * <b>comparingDouble</b>.
     */
    public static <T>
    Comparator<T> comparing(ToIntFunction<T> keyExtractor) {
        return null;
    }

    Supplier<Exception> execNoArgs = () -> new Exception();
    Function<String, Exception> exec = s -> new Exception(s + " message");

    <T> void foo(Supplier<T> factory) {}

    <T, U> void foo(Function<T, U> transformer) {}

    public Stream<Integer> sortArray() {
        //will fail to compile with the error message “reference to foo is ambiguous.”
        //foo(Exception::new);
        this.<Exception>foo(Exception::new);
        this.<String, Exception>foo(Exception::new);
        Integer[] intArray = new Integer[]{8, 13, 3, 6, 1, 5};
        Arrays.sort(intArray, Integer::compareUnsigned); //With lambda: Arrays.sort(integerArray, (x,y) -> Integer.compareUnsigned(x, y));
        return Arrays.stream(intArray);
    }

    public void mapReplaceAllExample() {
        TreeMap<String, String> map = new TreeMap<>(Map.of("alpha", "X", "bravo", "Y", "charlie", "Z"));

        String str = "alpha-bravo-charlie";
        //map.replaceAll(str::replace);
        //map.replaceAll((k, v) -> str.replace(k, v));

        map.replaceAll(String::concat);
        //map.replaceAll((k, v) -> k.concat(v));

        System.out.println(map);

        //foo(Exception::new);          //java: reference to foo is ambiguous
        this.<Exception>foo(Exception::new);
        this.<String, Exception>foo(Exception::new);

        //bar(String::valueOf);           //java: reference to bar is ambiguous

        bar((IntFunction<String>) String::valueOf);
        bar((DoubleFunction<String>) String::valueOf);
        bar((double i) -> String.valueOf(i));
        bar((int i) -> String.valueOf(i));
    }

    public static void main(String[] args) {
        new Thread(demo.returnDatePrinter()).start();
        boolean flag = args.length == 2;
        Callable<Integer> c = flag ? (() -> 23) : (() -> 42);

        demo.sortArray().forEach(System.out::println);
        demo.mapReplaceAllExample();

        int sum = Stream.of(1, 2, 3, 4, 5)
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println(sum);
    }

    class FooClass {
        Object i, j;
        IntUnaryOperator iuo = i -> {
            int j = 3;
            return i + j;
        };
    }

}
