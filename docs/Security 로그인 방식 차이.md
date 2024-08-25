# Security 로그인 방식 차이

---

## Form 기반 로그인
- Spring Security에서는 기본저그올 폼 기반 인증을 사용할 때, 로그인 폼의 입력 필드 이름을 'username', 'password'로 받는다.
- Spring Security가 제공하는 기본 인증 필터(UsernamePasswordAuthenticationFilter)가 두 필드 이름을 사용하여 인증을 처리한다.
```java
public class UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    // ...
}
```
```html
<form role="form" id="login-form" th:action="@{/login-proc}" method="post">
    <div class="form-group">
        <label th:for="username">아이디</label>
        <input type="text" name="username" class="form-control" placeholder="아이디 입력해주세요">
    </div>
    <div class="form-group">
        <label th:for="password">비밀번호</label>
        <input type="password" class="form-control" name="password" placeholder="비밀번호">
    </div>
    <button type="submit" class="btn btn-primary">Submit</button>
</form>
```

## API 로그인
- API를 통해 로그인을 수행할 때는, Spring Security의 기본 인증 프로세스를 사용하지 않고
- 직접적인 HTTP 요청을 받아 JSON 형태로 전달된 데이터를 처리하는 방식을 사용할 수 있다.
```java
@PostMapping("/api/login")
public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    String memberId = loginRequest.getMemberId();
    String password = loginRequest.getPwd();

    // Custom authentication logic
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(memberId, password)
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    
    return ResponseEntity.ok();
}
```
- UsernamePasswordAuthenticationFilter 를 상속하는 커스텀 필터를 구현하여 사용할 수도 있다.
```java
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /**
     * 사용자가 인증을 시도할 때 호출.
     * HTTP 요청과 응답 객체를 받아 인증을 처리한다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 사용자의 인증 정보를 담는 컨테이너
        final UsernamePasswordAuthenticationToken authRequest;
        try {
            final Member member = new ObjectMapper().readValue(request.getInputStream(), Member.class);
            // 사용자 정보를 포함하는 토큰 객체 생성
            authRequest = new UsernamePasswordAuthenticationToken(member.getMemberId(), member.getPwd());
        } catch (IOException e) {
            throw new InputNotFoundException();
        }

        // 추가적인 요청 정보 설정 - IP 주소, 세션 ID 등
        setDetails(request, authRequest);
        // 생성된 토큰을 사용하여 AuthenticationManager 를 통해 실제 인증 수행
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
```