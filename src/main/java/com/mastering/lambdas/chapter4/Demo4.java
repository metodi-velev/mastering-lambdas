package com.mastering.lambdas.chapter4;

import com.mastering.lambdas.chapter1.Point;
import com.mastering.lambdas.chapter3.Book;
import com.mastering.lambdas.chapter3.Demo3;
import com.mastering.lambdas.chapter3.LibraryInit;
import com.mastering.lambdas.chapter3.Topic;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.Year;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.*;

import static java.util.stream.Collectors.groupingBy;

@Getter
@Setter
public class Demo4 {

    private List<Book> library;
    private Map<Topic, List<Book>> booksByTopic;
    private Map<String, Year> titleToPubDate;
    private Map<Boolean, List<Book>> fictionOrNon;
    private Map<Topic, Optional<Book>> mostAuthorsByTopic;
    private Map<Topic, Integer> volumeCountByTopic;
    private Optional<Topic> mostPopularTopic;
    private Map<Topic, String> concatenatedTitlesByTopic;

    public Demo4() {
        library = new LibraryInit().getLibrary();
        booksByTopic = booksByTopic();
        titleToPubDate = titleToPubDate();
        fictionOrNon = fictionOrNon();
        mostAuthorsByTopic = mostAuthorsByTopic();
        volumeCountByTopic = volumeCountByTopic();
        mostPopularTopic = mostPopularTopic();
        concatenatedTitlesByTopic = concatenatedTitlesByTopic();
    }

    /**
     * A map classifying books by topic:
     */
    private Map<Topic, List<Book>> booksByTopic() {
        Map<Topic, List<Book>> booksByTopic = library.stream()
                .collect(groupingBy(Book::getTopic));
        return booksByTopic;
    }

    /**
     * An ordered map from book titles to publication date of latest edition:
     */
    private Map<String, Year> titleToPubDate() {
        Map<String, Year> titleToPubDate = library.stream()
                .collect(Collectors.toMap(Book::getTitle,
                        Book::getPubDate,
                        BinaryOperator.maxBy(Comparator.naturalOrder()),
                        TreeMap::new));
        return titleToPubDate;
    }

    /**
     * A map partitioning books into fiction (mapped to true) and non-fiction ( false):
     */
    private Map<Boolean, List<Book>> fictionOrNon() {
        Map<Boolean, List<Book>> fictionOrNon = library.stream()
                .collect(Collectors.partitioningBy(b -> b.getTopic() == Topic.FICTION));
        return fictionOrNon;
    }

