# Picke (픽케) Android

논쟁적인 주제를 두고 서로 다른 관점을 듣고, 투표하고, 의견을 나누는 소셜 디스커션 앱입니다.
철학자의 관점으로 구성된 오디오 시나리오를 듣고 사전·최종 투표로 내 생각의 변화를 확인한 뒤 댓글로 의견을 나눕니다.

- 패키지명: `com.picke.app`
- 플랫폼: Android (minSdk 26 / targetSdk 36)
- [Google Play에서 보기](https://play.google.com/store/apps/details?id=com.picke.app&hl=ko)

## 주요 기능

| 기능 | 설명 |
|---|---|
| 배틀 | 사전 투표 → 오디오 시나리오 청취 → 최종 투표 → 관점(댓글) 순서로 한 주제에 참여 |
| 빠른 배틀 | 오늘 선정된 배틀에 바로 입장 |
| 홈·탐색 | 오늘의 픽·베스트·신규 배틀 큐레이션, 카테고리·정렬별 배틀 검색 |
| 클래스 (개발 중) | 모임 주최자가 클래스를 열고 참가자는 코드로 입장. 기존 콘텐츠 배틀 또는 AI 철학자와의 3라운드 토론에 참여하고, 주최자는 클래스 요약, 참가자는 개인 AI 리포트를 확인 |
| 마이 | 나의 철학자 유형 진단·공유, 포인트, 배틀 기록, 출석체크 |
| 기타 | 카카오·구글 로그인, 카카오톡·인스타그램 공유, FCM 알림과 딥링크, 배틀 주제 제안 |

## 아키텍처

### 모듈 구조

```
:app ──────────┬──▶ :presentation ──▶ :domain
               └──▶ :data ──────────▶ :domain
```

| 모듈 | 역할 |
|---|---|
| `:domain` | 순수 Kotlin(JVM) 모듈. 도메인 모델(`XxxBoard`), Repository 인터페이스, UseCase |
| `:data` | Retrofit API, DTO, Repository 구현체, 로컬 저장소, 네트워크 설정 |
| `:presentation` | Compose 화면, ViewModel, 내비게이션, 분석·광고·알림 |
| `:app` | Application(Hilt 진입점)과 UseCase DI 등록, 모듈 조립 |

**이렇게 나눈 이유**
- `domain`은 Android·Hilt에 의존하지 않아 비즈니스 규칙을 프레임워크와 분리하고 JVM 단위 테스트가 가능한 상태로 둡니다.
- `presentation`과 `data`는 서로를 모르고 `domain`의 인터페이스로만 연결됩니다. 화면 작업이 API 구현에, API 변경이 화면에 직접 번지지 않습니다.
- `domain`이 `javax.inject`에도 의존하지 않게 UseCase 생성은 `:app`의 DI 모듈에서 합니다.

### 레이어 간 데이터 흐름

```
Retrofit API ─▶ XxxDto ─toDomainModel()─▶ XxxBoard ─toUiModel()─▶ XxxUiModel ─▶ Compose
               (:data)                  (:domain)                (:presentation)
```

- 레이어마다 전용 모델을 두고 매퍼 확장 함수로 변환합니다. 서버 응답 형태가 바뀌어도 DTO와 매퍼만 고치면 됩니다.
- ViewModel은 Repository를 직접 부르지 않고 UseCase를 거칩니다.
- API 호출은 공통 `apiCall { }`로 감싸 취소 처리와 에러 보고(Sentry)를 한곳에서 처리하고, 결과는 `Result`로 돌려줍니다.

### 화면 구조

각 기능은 `presentation/ui/<feature>/` 아래에 같은 패턴으로 둡니다.

```
ui/<feature>/
├── XxxScreen.kt       # Screen(ViewModel 연결) → Content(상태만 받는 UI) → Section
├── XxxViewModel.kt
├── XxxReducer.kt      # MVI 화면의 상태 변경 함수
├── component/         # 화면 안에서 쓰는 UI 조각 (모두 Preview 포함)
└── model/             # UiState, UiModel, UiAction, UiEvent
```

- **상태 관리**: 기존 화면은 MVVM(`StateFlow` 상태 + 일회성 이벤트), 클래스 화면부터는 MVI를 씁니다.
  사용자 동작을 `UiAction`으로 받아 순수 함수 `reduce`로 상태를 바꾸고, 화면 이동·토스트 같은 부수 효과는 따로 처리합니다.
  상태 변경이 한곳에 모여 화면 동작을 추적하고 확인하기 쉬워집니다.
- **UI 분리**: Screen은 ViewModel과 이벤트만 다루고, 실제 UI는 상태와 콜백만 받는 Content가 그립니다.
  덕분에 모든 화면을 Preview로 확인할 수 있습니다.
- **디자인 시스템**: 색·타이포그래피·간격은 Figma 변수와 같은 이름의 토큰(`PickeTheme.colors`, `PickeTheme.typography`,
  `SpacingTokens`)으로만 쓰고, 화면 문구는 모두 `strings.xml`에 둡니다.

### 분석·모니터링

**Mixpanel (사용자 행동 분석)**
- 이벤트·속성·화면 이름을 `AnalyticsSpec.kt` 한 파일에 모아 iOS와 같은 이름을 쓰는 크로스플랫폼 명세로 관리합니다.
- 전송은 `AnalyticsTracker`의 타입이 정해진 메서드로만 하고, ViewModel·Screen의 부수 효과에서 부릅니다.
  상태만 받는 Content와 Preview는 트래킹에 의존하지 않습니다.
- 화면 조회(`screen_view`)는 내비게이션 백스택을 구독해 route를 화면 이름으로 바꿔 자동으로 보냅니다.
  새 화면은 route 매핑만 추가하면 됩니다.
- 공유·투표처럼 결과가 있는 행동은 성공했을 때만 보내고, 사용자 식별은 개인정보가 아닌 `user_tag`만 씁니다.
- debug 빌드에서는 전송하지 않습니다.

**Sentry (오류 모니터링)**
- API 호출의 예외는 공통 `apiCall { }`에서 걸러 앱 버그로 볼 수 있는 것만 보고합니다.
  네트워크 끊김, HTTP 오류, 서버가 돌려준 에러 응답, 포인트 부족 같은 도메인 예외는 정상적인 실패로 보고 제외합니다.
- 서버 5xx 응답은 OkHttp 자동 계측에 맡기고 직접 보고하지 않습니다.
- 화면 쪽에서는 비트맵·파일 처리, 외부 SDK 호출처럼 앱 버그일 수 있는 실패만 보고하고, 사용자 취소나 대체값으로 넘어가는 흐름은 보고하지 않습니다.

## 기술 스택

| 영역 | 사용 기술 |
|---|---|
| 언어·빌드 | Kotlin 2.0, AGP 9, Gradle 9, 버전 카탈로그(`gradle/libs.versions.toml`) |
| UI | Jetpack Compose, Material3, Navigation Compose, Coil |
| 아키텍처 | Hilt, Coroutines·Flow, Paging3 |
| 네트워크 | Retrofit, OkHttp, Gson |
| 로컬 저장 | EncryptedSharedPreferences (로컬 DB 없음) |
| 미디어 | Media3 ExoPlayer (시나리오 오디오) |
| 인증·공유 | Kakao SDK, Google 로그인, Kakao 공유 |
| Firebase | FCM, Realtime Database, Dynamic Links |
| 분석·모니터링 | Mixpanel, Sentry |
| 광고 | Kakao AdFit |

## 시작하기

1. JDK 17과 최신 Android Studio를 준비합니다.
2. 저장소에 포함되지 않은 설정을 받아 둡니다. 값은 팀에 문의하세요.
   - 프로젝트 루트의 로컬 설정 파일: 서버 주소, SDK 키, 웹 URL 등 (필요한 키는 각 모듈 `build.gradle.kts`의 `getProperty(...)`)
   - `app/google-services.json`
3. 빌드합니다.
   ```bash
   ./gradlew assembleDebug
   ```
   - 서버 주소 키가 없으면 앱이 실행 직후 종료되고, 그 밖의 키가 없으면 해당 기능(로그인·공유·광고 등)만 동작하지 않습니다.

## 개발 규칙

- 코드 컨벤션, 화면 구조, 커밋 메시지 형식은 [`CLAUDE.md`](CLAUDE.md)에 정리돼 있습니다.
- 브랜치: `develop` 기준, 작업 브랜치는 `<type>/<이슈번호>-<설명>` (예: `feat/100-class-ui`)
- 커밋: `<type>: <요약>` (`feat` / `fix` / `refactor` / `style` / `rename` / `chore`)
- CI는 없고, 빌드 확인은 로컬에서 합니다.