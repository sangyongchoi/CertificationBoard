package com.example.certificationboard.common.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class PageUtil {

    public static <T> boolean hasNext(Page<T> info, Pageable pageable){
        return pageable.getPageNumber() + 1 < info.getTotalPages();
    }
}
