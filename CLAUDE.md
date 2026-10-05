# CLAUDE.md

Picke Android 프로젝트에서 Claude Code가 항상 따르는 규칙입니다.
새 코드는 이 컨벤션을 따르고 기존 코드는 손대는 김에 점진적으로 맞춥니다. 관련 없는 대규모 리팩터링은 임의로 하지 않습니다.

## 프로젝트 개요

Picke(픽케)는 논쟁적인 주제에 대한 관점을 나누고 배틀·투표·댓글로 소통하는 소셜 디스커션 앱.
패키지명 `com.picke.app`, Gradle 루트 프로젝트명 `PickeApp`.

## 기술 스택

- Kotlin 2.0.21 (JVM 17, `domain`만 JVM 11), AGP 9.0.1, compile/target SDK 36, min SDK 26. 버전은 `gradle/libs.versions.toml`에서 관리
- Jetpack Compose (Material3, XML 뷰 미사용), Navigation Compose
- Hilt (DI), Retrofit + Gson converter + OkHttp, Paging3, Coil, Media3 (시나리오 오디오 재생)
- Kakao/Google 소셜 로그인, Kakao 공유
- Firebase (Messaging, Realtime Database, Dynamic Links), Sentry, Mixpanel
- 광고: AdMob(리워드), Kakao AdFit(배너/네이티브/전면)
- 로컬 저장은 EncryptedSharedPreferences (Room/DataStore 미사용)

## 아키텍처: 4개 모듈

```
settings.gradle.kts → include(":app", ":presentation", ":data", ":domain")
```

| 모듈 | 역할 | 의존 |
|---|---|---|
| `:domain` | 순수 Kotlin(JVM) 라이브러리. Board 모델, Repository 인터페이스, UseCase | coroutines-core, paging-common만 |
| `:data` | Retrofit API, DTO, RepositoryImpl, 네트워크/로컬 저장소, Hilt 모듈 | `:domain` |
| `:presentation` | Compose 화면, ViewModel, `MainActivity`, FCM, 광고, 분석 | `:domain` |
| `:app` | `PickeApplication`(Hilt 진입점), **UseCase DI 모듈**, 전체 모듈 조립 | 전부 |

```
domain/src/main/java/com/picke/domain/
├── common/
│   ├── exception/     # NotEnoughPointsException 등 도메인 예외
│   └── local/          # LocalPreferencesRepository, LocalPreferencesUseCases
└── feature/<feature>/
    ├── model/          # XxxBoard  (도메인 모델)
    ├── repository/      # XxxRepository 인터페이스
    └── usecase/          # XxxUseCase (단일 동작) + XxxUseCases (묶음, data class)

data/src/main/java/com/picke/data/
├── common/
│   ├── local/          # PreferencesManager (EncryptedSharedPreferences), LocalPreferencesRepositoryImpl
│   ├── model/          # BaseResponse<T>, ErrorResponse, toResult()
│   └── network/        # AuthInterceptor, TokenAuthenticator
├── di/                 # ApiModule, NetworkModule, RepositoryModule(@Binds)
└── feature/<feature>/
    ├── datasource/       # XxxApi (Retrofit)
    ├── model/             # XxxDto + toDomainModel() 매퍼
    └── repository/         # XxxRepositoryImpl

presentation/src/main/java/com/picke/presentation/
├── MainActivity.kt
├── ads/  analytics/  notification/  util/
└── ui/
    ├── component/  theme/  main/     # 공통 컴포넌트, 디자인 토큰, 바텀 내비게이션
    └── <feature>/
        ├── XxxScreen.kt          # XxxScreen(stateful) + XxxContent(stateless) + private XxxSection
        ├── XxxViewModel.kt
        ├── component/          # Section 안에서 쓰는 화면 전용 컴포넌트, XxxSkeleton
        └── model/                # XxxUiState, XxxUiEvent, XxxUiModel

app/src/main/java/com/picke/app/
├── PickeApplication.kt
└── di/UseCaseModule.kt   # 모든 UseCase / XxxUseCases 를 @Provides로 생성
```

### feature 패키지 이름 대응 (현재 상태)