    /**
     * A map associating each topic with the total number of volumes on that topic:
     */
    private Map<Topic, Optional<Book>> mostAuthorsByTopic() {
        Map<Topic, Optional<Book>> mostAuthorsByTopic = library.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.groupingBy(Book::getTopic,
                        Collectors.maxBy(Comparator.comparing(b -> b.getAuthors().size()))));
        return mostAuthorsByTopic;
    }

    /**
     * A map associating each topic with the total number of volumes on that topic:
     */
    private Map<Topic, Integer> volumeCountByTopic() {
        Map<Topic, Integer> volumeCountByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.summingInt(b -> b.getPageCounts().length)));
        return volumeCountByTopic;
    }

    /**
     * The topic with the most books - most popular topic:
     */
    private Optional<Topic> mostPopularTopic() {
        Optional<Topic> mostPopularTopic = library.stream()
                .collect(groupingBy(Book::getTopic, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
        return mostPopularTopic;
    }

    /**
     * A map from each topic to the concatenation of all the book titles on that topic:
     */
    private Map<Topic, String> concatenatedTitlesByTopic() {
        Map<Topic, String> concatenatedTitlesByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.mapping(Book::getTitle, Collectors.joining(";"))));
        return concatenatedTitlesByTopic;
    }

    public static void main(String[] args) {
        Demo4 demo4 = new Demo4();
        List<Book> library = demo4.getLibrary();

        System.out.println();

        int totalPageCount = library.stream()
                .flatMapToInt(book -> IntStream.of(book.getPageCounts()))
                .sum();
        System.out.println("The sum of all pages is: " + totalPageCount);

        System.out.println();

        System.out.println("A map classifying books by topic:");
        demo4.booksByTopic().forEach((k, v) -> System.out.println(k + " " + v.stream().map(Book::getTitle).collect(Collectors.joining(" ; "))));

        System.out.println();

        System.out.println("An ordered map from book titles to publication date of latest edition:");
        demo4.titleToPubDate().forEach((k, v) -> System.out.println(k + " " + v));

        System.out.println();

        System.out.println("A map partitioning books into fiction (mapped to true) and non-fiction ( false):");
        demo4.fictionOrNon().forEach((k, v) -> System.out.println(k.toString().toUpperCase() + ": " + v.stream().map(Book::getTitle).collect(Collectors.joining(" ; "))));

        System.out.println();

        System.out.println("A map associating each topic with the book on that topic having the most authors:");
        demo4.mostAuthorsByTopic().forEach((k, v) -> System.out.println(k + ": " + v.stream().map(Book::getTitle).collect(Collectors.joining(" ; "))));

        System.out.println();

        System.out.println("A map associating each topic with the total number of volumes on that topic:");
        demo4.volumeCountByTopic().forEach((k, v) -> System.out.println(k + " " + v));

        System.out.println();

        demo4.mostPopularTopic().ifPresent(t -> System.out.println("The topic with the most books - most popular topic: " + t.toString()));

        System.out.println();

        System.out.println("A map from each topic to the concatenation of all the book titles on that topic:");
        demo4.concatenatedTitlesByTopic().forEach((k, v) -> System.out.println(k + " = " + v));

        System.out.println();

        String concatenatedTitles = library.stream()
                .map(Book::getTitle)
                .collect(Collectors.joining("::"));
        System.out.println(concatenatedTitles);

        System.out.println();

        List<String> authorsForBooks = library.stream()
                .map(b -> b.getAuthors().stream()
                        .collect(Collectors.joining(", ", b.getTitle() + ": ", " - Publishing Date: " + b.getPubDate().toString())))
                .collect(Collectors.toList());

        authorsForBooks.forEach(System.out::println);

        System.out.println();

        Map<String, Year> titleToPubDate = library.stream()
                .collect(Collectors.toMap(Book::getTitle,
                        Book::getPubDate,
                        (x, y) -> x.isAfter(y) ? x : y,  //BinaryOperator.maxBy(Comparator.naturalOrder());
                        TreeMap::new));
        System.out.println(titleToPubDate);

        System.out.println();

        NavigableSet<String> sortedTitles = library.stream()
                .map(Book::getTitle)
                .collect(Collectors.toCollection(TreeSet::new));
        System.out.println(sortedTitles);

        System.out.println();

        BlockingQueue<Book> queueInPubDateOrder = library.stream()
                .sorted(Comparator.comparing(Book::getPubDate))
                .collect(Collectors.toCollection(LinkedBlockingQueue::new));
        System.out.println(queueInPubDateOrder);

        System.out.println();

        Map<Topic, List<Book>> booksByTopic = library.stream()
                .collect(groupingBy(Book::getTopic));  //equals to .collect(groupingBy(Book::getTopic, Collectors.toList()));
        booksByTopic.forEach((k, v) -> System.out.println(k.toString().toUpperCase() + ": " + v.stream().map(Book::getTitle).collect(Collectors.joining(" ; "))));

        System.out.println();

        //A variant of groupingBy is the convenience method partitioningBy, in which
        //the key type K is specialized to Boolean:
        Map<Boolean, List<Book>> fictionOrNonFiction = library.stream()
                .collect(Collectors.partitioningBy(b -> b.getTopic() == Topic.FICTION ||
                        b.getTopic() == Topic.SCIENCE_FICTION));
        System.out.println(fictionOrNonFiction);

        System.out.println();

        Map<Topic, Long> distributionByTopic = library.stream()
                .collect(groupingBy(Book::getTopic, Collectors.counting()));
        System.out.println(distributionByTopic);

        System.out.println();

        Map<Topic, Optional<Book>> mostAuthorsByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.maxBy(Comparator.comparing(b -> b.getAuthors().size()))));
        mostAuthorsByTopic.forEach((k, v) -> System.out.println(k + " : " + v));

        System.out.println();

        Map<Topic, Integer> volumeCountByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.summingInt(b -> b.getPageCounts().length)));
        System.out.println(volumeCountByTopic);

        System.out.println();

        Map<Topic, Double> averageHeightByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.averagingDouble(Book::getHeight)));
        System.out.println(averageHeightByTopic);

        System.out.println();

        Map<Topic, IntSummaryStatistics> volumeStats = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.summarizingInt(b -> b.getPageCounts().length)));
        System.out.println(volumeStats);
        System.out.println(volumeStats.get(Topic.COMPUTING));

        System.out.println();

        Map<Topic, String> concatenatedTitlesByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.mapping(Book::getTitle, Collectors.joining(";"))));
        System.out.println(concatenatedTitlesByTopic);

        System.out.println();

        Map<Topic, Long> bookCountByTopic = library.stream()
                .collect(groupingBy(Book::getTopic, Collectors.counting()));
        System.out.println(bookCountByTopic);

        System.out.println();

        Stream<Map.Entry<Topic, Long>> entries = library.stream()
                .collect(groupingBy(Book::getTopic, Collectors.counting()))
                .entrySet().stream();
        Optional<Topic> mostPopularTopic = entries
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
        mostPopularTopic.ifPresent(System.out::println);

        System.out.println();

        Optional<Topic> mostPopularTopicChained = library.stream()
                .collect(groupingBy(Book::getTopic, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
        System.out.println(mostPopularTopicChained);

        System.out.println();

        Optional<Set<Topic>> mostPopularTopics = library.stream()
                .collect(groupingBy(Book::getTopic, Collectors.counting()))
                .entrySet().stream()
                .collect(groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toSet())))
                .entrySet().stream()
                .max(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue);
        System.out.println(mostPopularTopics);

        System.out.println();

        List<String> titles = library.stream()
                .map(Book::getTitle)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        Collections::unmodifiableList));
        System.out.println(titles.stream().collect(Collectors.joining(" ; ")));
        System.out.println(titles);

        System.out.println();

        Stream<String> concatenatedAuthors = library.stream()
                .map(b -> b.getAuthors().stream().collect(Collectors.joining()));

        Map<Character, String> collect1 = library.stream()
                .map(Book::getTitle)
                .collect(groupingBy(t -> t.charAt(0), Collectors.joining(";")));
        System.out.println(collect1);

        System.out.println();

        Map<Topic, Set<String>> titlesByTopic =
                library.stream().collect(groupingBy(Book::getTopic,
                        Collectors.mapping(Book::getTitle, Collectors.toSet())));
        System.out.println(titlesByTopic);
    }

    Deque<Deque<Point>> groupByProximity(List<Point> sortedPointList) {
        Deque<Deque<Point>> points = new ArrayDeque<>();
        points.add(new ArrayDeque<>());
        for (Point p : sortedPointList) {
            Deque<Point> lastSegment = points.getLast();
            if (!lastSegment.isEmpty() &&
                    lastSegment.getLast().distance(p.getX(), p.getY()) > 10) {
                Deque<Point> newSegment = new ArrayDeque<>();
                newSegment.add(p);
                points.add(newSegment);
            } else {
                lastSegment.add(p);
            }
        }
        return points;
    }

    Supplier<Deque<Deque<Point>>> supplier =
            () -> {
                Deque<Deque<Point>> ddp = new ArrayDeque<>();
                ddp.add(new ArrayDeque<>());
                return ddp;
            };

    BiConsumer<Deque<Deque<Point>>, Point> accumulator =
            (ddp, p) -> {
                Deque<Point> last = ddp.getLast();
                if (!last.isEmpty()
                        && last.getLast().distance(p.getX(), p.getY()) > 10) {
                    Deque<Point> dp = new ArrayDeque<>();
                    dp.add(p);
                    ddp.add(dp);
                } else {
                    last.add(p);
                }
            };

    BinaryOperator<Deque<Deque<Point>>> combiner =
            (left, right) -> {
                Deque<Point> leftLast = left.getLast();
                if (leftLast.isEmpty()) return right;
                Deque<Point> rightFirst = right.getFirst();
                if (rightFirst.isEmpty()) return left;
                Point p = rightFirst.getFirst();
                if (leftLast.getLast().distance(p.getX(), p.getY()) <= 10) {
                    leftLast.addAll(rightFirst);
                    right.removeFirst();
                }
                left.addAll(right);
                return left;
            };

    List<Point> sortedPointList = Arrays.asList(new Point(1, 2),
            new Point(2, 3), new Point(3, 4));
    Deque<Deque<Point>> displacementRecords = sortedPointList.stream()
            .collect(Collector.of(supplier, accumulator, combiner));
}

