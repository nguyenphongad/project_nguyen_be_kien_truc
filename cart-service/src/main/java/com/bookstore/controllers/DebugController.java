package com.bookstore.controllers;

import com.bookstore.dtos.BookDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${book.service.url}")
    private String bookServiceUrl;

    @GetMapping("/book/{id}")
    public ResponseEntity<?> getBookDetails(@PathVariable Long id) {
        try {
            String url = bookServiceUrl + "/api/books/" + id;
            ResponseEntity<BookDTO> response = restTemplate.getForEntity(url, BookDTO.class);
            BookDTO bookDTO = response.getBody();
            
            return ResponseEntity.ok()
                .body(java.util.Map.of(
                    "success", true,
                    "book", bookDTO,
                    "imageUrl", bookDTO != null ? bookDTO.getImageUrl() : null,
                    "coverImage", bookDTO != null ? bookDTO.getCoverImage() : null,
                    "url", url
                ));
        } catch (Exception e) {
            return ResponseEntity.ok()
                .body(java.util.Map.of(
                    "success", false,
                    "error", e.getMessage(),
                    "type", e.getClass().getName()
                ));
        }
    }
}
