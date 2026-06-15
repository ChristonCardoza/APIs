package org.cardoza.sec03;

import org.cardoza.models.sec03.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Lec05Collection {

    private static final Logger log = LoggerFactory.getLogger(Lec05Collection.class);

    public static void main(String[] args) {

        // create books

        var book1 = Book.newBuilder()
                .setTitle("Harry Potter - Part 1")
                .setAuthor("J K Rowling")
                .setPublicationYear(1997)
                .build();
        var book2 = book1.toBuilder().setTitle("Harry Potter - Part 2").setPublicationYear(1998).build();
        var book3 = book1.toBuilder().setTitle("Harry Potter - Part 3").setPublicationYear(1999).build();

        var library = Library.newBuilder()
                .setName("Fantasy Library")
//                .addBooks(book1)
//                .addBooks(book2)
//                .addBooks(book3)
                .addAllBooks(List.of(book1, book2, book3))
                .build();

        log.info("{}", library);
    }
}
