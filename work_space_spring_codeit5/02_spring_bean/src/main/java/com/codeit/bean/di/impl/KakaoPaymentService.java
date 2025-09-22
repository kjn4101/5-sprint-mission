package com.codeit.bean.di.impl;


import com.codeit.bean.di.PaymentService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service("kakaoPaymentService") // 별칭의 표준적인 방법, Class의 앞글자만 소문자
public class KakaoPaymentService implements PaymentService {
    @Override
    public String pay(int amount) {
        return "Kakao Payment : " + amount + "원 결제 완료";
    }
}
