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

### 🌱 사용자

#### []
- 이메일 주소와 비밀번호를 입력하면 입력창에서 바로 유효성 검사가 진행되고 통과하지 못한 경우 각 경고 문구가 입력창 하단에 표시됩니다.
- 이메일 주소의 형식이 유효하지 않거나 비밀번호가 6자 미만일 경우에는 각 입력창 하단에 경구 문구가 나타납니다.
<img src="https://github.com/user-attachments/assets/e2cbd836-5fd4-4a76-9bf2-96e1ac17a725">
<img src="https://github.com/user-attachments/assets/a6ca889e-c832-47d8-a18c-fed5656f0aed">
<img src="https://github.com/user-attachments/assets/e074aa05-41f3-4a06-b27a-1db245b5d560">
<img src="https://github.com/user-attachments/assets/78286508-2bb3-46eb-a0af-16d0ed3b14a5">
<img src="https://github.com/user-attachments/assets/aa0f17b4-7ba2-410a-bb84-17be7c0a511f">
<img src="https://github.com/user-attachments/assets/bcf6d5ec-cb9b-4426-ae1b-cbd3ff4fa767">
<img src="https://github.com/user-attachments/assets/7260bb6d-df14-45a4-a8ac-036475c4b20d">
<img src="https://github.com/user-attachments/assets/70dceb4f-2be0-4f19-81ec-06c3761d3330">
<img src="https://github.com/user-attachments/assets/0bc87698-4739-4b0f-80fe-3bc5c736403e">
<img src="https://github.com/user-attachments/assets/85e3c257-c982-4eff-a970-e1aa2bd76fc7">

#### [기능입니다]
- 이메일 주소와 비밀번호를 입력하면 입력창에서 바로 유효성 검사가 진행되고 통과하지 못한 경우 각 경고 문구가 입력창 하단에 표시됩니다.
- 이메일 주소의 형식이 유효하지 않거나 비밀번호가 6자 미만일 경우에는 각 입력창 하단에 경구 문구가 나타납니다.

### 🌱 관리자

#### [기능입니다]
- 이메일 주소와 비밀번호를 입력하면 입력창에서 바로 유효성 검사가 진행되고 통과하지 못한 경우 각 경고 문구가 입력창 하단에 표시됩니다.
- 이메일 주소의 형식이 유효하지 않거나 비밀번호가 6자 미만일 경우에는 각 입력창 하단에 경구 문구가 나타납니다.
  
#### [기능입니다]
- 이메일 주소와 비밀번호를 입력하면 입력창에서 바로 유효성 검사가 진행되고 통과하지 못한 경우 각 경고 문구가 입력창 하단에 표시됩니다.
- 이메일 주소의 형식이 유효하지 않거나 비밀번호가 6자 미만일 경우에는 각 입력창 하단에 경구 문구가 나타납니다.

<br>

## 4. 개선할 부분

- 결제 작업을 못했기 때문에 관련 API를 활용하여 돈이 입출금되는 과정을 구현해야 할 것입니다.
- 주문 내역에 따라 사용되는 자재 및 재료의 재고를 계산하여 물품 관리를 함께 진행할 수 있도록 관리자 기능을 추가하고 싶습니다. 

<br>

## 5. 프로젝트 후기

자바를 활용해 개발하는 첫 프로젝트를 완성도있게 작업하고 싶었기 때문에, 아르바이트를 반년 이상 다니며 비교적 익숙해져있던 햄버거 가게 키오스크를 구현해보았습니다. 그동안 배워왔던 문법과 기술들을 실제로 활용해볼 수 있었던 좋은 기회였습니다. 

중간중간 DB를 수정하거나 계획을 변경하는 과정에서 코드 작성만큼이나 중요한 것이 사전 계획이라는 것을 깨달았습니다.

스윙의 존재를 처음 알았고, 자료도 많지 않았기 때문에 프로젝트 진행 속도가 더뎌지기도 했지만 이러한 어려움을 극복하면서 원하는 결과물을 낼 수 있던 이번 경험은 저에게 좋은 성장제가 된 것 같습니다.