domain/data는 이름이 같지만 presentation은 일부 다릅니다. 기존 이름은 바꾸지 않습니다.

| domain / data | presentation `ui/` |
|---|---|
| `mypage` | `my` |
| `auth` | `login` |
| `battle` | `battleentry` |
| `pollquiz`, `share`, `device`, `proposal` | 전용 화면 패키지 없음 (다른 화면에서 사용) |
| 나머지 (`home`, `vote`, `comment`, `perspective` 등) | 같은 이름 |

### 모델 3단 변환

레이어를 넘어갈 때는 전용 모델 + 매퍼 확장 함수로 변환합니다. 레이어 간 모델을 직접 재사용하지 않습니다.

| 레이어 | 접미사 | 예시 |
|---|---|---|
| data (DTO) | `Dto` | `HomeResponseDto` |
| domain | `Board` | `HomeBoard` |
| presentation | `UiModel` / `UiState` | `HomeContentUiModel`, `HomeUiState` |

매퍼는 `fun XxxDto.toDomainModel(): XxxBoard`, `fun XxxBoard.toUiModel(): XxxUiModel` 형태.

### Domain: Repository + UseCase

Repository 인터페이스는 `domain`에, 구현체는 `data`에 두고 `data/di/RepositoryModule`에서 `@Binds`로
연결합니다. ViewModel은 Repository를 직접 호출하지 않고 반드시 **UseCase**를 거칩니다. 같은 feature의
UseCase들은 `XxxUseCases` data class로 묶어서 Hilt로 한 번에 주입받습니다.

`domain`은 `javax.inject`에도 의존하지 않으므로 UseCase에 `@Inject`를 붙이지 않습니다. 대신
**`app/di/UseCaseModule.kt`에 `@Provides`로 등록**합니다. 새 UseCase를 만들면 이 모듈 등록까지 해야
주입됩니다.

```kotlin
// domain/feature/home/usecase/FetchHomeDataUseCase.kt
class FetchHomeDataUseCase(private val homeRepository: HomeRepository) {
    suspend operator fun invoke(): Result<HomeBoard> = homeRepository.fetchHomeData()
}

// domain/feature/home/usecase/HomeUseCases.kt
data class HomeUseCases(val fetchHomeDataUseCase: FetchHomeDataUseCase)
```

### Presentation: ViewModel

공통 BaseViewModel은 두지 않습니다. 새로 작성하는 ViewModel도 기존처럼 ViewModel마다
`MutableStateFlow` + `StateFlow`를 직접 선언합니다. BaseViewModel 같은 공통 부모 클래스를 임의로
만들거나 도입을 제안하지 않습니다.

```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
}
```

- 상태 프로퍼티 이름은 `uiState`로 통일 (현재 대부분의 ViewModel이 이 이름 사용)
- 상태 갱신은 `_uiState.update { it.copy(...) }` 사용 (`value =` 직접 대입보다 우선, 현재 다수 방식)
- `XxxUiState`: 지속 화면 상태 (data class, 기본값 포함)
  - 로딩은 `isLoading: Boolean` 같은 필드로 표현합니다. UiState를 sealed(Loading/Success/Error)로 만들지 않습니다.
  - 에러 메시지는 UiState에 두지 않고 `XxxUiEvent`로 전달합니다.
  - 리스트 필드 기본값은 `emptyList()`.
- `XxxUiEvent`: 1회성 사이드이펙트(토스트, 네비게이션 등). `sealed class`로 정의합니다.
  새 이벤트 클래스 이름은 `XxxUiEvent`로 맞춥니다 (기존 `MakeBattleEvent`, `DeepLinkEvent`는 그대로 둠)
  - 화면 하나에서 쓰는 이벤트: `private val _uiEvent = Channel<XxxUiEvent>(Channel.BUFFERED)` +
    `val uiEvent: Flow<XxxUiEvent> = _uiEvent.receiveAsFlow()` (수집자가 없을 때 이벤트 유실 방지)
  - 로그아웃처럼 여러 곳에 동시에 알려야 하는 앱 전역 이벤트만 `MutableSharedFlow` + `SharedFlow`
  - 기존 `MutableSharedFlow` UiEvent는 그대로 두고, 손대는 김에 바꿉니다.
