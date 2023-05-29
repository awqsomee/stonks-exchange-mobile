package com.example.stonksexchange.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

public class ArrayListSortUtil {
    public static <T, U> List<T> sortArrayList(List<T> list, Comparator<T> comparator, boolean sortOrder) {

        if (sortOrder) {
            comparator = comparator.reversed();
        }

        Collections.sort(list, comparator);
        return list;
    }
}