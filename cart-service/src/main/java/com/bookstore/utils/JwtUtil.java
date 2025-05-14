package com.bookstore.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;

@Component
public class JwtUtil {
    
    /**
     * Phân tích JWT token theo cách đơn giản, không cần secret key
     * @param token JWT token
     * @return userId được trích xuất từ token
     */
    public Long getUserIdFromToken(String token) {
        try {
            // Phân tích token thành các phần: header, payload, signature
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                System.err.println("Token không đúng định dạng JWT");
                return null;
            }
            
            // Giải mã phần payload (phần thứ 2)
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            System.out.println("Payload: " + payload);
            
            // Sử dụng thư viện JSON đơn giản để parse payload
            org.json.JSONObject jsonPayload = new org.json.JSONObject(payload);
            
            // Thử các trường thông dụng để lấy userId
            Long userId = null;
            if (jsonPayload.has("userId")) {
                userId = jsonPayload.getLong("userId");
            } else if (jsonPayload.has("sub")) {
                try {
                    userId = Long.parseLong(jsonPayload.getString("sub"));
                } catch (NumberFormatException e) {
                    // sub không phải là số, có thể là email hoặc username
                }
            } else if (jsonPayload.has("id")) {
                userId = jsonPayload.getLong("id");
            } else if (jsonPayload.has("user_id")) {
                userId = jsonPayload.getLong("user_id");
            }
            
            if (userId != null) {
                return userId;
            }
            
            // In thông tin debug nếu không tìm thấy userId
            System.out.println("Không tìm thấy userId trong token. Keys trong payload:");
            jsonPayload.keySet().forEach(key -> System.out.println("- " + key + ": " + jsonPayload.opt(key)));
            
            return null;
        } catch (Exception e) {
            System.err.println("Lỗi khi parse token: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Phương thức đơn giản để lấy tất cả các claims từ token
     * @param token JWT token
     * @return Map chứa các claims
     */
    public Map<String, Object> getAllClaimsFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            org.json.JSONObject jsonPayload = new org.json.JSONObject(payload);
            
            // Chuyển đổi JSONObject thành Map
            Map<String, Object> claims = new java.util.HashMap<>();
            jsonPayload.keySet().forEach(key -> claims.put(key, jsonPayload.opt(key)));
            
            return claims;
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy claims từ token: " + e.getMessage());
            return null;
        }
    }
}
