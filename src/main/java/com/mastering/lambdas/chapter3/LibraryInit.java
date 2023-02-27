package com.mastering.lambdas.chapter3;

import lombok.Getter;

import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class LibraryInit {

    private final List<Book> library = new ArrayList<>();

    public LibraryInit() {
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
    }
}
