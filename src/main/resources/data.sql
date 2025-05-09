-- 기존 테이블 삭제 (역순으로 삭제: 외래 키 참조 고려)
DROP TABLE IF EXISTS portfolio_item;
DROP TABLE IF EXISTS login_history;
DROP TABLE IF EXISTS portfolio_advice;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS stock;
DROP TABLE IF EXISTS `user`;

-- 1. user 테이블 생성
CREATE TABLE `user` (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        username VARCHAR(20) NOT NULL UNIQUE,
                        password VARCHAR(100) NOT NULL,
                        name VARCHAR(30) NOT NULL,
                        role VARCHAR(20) NOT NULL,
                        email VARCHAR(100),
                        phone_number VARCHAR(15),
                        address VARCHAR(255),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        created_by VARCHAR(20),
                        updated_by VARCHAR(20)
);

-- user 데이터 삽입
INSERT INTO `user` (username, password, name, role, email, phone_number, address, created_at, updated_at, created_by, updated_by)
VALUES
    ('admin', '$2a$10$mWAbJ7txsKNbN/aHav4K7e598rs.VrAy2LLdUFI7aMz4JovW9xKiK', '관리자', 'ADMIN', 'admin@example.com', '010-1234-5678', '서울시 강남구 테헤란로 123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('gildong.hong', '$2a$10$mWAbJ7txsKNbN/aHav4K7e598rs.VrAy2LLdUFI7aMz4JovW9xKiK', '홍길동', 'USER', 'user1@example.com', '010-2345-6789', '서울시 송파구 올림픽로 456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('mytestaccount', '$2a$10$mWAbJ7txsKNbN/aHav4K7e598rs.VrAy2LLdUFI7aMz4JovW9xKiK', '테스트', 'USER', 'user2@example.com', '010-3456-7890', '서울시 마포구 월드컵북로 789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('tester', '$2a$10$mWAbJ7txsKNbN/aHav4K7e598rs.VrAy2LLdUFI7aMz4JovW9xKiK', '테스터', 'USER', 'tester@example.com', '010-5678-9012', '경기도 성남시 분당구 판교로 123', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM');

-- 3. Account 테이블 생성
CREATE TABLE account (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT NOT NULL,
                         account_number VARCHAR(50) NOT NULL UNIQUE,
                         account_name VARCHAR(100),
                         balance BIGINT NOT NULL DEFAULT 0,
                         last_transaction_time TIMESTAMP,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         created_by VARCHAR(20),
                         updated_by VARCHAR(20),
                         CONSTRAINT uk_account_number_name UNIQUE (account_number, account_name),
                         CONSTRAINT fk_account_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);

-- Account 데이터 삽입
INSERT INTO account (user_id, account_number, account_name, balance, last_transaction_time, created_at, updated_at, created_by, updated_by)
VALUES
    (1, '123-45657-890', '테스트 계좌 1', 5000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    (1, '234-56789-012', '테스트 계좌 2', 50000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM');

-- 4. Stock 테이블 생성
CREATE TABLE stock (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       stock_code VARCHAR(20) NOT NULL UNIQUE,
                       stock_name VARCHAR(100) NOT NULL,
                       stock_price BIGINT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                       created_by VARCHAR(20),
                       updated_by VARCHAR(20)
);

-- Stock 데이터 삽입
INSERT INTO stock (stock_code, stock_name, stock_price, created_at, updated_at, created_by, updated_by)
VALUES
    ('005930', '삼성전자', 54000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('000660', 'SK하이닉스', 180000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('373220', '삼성바이오로직스', 1000000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('207940', 'LG에너지솔루션', 300000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('006400', '현대차', 180000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('051910', '한화에어로스페이스', 818000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('005380', 'KB금융', 90000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('000270', 'HD현대중공업', 400000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('035420', '셀트리온', 160000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('035720', '기아', 80000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('068270', 'NAVER', 197000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('105560', '신한지주', 51000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('005490', '한화오션', 7800, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('012330', '현대모비스', 24000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM'),
    ('096770', '메리츠금융지주', 18000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM');

-- 5. transaction 테이블 생성
CREATE TABLE transaction (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             transaction_id VARCHAR(50) NOT NULL UNIQUE,
                             account_id BIGINT,
                             account_number VARCHAR(50) NOT NULL,
                             transaction_type VARCHAR(20) NOT NULL,
                             balance_before BIGINT NOT NULL,
                             transaction_amount BIGINT NOT NULL,
                             balance_after BIGINT NOT NULL,
                             transaction_time DATETIME NOT NULL,
                             error_reason VARCHAR(50),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             created_by VARCHAR(20),
                             updated_by VARCHAR(20),
                             CONSTRAINT fk_transaction_account FOREIGN KEY (account_id) REFERENCES account (id)
);

-- 6. login_history 테이블 생성
CREATE TABLE login_history (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               user_id BIGINT,
                               username VARCHAR(20) NOT NULL,
                               event_type VARCHAR(20) NOT NULL,
                               event_time DATETIME NOT NULL,
                               ip_address VARCHAR(15) NOT NULL,
                               error_reason VARCHAR(50),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                               created_by VARCHAR(20),
                               updated_by VARCHAR(20),
                               CONSTRAINT fk_login_history_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);

-- 7. portfolio_advice 테이블 생성
CREATE TABLE portfolio_advice (
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  user_id BIGINT NOT NULL,
                                  account_number VARCHAR(50) NOT NULL,
                                  portfolio_type VARCHAR(40) NOT NULL,
                                  balance BIGINT,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  created_by VARCHAR(20),
                                  updated_by VARCHAR(20),
                                  CONSTRAINT fk_portfolio_advice_user FOREIGN KEY (user_id) REFERENCES `user` (id)
);


-- 8. portfolio_item 테이블 생성
CREATE TABLE portfolio_item (
                                id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                portfolio_advice_id BIGINT NOT NULL,
                                stock_id BIGINT NOT NULL,
                                quantity INT,
                                stock_price BIGINT,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                created_by VARCHAR(20),
                                updated_by VARCHAR(20),
                                CONSTRAINT fk_portfolio_item_portfolio_advice FOREIGN KEY (portfolio_advice_id) REFERENCES portfolio_advice (id),
                                CONSTRAINT fk_portfolio_item_stock FOREIGN KEY (stock_id) REFERENCES stock (id)
);