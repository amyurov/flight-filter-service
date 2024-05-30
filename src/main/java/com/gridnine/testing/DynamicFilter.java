package com.gridnine.testing;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Класс {@code DynamicFilter} предоставляет методы для фильтрации коллекций данных
 * с использованием нескольких предикатов. Поддерживается последовательная и
 * параллельная фильтрация.
 *
 * @param <T> тип фильтруемых элементов.
 */
public class DynamicFilter<T> {

    private final Collection<T> data;

    /**
     * @param data коллекция данных для фильтрации.
     * @throws IllegalArgumentException если коллекция данных пуста.
     */
    protected DynamicFilter(Collection<T> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Data for filtering should not be empty");
        }
        this.data = data;
    }

    /**
     * Метод последовательно фильтрует данные, с использованием указанных предикатов.
     *
     * @param filtersLists списки предикатов для фильтрации данных. Список предикатов конвертируется
     * в один предикат. При получении нескольких списков, получившиеся предикаты объединятся через
     * логическое ИЛИ.
     * @return список отфильтрованных данных.
     * @throws IllegalArgumentException если любой из списков предикатов пуст.
     */
    @SafeVarargs
    public final List<T> filterBy(Collection<Predicate<T>>... filtersLists) {
        Predicate<T> resultFilter = processFilters(filtersLists);
        return data.stream().filter(resultFilter).toList();
    }

    /**
     * Метод фильтрует данные несколькими потоками, с использованием указанных предикатов.
     *
     * @param filtersLists списки предикатов для фильтрации данных. Список предикатов конвертируется
     * в один предикат. При получении нескольких списков, получившиеся предикаты объединятся через
     * логическое ИЛИ.
     * @return список отфильтрованных данных.
     * @throws IllegalArgumentException если любой из списков предикатов пуст.
     */
    @SafeVarargs
    public final List<T> parallelFilterBy(Collection<Predicate<T>>... filtersLists) {
        Predicate<T> resultFilter = processFilters(filtersLists);
        return data.stream().parallel().filter(resultFilter).toList();
    }

    /**
     * Метод обрабатывает списки предикатов и создает комбинированный предикат.
     *
     * @param filters массив списков предикатов.
     * @return комбинированный предикат.
     * @throws IllegalArgumentException если любой из списков предикатов пуст.
     */
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

    /**
     * Создает комбинированный предикат из списка предикатов.
     *
     * @param filters список предикатов.
     * @return комбинированный предикат.
     * @throws IllegalArgumentException если список предикатов пуст.
     */
    private Predicate<T> compositeFilterFromList(Collection<Predicate<T>> filters) {
        return filters.stream().reduce(Predicate::and).orElseThrow();
    }

    public Collection<T> getData() {
        return data;
    }
}