- ViewModel 공개 함수는 화면에서 직접 호출 (`onAction()` 단일 디스패처로 억지로 합치지 않음 — 지금 방식 유지)
- 함수 이름: 데이터 로드는 `fetchXxx()`, 사용자 액션은 동사형(`toggleLike()`, `selectOption()`, `submitComment()`).
  `onXxxClick` 형태는 Composable 콜백 파라미터 이름에만 씁니다.
- `Result` 실패 처리 람다 파라미터 이름은 `exception`으로 씁니다 (`.onFailure { exception -> }`).
- API 실패 시 사용자에게 어떻게 알릴지(Toast / 화면 에러 상태 + 재시도 등)는 화면 성격에 맞게 고릅니다.
- `fun XxxBoard.toUiModel()` 매퍼는 `model/XxxUiModel.kt` 안에 둡니다 (ViewModel 안에 두지 않음).

### Presentation: Compose 화면 구조

화면은 **Screen → Content → Section → Component** 4단계로 나눕니다.

| 단계 | 이름 | 역할 | 위치 |
|---|---|---|---|
| Screen | `XxxScreen(viewModel = hiltViewModel(), 네비게이션 콜백)` | ViewModel 연결, `uiState` 수집, `uiEvent` 수집과 사이드이펙트(토스트·네비게이션·트래킹) 처리. UI는 그리지 않고 Content만 호출 | `XxxScreen.kt` |
| Content | `XxxContent(uiState, 콜백...)` | 전체 화면 UI. 상태와 콜백만 받는 stateless | `XxxScreen.kt` |
| Section | `private fun XxxSection(...)` | 화면을 구역(헤더, 목록, 하단 버튼 등)별로 나눈 단위 | `XxxScreen.kt` 안 |
| Component | `XxxCard`, `XxxItem` 등 | Section 안의 재사용 가능한 UI 조각 | `component/XxxCard.kt` (여러 화면 공용이면 `ui/component/`) |
| Skeleton | `XxxSkeleton` | 로딩 중 화면 | `component/XxxSkeleton.kt` |

- 모든 Screen은 Screen/Content를 분리합니다. Preview는 Content부터 아래 단계에 둡니다.
- `uiState`는 `collectAsStateWithLifecycle()`로 수집합니다.
- 모든 Composable은 `modifier: Modifier = Modifier`를 첫 번째 선택 파라미터로 받습니다.
- 네비게이션은 기존 문자열 route + `navArgument` 방식(`AppRoute`)을 유지합니다.

## 코드 규칙 (필수)

- **`GlobalScope` 금지.** 항상 `viewModelScope` 사용.
- **`CancellationException` 삼키지 않기.** `catch (e: Exception)`만 있으면 코루틴 취소까지 `Result.failure`로
  바뀝니다. `catch (e: Exception)` 앞에 항상 `catch (e: CancellationException) { throw e }`를 둡니다.
- API 응답은 statusCode를 직접 분기하지 말고 `data/common/model/BaseDto.kt`의
  `BaseResponse<T>.toResult(fallbackMessage)`로 변환합니다. 기준 예시는 `PollQuizRepositoryImpl`,
  `PerspectiveRepositoryImpl`:

  ```kotlin
  override suspend fun getMyPollVote(battleId: Long): Result<PollQuizVoteBoard> = try {
      pollQuizApi.getMyPollVote(battleId)
          .toResult("내 투표 내역을 불러오지 못했습니다.")
          .map { it.toDomainModel() }
  } catch (e: CancellationException) {
      throw e
  } catch (e: Exception) {
      Result.failure(e)
  }
  ```

  `HomeRepositoryImpl`(`data ?: throw`), `VoteRepositoryImpl`(`when (statusCode)` 분기)처럼 짜지 않습니다.
  특정 에러 코드별 처리가 필요하면 `domain/common/exception`에 예외 타입을 정의해서 씁니다
  (`NotEnoughPointsException` 참고, 문자열 `contains("400")` 비교 금지).
