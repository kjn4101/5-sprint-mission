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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderDeliveryService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    private final MailService mailService;

    // 1) 정상 시나리오: 주문 저장 -> 배송 저장 -> 메일 발송
    @Transactional
//    @Transactional(isolation = Isolation.DEFAULT) // DB default 옵션, postgreSQL에선 READ_COMMITTED
//    @Transactional(isolation = Isolation.READ_COMMITTED)  // Isolation=격리레벨, 높을수록 성능은 떨어지나 격리성을 보장 -> DB 이상현상이 방지
//    @Transactional(isolation = Isolation.REPEATABLE_READ) // 성능과 격리성 보장하는 레벨, REPEATABLE_READ = mysql 기본 값 ★
//    @Transactional(isolation = Isolation.SERIALIZABLE) // 가장 높은 격리성 보장 레벨, 일반적으로 성능저하 때문에 잘쓰이진 않는다.
//    @Transactional(timeout = 3) // transaction timeout expired
    public void placeOrder(Long userId, Long productId, int quantity, String address) throws IOException {
        // 사용자, 상품 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productId));

        // 주문 저장
        Orders purchaseOrder = Orders.builder().user(user).product(product).quantity(quantity).orderDate(LocalDateTime.now()).build();
        orderRepository.save(purchaseOrder);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 배송 저장
        Delivery delivery = Delivery.builder().order(purchaseOrder).address(address).status("READY").build();
        deliveryRepository.save(delivery);

        // 메일 발송
        mailService.sendOrderConfirmation(purchaseOrder, true);
        System.out.println("[SERVICE] 주문 및 배송 처리 완료. orderId=" + purchaseOrder.getId());
    }


    // 2) 중간 예외 발생 시나리오: 주문 저장 -> 배송 저장 (여기서 런타임 예외 발생 예정!) -> 메일 발송
    @Transactional // 예외 발생시 롤백이 동작되고, RuntimeException 또는 진짜 Error만 롤백을 실행함.
    public void placeOrderTest2(Long userId, Long productId, int quantity, String address) throws IOException {
        // 사용자, 상품 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productId));

        // 주문 저장
        Orders purchaseOrder = Orders.builder().user(user).product(product).quantity(quantity).orderDate(LocalDateTime.now()).build();
        orderRepository.save(purchaseOrder);

        // 배송 저장
        Delivery delivery = Delivery.builder().order(purchaseOrder).address(address).status("READY").build();
        deliveryRepository.save(delivery);

        if(true){
            // PersistenceException : JPA 쿼리 조회시 DB 예외로 인해 잘못된 경우 발생하는 예외!
            // -> RuntimeException이다.
            throw new PersistenceException("아이디가 중복되었습니다.");
//            throw new RuntimeException("아이디가 중복되었습니다.");
        }

        // 메일 발송
        mailService.sendOrderConfirmation(purchaseOrder, true);
        System.out.println("[SERVICE] 주문 및 배송 처리 완료. orderId=" + purchaseOrder.getId());
    }
    
    // 3) IOException(checked 예외) 시나리오 : 주문 저장 -> 배송 저장  -> 메일 발송(IO 예외 발생!!)
