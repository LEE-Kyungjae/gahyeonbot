# 가현봇 (GahyeonBot)
```
연인과 취침전에 보이스채널을 나가는 번거로움을 겪어보신적 있으신가요?
다른 보이스채널 유저에게 공지사항이있어 모두 보이스채널로 집결시키고싶진 않으신가요?
가현봇이 해결해줍니다!
```

가현봇은 디스코드 서버에서 예약 관리 및 일정 조율을 돕는 봇입니다. 간단한 명령어 입력으로 예약 생성, 취소, 조회 기능을 제공합니다.

## 주요 기능
- **나가기 예약 생성:** 특정 날짜와 시간에 나가기 예약 생성
- **같이나가기 예약 생성:** 여러 사람이 함께 나가는 예약 생성
- **예약 취소:** 기존 예약 취소
- **예약 목록 조회:** 모든 예약된 목록 확인
- **뮤직봇 기능:** 음악 재생, 일시 정지, 스킵 등 기본 음악 관리 기능 제공

---

## 사용 방법
### 1. 봇 초대
가현봇을 디스코드 서버에 초대하세요.
[가현봇 초대 링크](https://discord.com/oauth2/authorize?client_id=1220338955082399845)
[가현봇 위키][https://github.com/LEE-Kyungjae/gahyeonbot/wiki/%EA%B0%80%ED%98%84%EB%B4%87-%EA%B5%AC%EC%84%B1-%EC%84%A4%EB%AA%85](https://github.com/LEE-Kyungjae/gahyeonbot/wiki)
### 2. 명령어 사용
디스코드 채팅 창에 다음과 같은 명령어를 입력하세요:

- **나가기 예약 생성:**
  ```
  Out [날짜] [시간] [설명(선택)]
  ```
  예: `Out 2024-12-25 18:00 크리스마스 파티`

- **같이나가기 예약 생성:**
  ```
  WithOut [날짜] [시간] [설명(선택)]
  ```
  예: `WithOut 2024-12-31 23:30 연말 카운트다운`

- **예약 취소:**
  ```
  CancelOut [예약 번호]
  ```
  예: `CancelOut 1`

- **예약 목록 조회:**
  ```
  SearchOut
  ```
  예: `SearchOut`

---

## 설치 및 실행
가현봇은 JAVA 17, JDA 기반으로 제작되었습니다.

### 1. 프로젝트 클론
```bash
 git clone https://github.com/LEE-Kyungjae/gahyeonbot.git
```

### 2. 의존성 설치
```bash
 cd gahyeonbot
 ./gradlew build
```

### 3. 봇 실행
```bash
 java -jar build/libs/gahyeonbot.jar
```

---

## 기여하기
1. 이슈 등록
2. Fork 후 새 브랜치 생성
3. 수정 후 Pull Request 제출

---

## 라이선스
이 프로젝트는 [MIT 라이선스](LICENSE)를 따릅니다.<br>
해당 조건하에 누구나 가현봇을 확장하여 자신이 원하는 봇을 만드시는게 가능합니다.

---

## 문의
- 개발자: [LEE-Kyungjae](https://github.com/LEE-Kyungjae)
- 이메일: ze2@kakao.com