class DispRecord {
    List<Book> library;
    String title;
    int disp, length;

    DispRecord(String t, int d, int l) {
        this.title = t;
        this.disp = d;
        this.length = l;
    }

    DispRecord() {
        library = new Demo3().getLibrary();

        Map<String, Integer> displacementMap = library.stream()
                .collect(Collector.of(supplier, accumulator, combiner, finisher));
    }

    DispRecord(Book b) {
        this(b.getTitle(), 0, IntStream.of(b.getPageCounts()).sum());
    }

    Deque<DispRecord> wrap(DispRecord dr) {
        Deque<DispRecord> ddr = new ArrayDeque<>();
        ddr.add(dr);
        return ddr;
    }

    int totalDisp() {return disp + length;}

    Supplier<Deque<DispRecord>> supplier = ArrayDeque::new;

    BiConsumer<Deque<DispRecord>, Book> accumulator =
            (dqLeft, b) -> {
                int disp = dqLeft.isEmpty() ? 0 :
                        dqLeft.getLast().totalDisp();
                dqLeft.add(new DispRecord(b.getTitle(),
                        disp,
                        Arrays.stream(b.getPageCounts()).sum()));
            };

    BinaryOperator<Deque<DispRecord>> combiner =
            (left, right) -> {
                if (left.isEmpty()) return right;
                int newDisp = left.getLast().totalDisp();
                List<DispRecord> displacedRecords = right.stream()
                        .map(dr -> new DispRecord(
                                dr.title, dr.disp + newDisp, dr.length))
                        .collect(Collectors.toList());
                left.addAll(displacedRecords);
                return left;
            };


