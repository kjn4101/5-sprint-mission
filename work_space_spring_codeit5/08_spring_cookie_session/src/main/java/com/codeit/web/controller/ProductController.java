package com.codeit.web.controller;

import com.codeit.web.dto.ProductDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/products")
public class ProductController {
    // 임시 상품 리스트
    private static final Map<String, ProductDto> productMap = new LinkedHashMap<>();

    static {
        productMap.put("1", new ProductDto("1", "에어팟", 199000));
        productMap.put("2", new ProductDto("2", "아이폰", 1499000));
        productMap.put("3", new ProductDto("3", "맥북", 2499000));
    }

    @GetMapping("")
    public String showProductList(Model model) {
        model.addAttribute("products", productMap.values());
        return "product/productList";
    }

    @GetMapping("/add/{id}")
    public String addProduct(@PathVariable String id, HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie("recent_product", URLEncoder.encode(id, StandardCharsets.UTF_8));
        cookie.setPath("/");                     // 모든 경로에서 사용
        cookie.setMaxAge(60 * 60);               // 1시간 유지
        response.addCookie(cookie);

        return "redirect:/products";
    }

    @GetMapping("/recent")
    public String recentViewedProducts(HttpServletRequest request, Model model) throws Exception {
        Cookie[] cookies = request.getCookies();
        String recentId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("recent_product".equals(cookie.getName())) {
                    recentId = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    break;
                }
            }
        }

        if (recentId != null && productMap.containsKey(recentId)) {
            model.addAttribute("recentProduct", productMap.get(recentId));
        } else {
            model.addAttribute("recentProduct", null);
        }

        return "product/recentView";
    }
}
