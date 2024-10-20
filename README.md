# 🍔 햄버거 가게 키오스크 시스템

<br>

## 프로젝트 소개

- 본 프로젝트는 Java 프로그래밍 언어와 Oracle 데이터베이스를 활용한 키오스크 시스템 개발을 목표로 하였습니다.
- 이 시스템은 고객이 손쉽게 정보를 조회하고, 주문을 진행할 수 있는 통합 솔루션을 제공합니다.
- 또한 관리자 화면에서는 고객의 주문내역 및 매출, 메뉴 관리, 쿠폰 관리 등의 기능을 이용할 수 있도록 제작하였습니다.

<br>

## 1. 개발 환경

- 개발 인원 : 1명
- 개발 기간 : 2024-3-6 ~ 2024-3-13
- 프로그래밍 언어: Java
- GUI 프레임워크: Swing
- 데이터베이스: Oracle

<br>

## 2. 중요 프로젝트 구조

```
├── README.md
└── Project
     ├── bin
     ├── images
     ├── images_png
     ├── .classpath
     ├── .settings
     ├── .project
     └── src
          ├── Admin.java
          ├── CouponBean.java
          ├── CouponDao.java
          ├── FilterDao.java
          ├── Main.java
          ├── Menu.java
          ├── MenuBean.java
          ├── MenuDao.java
          ├── OrderDao.java
          ├── OrderMenuBean.java
          ├── ProductsBean.java
          ├── SortDao.java
          ├── Surve.java
          └── memo.txt
```

<br>

## 3. 기능

### 🌱 사용자 화면

#### [메뉴확인]
- 처음에 매장 식사인지, 포장해서 가져가는 것인지 선택해야합니다.
- 메뉴판에서 매장 내의 모든 메뉴와 가격 정보를 확인할 수 있습니다.
- 메뉴판은 여섯가지 카테고리별로 보기 쉽게 구성되어 있습니다.
<img src="https://github.com/user-attachments/assets/e2cbd836-5fd4-4a76-9bf2-96e1ac17a725">

#### [메뉴선택]
- 메뉴 이미지를 선택하여 장바구니에 담을 수 있으며, 선택시마다 수량이 올라갑니다.
- 수량조절은 우측 하단의 +/- 버튼을 통해서도 가능합니다.
- 전체취소 버튼 클릭시 장바구니가 모두 비워집니다.
- 장바구니에 담긴 메뉴의 총금액은 하단에 표시됩니다.
<img src="https://github.com/user-attachments/assets/a6ca889e-c832-47d8-a18c-fed5656f0aed">

#### [쿠폰사용]
- 쿠폰사용 버튼을 눌러 발급받은 쿠폰 코드 입력을 통해 쿠폰을 사용할 수 있습니다.
- 올바르지 않은 쿠폰 코드는 사용할 수 없으며, 성공적으로 사용되었을 경우 장바구니에 추가됩니다.
<img src="https://github.com/user-attachments/assets/85e3c257-c982-4eff-a970-e1aa2bd76fc7">

#### [주문하기]
- 메뉴를 담은 뒤 주문하기 버튼을 통해 결제를 진행할 수 있습니다.
- 주문시 해당 주문내역의 주문번호가 안내됩니다.
<img src="https://github.com/user-attachments/assets/e074aa05-41f3-4a06-b27a-1db245b5d560">

### 🌱 관리자 화면면

#### [관리자 화면 접속]
- 관리자는 첫 화면의 상단 부분을 클릭하여 관리자 화면에 접속할 수 있습니다.
- 설정되어있는 관리자 비밀번호를 올바르게 입력하면 접속에 성공할 수 있습니다.
  
#### [주문관리(조회/삭제/필터/정렬)]
- 고객의 주문 내역을 조회할 수 있습니다. 주문 내역에는 주문 번호, 상품, 수량, 금액, 날짜, 방식(포장/매장) 등이 포함됩니다. (조회)
- 오늘/이번주/이번달 날짜에 따른 주문 내역 조회가 가능합니다. (필터)
- 주문 번호, 상품, 날짜에 따라 정렬된 주문 내역 조회가 가능합니다. (정렬)
- 원하는 조회 방식에 따른 총 매출이 하단에 표시됩니다.
- 삭제 버튼을 통해 원하는 주문 내역 정보를 삭제할 수 있습니다. (삭제)
<img src="https://github.com/user-attachments/assets/78286508-2bb3-46eb-a0af-16d0ed3b14a5">