    Function<Deque<DispRecord>, Map<String, Integer>> finisher =
            ddr -> ddr.parallelStream().collect(
                    Collectors.toConcurrentMap(dr -> dr.title, dr -> dr.disp));


    private void initializeDispRecord() {
        Map<String, Integer> displacementMap = library.stream()
                .map(DispRecord::new)
                .map(this::wrap)
                .reduce(combiner).orElseGet(ArrayDeque::new)
                .stream()
                .collect(Collectors.toMap(dr -> dr.title, dr -> dr.disp));
    }

    public static void main(String[] args) {
        DispRecord dispRecord = new DispRecord();
        List<Book> library = dispRecord.library;

        int sumResult = IntStream.of(1, 2, 3)
                .sum();

        int sum = IntStream.of(1, 2, 3)
                .reduce(0, (a, b) -> a + b);

        int intArg = 10;
        int intArgFactorial = IntStream.rangeClosed(1, intArg)
                .reduce(1, (a, b) -> a * b);

        OptionalInt min = IntStream.of(1, 2, 3)
                .reduce((a, b) -> Math.min(a, b));

        Comparator<Book> titleComparator = Comparator.comparing(Book::getTitle);
        Optional<Book> first = library.stream()
                .reduce(BinaryOperator.minBy(titleComparator));
        System.out.println(first);

        System.out.println();

        Stream<BigInteger> biStream = LongStream.of(1, 2, 3)
                .mapToObj(BigInteger::valueOf);
        Optional<BigInteger> bigIntegerSum = biStream
                .reduce(BigInteger::add);
        System.out.println(bigIntegerSum);

        System.out.println();

        //combiner.apply(combiner.apply(q, r), s)) == combiner.apply(q, combiner.apply(r, s))

        Stream<BigInteger> biStream2 = LongStream.of(1, 2, 3)
                .mapToObj(BigInteger::valueOf);
        BigInteger bigIntegerSum2 = biStream2
                .reduce(BigInteger.ZERO, BigInteger::add);
        System.out.println(bigIntegerSum2);

        /*
        Again, as with collectors, the combiner must respect the identity constraint:
        given any s and the identity id
        s == combiner.apply(s, id) == combiner.apply(id, s)
         */

        System.out.println();

        int totalVolumes = library.stream()
                .reduce(0,
                        (sum2, book) -> sum2 + book.getPageCounts().length,
                        Integer::sum);
        System.out.println(totalVolumes);

        System.out.println();

        int totalVolumes2 = library.stream()
                .mapToInt(b -> b.getPageCounts().length)
                .sum();
        System.out.println(totalVolumes2);

        //combiner(r, accumulator(s, t)) == accumulator(combiner(r, s), t)
        System.out.println();

        Comparator<Book> htComparator = Comparator.comparing(Book::getHeight);
        Map<Topic, Optional<Book>> maxHeightByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.reducing(BinaryOperator.maxBy(htComparator))));
        System.out.println(maxHeightByTopic);

        System.out.println();

        Map<Topic, Integer> volumesByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.reducing(0, b -> b.getPageCounts().length, Integer::sum)));
        System.out.println("Volumes by Topic: " + volumesByTopic);

        System.out.println();

        /*
        The factory method Collector.counting is implemented using this overload.
        For example, if it were not provided, I could count the number of books in each topic
        of my library like this:
         */
        Map<Topic, Long> booksByTopic = library.stream()
                .collect(groupingBy(Book::getTopic,
                        Collectors.reducing(0L, e -> 1L, Long::sum)));
        System.out.println(booksByTopic);
    }
}