- UI 상태는 항상 `StateFlow`로 캡슐화해서 노출 (`private val _x` + `val x: StateFlow`).
- 로깅: Timber는 아직 도입되지 않았고, 현재는 `android.util.Log` + 클래스별 `TAG` 상수를 씁니다.
  Timber로 바꾸려면 먼저 도입 여부를 물어볼 것. `println`, `printStackTrace()` 금지.
  토큰·개인정보는 로그에 남기지 않습니다.
  - `TAG`는 `private const val TAG = "XxxViewModel_Picke"`처럼 `_Picke` 접미사를 붙입니다 (logcat 필터용).
  - 로그는 장애 원인 파악에 꼭 필요한 곳(API 실패, 예외, 외부 SDK 콜백 등)에만 남깁니다. 동작 추적용 로그는 남기지 않습니다.
- API 키/토큰 등 시크릿 하드코딩 금지. `local.properties` 경유.
- `domain` 모듈은 `android.*`, `javax.inject`, Hilt에 의존하지 않음 (순수 Kotlin/JVM 유지).
  새 의존성 추가가 필요하면 먼저 물어볼 것.
- 더미 데이터는 `presentation/util/DummyData.kt` 외에 새로 만들지 않고, 실제 API 경로에 섞지 않습니다.
- `var`보다 `val` 우선.
- `let`/`run`/`apply`/`also` 중첩 남용 금지 — 가독성 우선.
- `@Immutable`/`@Stable` 어노테이션과 immutable collections 라이브러리는 쓰지 않습니다. 불필요한
  리컴포지션은 `remember`나 콜백 분리로 해결합니다.
- 여백·모서리는 새 코드부터 토큰에 있는 값이면 토큰을 씁니다 (`ui/theme/tokens/`).
  - 간격: `SpacingTokens.s16` (0·2·4·6·8·12·16·20·24·32·40·48·64·80·96)
  - 모서리: `RoundedCornerShape(RadiusTokens.default)` (2dp), 완전히 둥근 모양은 `RadiusTokens.max`
  - 토큰에 없는 값(아이콘·이미지 크기, 10dp 같은 간격 등)만 `.dp` 리터럴로 씁니다.
  - 기존 리터럴은 일괄로 바꾸지 않습니다.
- 간격은 `Spacer`와 `Arrangement.spacedBy` 중 편한 쪽을 씁니다.
- trailing comma를 쓰지 않습니다. 여러 줄 파라미터·인자·리스트의 마지막 항목 뒤에 쉼표를 붙이지 않습니다.
- 주석은 로직이 어렵거나, 중요하거나, 의도가 코드만으로 드러나지 않는 곳에만 한국어 `//`로 "왜"를 적습니다.
  KDoc을 일괄로 달지 않습니다.
- **타이포그래피는 `PickeTheme.typography`의 Figma 대응 토큰만 사용합니다.** (`ui/theme/Type.kt`)
  - 토큰은 Figma의 소문자 텍스트 스타일(`display/`, `heading/`, `body/`, `caption/`)과 1:1이며, 이름은 경로를
    camelCase로 옮긴 것입니다 (`body/sm/semibold` → `bodySmSemiBold`). 대문자 `Headings/`·`Body/`·`Caption/`,
    `Class WF/` 스타일은 쓰지 않습니다.
  - 디자인에 쓰인 스타일이 토큰에 없으면 `.copy()`로 우회하지 말고 Figma 스타일 목록을 확인해 토큰을 추가합니다.
    `.copy()`는 자간·이탤릭처럼 한 화면에만 필요한 변형에만 씁니다.
- **화면에 보이는 텍스트는 하드코딩하지 않고 `presentation/src/main/res/values/strings.xml`에 둡니다.**
  - Composable에서는 `stringResource(R.string.xxx)`로 읽습니다. 제목·버튼·안내 문구·토스트·다이얼로그·
    `contentDescription` 모두 해당합니다.
  - 리소스 이름은 `<화면/기능>_<용도>` 형식(`terms_sheet_title`, `policy_load_error`), 같은 화면 것은
    `<!-- 섹션 -->` 주석 아래에 모읍니다. 의미가 같으면 기존 리소스를 재사용합니다.
  - 예외: 로그 메시지, 분석 이벤트 이름·속성 값, Preview 함수 안의 샘플 값.
