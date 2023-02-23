package com.mastering.lambdas.chapter3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Year;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Book {
    private String title;

    private List<String> authors;

    private int[] pageCounts;
    private Topic topic;

    private Year pubDate;

    private double height;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return getTitle().equals(book.getTitle()) && getAuthors().equals(book.getAuthors()) && getPubDate().equals(book.getPubDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getAuthors(), getPubDate());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Book{");
        sb.append("title='").append(title).append('\'');
        sb.append(", authors=").append(authors);
        sb.append(", pageCounts=").append(Arrays.toString(pageCounts));
        sb.append(", pubDate=").append(pubDate);
        sb.append(", heightCms=").append(height);
        sb.append(", topic=").append(topic);
        sb.append('}');
        return sb.toString();
    }
}
