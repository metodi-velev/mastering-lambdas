package com.mastering.lambdas.chapter5;

import com.mastering.lambdas.chapter3.Book;
import com.mastering.lambdas.chapter3.LibraryInit;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.BitSet;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Getter
@Setter
public class Demo5 {

    private List<Book> library;

    public Demo5() {
        library = new LibraryInit().getLibrary();
    }

    String exampleSplit(String pattern) {
        Pattern p = Pattern.compile(pattern);
        String s = "-rw-rw-r-- 1 root admin 21508 26 Feb 2014 /.bashrc";
        return p.splitAsStream(s)
                .collect(Collectors.joining("\",\"", "\"", "\""));
    }

    private void handleIOExceptionV1() {
        try (Stream<Path> paths = Files.list(Paths.get("./src"))) {
            paths.peek(path -> {
                        try {
                            Files.lines(path);
                        } catch (IOException e) {
                            System.err.println("** exception from Files.lines");
                            e.printStackTrace();
                        }
                    })
                    .forEach(line -> {});
        } catch (IOException e) {
            System.err.println("++ exception from Files.list");
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    private void handleIOExceptionV2() {
        try (Stream<Path> paths = Files.list(Paths.get("./src"))) {
            paths.flatMap(path -> {
                        Stream<String> lines;
                        try {
                            lines = Files.lines(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                            lines = Stream.of("Unreadable file: " + path);
                        }
                        return lines;
                    })
                    .forEach(line -> {}); // line 57
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    private void handleIOExceptionV3() {
        Stream<Path> paths = null;
        try {
            paths = Files.list(Paths.get("./src"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        Stream<String> linesResult = paths.flatMap(path -> {
            Stream<String> lines;
            try {
                lines = Files.lines(path);
            } catch (IOException e) {
                e.printStackTrace();
                lines = Stream.of("Unreadable file: " + path);
            }
            return lines;
        });
        linesResult.forEach(line -> {});
    }

    private void showFilesAndLengths() {
        try (Stream<Path> paths = Files.list(Paths.get("./src"))) {
            paths
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .map(f -> f.getAbsolutePath() + " " + f.length())
                    .forEachOrdered(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    private void grepH() {
        Path start = new File(".").toPath();
        Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");
        PathMatcher pathMatcher =
                FileSystems.getDefault().getPathMatcher("glob:" + "**/test*.txt");

        try (Stream<Path> pathStream = Files.walk(start)) {
            pathStream
                    .filter(Files::isRegularFile)
                    .filter(pathMatcher::matches)
                    .flatMap(path -> {
                        try {
                            return Files.readAllLines(path).stream()
                                    .filter(line -> pattern.matcher(line).find())
                                    .map(line -> path + ": " + line);
                        } catch (IOException e) {
                            return Stream.of("");
                        }
                    })
                    .filter(line -> !line.isEmpty())
                    .filter(line -> pattern.matcher(line).find())
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args) {
        Demo5 demo5 = new Demo5();
        List<Book> library = demo5.getLibrary();

        IntStream.iterate(1, i -> -i).limit(10).forEach(System.out::println);

        IntStream.range(1, 6).forEach(System.out::print); //prints 12345

        System.out.println();

        IntStream.rangeClosed(1, 5).forEach(System.out::print); //prints 12345

        System.out.println();

        library.stream()
                .map(book -> {
                    int[] volumes = book.getPageCounts();
                    return
                            IntStream.rangeClosed(1, volumes.length)
                                    .mapToObj(i -> i + ":" + volumes[i - 1])
                                    .collect(Collectors.joining(", ", book.getTitle() + ": ", ""));
                })
                .forEach(System.out::println);

        System.out.println();

        Path start = new File(".").toPath();
        try (Stream<Path> pathStream = Files.walk(start)) {
            pathStream
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .map(f -> f.getAbsolutePath() + " " + f.length())
                    .forEachOrdered(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println();

        System.out.println(demo5.exampleSplit("o+"));
        System.out.println(demo5.exampleSplit("@"));
        System.out.println(demo5.exampleSplit("^-"));
        System.out.println(demo5.exampleSplit("^"));

        System.out.println();

        Stream<Character> chrStream = "example".chars().mapToObj(i -> (char) i);
        chrStream.forEach(System.out::print);

        System.out.println();

        "example".chars().forEach(c -> System.out.println("The ASCII value of " + (char) c + " is: " + c));

        System.out.println();

        byte[] bits = {10, 18}; // 01010000 01001000 (little-endian)
        BitSet bs = BitSet.valueOf(bits);
        System.out.println(bs.stream()
                .boxed().collect(Collectors.toList())); // prints [1, 3, 9, 12]

        demo5.showFilesAndLengths();

        System.out.println();

        System.out.println("Test grepH: ");
        demo5.grepH();
    }
}

class DispLine {
    final int disp;
    final String line;

    DispLine(int d, String l) {
        disp = d;
        line = l;
    }

    public String toString() {return disp + " " + line;}
}

class LineSpliterator implements Spliterator<DispLine> {
    private static final int AVERAGE_LINE = 30;
    private static final int LENGTH = 3;
    private ByteBuffer bb;
    private int lo, hi;

    LineSpliterator(ByteBuffer bb, int lo, int hi) {
        this.bb = bb;
        this.lo = lo;
        this.hi = hi;
    }

    LineSpliterator() {}

    @Override
    public boolean tryAdvance(Consumer<? super DispLine> action) {
        int index = lo;
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char) bb.get(index));
        } while (bb.get(index++) != '\n');
        action.accept(new DispLine(lo, sb.toString()));
        lo = lo + sb.length();
        return lo <= hi;
    }

    @Override
    public Spliterator<DispLine> trySplit() {
        int index = lo;
        int mid = (lo + hi) >>> 1;
        while (bb.get(index) != '\n') mid++;
        LineSpliterator newSpliterator = null;
        if (mid != hi) {
            newSpliterator = new LineSpliterator(bb, lo, mid);
            lo = mid + 1;
        }
        return newSpliterator;
    }

    @Override
    public long estimateSize() {
        return (((hi - lo) + 1) / AVERAGE_LINE) - LENGTH;
    }

    @Override
    public int characteristics() {
        return ORDERED | IMMUTABLE | NONNULL;
    }

    private void grepBSingleFile() {
        Path start = new File(".\\src\\testGrepH.txt").toPath();
        Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+");

        try (FileChannel fc = FileChannel.open(start)) {
            MappedByteBuffer bB =
                    fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            Spliterator<DispLine> ls =
                    new LineSpliterator(bB, 0, bB.limit() - 1);
            StreamSupport.stream(ls, true)
                    .filter(dl -> pattern.matcher(dl.line).find())
                    .forEachOrdered(System.out::print);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UncheckedIOException(e);
        }
    }

    public static void main(String[] args) {
        //ByteBuffer buffer = ByteBuffer.allocate(1000);
        new LineSpliterator().grepBSingleFile();
    }
}

