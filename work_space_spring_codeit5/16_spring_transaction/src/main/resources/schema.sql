DROP TABLE IF EXISTS delivery;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS users;


-- 사용자 테이블
CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100)        NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 상품 테이블
CREATE TABLE product
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(100)   NOT NULL,
    category VARCHAR(50)    NOT NULL,
    price    DOUBLE PRECISION NOT NULL
);

-- 주문 테이블 (사용자, 상품 연결)
CREATE TABLE orders
(
    id         BIGSERIAL PRIMARY KEY,
    user_id    BIGINT NOT NULL REFERENCES users (id),
    product_id BIGINT NOT NULL REFERENCES product (id),
    quantity   INT    NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 배송 테이블 (주문 연결)
CREATE TABLE delivery
(
    id           BIGSERIAL PRIMARY KEY,
    order_id     BIGINT       NOT NULL REFERENCES orders (id),
    address      VARCHAR(200) NOT NULL,
    status       VARCHAR(20) DEFAULT 'READY',
    delivered_at TIMESTAMP
);
