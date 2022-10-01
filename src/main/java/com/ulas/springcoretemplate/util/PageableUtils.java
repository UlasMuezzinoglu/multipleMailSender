package com.ulas.springcoretemplate.util;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Component;

import static com.ulas.springcoretemplate.constant.GeneralConstants.DEFAULT_SORT_BY;
import static com.ulas.springcoretemplate.constant.GeneralConstants.PAGE_SIZE;

@Component
public class PageableUtils {

    /**
     * this method provides pageable of anything and set by given params this is template
     *
     * @param pageNumber as int which page to show (starts from zero )
     * @param pageSize   as Integer number of data on a page
     * @param sortBy     as String which field to sort by
     * @param direction  as Sort.Direction descending or ascending
     * @return PageRequest format
     */
    public static PageRequest getPageable(final int pageNumber, final Integer pageSize,
                                          final String sortBy, final Sort.Direction direction) {
        if (sortBy == null) {
            return getPageable(pageNumber);
        } else {
            try {
                return PageRequest.of(pageNumber, pageSize, direction, sortBy);
            } catch (PropertyReferenceException exception) {
                return PageRequest.of(pageNumber, pageSize,
                        Sort.by(DEFAULT_SORT_BY).descending());
            }
        }
    }

    /**
     * this method provides pageable of anything. but as static
     *
     * @param pageNumber as int which page to show (starts from zero )
     * @param sortBy     as String which field to sort by
     * @param direction  as Sort.Direction  descending or ascending
     * @return PageRequest format
     */
    public static final PageRequest getPageable(final int pageNumber, final String sortBy,
                                                final Sort.Direction direction) {
        return getPageable(pageNumber, PAGE_SIZE, sortBy, direction);
    }

    /**
     * this method provides pageable of anything. but just pageNumber param
     *
     * @param pageNumber int which page to show (starts from zero )
     * @return PageRequest format
     */
    public static PageRequest getPageable(final int pageNumber) {
        return PageRequest.of(pageNumber, PAGE_SIZE, Sort.by(Sort.Direction.DESC, DEFAULT_SORT_BY));
    }

    /**
     * this method provides pageable of anything
     *
     * @param pageNumber as int which page to show (starts from zero )
     * @param direction  as Sort.Direction descending or ascending
     * @param sortBy     as String which field to sort by
     * @return PageRequest format
     */
    public final PageRequest getPageable(final int pageNumber, final Sort.Direction direction, final String sortBy) {
        return getPageable(pageNumber, PAGE_SIZE, sortBy, direction);
    }
}
