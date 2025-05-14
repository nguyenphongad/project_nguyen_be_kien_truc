package com.bookstore.config;

import com.bookstore.dtos.BookDTO;
import com.bookstore.entities.Book;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        // Sử dụng STRICT matching để đảm bảo properties khớp chính xác
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        // Tùy chỉnh mapping giữa BookDTO và Book
        modelMapper.addMappings(new PropertyMap<Book, BookDTO>() {
            protected void configure() {
                map().setId(source.getId());
                map().setTitle(source.getTitle());
                map().setAuthor(source.getAuthor());
                map().setPrice(source.getPrice());
                map().setStockQuantity(source.getStockQuantity());
                map().setCategory(source.getCategory());
                map().setDescription(source.getDescription());
                map().setStatus(source.isStatus());
                map().setCreatedAt(source.getCreatedAt());
                map().setUpdatedAt(source.getUpdatedAt());
                map().setImageUrl(source.getImageUrl());
                map().setPublicId(source.getPublicId());
                map().setCoverImage(source.getImageUrl()); // Sử dụng imageUrl làm coverImage
            }
        });
        
        // Tùy chỉnh mapping giữa BookDTO và Book
        modelMapper.addMappings(new PropertyMap<BookDTO, Book>() {
            protected void configure() {
                map().setId(source.getId());
                map().setTitle(source.getTitle());
                map().setAuthor(source.getAuthor());
                map().setPrice(source.getPrice());
                map().setStockQuantity(source.getStockQuantity());
                map().setCategory(source.getCategory());
                map().setDescription(source.getDescription());
                map().setStatus(source.isStatus());
                map().setCreatedAt(source.getCreatedAt());
                map().setUpdatedAt(source.getUpdatedAt());
                map().setImageUrl(source.getImageUrl());
                map().setPublicId(source.getPublicId());
                map().setCoverImage(source.getCoverImage());
            }
        });
        
        return modelMapper;
    }
}
