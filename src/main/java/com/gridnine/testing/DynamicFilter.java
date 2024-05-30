package com.gridnine.testing;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DynamicFilter<T> {

    private final Collection<T> data;

    protected DynamicFilter(Collection<T> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data for filtering should not be empty");
        }
        this.data = data;
    }

    @SafeVarargs
    public final List<T> filterBy(Collection<Predicate<T>>... filtersLists) {
        Predicate<T> resultFilter = processFilters(filtersLists);
        return data.stream().filter(resultFilter).toList();
    }

    @SafeVarargs
    public final List<T> parallelFilterBy(Collection<Predicate<T>>... filtersLists) {
        Predicate<T> resultFilter = processFilters(filtersLists);
        return data.stream().parallel().filter(resultFilter).toList();
    }

    private Predicate<T> processFilters(Collection<Predicate<T>>[] filters) {
        for (Collection<Predicate<T>> filter : filters) {
            if (filter.isEmpty()) {
                throw new IllegalArgumentException("Filters list should not be empty");
            }
        }

        if (filters.length == 1) {
            return compositeFilterFromList(filters[0]);
        }

        List<Predicate<T>> resultFiltersList =
                Stream.of(filters).map(this::compositeFilterFromList).toList();

        return resultFiltersList.stream().reduce(Predicate::or).orElseThrow();
    }

    private Predicate<T> compositeFilterFromList(Collection<Predicate<T>> filters) {
        return filters.stream().reduce(Predicate::and).orElseThrow();
    }

    public Collection<T> getData() {
        return data;
    }
}
