# Section 5. 회원 관리 예제 - 웹 MVC 개발

## 1. 회원 웹 기능 - 홈 화면 추가

### 홈 컨트롤러 추가

```java

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
``` 

### 회원 관리용 홈

- controller 에서 home 을 호출했을 때 보여지는 페이지
- controller 의 우선 순위가 더 높기 때문에 welcome page 가 아닌 home.html 이 화면에 보여짐!

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
    <div>
        <h1>Hello Spring</h1>
        <p>회원 기능</p>
        <p>
            <a href="/members/new">회원 가입</a>
            <a href="/members">회원 목록</a>
        </p>
    </div>
</div> <!-- /container -->
</body>
</html>
```

## 2. 회원 웹 기능 - 등록

### 회원 등록 Form 컨트롤러

- 회원 가입 클릭 시 GET 을 통해 웹 페이지 접속
- 등록 클릭 후 Form 이 전달되면 그에 대한 수행은 POST 로 진행
- 현재는 메모리 안에 정보를 등록하기 때문에 스프링 서버를 내리면 모든 내용이 날아감!

```java

@Controller
public class MemberController {
    // ...

    // 일반적으로 데이터 조회는 get, 데이터 등록은 post 사용
    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }

    // form을 통해 들어온 값을 처리하는 메소드
    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/"; // 홈화면으로 보내는 것
    }
}
```

- 웹 등록 화면에서 데이터를 전달 받을 Form 객체

```java
public class MemberForm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

### 회원 등록 Form HTML

```html

<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
    <form action="/members/new" method="post">
        <div class="form-group">
            <label for="name">이름</label>
            <input type="text" id="name" name="name" placeholder="이름을 입력하세요">
        </div>
        <button type="submit">등록</button>
    </form>
</div> <!-- /container -->
</body>
</html>
```

## 3. 회원 웹 기능 - 조회

### 회원 조회 컨트롤러

```java

@Controller
public class MemberController {
    // ...

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
```

### 회원 조회 HTML

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<body>
<div class="container">
    <div>
        <table>
            <thead>
            <tr>
                <th>#</th>
                <th>이름</th>
            </tr>
            </thead>
            <tbody>
            <tr th:each="member : ${members}">
                <td th:text="${member.id}"></td>
                <td th:text="${member.name}"></td>
            </tr>
            </tbody>
        </table>
    </div>
</div> <!-- /container -->
</body>
</html>
```