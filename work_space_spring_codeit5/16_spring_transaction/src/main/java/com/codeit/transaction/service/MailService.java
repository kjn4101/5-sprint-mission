package com.codeit.transaction.service;

import com.codeit.transaction.entity.Delivery;
import com.codeit.transaction.entity.Orders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
public class MailService {
    public void sendOrderConfirmation(Orders order, boolean isSuccess) throws IOException {
        System.out.println("[MAIL] 주문 확인 메일 발송 완료 orderId=" + order.getId());
        if(!isSuccess){
            System.out.println("예외 발생!!");
            throw new IOException("예외 발생!");
        }
    }
}