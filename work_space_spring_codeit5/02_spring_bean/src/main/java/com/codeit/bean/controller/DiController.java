package com.codeit.bean.controller;


import com.codeit.bean.di.CustomerService;
import com.codeit.bean.di.OrderService;
import com.codeit.bean.di.PaymentService;
import com.codeit.bean.di.impl.TossPaymentService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class DiController {

    @Autowired // 필드 주입
    @Qualifier("kakaoPaymentService") // 생략도 가능, 이유는 primary + kakaoPaymentService이름으로 매칭가능
    private PaymentService kakaoPaymentService;

    // 생성자 주입
    private final PaymentService naverPaymentService;

    public DiController(@Qualifier("naverPaymentService") PaymentService service){
        this.naverPaymentService = service;
    }

    // setter 주입
    private PaymentService tossPaymentService;

    @Autowired
    @Qualifier("tossPaymentService")
    public void setTossPaymentService(@Nullable /*@Qualifier("tossPaymentService")*/ PaymentService tossPaymentService) {
        if(tossPaymentService == null){
            tossPaymentService = new TossPaymentService();
        }
        this.tossPaymentService = tossPaymentService;
    }

    // Optional 사용법
    public void setTossPaymentService2(Optional<PaymentService> tossPaymentService) {
        this.tossPaymentService = tossPaymentService.orElseGet(TossPaymentService::new);
    }


    @GetMapping("/pay/kakao")
    public String payWithKakao(int amount) {
        return kakaoPaymentService.pay(amount);
    }

    @GetMapping("/pay/naver")
    public String payWithNaver(int amount) {
        return naverPaymentService.pay(amount);
    }

    @GetMapping("/pay/toss")
    public String payWithToss(int amount) {
        return tossPaymentService.pay(amount);
    }


    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;

    // 순환 참조 에러 발생!!! 그냥 하면 실행안된다.
    // @Lazy로 해결 가능!
    @GetMapping("/order")
    public String order(String product, int amount) {
        String result1 = orderService.order(product,  amount);
        String result2 = customerService.customOrder(product,  amount);
        return result1 + "<br>" + result2;
    }



}
