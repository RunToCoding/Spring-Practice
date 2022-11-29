# Section 04. 스프링 빈과 의존관계

## 1. 컴포넌트 스캔과 자동 의존관계 설정

회원 컨트롤러가 회원 서비스와 회원 리포지토리를 사용할 수 있게 의존관계를 준비해야 함

```java
// Controller 애노테이이 붙으면 스프링 컨테이너에 Controller 객체 생성 후 스프링에서 관리
@Controller
class MemberController {

    // private final MemberService memberService = new MemberService();
    // 스프링이 관리를 하게 되면 스프링 컨테이너에 등록하고, 이를 받아서 쓰도록 변경해야 함
    // new 를 통해 생성하면 다른 컨트롤러에서도 MemberService 를 생성하게 되는데,
    // 스프링 컨테이너에 등록하고 공용으로 쓰는 것이 더 좋음
    private final MemberService memberService;
    
    // 생성자에 Autowired 가 있으면 스프링이 스프링 컨테이너에 있는 memberService 를 가져와 연결시켜 줌
    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
}
```

이를 실행하면 오류가 발생함

> Consider defining a bean of type 'hello.hellospring.service.MemberService' in your configuration.

현재 오류는 memberSerivce 가 스프링 빈으로 등록되어 있지 않아서 발생함<br/>
⇒ MemberService는 순수 자바이므로 애노테이션을 사용하여 인식하도록 바꿔주자!

### 스프링 빈을 등록하는 두 가지 방법
1. 컴포넌트 스캔과 자동 의존 관계 설정
2. 자바 코드로 직접 스프링 빈 등록하기

## 2. 자바 컴포넌트 스캔가 자동 의존 관계
- `@Component` 애노테이션이 있으면 스프링 빈으로 자동 등록됨
- `@Controller` (컨트롤러)가 ㅅ프링 빈으로 자동 등록된 이유도 컴포넌트 스캔 때문
- `@Component` 를 포함하는 다음 애노테이션도 스프링 빈으로 자동 등록됨
    - `@Controller`
    - `@Service`
    - `@Repository`
- 회원 서비스 스프링 빈 등록
```java
@Service
public class MemberService {
  // ...
}
```
- 회원 리포지토리 스프링 빈 등록
```java
@Repository
public class MemoryMemberRepository implements MemberRepository {
  // ...
}
```
- @Autowired를 통해 Controller와 Service를 연결하고, Service와 Repository를 연결함
```java
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
    
    // ...
    
}
```
> **참고!**<br>
> 스프링은 스프링 컨테이너에 스프링 빈을 등록할 때, 싱글톤으로 등록한다.
> (Default, 유일하게 하나만 등록해서 공유)<br/>
> 따라서 같은 스프링 빈이면 모두 같은 인스턴스이다.<br/>
> 설정으로 싱글톤이 아니게 설정할 수 있지만, 특별한 경우를 제외하면 대부분 싱글톤을 사용한다.

## 3. 자바 코드로 직접 스프링 빈 등록하기
- 회원 서비스와 회원 리포지토리의 @Service, @Repository, @Autowired 애노테이션을 제거하고 진행
```java
// 자바 코드로 직접 스프링 빈 등록하기
@Configuration
public class SpringConfig {

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }
}
```

### **참고**
- DI 종류 : 필드 주입, setter 주입, 생성자 주입
- 의존관계가 실행 중에 동적으로 변하는 경우는 거의 없으므로 생성자 주입 권장. 

#### 필드 주입 (별로 안 좋음. 중간에 변경 불가)

```java
@Autowired private MemberService memberService;
```

#### setter 주입 (public하게 호출되기 때문에 잘못 바꾸면 에러 발생)

```java
private MemberService memberService;
  
@Autowired
public void setMemberService(MemberService memberService) {
    this.memberService = memberService;
}
```

#### 생성자 주입
```java
private final MemberService memberService;
  
@Autowired
public void MemberService(MemberService memberService) {
    this.memberService = memberService;
}
```

실무에서는 주로 정형화된 컨트롤러, 서비스, 리포지토리 같은 코드는 컴포넌트 스캔을 사용한다.<br/>
**정형화되지 않거나, 상황에 따라 구현 클래스를 변경해야 하면 설정을 통해 스프링 빈으로 등록한다.
(스프링 빈 설정 파일만 수정하면 됨)**<br/>
> 주의!<br>
> @Autowired 를 통한 DI는 helloConroller , memberService 등과 같이 스프링이 관리하는 객체에서만 동작한다.<br/>
> 스프링 빈으로 등록하지 않고 내가 직접 생성한 객체에서는 동작하지 않는다.<br/> 
> 스프링 컨테이너, DI 관련된 자세한 내용은 스프링 핵심 원리 강의에서 설명한다.