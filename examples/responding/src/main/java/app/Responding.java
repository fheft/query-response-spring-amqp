package app;

import com.studiomediatech.queryresponse.EnableQueryResponse;
import com.studiomediatech.queryresponse.ResponseBuilder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@SpringBootApplication
@EnableQueryResponse
class Responding implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        println("Registering responses...");

        respondToBooks();
        respondToAuthors();
        respondToNames();

        println("Waiting for queries! Press CTRL-C to exit.");
        Thread.currentThread().join();
    }


    private void respondToBooks() {

        ResponseBuilder.respondTo("books/sci-fi", String.class)
            .withAll()
            .from("Neuromancer", "Snow Crash", "I, Robot", "The Gods Themselves", "Pebble in the Sky");
    }


    private void respondToAuthors() {

        var tolkien = new Author("J. R. R. Tolkien", 1892, "South Africa");
        var lewis = new Author("C. S. Lewis", 1898, "United Kingdom");
        var asimov = new Author("Isaac Asimov", 1920, "Russia");
        var gibson = new Author("William Gibson", 1948, "United States");

        var authors = List.of(tolkien, lewis, asimov, gibson);

        ResponseBuilder.respondTo("authors", Author.class)
            .withAll()
            .suppliedBy(() -> authors.subList(0, ThreadLocalRandom.current().nextInt(1, authors.size() + 1)));
    }


    private void respondToNames() {

        var names = Arrays.asList("Yasir", "Araceli", "Emídio", "Rebekka", "Jack", "Hertha", "Oscar", "Astrid",
                "Sedef", "Naomi", "Ioque", "Davut", "Edith", "Ortrun", "Eddie", "Noah", "Anthony", "Connor", "Mestan",
                "Erich", "Marius", "Adrian", "Jenny", "Enio", "Grazia", "Batur", "Fabien", "Oscar", "Rafael", "Efe",
                "Arthur", "Eva", "Thea", "Finn", "Esat", "Ramon", "Amanda", "Anouchka", "Zachary", "Ece",
                "Hans-Günther", "Ebenezer", "Loni", "Ava", "Rose", "Lucy", "Katarina", "Ana", "Jonathan", "Bertram",
                "Balthasar", "Fletcher", "Annelene", "Alberto", "Matilda", "Juanita", "Levin", "Latife", "Alexandre",
                "Dylan", "آریا", "Carola", "Oswald", "Noury", "Logan", "Oliver", "Patricia", "Reitze", "Hayley",
                "Saya", "Alexa", "Clarindo", "Mestan", "Nadine", "Salome", "Erin", "Elina", "Charlie", "Juliette",
                "Cindy", "Hannah", "Victoria", "Eleanor", "Alexander", "Lázaro", "Daniel", "Sally", "Ashton");

        ResponseBuilder.respondTo("names", String.class)
            .withBatchesOf(12)
            .from(names);
    }


    private static void println(String message, Object... args) {

        System.out.println("> " + new Date() + "\t: " + String.format(message, args));
    }


    public static void main(String[] args) {

        new SpringApplicationBuilder(Responding.class).web(WebApplicationType.NONE).run(args);
    }

    static class Author {

        public String name;
        public int year;
        public String country;

        public Author(String name, int year, String country) {

            this.name = name;
            this.year = year;
            this.country = country;
        }
    }
}
