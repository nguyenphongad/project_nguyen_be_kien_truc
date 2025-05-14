package com.bookstore.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private int stockQuantity;
    private String category;
    private String description;
    private boolean status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
    private String publicId;
    private String coverImage;
    
    // Field cho upload ảnh - sẽ không được ánh xạ vào entity
    private transient MultipartFile imageFile;
}