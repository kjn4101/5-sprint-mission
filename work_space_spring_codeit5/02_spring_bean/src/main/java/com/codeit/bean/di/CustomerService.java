package com.codeit.bean.di;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerService {

    private final OrderService orderService;

    @Autowired
    public CustomerService(OrderService orderService) {
        this.orderService = orderService;
    }

    public String customOrder(String productName, int amount){
        if(amount == 1){
            return orderService.order(productName, amount);
        }
        System.out.println("커스텀 주문 처리 : " + productName);
        return productName + " 커스텀 주문이 완료되었습니다. "
                + productName + ", amount : " + amount;
    }


}
