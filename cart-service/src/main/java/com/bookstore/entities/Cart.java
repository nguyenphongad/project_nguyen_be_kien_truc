package com.bookstore.entities;

import javax.persistence.*;  // Thay đổi từ jakarta.persistence sang javax.persistence
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "book_id")
    private Long bookId;

    @Column(nullable = false, name = "quantity")
    private int quantity;
}