- **색상은 하드코딩하지 않고 `PickeTheme.colors`로만 사용합니다.** (`ui/theme/PickeColors.kt`)
  - `PickeColors` 항목 이름은 Figma 변수 경로를 camelCase로 옮긴 것으로, 생성 토큰(`tokens/`) 이름과 같습니다
    (`text/default` → `textDefault`, `border/beige/default` → `borderBeigeDefault`, `primary/500` → `primary500`).
    Figma에서 쓰인 변수 이름 그대로 `PickeTheme.colors.xxx`를 쓰면 됩니다.
  - 시맨틱 토큰(`textDefault`, `surfaceBeigeDefault`, `borderBeigeDefault` 등)을 먼저 쓰고, 해당하는 시맨틱 토큰이
    없을 때만 `PickeColors`의 브랜드 팔레트 항목(`gray600`, `secondary700` 등)을 씁니다.
  - `tokens/`는 `SWYP-Find/design-tokens`에서 자동 동기화되는 파일이라 직접 수정하지 않습니다. 새 토큰이 들어오면
    `PickeColors`에 같은 이름으로 연결합니다.
  - `Color(0xFF...)`, `Color.White`/`Color.Black` 같은 직접 지정,
    `BrandColorTokens`·`SemanticColorTokens` 직접 참조를 새 코드에 쓰지 않습니다. (`Color.Transparent`는 예외)
  - 필요한 색이 `PickeColors`에 없으면 Figma 변수를 확인해 토큰을 추가합니다.
  - 기존 코드의 하드코딩 색상은 손대는 김에 토큰으로 바꿉니다.
- **UI Composable을 만드는 파일에는 `@Preview`를 반드시 하나 이상 둡니다.** (Screen, 화면 전용 component,
  공통 component 모두 해당)
  - Preview 함수는 `private fun XxxPreview()`로 같은 파일 하단에 두고 `PickeTheme { }`로 감쌉니다.
  - `heightDp`/`widthDp`/`device` 등으로 화면 크기를 임의로 지정하지 않습니다.
  - `hiltViewModel()`이나 Hilt EntryPoint(`rememberAnalyticsTracker()` 등)에 의존하는 Composable은
    Preview에서 렌더링되지 않습니다. 상태를 받는 `XxxContent(uiState, 콜백...)`를 분리해서 그걸 Preview하고,
    트래킹 같은 사이드이펙트는 콜백으로 밖으로 뺍니다.
  - Preview용 샘플 값은 Preview 함수 안에 직접 적습니다 (`DummyData.kt`에 추가하지 않음).
- 주석 처리된 죽은 코드는 남기지 않고 삭제. (예: `HomeRepositoryImpl`의
  `// return Result.success(DummyHomeData...)` 같은 줄 — 발견하면 지울 것)
- null 안전 처리는 `?:` 로 명시적으로 (기존 `HomeRepositoryImpl` 스타일 유지).

### Data / Domain 세부 규칙

- DTO 필드는 전부 nullable로 선언하고, `toDomainModel()`에서 `?:`로 기본값을 채웁니다.
- `@SerializedName`은 필드명과 JSON 키가 다를 때만 붙입니다.
- ID 타입은 `Long`으로 통일하고, DTO → Board → UiModel까지 `Long`을 유지합니다. 다른 타입이 필요한 곳
  (네비게이션 route 문자열, 서버가 String으로 받는 API 등)에서만 그 경계에서 변환합니다.
  기존 `String`/`Int` ID는 일괄로 바꾸지 않습니다.
- 날짜는 domain까지 `String`으로 두고, 표시용 포맷 변환은 presentation에서 합니다.
- 타입·상태 값(`type`, `status`, `voteSide` 등)은 `String`으로 둡니다 (enum으로 매핑하지 않음).
- `toResult()` fallback 문구는 "~하지 못했습니다." 형태로 씁니다. 이 문구는 로그·디버깅용이고, 화면에
  보여 줄 문구는 presentation의 `strings.xml`에서 가져옵니다.
