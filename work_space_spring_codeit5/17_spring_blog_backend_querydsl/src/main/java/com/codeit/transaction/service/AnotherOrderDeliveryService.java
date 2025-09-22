package com.codeit.transaction.service;

import com.codeit.transaction.entity.Delivery;
import com.codeit.transaction.entity.Orders;
import com.codeit.transaction.entity.Product;
import com.codeit.transaction.entity.User;
import com.codeit.transaction.repository.DeliveryRepository;
import com.codeit.transaction.repository.OrdersRepository;
import com.codeit.transaction.repository.ProductRepository;
import com.codeit.transaction.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnotherOrderDeliveryService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void internalSaveWithRequiresNewAndFail(Long userId, Long productId, int quantity, String address) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        Orders po = Orders.builder().user(user).product(product).quantity(quantity).orderDate(LocalDateTime.now()).build();
        orderRepository.save(po);

        Delivery dv = Delivery.builder().order(po).address(address).status("READY").build();
        deliveryRepository.save(dv);

        if (true) {
            System.out.println("REQUIRES_NEW 적용");
            throw new IllegalStateException("REQUIRES_NEW 적용");
        }
    }

}
