package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public enum FlightFilters {

    NO_FILTER(flight -> true),

    DEPARTURE_BEFORE_NOW(flight -> flight.getSegments().stream()
            .anyMatch(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now()))),

    ARRIVAL_BEFORE_DEPARTURE(flight -> flight.getSegments().stream()
            .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()))),

    GROUND_TIME_MORE_THAN_2_HOURS(flight -> {
        if (flight.getSegments().size() > 1) {
            var segments = flight.getSegments();
            var groundDuration = IntStream.range(0, segments.size() - 1)
                    .mapToObj(
                            i -> Duration.between(segments.get(i).getArrivalDate(),
                                    segments.get(i + 1).getDepartureDate()))
                    .reduce(Duration::plus).orElseThrow();
            return groundDuration.toHours() > 2;
        }
        return false;
    });

    private final Predicate<Flight> predicate;

    FlightFilters(Predicate<Flight> predicate) {
        this.predicate = predicate;
    }

    public Predicate<Flight> get() {
        return predicate;
    }

    public Predicate<Flight> getNegative() {
        return Predicate.not(predicate);
    }
}