- Retrofit suspend 호출은 `withContext(Dispatchers.IO)`로 감싸지 않습니다. 파일 I/O, 암호화 저장소 접근처럼
  블로킹 작업만 `Dispatchers.IO`를 씁니다.

## 문서화 규칙

모든 작업은 커밋 메시지 / PR 설명을 남기고, 원인 분석이 필요했던 문제나 여러 커밋에 걸친 작업은
**노션 트러블 슈팅 문서**로 남깁니다. 저장소에는 `docs/`를 만들지 않습니다.

- 노션 위치: [Picke](https://app.notion.com/p/3a5520b13f1f8010a563c757023b0692) 페이지의 `트러블 슈팅` 콜아웃 아래
- Notion MCP로 작성합니다. 페이지에 접근할 수 없으면(404) 임의의 다른 위치에 만들지 말고, 노션 연결 권한을 먼저 확인해 달라고 요청합니다.
- 단순 스타일 수정, 오타, 사소한 버그 픽스는 커밋/PR 설명만 남기고 문서를 만들지 않습니다.
- 작업을 요청받아 끝낸 직후, 문서화 대상이면 먼저 "노션 문서로 남길까요?" 라고 확인하고 진행합니다
  (매번 자동으로 만들지 않고 확인 후 작성).

### 1. 커밋 / PR 설명

- 커밋 메시지: `<type>: <요약>` (`feat` / `fix` / `refactor` / `style` / `chore` / `rename`), 본문에 이유를 한두 줄.
  scope(`feat(ui):`)는 쓰지 않습니다.
- type 선택 기준 (**앱 동작이 바뀌는지**를 먼저 봅니다):

  | type | 기준 | 예시 |
  |---|---|---|
  | `feat` | 앱 코드에 새 동작 추가. 사용자에게 안 보여도 포함 | 새 화면·API 연동, 새 분석 이벤트, Sentry 예외 보고 |
  | `fix` | 의도·명세와 다르게 동작하던 것을 바로잡음 | 크래시·잘못된 화면 이동 수정, 명세에 있는데 빠진 이벤트 추가 |
  | `refactor` | 동작은 그대로 두고 코드 구조·구현을 개선 | 패키지 이동, 하드코딩 색상을 토큰으로 교체, 미사용 코드 삭제 |
  | `style` | 동작·구조 변화 없이 포맷·정렬·공백만 변경 | 코드 정렬, import 정리, 항목 순서 정렬 |
  | `rename` | 파일·클래스·패키지·리소스 이름만 변경 (내용 수정 없음) | 클래스명 변경, 오타 난 폴더명 수정 |
  | `chore` | 빌드·설정·의존성·문서 등 앱 동작이 바뀌지 않는 변경 | Gradle 설정, 버전 업데이트, 서명 설정, CLAUDE.md 수정 |

  - 여러 type에 걸치면 커밋의 **주된 목적**으로 고릅니다. 목적이 둘이면 커밋을 나눕니다.
- 브랜치 이름: GitHub 이슈가 있으면 `<type>/<이슈번호>-<설명>` (`feat/96-audio`), 이슈 없이 로컬에서 만든
  브랜치는 `<type>/<설명>` (`chore/sentry-check`).
- PR 설명은 **작업 내용 → 주요 변경 사항 → 체크리스트** 순서로 작성.

### 2. 문서 단위

**영향받는 영역 하나 × 작업 흐름 하나 = 문서 하나**로 나눕니다.

| 판단 기준 | 처리 |
|---|---|
| 같은 화면·클래스를 한 흐름에서 고쳤다 (기능 추가, 그 과정의 버그 수정, 이어진 리팩토링) | 한 문서에 이슈별 섹션으로 모음 |
| 작업 중 **다른 영역**의 문제를 발견해 고쳤다 | 별도 문서 |
| 커밋 메시지 한 줄로 충분한 수정 (스타일, 누락된 import 등) | 관련 문서의 커밋 표에만 기재 |

- 모든 문서는 `트러블 슈팅` 콜아웃 바로 아래에 둡니다. **문서 안에 하위 페이지를 만들지 않습니다.**
- 관련된 다른 문서는 `관련` 항목에서 멘션으로 연결합니다.
- 문서는 관련 코드를 커밋한 뒤에 작성하고, 커밋 해시를 남깁니다.

### 3. 문서 형식

```markdown
# <영역> <리팩토링 | 오류 | 기능>   (예: 시나리오 오디오 리팩토링, 스플래시 네비게이션 오류)

- 날짜: YYYY-MM-DD
- 관련: PR/이슈, 브랜치, 관련 문서 멘션

## 개요
배경(무엇이 문제였거나 무엇이 필요했는지), 커밋 표(커밋 / 파일 / 내용)

## 1. <이슈 제목>          ← 이슈마다 반복
### 문제      사용자가 겪은 현상 또는 코드상 문제
### 원인      왜 발생했는지. 원인이 되는 코드를 보여 주고 주석으로 짚음
### 해결      Before / After 코드
### 결과      바뀐 동작, 트레이드오프, 주의할 점

## 검토했지만 하지 않은 것
대안이나 되돌린 변경과 그 이유 (없으면 "없음")

## 검증
- `./gradlew test` 결과, 추가한 테스트
- 수동 확인 시나리오 (debug 빌드 기준)
- 리팩토링이면 **기존 동작과 동일함을 어떻게 확인했는지** 반드시 기재

## 후속 작업
남은 TODO (없으면 "없음")
```

- 이슈가 하나뿐인 문서(단일 버그 등)는 `## 1.` 없이 문제 / 원인 / 해결 / 결과를 바로 씁니다.
- **변경 내용은 가능한 한 코드로** 씁니다. 이슈마다 Before / After 핵심 부분만 10~20줄 정도로 넣고,
  전체 diff는 넣지 않고 커밋 해시로 대신합니다.
- 코드가 아닌 흐름은 코드 블록 흐름도, 여러 항목 비교는 노션 표로 씁니다.
- 파일명·클래스명은 인라인 코드로 감싸서 자동 링크(예: `CLAUDE.md` → `http://CLAUDE.md`)가 생기지 않게 합니다.
- 추정인지 확인한 사실인지 구분해서 씁니다 (예: "코드 분석 기준, 실기기 재현은 안 함").

## 빌드 & 명령어

- 시크릿·서버 주소는 `local.properties`(저장소 미포함)에서 읽습니다. 필요한 키 목록은 각 모듈
  `build.gradle.kts`의 `getProperty("...")`를 참고합니다. 값이 없으면 빈 문자열로 빌드됩니다.
- `MIXPANEL_PROJECT_TOKEN`은 `presentation`의 `analytics/MixpanelModule`에서 쓰며, 이벤트는 release 빌드에서만 전송됩니다.
- 서명: release 키(`storeFile` 등)는 선택입니다. 배포는 Android Studio Generate Signed Bundle + Play 앱 서명을 씁니다.
  공용 debug 키(`debugStoreFile` 등, 경로는 프로젝트 루트 기준)도 선택이며, 없으면 PC별 기본 debug keystore를 씁니다.
- Sentry ProGuard 매핑 업로드용 인증 토큰은 루트 `sentry.properties`(gitignore) 또는 `SENTRY_AUTH_TOKEN` 환경변수.
- `google-services.json`은 저장소에 없음 (gitignore).
- keystore 파일(`*.jks`, `*.keystore`)은 gitignore 대상이라 저장소에 올리지 않고 팀 드라이브로 공유합니다. 카카오 로그인·공유는
  서명 키 해시가 카카오 개발자 콘솔에 등록돼 있어야 동작합니다(debug·release·Play 앱 서명 키 각각).
- API 서버 주소는 `local.properties`의 `BASE_URL_DEBUG` / `BASE_URL_RELEASE`에서 읽어 `data`(Retrofit)와
  `presentation`(공유 링크, 오디오 URL) 두 모듈의 `BuildConfig.BASE_URL`로 들어갑니다. debug 빌드는 `BASE_URL_DEBUG`,
  release 빌드는 `BASE_URL_RELEASE`를 씁니다. 두 키는 필수입니다(없으면 `BASE_URL`이 `"null"`로 들어감).
  값은 `/`로 끝나야 하고(Retrofit 관례), 코드에서는 `"${BuildConfig.BASE_URL}경로"`처럼 앞에 `/` 없이 이어 붙입니다.
  서버 주소는 저장소(코드·README·커밋 메시지)에 적지 않습니다.
- `release`: minify + shrinkResources, 서명 필요.
- CI 없음 — 로컬에서 `./gradlew assembleDebug`로 확인.

### 테스트 현황

- 단위 테스트 15개가 `app/src/test/`에 있지만, 멀티모듈 전환 이전 패키지(`com.picke.app.domain...`)를
  import하고 있고 어느 모듈에도 `testImplementation` 의존성(JUnit, MockK, coroutines-test)이 선언되어
  있지 않습니다. 버전 카탈로그에는 있음. 따라서 `./gradlew test`는 현재 그대로는 통과한다고 가정하지 않습니다.
- 새 테스트는 대상 코드가 있는 모듈의 `src/test/`에 둡니다 (UseCase → `domain/src/test`,
  RepositoryImpl → `data/src/test`). 테스트 의존성 추가나 기존 테스트 이전은 별도 작업으로 먼저 물어볼 것.
- 테스트 함수 이름은 백틱 한국어 문장으로 씁니다 (`` fun `빈 문장만 있으면 결과에서 제외한다`() ``).

## Claude 작업 방식

- **규칙이 없는 판단은 임의로 정하지 않고 바로 물어봅니다.**
  - 대상: CLAUDE.md에 규칙이 없고, 기존 코드도 여러 방식이 섞여 있거나 참고할 코드가 없어서 선택이 필요한 경우
    (네이밍, 파일 위치, 상태 설계, 에러 처리 방식, 라이브러리 사용법 등).
  - 기존 코드가 한 가지 방식으로 일관되면 묻지 않고 그 방식을 따릅니다.
  - 질문할 때는 현재 코드 상태(어떤 방식이 몇 곳에서 쓰이는지)와 선택지를 함께 보여 주고, 추천안이 있으면 표시합니다.
  - 답을 받으면 해당 섹션에 규칙으로 추가한 뒤 작업을 이어갑니다. 한 번 정한 규칙은 다시 묻지 않습니다.
- "손대는 김에" 기존 코드 개선(색상 토큰, `CancellationException` 등)은 **수정한 함수 안에서만** 합니다.
- 작업 범위 밖에서 발견한 문제는 고치지 않고 보고만 합니다.
- `./gradlew assembleDebug`는 작업을 마친 뒤 한 번 실행해 확인합니다.
- 구현은 바로 진행하고 끝난 뒤 diff를 보고합니다. (모듈 경계를 넘는 구조 변경은 아래 절대 규칙대로 plan 모드)

## 절대 규칙

- 새 feature를 추가할 땐 `domain/data/presentation` 3개 모듈에 동일한 feature 패키지명으로 대칭되게
  만들 것 (`feature/battle`이면 세 모듈 모두 `feature/battle` 또는 `ui/battle`). 이미 이름이 다른
  기존 feature(`mypage`↔`my` 등, 위 대응표 참고)는 이름을 바꾸지 않습니다.
- 새 feature 추가 체크리스트: domain(Board, Repository, UseCase, UseCases) → data(Api, Dto, RepositoryImpl,
  `ApiModule`·`RepositoryModule` 등록) → `app/di/UseCaseModule` 등록 → presentation(Screen, ViewModel, UiState).
- 한 프롬프트/한 커밋에는 하나의 작업만.
  - "하나의 작업"은 목적 단위입니다. 여러 화면·파일에 걸쳐 있어도 같은 목적(예: 누락된 Mixpanel 이벤트 추가,
    하드코딩 색상을 토큰으로 교체)의 같은 종류 수정이면 한 커밋으로 묶습니다. 본문에 무엇을 고쳤는지 나열합니다.
  - 목적이 다르면(예: 이벤트 누락 수정 + 트래킹 구조 리팩토링) 커밋을 나눕니다.
- 모듈 경계를 넘나드는 큰 구조 변경(공통 상위 클래스 도입, UseCase 레이어 제거 등)은 plan 모드로
  먼저 계획을 제시하고 승인받은 뒤 진행.