package example;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static example.Movie.MovieType.*;
import static org.junit.Assert.assertEquals;

public class CustomerTest {

    private Customer customer;
    private Movie movieRembo;
    private Movie movieLOTR;
    private Movie movieHarryPotter;

    // The code below initializes everything *before* each test is executed
    @Before
    public void init() {
        movieRembo = new Movie("Rembo", REGULAR);
        movieLOTR = new Movie("Lord of the Rings", NEW_RELEASE);
        movieHarryPotter = new Movie("Harry Potter", CHILDRENS);

        customer = new Customer("John Doe", List.of(
                new Rental(movieRembo, 1),
                new Rental(movieLOTR, 4),
                new Rental(movieHarryPotter, 5)
        ));
    }

    // LOTR is rented for three days, the amount owed should be correct
    @Test
    public void lotrMovieRentedForThreeDays() {
        customer = new Customer("John Doe", List.of(new Rental(movieLOTR, 3)));
        String result = """
                Rental Record for John Doe
                \tLord of the Rings\t9.0
                Amount owed is 9.0
                You earned 2 frequent renter points""";
        assertEquals(result, customer.statement());
    }


    // LOTR is rented for one day, the amount owed should be correct
    @Test
    public void lotrMovieRentedForOneDay() {
        customer = new Customer("John Doe", List.of(new Rental(movieLOTR, 1)));
        String expected = """
                Rental Record for John Doe
                \tLord of the Rings\t3.0
                Amount owed is 3.0
                You earned 1 frequent renter points""";
        assertEquals(expected, customer.statement());
    }

    // Rambo is rented for three days, the amount owed should be correct
    @Test
    public void ramboMovieRentedForThreeDays() {
        customer = new Customer("John Doe", List.of(new Rental(movieRembo, 3)));
        String result = """
                Rental Record for John Doe
                \tRembo\t6.0
                Amount owed is 6.0
                You earned 3 frequent renter points""";
        assertEquals(result, customer.statement());
    }

    // Multiple copies of the same movie rented for different days each
    // The amount should be correct
    @Test
    public void sameMoviesForDifferentDays() {
        customer = new Customer("John Doe", List.of(
                new Rental(movieRembo, 1),
                new Rental(movieRembo, 2),
                new Rental(movieRembo, 3)
        ));
        String expected = """
                Rental Record for John Doe
                \tRembo\t2.0
                \tRembo\t2.0
                \tRembo\t3.5
                Amount owed is 7.5
                You earned 3 frequent renter points""";
        assertEquals(expected, customer.statement());
    }

    // Each available movie is rented simultaneously
    // The amount should be correct
    @Test
    public void differentMoviesForDifferentDays() {
        customer = new Customer("John Doe", List.of(
                new Rental(movieRembo, 2),
                new Rental(movieLOTR, 1),
                new Rental(movieHarryPotter, 4)
        ));
        String expected = """
                Rental Record for John Doe
                \tRembo\t2.0
                \tLord of the Rings\t3.0
                \tHarry Potter\t3.0
                Amount owed is 8.0
                You earned 3 frequent renter points""";
        assertEquals(expected, customer.statement());
    }
}
