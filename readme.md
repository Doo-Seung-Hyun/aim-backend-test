# Backend Coding Test - 자문서버 프로젝트

## 프로젝트 소개
이 프로젝트는 백엔드 코딩테스트 과제 프로젝트로, 회원/입출금/증권 등의 데이터와 기능을 기반으로 포트폴리오 자문을 제공하는 테스트용 프로젝트입니다.

## 기술 스택
- Java 17
- Spring Boot 3.4.5
- Spring Security 3.4.5
- Spring Data JPA 3.4.5
- Lombok
- MySQL 9.3.0
- Gradle

## 도메인 구성
- **User**: 회원가입/로그인 기능 및 인증 관련 도메인
- **Common**: 공통 DTO, 시큐리티 설정, 로그인 히스토리 등
- **Account**: 계좌 입출금 기능 및 거래내역 관리
- **Stock**: 증권 관리 기능
- **Portfolio**: 포트폴리오(자문) 관리 기능

## API 명세

### 사용자 관리
- `POST /api/users/register`: 회원가입
- `POST /api/users/login`: 로그인
- `POST /api/users/logout`: 로그아웃

### 계좌 관리
- `POST /api/accounts/deposit`: 입금
- `POST /api/accounts/withdraw`: 출금
- `GET /api/accounts/my`: 나의 계좌 조회
- `GET /api/accounts/{id}`: 계좌id로 계좌조회

 > 계좌번호가 민감정보임을 감안하여, api url로 노출시키지 않음

### 증권 관리 (관리자 전용)
- `POST /api/stocks`: 증권 등록
- `PUT /api/stocks/{code}`: 증권 가격 업데이트
- `DELETE /api/stocks/{code}`: 증권 삭제
- `GET /api/stocks`: 증권 목록 조회

### 포트폴리오 관리
- `POST /api/portfolio/advice`: 자문 요청

## 요구사항 및 구현 현황

### 데이터베이스
- **MySQL 사용**: 완료
    - 테스트 데이터 (증권데이터 10개 이상)를 위해 data.sql 파일 작성

### 사용자 관리
- **회원가입 기능**: 완료
    - 사용자 ID, 암호화된 비밀번호로 회원가입 가능
- **로그인/로그아웃 기능**: 완료
    - Spring Security를 활용한 인증 처리
    - 로그인 성공, 실패, 로그아웃 이력 기록
- **인증 필요 API 제한**: 완료
    - 로그인/회원가입 외 모든 API는 인증된 사용자만 접근 가능
> 참고 - 사용자 계정은 admin // pasword123!@#  으로 기본 설정되어있음

### 계좌 관리
- **입금 기능**: 완료
    - 입금 시 사용자 잔고 증가
- **출금 기능**: 완료
    - 출금 시 사용자 잔고 차감
- **거래내역 기록**: 완료
    - 모든 입출금 내역 기록
    - 입출금 실패 내역도 기록
> 참고 - user_id : 1 (username : admin)의 테스트 계좌가 등록되어 있음

### 증권 관리
- **증권 속성 구현**: 완료
    - 증권코드, 증권이름, 가격 정보 포함
- **기본 증권 데이터**: 완료
    - 최소 10개의 증권 데이터 등록 (data.sql 활용)
- **증권 관리 API**: 완료
    - 등록, 가격 업데이트, 삭제 API 구현
    - 관리자 권한 검증 처리

### 포트폴리오 관리
- **위험도 선택 기능**: 완료
    - 2가지 유형의 포트폴리오 위험도 선택 가능
- **자문 요청 기능**: 완료
    - 선택한 유형에 따라 잔고 활용 방식 다르게 적용
- **정수 단위 증권 구성**: 완료
    - 모든 증권은 정수 단위로만 구성

## 설치 및 실행 방법

### 요구사항
- Java 17 이상
- Gradle 7.0 이상

### 실행 방법
1. 프로젝트 클론
   ```bash
   git clone https://github.com/Doo-Seung-Hyun/aim-backend-test.git
   ```

2. 애플리케이션 빌드
   ```bash
   ./gradlew build
   ```

3. 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```

4. 애플리케이션 접근
    - 기본 접속 주소: http://localhost:8080
  ~~- H2 콘솔: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:test)~~

## 주요 기능 설명


### 참고 사항
- data.sql 파일에 의해 테스트 데이터 생성
- 유저도 미리 생성해뒀으며, ADMIN 권한을 가진 admin // password123!@# 사용 가능

### 인증 처리
- Spring Security 기반의 세션 인증
- 인증 이벤트 리스너 및 로그아웃 핸들러를 통한 로그인/로그아웃 이력 기록

### 계좌 거래 처리
- 템플릿 메소드 패턴을 활용한 거래 프로세스 구현
- 추상 클래스를 통해 공통 거래 로직과 입출금 전용 로직 분리

### 포트폴리오 자문 알고리즘
- 동적 프로그래밍(DP)을 응용한 최적 포트폴리오 구성 알고리즘 구현
- 투자가능금액에 가장 근접한 증권 조합 탐색
- 타임아웃 방지를 위한 최대 루프 카운터 설정

### 관리자 기능
- ADMIN 권한을 가진 사용자만 증권 관리 기능 접근 가능
- 권한 기반 API 접근 제어
