package com.mastering.lambdas.chapter3;

import com.mastering.lambdas.chapter1.Point;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Year;
import java.util.*;
import java.util.stream.*;

@Getter
@Setter
public class Demo3 {

    private List<Integer> intList;
    private final List<Book> library = new ArrayList<>();
    private Stream<Book> computingBooks;
    private Stream<String> bookTitles;
    private Stream<Book> booksSortedByTitle;
    private Optional<Book> oldest;
    private Set<String> titles;
    private Stream<String> authorsInBookTitleOrder;
    private Stream<Book> readingList;
    private Stream<Book> remainderList;
    private final IntStream firstTenPowersOfTwo;
    private Stream<Year> bookPubDates;
    private int totalAuthorships;
    int pages;
    List<Book> multipleAuthoredHistories;
    Stream<Book> booksSortedByAuthorCount;
    boolean withinShelfHeight;
    Optional<Book> anyBook;
    IntSummaryStatistics pageCountStatistics;
    Map<String, Year> titleToPubDate;
    Map<String, Year> titleToPubDateLatest;

    public Demo3() {
        Book nails = new Book("Fundamentals of Chinese Fingernail Image",
                List.of("Li", "Fu", "Li"),
                new int[]{256}, // pageCount per volume
                Topic.MEDICINE,
                Year.of(2014), // publication date
                25.2); // height in cms
        Book dragon = new Book("Compilers: Principles, Techniques and Tools",
                List.of("Aho", "Lam", "Sethi", "Ullman"),
                new int[]{1009},
                Topic.COMPUTING,
                Year.of(2006), // publication date (2nd edition)
                23.6);
        Book voss = new Book("Voss",
                List.of("Patrick White"),
                new int[]{478},
                Topic.FICTION,
                Year.of(1957),
                19.8);
        Book lotr = new Book("Lord of the Rings",
                List.of("Tolkien"),
                new int[]{531, 416, 624},
                Topic.FICTION,
                Year.of(1955),
                23.0);

        library.addAll(Arrays.asList(nails, dragon, voss, lotr));

        intList = Arrays.asList(1, 2, 3, 4, 5);

        computingBooks = computingBooks();

        bookTitles = bookTitles();

        booksSortedByTitle = booksSortedByTitle();

        oldest = oldest();

        titles = titles();

        authorsInBookTitleOrder = authorsInBookTitleOrder();

        readingList = readingList();

        remainderList = remainderList();

        pages = pages();

        firstTenPowersOfTwo = IntStream.iterate(2, i -> i * 2)
                .limit(10);

        bookPubDates = bookPubDates();

        totalAuthorships = totalAuthorships();

        multipleAuthoredHistories = multipleAuthoredHistories();

        booksSortedByAuthorCount = booksSortedByAuthorCount();

        withinShelfHeight = withinShelfHeight();

        anyBook = anyBook();

        pageCountStatistics = pageCountStatistics();

        titleToPubDate = titleToPubDate();

        titleToPubDateLatest = titleToPubDateLatest();
    }


    /**
     * A stream yielding the first 100 books in alphabetical order of title:
     */
    private Stream<Book> readingList() {
        readingList = library.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .limit(100);
        return readingList;
    }

    /**
     * This code snippet involves executing the Integer methods intValue and valueOf, respectively,
     * before and after every addition.
     */
    private int calculateMaxInteger() {
        Optional<Integer> maxInteger = Arrays.asList(1, 2, 3, 4, 5).stream()
                .map(i -> i + 1)
                .max(Integer::compareTo);

        return maxInteger.orElseThrow(IllegalStateException::new);
    }

    private int calculateMaxPrimitive() {
        OptionalInt maxPrimitive = IntStream.rangeClosed(1, 5)
                .map(i -> i + 1)
                .max();

        return maxPrimitive.orElseThrow(IllegalStateException::new);
    }

    /**
     * A stream that contains only computing books:
     */
    private Stream<Book> computingBooks() {
        computingBooks = library.stream()
                .filter(b -> b.getTopic() == Topic.COMPUTING);
        return computingBooks;
    }

    /**
     * A stream of book titles:
     */
    private Stream<String> bookTitles() {
        bookTitles = library.stream()
                .map(Book::getTitle);
        return bookTitles;
    }


    /**
     * A stream of Books, sorted by title:
     */
    private Stream<Book> booksSortedByTitle() {
        booksSortedByTitle = library.stream()
                .sorted(Comparator.comparing(Book::getTitle).reversed());
        return booksSortedByTitle;
    }

