package com.bookstore.services.impl;

import com.bookstore.dtos.BookDTO;
import com.bookstore.dtos.CartResponseDTO;
import com.bookstore.entities.Cart;
import com.bookstore.repositories.CartRepository;
import com.bookstore.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${book.service.url}")
    private String bookServiceUrl;  // Sử dụng giá trị từ application.properties

    @Override
    public CartResponseDTO addBookToCart(Long userId, Long bookId) {
        try {
            // Sửa URL endpoint
            String bookEndpoint = bookServiceUrl + "/api/books/" + bookId;
            ResponseEntity<BookDTO> response = restTemplate.getForEntity(bookEndpoint, BookDTO.class);
            BookDTO bookDTO = response.getBody();

            if (bookDTO == null) {
                throw new RuntimeException("Không tìm thấy sách với ID: " + bookId);
            }

            // Tìm xem sách đã có trong giỏ hàng chưa
            Optional<Cart> existingCartItem = cartRepository.findByUserIdAndBookId(userId, bookId);
            
            Cart cartItem;
            if (existingCartItem.isPresent()) {
                cartItem = existingCartItem.get();
                cartItem.setQuantity(cartItem.getQuantity() + 1);
            } else {
                cartItem = new Cart();
                cartItem.setUserId(userId);
                cartItem.setBookId(bookId);
                cartItem.setQuantity(1);
            }
            
            cartItem = cartRepository.save(cartItem);

            // Tạo response DTO
            CartResponseDTO dto = new CartResponseDTO();
            dto.setCartId(cartItem.getId());
            dto.setUserId(userId);
            dto.setBookId(bookId);
            dto.setQuantity(cartItem.getQuantity());
            dto.setBookTitle(bookDTO.getTitle());
            dto.setBookAuthor(bookDTO.getAuthor());
            dto.setStockQuantity(bookDTO.getStockQuantity());
            dto.setBookCategory(bookDTO.getCategory());
            dto.setBookDescription(bookDTO.getDescription());
            dto.setBookStatus(bookDTO.isStatus());
            
            // Thiết lập bookImageUrl từ imageUrl hoặc coverImage của BookDTO
            if (bookDTO.getImageUrl() != null) {
                dto.setBookImageUrl(bookDTO.getImageUrl());
            } else if (bookDTO.getCoverImage() != null) {
                dto.setBookImageUrl(bookDTO.getCoverImage());
            }
            
            // Xử lý giá tiền
            if (bookDTO.getPrice() != null) {
                dto.setPrice(bookDTO.getPrice());
                dto.setTotalPrice(bookDTO.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }
            
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi thêm sách vào giỏ hàng: " + e.getMessage());
        }
    }

    @Override
    public List<CartResponseDTO> getAllBooksInCart(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        List<CartResponseDTO> result = new ArrayList<>();
        
        for (Cart item : cartItems) {
            try {
                String bookEndpoint = bookServiceUrl + "/api/books/" + item.getBookId();
                ResponseEntity<BookDTO> response = restTemplate.getForEntity(bookEndpoint, BookDTO.class);
                BookDTO bookDTO = response.getBody();
                
                if (bookDTO == null) {
                    continue;  // Bỏ qua nếu không tìm thấy sách
                }
                
                CartResponseDTO dto = new CartResponseDTO();
                dto.setCartId(item.getId());
                dto.setUserId(userId);
                dto.setBookId(item.getBookId());
                dto.setQuantity(item.getQuantity());
                dto.setBookTitle(bookDTO.getTitle());
                dto.setBookAuthor(bookDTO.getAuthor());
                dto.setStockQuantity(bookDTO.getStockQuantity());
                dto.setBookCategory(bookDTO.getCategory());
                dto.setBookDescription(bookDTO.getDescription());
                dto.setBookStatus(bookDTO.isStatus());
                
                // Thiết lập bookImageUrl từ imageUrl hoặc coverImage của BookDTO
                if (bookDTO.getImageUrl() != null) {
                    dto.setBookImageUrl(bookDTO.getImageUrl());
                } else if (bookDTO.getCoverImage() != null) {
                    dto.setBookImageUrl(bookDTO.getCoverImage());
                }
                
                if (bookDTO.getPrice() != null) {
                    dto.setPrice(bookDTO.getPrice());
                    dto.setTotalPrice(bookDTO.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                }
                
                result.add(dto);
            } catch (Exception e) {
                // Ghi log lỗi và tiếp tục vòng lặp
                System.err.println("Không thể lấy thông tin của sách " + item.getBookId() + ": " + e.getMessage());
            }
        }
        
        return result;
    }

    @Override
    public void removeBookFromCart(Long userId, Long bookId) {
        Cart cart = cartRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found for userId: " + userId + " and bookId: " + bookId));
        cartRepository.delete(cart);
    }

    @Override
    public void increaseBookQuantity(Long userId, Long bookId) {
        Cart cart = cartRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found for userId: " + userId + " and bookId: " + bookId));

        String bookEndpoint = bookServiceUrl + "/api/books/" + bookId;
        BookDTO bookDTO = restTemplate.getForObject(bookEndpoint, BookDTO.class);

        if (bookDTO == null || !bookDTO.isStatus()) {
            throw new IllegalArgumentException("Book not found or unavailable");
        }

        if (cart.getQuantity() + 1 > bookDTO.getStockQuantity()) {
            cart.setQuantity(bookDTO.getStockQuantity());
        } else {
            cart.setQuantity(cart.getQuantity() + 1);
        }

        cartRepository.save(cart);
    }

    @Override
    public void decreaseBookQuantity(Long userId, Long bookId) {
        Cart cart = cartRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found for userId: " + userId + " and bookId: " + bookId));

        if (cart.getQuantity() - 1 <= 0) {
            cartRepository.delete(cart);
        } else {
            cart.setQuantity(cart.getQuantity() - 1);
            cartRepository.save(cart);
        }
    }

    @Override
    public void clearCart(Long userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartItems);
    }
}