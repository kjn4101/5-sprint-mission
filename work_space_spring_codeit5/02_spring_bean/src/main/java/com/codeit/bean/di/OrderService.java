package com.codeit.bean.di;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final CustomerService customerService;

    @Autowired
    @Lazy // 순환 참조 발생시 지연로딩으로 해결!
    public OrderService(CustomerService customerService){
        this.customerService = customerService;
    }

    public String order(String productName, int amount){
        if(amount > 1){
            return customerService.customOrder(productName, amount);
        }
        System.out.println("주문 처리 : " + productName);
        return productName + " 주문이 완료되었습니다." + productName;
    }

}
