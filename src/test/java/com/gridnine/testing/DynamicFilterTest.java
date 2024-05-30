package com.gridnine.testing;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.gridnine.testing.FlightFilters.*;
import static org.assertj.core.api.Assertions.assertThat;

class DynamicFilterTest {


    static List<Flight> noFilteredFlights;
    static List<Flight> departureBeforeNowExpected;
    static List<Flight> arrivalBeforeDepartureExpected;
    static List<Flight> groundTimeMoreThanTwoHoursExpected;
    static List<Flight> arrivalBeforeDepartureOrDepartureBeforeNowExpected;

    DynamicFilter<Flight> flightDynamicFilter;

    @BeforeAll
    static void init() {
        noFilteredFlights = FlightBuilder.createFlights();
        departureBeforeNowExpected = List.of(noFilteredFlights.get(2));
        arrivalBeforeDepartureExpected = List.of(noFilteredFlights.get(3));
        groundTimeMoreThanTwoHoursExpected = List.of(noFilteredFlights.get(4), noFilteredFlights.get(5));
        arrivalBeforeDepartureOrDepartureBeforeNowExpected = List.of(noFilteredFlights.get(2),
                noFilteredFlights.get(3));
    }

    @BeforeEach
    void filterInit() {
        flightDynamicFilter = new DynamicFilter<>(noFilteredFlights);
    }

    @Nested
    class SequentialFilteringTest {
        @Test
        void parallelFilterBy_departureBeforeNow_withCorrectArguments() {
            var filtersList = List.of(DEPARTURE_BEFORE_NOW.get());

            var flights = flightDynamicFilter.filterBy(filtersList);

            assertThat(flights).isEqualTo(departureBeforeNowExpected);
        }

        @Test
        void parallelFilterBy_arrivalBeforeDeparture_withCorrectArguments() {
            var filtersList = List.of(ARRIVAL_BEFORE_DEPARTURE.get());

            var flights = flightDynamicFilter.filterBy(filtersList);

            assertThat(flights).isEqualTo(arrivalBeforeDepartureExpected);
        }

        @Test
        void parallelFilterBy_groundTimeMoreThanTwoHours_withCorrectArguments() {
            var filtersList = List.of(GROUND_TIME_MORE_THAN_2_HOURS.get());

            var flights = flightDynamicFilter.filterBy(filtersList);

            assertThat(flights).isEqualTo(groundTimeMoreThanTwoHoursExpected);
        }

        @Test
        void parallelFilterBy_arrivalBeforeDeparture_Or_departureBeforeNow_withCorrectArguments() {
            var filtersList = List.of(DEPARTURE_BEFORE_NOW.get());
            var filtersList2 = List.of(ARRIVAL_BEFORE_DEPARTURE.get());

            var flights = flightDynamicFilter.filterBy(filtersList, filtersList2);

            assertThat(flights).isEqualTo(arrivalBeforeDepartureOrDepartureBeforeNowExpected);
        }

        @Test
        void parallelFilterBy_throwIAE_withEmptyFilterList() {
            List<Predicate<Flight>> emptyFiltersList = Collections.emptyList();

            org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                    () -> flightDynamicFilter.filterBy(emptyFiltersList), "emptyFiltersList");
        }
    }

    @Nested
    class ParallelFilteringTest {
        @Test
        void parallelFilterBy_departureBeforeNow_withCorrectArguments() {
            var filtersList = List.of(DEPARTURE_BEFORE_NOW.get());

            var flights = flightDynamicFilter.parallelFilterBy(filtersList);

            assertThat(flights).isEqualTo(departureBeforeNowExpected);
        }

        @Test
        void parallelFilterBy_arrivalBeforeDeparture_withCorrectArguments() {
            var filtersList = List.of(ARRIVAL_BEFORE_DEPARTURE.get());

            var flights = flightDynamicFilter.parallelFilterBy(filtersList);

            assertThat(flights).isEqualTo(arrivalBeforeDepartureExpected);
        }

        @Test
        void parallelFilterBy_groundTimeMoreThanTwoHours_withCorrectArguments() {
            var filtersList = List.of(GROUND_TIME_MORE_THAN_2_HOURS.get());

            var flights = flightDynamicFilter.parallelFilterBy(filtersList);

            assertThat(flights).isEqualTo(groundTimeMoreThanTwoHoursExpected);
        }

        @Test
        void parallelFilterBy_arrivalBeforeDeparture_Or_departureBeforeNow_withCorrectArguments() {
            var filtersList = List.of(DEPARTURE_BEFORE_NOW.get());
            var filtersList2 = List.of(ARRIVAL_BEFORE_DEPARTURE.get());

            var flights = flightDynamicFilter.parallelFilterBy(filtersList, filtersList2);

            assertThat(flights).isEqualTo(arrivalBeforeDepartureOrDepartureBeforeNowExpected);
        }

        @Test
        void parallelFilterBy_throwIAE_withEmptyFilterList() {
            List<Predicate<Flight>> emptyFiltersList = Collections.emptyList();

            org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                    () -> flightDynamicFilter.parallelFilterBy(emptyFiltersList), "emptyFiltersList");
        }
    }

}