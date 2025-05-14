package com.bookstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private int stockQuantity;
    private String category;
    private String description;
    private boolean status;
    private String imageUrl;
    private String publicId;
    private String coverImage;
    
    // Thêm method này cho tương thích với Cart Service
    public String getTitle() {
        return title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public boolean isStatus() {
        return status;
    }
}