//    @Transactional // -> IOException이나 일반 Exception으로는 롤백이 안된다!
    @Transactional(rollbackFor = {IOException.class}) // -> IOException이 발생해도 롤백 되는 문장!
    public void placeOrderTest3(Long userId, Long productId, int quantity, String address) throws IOException {
        // 사용자, 상품 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품 없음: " + productId));

        // 주문 저장
        Orders purchaseOrder = Orders.builder().user(user).product(product).quantity(quantity).orderDate(LocalDateTime.now()).build();
        orderRepository.save(purchaseOrder);

        // 배송 저장
        Delivery delivery = Delivery.builder().order(purchaseOrder).address(address).status("READY").build();
        deliveryRepository.save(delivery);

        // 메일 발송
        mailService.sendOrderConfirmation(purchaseOrder, false); // IOException 발생 지점!!
        System.out.println("[SERVICE] 주문 및 배송 처리 완료. orderId=" + purchaseOrder.getId());
    }


    // 내부 메서드 실패 시나리오들
    // 1) 내부 런타임 예외 → 기본정책 롤백
    @Transactional
    public void placeOrderTest4(Long userId, Long productId, int quantity, String address) throws IOException {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        Orders po = Orders.builder().user(user).product(product).quantity(quantity).orderDate(LocalDateTime.now()).build();
        orderRepository.save(po);

        Delivery dv = Delivery.builder().order(po).address(address).status("READY").build();
        deliveryRepository.save(dv);

        // 내부 메서드에서 런타임 예외 발생 → 트랜잭션 롤백
        doAfterDeliveryRuntimeCheck(po, dv);   // 내부에서 IllegalStateException 발생

        mailService.sendOrderConfirmation(po, true);
        System.out.println("[SERVICE] 완료(도달 불가)");
    }

    private void doAfterDeliveryRuntimeCheck(Orders po, Delivery dv) {
        if (dv.getId() != null) {
            System.out.println("내부 런타임 예외 발생!!!");
            throw new IllegalStateException("내부 런타임 예외 발생");
        }
    }


    final AnotherOrderDeliveryService anotherOrderDeliveryService;

    // 정말 주의해야하는 롤백이 걸리지 않는 예외 케이스 ★★★★★★
    // 4) self-invocation 시나리오: 같은 클래스의 @Transactional 메서드를 내부 호출
    //  - 내부 메서드에 REQUIRES_NEW를 달아도 '같은 클래스 내부 호출'이면 프록시가 적용되지 않아 새 트랜잭션이 열리지 않는다
    //  - 결과적으로 내부 런타임 예외가 외부 트랜잭션까지 롤백시킨다
    @Transactional
    public void placeOrderTest5(Long userId, Long productId, int quantity, String address) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        Orders po = Orders.builder().user(user).product(product).quantity(quantity).orderDate(LocalDateTime.now()).build();
        orderRepository.save(po);

        Delivery dv = Delivery.builder().order(po).address(address).status("READY").build();
        deliveryRepository.save(dv);

        // 같은 클래스 내부 호출 → 프록시 우회 → 아래 @Transactional 설정(심지어 REQUIRES_NEW라 해도) 적용되지 않음
        try {
//           this.internalSaveWithRequiresNewAndFail(userId,productId, quantity, address); // 비정상, REQUIRES_NEW가 동작하지 않음
//           internalSaveWithRequiresNewAndFail(userId, productId, quantity, address); // 비정상, REQUIRES_NEW가 동작하지 않음
//            anotherOrderDeliveryService.internalSaveWithRequiresNewAndFail(userId, productId, quantity, address);
           ((OrderDeliveryService) AopContext.currentProxy()).internalSaveWithRequiresNewAndFail(userId, productId, quantity, address);
        } catch (Exception e) {}
    }

    // 내부 메서드에 트랜잭션 어노테이션을 달더라도 '같은 클래스 내부 호출'이면 적용되지 않는다
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void internalSaveWithRequiresNewAndFail(Long userId, Long productId, int quantity, String address) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        Orders po = Orders.builder().user(user).product(product).quantity(quantity).orderDate(LocalDateTime.now()).build();
        orderRepository.save(po);

        Delivery dv = Delivery.builder().order(po).address(address).status("READY").build();
        deliveryRepository.save(dv);

        if (true) {
            System.out.println("self-invocation으로 REQUIRES_NEW 미적용");
            throw new IllegalStateException("self-invocation으로 REQUIRES_NEW 미적용");
        }
    }


//    트랜잭션 활용 원칙
//    1. Service 계층에만 트랜잭션을 단다.
//    2. 런타임 예외는 자동 롤백, 체크 예외는 rollbackFor로 지정.
//    3. 예외를 삼키지 않는다. (try-catch 문은 의도적으로만 활용)
//    4. 자기 자신 호출, final/private 주의
//    5. 조회 전용은 readOnly, 쓰기는 기본값.
//    6. 외부 시스템 호출은 커밋 이후.
//    7. 부분 커밋은 REQUIRES_NEW로 의도적으로만

}
