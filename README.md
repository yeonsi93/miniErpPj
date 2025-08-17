# miniERP

📊 **ERP(Enterprise Resource Planning) 기본 기능을 구현한 개인 프로젝트**  
재고 및 자재 관리 프로세스를 단순화하여 미니 ERP 시스템을 목표로 개발
데이터베이스 설계부터 CRUD API, 통계 조회까지 백엔드 전반을 직접 구현

---

## ✨ 주요 기능
- **자재 관리**: 자재 등록, 수정, 삭제, 검색
- **재고 관리**: 입출고 내역 기록, 재고 수량 확인
- **대시보드**:
  - Top N 재고 현황
  - 카테고리별 재고 분포
  - 일자별 입출고 히스토리

---

## 🛠 기술 스택
- **Backend**: Java, Spring Boot
- **Database**: MySQL
- **ORM/Mapper**: JPA
- **Build Tool**: Gradle
- **View**: Thymeleaf (간단한 페이지 렌더링)
- **IDE**: IntelliJ IDEA
- **Version Control**: Git, GitHub

---

## 📂 프로젝트 구조
```text
src/main/java/com/example/minierp
├── controller # API 엔드포인트
├── service # 비즈니스 로직
├── repository # DB 접근 (Spring Data JPA)
└── domain # Entity & DTO

src/main/resources
├── templates # Thymeleaf 템플릿
├── static # 정적 자원(css, js)
└── application.yml # 환경 설정
```