#### [메뉴관리(조회/추가/수정/삭제/필터/정렬)]
- 매장 내의 모든 메뉴를 조회할 수 있습니다. 메뉴 정보에는 상품 코드, 카테고리 분류, 상품명, 가격 등이 포함됩니다. (조회)
- 메뉴판의 여섯가지 카테고리별로 메뉴 조회가 가능합니다. (필터)
- 상품코드, 카테고리 분류, 상품명, 가격에 따라 정렬된 주문 내역 조회가 가능합니다. (정렬)
- 추가 버튼을 통해 원하는 메뉴 정보를 추가할 수 있습니다. 이때 상품분류, 상품명, 상품가격, 이미지를 반드시 선택해야하며, 상품 코드는 상품 분류에 따라 자동으로 입력됩니다. (추가)
- 변경 버튼을 통해 원하는 메뉴 정보를 수정할 수 있습니다. (수정)
- 삭제 버튼을 통해 원하는 메뉴 정보를 삭제할 수 있습니다. (삭제)
- 메뉴 정보 추가/변경/삭제시 결과는 사용자 화면에서 바로 확인 가능합니다.
<img src="https://github.com/user-attachments/assets/aa0f17b4-7ba2-410a-bb84-17be7c0a511f">
<img src="https://github.com/user-attachments/assets/bcf6d5ec-cb9b-4426-ae1b-cbd3ff4fa767">
<img src="https://github.com/user-attachments/assets/0bc87698-4739-4b0f-80fe-3bc5c736403e">

#### [쿠폰관리(조회/추가/수정/삭제/필터/정렬)]
- 주문시 사용가능한 쿠폰 관련 정보를 조회할 수 있습니다. 쿠폰 정보에는 쿠폰코드, 상품명, 유효기간이 포함됩니다. (조회)
- 기간에 따라 사용가능/불가능으로 나누어 조회가 가능합니다. (필터)
- 쿠폰코드, 상품명, 유효기간에 따라 정렬된 쿠폰 조회가 가능합니다. (정렬)
- 추가 버튼을 통해 원하는 쿠폰 정보를 추가할 수 있습니다. 이때 쿠폰코드, 상품명, 유효기간을 반드시 선택해야합니다. (추가)
- 변경 버튼을 통해 원하는 쿠폰 정보를 수정할 수 있습니다. (수정)
- 삭제 버튼을 통해 원하는 쿠폰 정보를 삭제할 수 있습니다. (삭제)
<img src="https://github.com/user-attachments/assets/7260bb6d-df14-45a4-a8ac-036475c4b20d">
<img src="https://github.com/user-attachments/assets/70dceb4f-2be0-4f19-81ec-06c3761d3330">

<br>

## 4. 개선할 부분

- 결제 작업을 못했기 때문에 관련 API를 활용하여 돈이 입출금되는 과정을 구현해야 할 것입니다.
- 주문 내역에 따라 사용되는 자재 및 재료의 재고를 계산하여 물품 관리를 함께 진행할 수 있도록 관리자 기능을 추가하고 싶습니다. 

<br>

## 5. 프로젝트 후기

자바를 활용해 개발하는 첫 프로젝트를 완성도있게 작업하고 싶었기 때문에, 아르바이트를 반년 이상 다니며 비교적 익숙해져있던 햄버거 가게 키오스크를 구현해보았습니다. 그동안 배워왔던 문법과 기술들을 실제로 활용해볼 수 있었던 좋은 기회였습니다. 

중간중간 DB를 수정하거나 계획을 변경하는 과정에서 코드 작성만큼이나 중요한 것이 사전 계획이라는 것을 깨달았습니다.

스윙의 존재를 처음 알았고, 자료도 많지 않았기 때문에 프로젝트 진행 속도가 더뎌지기도 했지만 이러한 어려움을 극복하면서 원하는 결과물을 낼 수 있던 이번 경험은 저에게 좋은 성장제가 된 것 같습니다.
