package com.gridnine.testing;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.gridnine.testing.FlightFilters.*;

public class Main {
    public static void main(String[] args) {

        // Get flights list
        var flightsList = FlightBuilder.createFlights();
        System.out.println("No filter flights:");
        flightsList.forEach(System.out::println);

        // Create parametrized DynamicFilter object.
        var flightDynamicFilter = new DynamicFilter<>(flightsList);

        // Preparation of filtration conditions:
        // 1. Departure before now case:
        var filtersList1 = List.of(DEPARTURE_BEFORE_NOW.get());
        // 2. Arrival before departure case:
        var filtersList2 = List.of(ARRIVAL_BEFORE_DEPARTURE.get());
        // 3. Ground time more than 2 hours case:
        var filtersList3 = List.of(GROUND_TIME_MORE_THAN_2_HOURS.get());

        System.out.println("Departure before now filter:");
        flightDynamicFilter.filterBy(filtersList1).forEach(System.out::println);

        System.out.println("Arrival before departure filter:");
        flightDynamicFilter.filterBy(filtersList2).forEach(System.out::println);

        System.out.println("Ground time more than 2 hours filter:");
        flightDynamicFilter.filterBy(filtersList3).forEach(System.out::println);

        // The several filters lists are wiring via OR
        System.out.println("Departure before now OR arrival before departure filter:");
        flightDynamicFilter.filterBy(Collections.emptyList(), filtersList2).forEach(System.out::println);
    }
}