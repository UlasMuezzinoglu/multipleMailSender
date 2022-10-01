package com.ulas.springcoretemplate.model.util;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class CustomPage<T> implements Streamable<T>, Serializable {
    private final Deque<T> content = new LinkedList<>();
    private final boolean last;
    private final int size;
    private final int number;
    private final Sort sort;
    private final boolean first;
    private final int numberOfElements;
    private final boolean empty;
    private final int totalPages;
    private final long totalElements;
    private final PageRequest pageRequest;
    private final int pageSize;
    private CustomPageable pageable;

    public CustomPage(List<T> items, PageRequest pageRequest, long totalElements) {
        addItems(items, pageRequest);
        this.totalElements = totalElements;
        this.pageSize = pageRequest.getPageSize();
        this.number = pageRequest.getPageNumber();
        this.pageRequest = pageRequest;
        this.size = items.size();
        this.sort = pageRequest.getSort();
        this.numberOfElements = items.size();
        this.empty = items.isEmpty();
        this.totalPages = getTotalPages();
        this.pageable = new CustomPageable(this.pageRequest.getOffset(), this.number, pageRequest.getPageSize(), false, true);
        this.last = isLast();
        this.first = isFirst();
    }

    public void addItems(Stack<T> items, PageRequest pageRequest) {
        int endIndex = pageRequest.getPageSize() * (pageRequest.getPageNumber() + 1);
        int startIndex = pageRequest.getPageSize() * pageRequest.getPageNumber();
        endIndex = Math.min(endIndex, items.size());
        startIndex = Math.min(startIndex, endIndex);
        this.sublist(items, startIndex, endIndex);
    }

    private void sublist(Stack<T> items, int startIndex, int endIndex) {
        ListIterator<T> iterator = items.listIterator(items.size());
        while (iterator.hasPrevious()) {
            if (endIndex <= 0)
                return;
            if (startIndex <= 0)
                this.content.addFirst(iterator.previous());
            else iterator.previous();
            startIndex--;
            endIndex--;
        }
    }

    private void addItems(List<T> items, PageRequest pageRequest) {
        if (items.size() <= pageRequest.getPageSize()) {
            this.content.addAll(items);
            return;
        }
        int endIndex = pageRequest.getPageSize() * (pageRequest.getPageNumber() + 1);
        int startIndex = pageRequest.getPageSize() * pageRequest.getPageNumber();
        endIndex = Math.min(endIndex, items.size());
        startIndex = Math.min(startIndex, endIndex);
        this.content.addAll(items.subList(startIndex, endIndex));
    }

    public long getTotalElements() {
        return this.totalElements;
    }

    public boolean hasNext() {
        return this.getNumber() + 1 < this.getTotalPages();
    }

    public boolean isLast() {
        return !this.hasNext();
    }

    public boolean isFirst() {
        return !this.hasPrevious();
    }

    public boolean hasPrevious() {
        return this.getNumber() > 0;
    }

    public int getNumber() {
        return this.pageable.isPaged() ? this.pageable.getPageNumber() : 0;
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) this.totalElements / (double) this.pageSize);
    }

    @Override
    public Iterator<T> iterator() {
        return this.content.iterator();
    }

    public <U> CustomPage<U> map(Function<? super T, ? extends U> converter) {
        return new CustomPage(this.getConvertedContent(converter), this.pageRequest, getTotalElements());
    }

    private <U> List getConvertedContent(Function<? super T, ? extends U> converter) {
        return this.stream().map(converter::apply).collect(Collectors.toList());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    class CustomPageable implements Serializable {
        private long offset;
        private int pageNumber;
        private int pageSize;
        private boolean unPaged;
        private boolean paged;
    }
}
