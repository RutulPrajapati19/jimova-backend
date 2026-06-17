package com.enterprise.smartEcommerce.dtos;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content; // The actual list of products/orders
    private int pageNumber;  // Current page number (starts from 0)
    private int pageSize;    // How many items per page
    private long totalElements;// Total items in the entire database
    private int totalPages;  // Total number of pages available
    private boolean isLast;  // Is this the final page?
}