    /**
     * Use this sorted stream to create a stream of authors, in order of book title,
     * with duplicates removed:
     */
    private Stream<String> authorsInBookTitleOrder() {
        authorsInBookTitleOrder = library.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .flatMap(book -> book.getAuthors().stream())
                .distinct();
        return authorsInBookTitleOrder;
    }

    /**
     * A stream with the rest:
     */
    private Stream<Book> remainderList() {
        remainderList = library.stream()
                .sorted(Comparator.comparing(Book::getTitle))
                .skip(100);
        return remainderList;
    }

    /**
     * The earliest-published book in my library:
     */
    private Optional<Book> oldest() {
        oldest = library.stream()
                .min(Comparator.comparing(Book::getPubDate));
        return oldest;
    }

    /**
     * The set of titles of the books in my library:
     */
    private Set<String> titles() {
        titles = library.stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());
        return titles;
    }

    /**
     * Create a Stream of publication dates:
     */
    private Stream<Year> bookPubDates() {
        bookPubDates = library.stream()
                .map(Book::getPubDate);
        return bookPubDates;
    }

    /**
     * Calculate overall page count:
     */
    private int pages() {
        pages = library.stream()
                .flatMapToInt(b -> IntStream.of(b.getPageCounts()))
                .sum();
        return pages;
    }

    /**
     * Count the total number of authorships (defining an authorship as
     * the contribution of an author to a book) of all books:
     */
    private int totalAuthorships() {
        totalAuthorships = library.stream()
                .mapToInt(b -> b.getAuthors().size())
                .sum();
        return totalAuthorships;
    }

    /**
     * Get all medicine books written by multiple authors:
     */
    private List<Book> multipleAuthoredHistories() {
        multipleAuthoredHistories = library.stream()
                .filter(b -> b.getTopic() == Topic.MEDICINE)
                .peek(b -> System.out.println(b.getTitle()))
                .filter(b -> b.getAuthors().size() > 1)
                .collect(Collectors.toList());
        return multipleAuthoredHistories;
    }

    /**
     * Books sorted in ascending order by the number of their authors:
     */
    private Stream<Book> booksSortedByAuthorCount() {
        booksSortedByAuthorCount = library.stream()
                .sorted(Comparator.comparing(Book::getAuthors, Comparator.comparing(List::size))
                        .thenComparing(Book::getTitle, Comparator.reverseOrder()));
        return booksSortedByAuthorCount;
    }

    /**
     * Create a classification map, mapping each topic to a list of books on that topic:
     * Behavioral parameter with state - don't do this!
     * <pre>{@code
     *         private Map<Topic, List<Book>> booksByTopic() {
     *         Map<Topic, List<Book>> booksByTopic = new HashMap<>();
     *         library.parallelStream()
     *                 .peek(b -> {
     *                     Topic topic = b.getTopic();
     *                     List<Book> currentBooksForTopic = booksByTopic.get(topic);
     *                     if (currentBooksForTopic == null) {
     *                         currentBooksForTopic = new ArrayList<>();
     *                     }
     *                     currentBooksForTopic.add(b);
     *                     booksByTopic.put(topic, currentBooksForTopic); // don't do this!
     *                 })
     *                 .anyMatch(b -> false); // throw the stream elements away
     *         return booksByTopic;
     *     }
     * }</pre>
     */
    private Map<Topic, List<Book>> booksByTopicNoGo() {
        Map<Topic, List<Book>> booksByTopic = new HashMap<>();
        library.parallelStream()
                .peek(b -> {
                    Topic topic = b.getTopic();
                    List<Book> currentBooksForTopic = booksByTopic.get(topic);
                    if (currentBooksForTopic == null) {
                        currentBooksForTopic = new ArrayList<>();
                    }
                    currentBooksForTopic.add(b);
                    booksByTopic.put(topic, currentBooksForTopic); // don't do this!
                })
                .anyMatch(b -> false); // throw the stream elements away
        return booksByTopic;
    }

    /**
     * Create a classification map, mapping each topic to a list of books on that topic.
     * Use groupingBy collectors. Avoid using {@link #booksByTopicNoGo() booksByTopicNoGo}
     */
    private Map<Topic, List<Book>> booksByTopicOK() {
        return library.parallelStream()
                .collect(Collectors.groupingBy(Book::getTopic));
    }

    /**
     * Check that all fiction books are less than 24cm high:
     */
    private boolean withinShelfHeight() {
        withinShelfHeight = library.stream()
                .filter(b -> b.getTopic() == Topic.FICTION)
                .allMatch(b -> b.getHeight() < 24);
        return withinShelfHeight;
    }

    /**
     * Find any book which has "Sethi" as one of its authors:
     */
    private Optional<Book> anyBook() {
        Optional<Book> anyBook = library.stream()
                .filter(b -> b.getAuthors().contains("Sethi"))
                .findAny();
        return anyBook;
    }

    /**
     * Find the first line in the text file "src/Mastering.tex" containing the word "findFirst":
     */
    private Optional<String> findFirst() throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader("src/Mastering.tex"));
        Optional<String> line = br.lines()
                .filter(s -> s.contains("findFirst"))
                .findFirst();
        return line;
    }

    /**
     * Obtain the summary statistics of the page count of the books:
     */
    private IntSummaryStatistics pageCountStatistics() {
        IntSummaryStatistics pageCountStatistics = library.stream()
                .mapToInt(b -> IntStream.of(b.getPageCounts()).sum())
                .summaryStatistics();
        return pageCountStatistics;
    }

    /**
     * Map each book title in my collection to its publication date:
     */
    private Map<String, Year> titleToPubDate() {
        Map<String, Year> titleToPubDate = library.stream()
                .collect(Collectors.toMap(Book::getTitle, Book::getPubDate));
        return titleToPubDate;
    }

    /**
     * Map each book title in my collection to its publication date including only
     * the latest edition of each book in the mapping:
     */
    private Map<String, Year> titleToPubDateLatest() {
        Map<String, Year> titleToPubDateLatest = library.stream()
                .collect(Collectors.toMap(Book::getTitle,
                        Book::getPubDate,
                        (x, y) -> x.isAfter(y) ? x : y));
        return titleToPubDateLatest;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Demo3 demo3 = new Demo3();
        List<Book> library = demo3.getLibrary();

        OptionalDouble maxDistance =
                demo3.intList.parallelStream()
                        .map(i -> new Point(i % 3, i / 3))
                        .mapToDouble(p -> p.distance(0, 0))
                        .max();

        maxDistance.ifPresent(System.out::println);

        DoubleStream ds =
                demo3.intList.parallelStream()
                        .map(i -> new Point(i % 3, i / 3))
                        .mapToDouble(p -> p.distance(0, 0));

        // the pipeline has now been set up, but no data has been processed yet
        OptionalDouble maxDistance2 = ds.max();

        maxDistance2.ifPresent(System.out::println);

        demo3.getFirstTenPowersOfTwo().forEachOrdered(System.out::println);

        System.out.println("Max value is: " + demo3.calculateMaxInteger());
        System.out.println("Max value is: " + demo3.calculateMaxPrimitive());

        DoubleStream dss = IntStream.rangeClosed(1, 10).asDoubleStream();

        LongStream ls = IntStream.rangeClosed(1, 10).asLongStream();

        Stream<Integer> is = IntStream.rangeClosed(1, 10).boxed();

        Stream<Integer> integerStream = Stream.of(1, 2);
        IntStream intStream = integerStream.mapToInt(Integer::intValue);

        System.out.println("---------------------------BOOKS----------------------------");

        System.out.println("Print only computing books:");
        demo3.computingBooks().forEach(System.out::println);

        System.out.println();

        System.out.println("Print book titles:");
        demo3.bookTitles().forEach(System.out::println);

        System.out.println();

        System.out.println("Print books, sorted by title:");
        System.out.println(demo3.booksSortedByTitle().map(Book::getTitle).collect(Collectors.joining(" ; ")));

        System.out.println();

        System.out.println("Print authors, in order of book title, with duplicates removed:");
        demo3.authorsInBookTitleOrder().forEach(System.out::println);

        System.out.println();

        System.out.println("Print the first 100 books in alphabetical order of title:");
        demo3.readingList().forEach(System.out::println);

        System.out.println();

        System.out.println("Print the rest of the books: ");
        demo3.remainderList().forEach(System.out::println);

        System.out.println();

        System.out.println("Print the earliest-published book in my library:");
        System.out.println(demo3.oldest().orElseGet(Book::new));

        System.out.println();

        System.out.println("Print the set of titles of the books in my library:");
        demo3.titles().forEach(System.out::println);

        System.out.println();

        System.out.println("Print the publish dates of all books:");
        String pubDates = demo3.bookPubDates().map(Year::toString).collect(Collectors.joining(" ; "));
        System.out.println(pubDates);

        System.out.println();

        System.out.println("Print the total number of authorships of all books:");
        System.out.println(demo3.totalAuthorships());

        System.out.println();

        System.out.println("Print overall page count is: " + demo3.pages());

        System.out.println();

        System.out.println("Print all medicine books written by multiple authors:");
        demo3.multipleAuthoredHistories().forEach(System.out::println);

        System.out.println();

        Stream<String> sortedTitles = library.stream()
                .map(Book::getTitle)
                .sorted();
        sortedTitles.forEach(System.out::println);

        System.out.println();

        Stream<Book> booksSortedByTitle = library.stream()
                .sorted(Comparator.comparing(Book::getTitle));
        booksSortedByTitle.forEach(System.out::println);

        System.out.println();

        System.out.println("Print the books sorted in ascending order by the number of their authors:");
        demo3.booksSortedByAuthorCount().forEach(System.out::println);

        System.out.println();

        System.out.println("Print all the topics and all the books assigned to them using the wrong booksByTopicNoGo() method:");
        demo3.booksByTopicNoGo().forEach((k, v) -> {
            System.out.println(new StringBuilder().append("For topic ").append(k).append(" we have the following books: ").append(v.stream()
                    .map(Book::getTitle).collect(Collectors.joining(" ; "))).toString());
        });

        System.out.println();

        System.out.println("Print all the topics and all the books assigned to them using the right booksByTopicOK() method:");
        demo3.booksByTopicOK().forEach((k, v) -> {
            System.out.println(new StringBuilder().append("For topic ").append(k).append(" we have the following books: ").append(v.stream()
                    .map(Book::getTitle).collect(Collectors.joining(" ; "))).toString());
        });

        System.out.println();

        System.out.println("Print whether all fiction books are less than 24cm high:");
        System.out.println("Within shelf height: " + demo3.withinShelfHeight());

        System.out.println();

        System.out.println("Print any book which has \"Sethi\" as one of its authors:");
        System.out.println(demo3.anyBook().orElseGet(Book::new));

        System.out.println();

        System.out.println("Print the first line in the text file \"src/Mastering.tex\" containing the word \"findFirst\":");
        demo3.findFirst().ifPresent(s -> System.out.println("Find first is found on this line: " + s));

        System.out.println();

        System.out.println("Print the summary statistics of the page count of the books: ");
        System.out.println("Summary Statistics: " + demo3.pageCountStatistics());

        System.out.println();

        OptionalInt maxVolumesPages = library.stream()
                .mapToInt(b -> IntStream.of(b.getPageCounts()).sum())
                .max();
        maxVolumesPages.ifPresent(s -> System.out.println("Max Volumes Pages Sum: " + s));

        Optional<Book> oldest = library.stream()
                .min(Comparator.comparing(Book::getPubDate));
        oldest.ifPresent(b -> System.out.println("Earliest-published book: " + b));

        Optional<String> firstTitle = library.stream()
                .map(Book::getTitle)
                .min(Comparator.naturalOrder());   //Comparator.reverseOrder()
        firstTitle.ifPresent(t -> System.out.println("First Title: " + t));

        Set<String> titles = library.stream()
                .map(Book::getTitle)
                .collect(Collectors.toSet());

        System.out.println();

        System.out.println("Print the map of each book title in my collection to its publication date " +
                "including only the latest edition of each book in the mapping:");
        System.out.println(demo3.titleToPubDate());

        System.out.println();

        System.out.println("Print the map of each book title in my collection to its publication date:");
        System.out.println(demo3.titleToPubDateLatest());

        System.out.println("---------------------------EXTRA - Sort a Map by key or by value----------------------------");

        // Version 1.
        Map<String, Year> titleToPubDate = library.stream()
                .collect(Collectors.toMap(Book::getTitle, Book::getPubDate));
        TreeMap<String, Year> titleToPubDateSortedByKey = new TreeMap<>();
        titleToPubDateSortedByKey.putAll(titleToPubDate);
        titleToPubDateSortedByKey.forEach((k, v) -> System.out.println(k + " " + v));

        System.out.println();

        // Version 2.
        Map<String, Year> titleToPubDate2 = library.stream()
                .collect(Collectors.toMap(Book::getTitle, Book::getPubDate));
        Map<String, Year> titleToPubDateSortedByKeyV2 = new LinkedHashMap<>();
        titleToPubDate2.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .forEach(e -> titleToPubDateSortedByKeyV2.put(e.getKey(), e.getValue()));
        titleToPubDateSortedByKeyV2.forEach((k, v) -> System.out.println(k + " " + v));

        System.out.println();

        // Version 3.
        Map<String, Year> titleToPubDateSortedByKeyV3 = library.stream()
                .collect(Collectors.toMap(Book::getTitle, Book::getPubDate))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        titleToPubDateSortedByKeyV3.forEach((k, v) -> System.out.println(k + " " + v));

        System.out.println();

        //Sort by map value
        Map<String, Year> titleToPubDateSortedByValue = library.stream()
                .collect(Collectors.toMap(Book::getTitle, Book::getPubDate))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        titleToPubDateSortedByValue.forEach((k, v) -> System.out.println(k + " " + v));
    }
}
