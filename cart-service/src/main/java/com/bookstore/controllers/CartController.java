package com.bookstore.controllers;

import com.bookstore.dtos.CartResponseDTO;
import com.bookstore.entities.Cart;
import com.bookstore.repositories.CartRepository;
import com.bookstore.services.CartService;
import com.bookstore.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3333"}, allowCredentials = "true")
public class CartController {

    @Autowired
    private CartService cartService;
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Value("${book.service.url}")
    private String bookServiceUrl;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDTO> addBookToCart(
            @RequestHeader(value = "UserId") String userIdHeader,
            @RequestParam Long bookId) {
        // Parse userId from header, handling potential format issues
        Long userId = parseUserId(userIdHeader);
        System.out.println("Adding book: " + bookId + " to cart for user: " + userId);
        CartResponseDTO response = cartService.addBookToCart(userId, bookId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CartResponseDTO>> getAllBooksInCart(
            @RequestHeader(value = "UserId") String userIdHeader) {
        // Parse userId from header
        Long userId = parseUserId(userIdHeader);
        System.out.println("Getting all books in cart for user: " + userId);
        List<CartResponseDTO> response = cartService.getAllBooksInCart(userId);
        System.out.println("Retrieved " + response.size() + " items from cart");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<List<CartResponseDTO>> removeBookFromCart(
            @RequestHeader(value = "UserId") String userIdHeader,
            @RequestParam Long bookId) {
        // Parse userId from header
        Long userId = parseUserId(userIdHeader);
        cartService.removeBookFromCart(userId, bookId);
        List<CartResponseDTO> updatedCart = cartService.getAllBooksInCart(userId);
        return ResponseEntity.ok(updatedCart);
    }
    
    @PatchMapping("/increase")
    public ResponseEntity<List<CartResponseDTO>> increaseBookQuantity(
            @RequestHeader(value = "UserId") String userIdHeader,
            @RequestParam Long bookId) {
        // Parse userId from header
        Long userId = parseUserId(userIdHeader);
        cartService.increaseBookQuantity(userId, bookId);
        List<CartResponseDTO> updatedCart = cartService.getAllBooksInCart(userId);
        return ResponseEntity.ok(updatedCart);
    }
    
    @PatchMapping("/decrease")
    public ResponseEntity<List<CartResponseDTO>> decreaseBookQuantity(
            @RequestHeader(value = "UserId") String userIdHeader,
            @RequestParam Long bookId) {
        // Parse userId from header
        Long userId = parseUserId(userIdHeader);
        cartService.decreaseBookQuantity(userId, bookId);
        List<CartResponseDTO> updatedCart = cartService.getAllBooksInCart(userId);
        return ResponseEntity.ok(updatedCart);
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(
            @RequestHeader(value = "UserId") String userIdHeader) {
        // Parse userId from header
        Long userId = parseUserId(userIdHeader);
        cartService.clearCart(userId);
        return ResponseEntity.ok().build();
    }
    
    /**
     * API đơn giản để lấy userId từ token
     * @param authHeader JWT token trong header Authorization
     * @return userId từ token
     */
    @GetMapping("/get-user-id")
    public ResponseEntity<?> getUserIdFromToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            // Kiểm tra header
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Cần cung cấp token trong header Authorization với định dạng 'Bearer {token}'"
                ));
            }

            // Lấy token từ header
            String token = authHeader.substring(7);
            System.out.println("Token: " + token);
            
            // Lấy userId từ token bằng cách đơn giản
            Map<String, Object> allClaims = jwtUtil.getAllClaimsFromToken(token);
            Long userId = jwtUtil.getUserIdFromToken(token);
            
            if (userId == null) {
                return ResponseEntity.status(400).body(Map.of(
                    "success", false,
                    "message", "Không tìm thấy userId trong token",
                    "claims", allClaims
                ));
            }
            
            // Trả về thành công
            return ResponseEntity.ok(Map.of(
                "success", true,
                "userId", userId
            ));
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of(
                "success", false,
                "message", "Lỗi server: " + e.getMessage()
            ));
        }
    }
    
    // Method to handle parsing userId from header
    private Long parseUserId(String userIdHeader) {
        try {
            // Check if the header is an object representation (like '[object Object]')
            if (userIdHeader != null && userIdHeader.contains("[object Object]")) {
                // Log the issue for debugging
                System.err.println("Received invalid UserId header: " + userIdHeader);
                // Default to userId 1 for demonstration purposes
                return 1L;
            }
            System.out.println("Parsing userId from header: " + userIdHeader);
            return Long.valueOf(userIdHeader);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing UserId: " + e.getMessage());
            // Default to userId 1 if parsing fails
            return 1L;
        }
    }
}