package com.yurets_y.payment_statistic_web.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.yurets_y.payment_statistic_web.entity.Views;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class JsonPage<T> extends org.springframework.data.domain.PageImpl<T> {

    public JsonPage(final List<T> content, final Pageable pageable, final long total) {
        super(content, pageable, total);
    }

    public JsonPage(final List<T> content) {
        super(content);
    }

    public JsonPage(final Page<T> page, final Pageable pageable) {
        super(page.getContent(), pageable, page.getTotalElements());
    }

    @JsonView(Views.ShortView.class)
    public int getTotalPages() {
        return super.getTotalPages();
    }

    @JsonView(Views.ShortView.class)
    public long getTotalElements() {
        return super.getTotalElements();
    }

    @JsonView(Views.ShortView.class)
    public boolean hasNext() {
        return super.hasNext();
    }

    @JsonView(Views.ShortView.class)
    public boolean isLast() {
        return super.isLast();
    }

    @JsonView(Views.ShortView.class)
    public boolean hasContent() {
        return super.hasContent();
    }

    @JsonView(Views.ShortView.class)
    public List<T> getContent() {
        return super.getContent();
    }
}
