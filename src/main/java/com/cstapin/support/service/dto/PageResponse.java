package com.cstapin.support.service.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageResponse<T> {

    private final List<T> content;
    private final int size;
    private final long page;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.size = page.getSize();
        this.page = page.getNumber() + 1;
    }
}
