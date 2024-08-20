# SecurityContextRepository

---

인증이 성공하면, AuthenticationManager로부터 반환된 Authentication 객체는 SecurityContext에 저장된다.

SecurityContext는 SecurityContextHolder를 통해 관리되며, SecurityContextHolder에 SecurityContext가 설정된다.
```java
SecurityContextHolder.getContext().setAuthentication(authentication);
```

SecurityContextHolder에 저장되었지만 stateless 한 상태로, 인증이 지속되지는 않는다.<br>
인증이 지속되려면 SecurityContextRepository에 사용자 정보를 저장해야 한다.

SecurityContextRepository의 여러 구현체를 사용하여 지속 인증 처리를 할 수 있다.

---

### HttpSessionSecurityContextRepository
SecurityContext를 HttpSession에 연결한다.

### NullSecurityContextRepository
SecurityContext를 세션에 저장하지 않고 OAuth로 인증하는 경우,
아무 작업도 수행하지 않는다.

### RequestAttributeSecurityContextRepository
SecurityContext가 요청 단위로 관리된다.
HTTP 요청마다 독립적인 SecurityContext를 제공하게 되어, 세션에 의존하지 않게 된다. -> stateless

### DelegatingSecurityContextRepository
SecurityContext를 저장할 때, 두 개 이상의 SecurityContextRepository를 지정할 수 있다.
지정된 SecurityContextRepository를 차례대로 사용하여 SecurityContext를 저장하거나 로드한다.
```java
new DelegatingSecurityContextRepository(
    new RequestAttributeSecurityContextRepository(),
    new HttpSessionSecurityContextRepository()
);
```
SecurityContext가 저장될 때, 첫번째로 지정한 구현체로 SecurityContext를 저장한다.
이후 두번째에 지정한 구현체로 동일한 SecurityContext를 저장한다.
위 코드에서는 requestAttribute와 세션 모두에 SecurityContext가 저장된다.

SecurityContext가 로드될 때도, 순차적으로 지정된 저장소에서 SecurityContext를 로드한다.
requestAttribute에서 SecurityContext를 찾고, 없으면 세션에서 찾는다. 