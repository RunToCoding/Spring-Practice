package hello.hellospring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import hello.hellospring.repository.JpaMemberRepository;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.service.MemberService;

@Configuration
public class SpringConfig {
	private final MemberRepository memberRepository;

	public SpringConfig(MemberRepository memberRepository) { // Spring이 생성한 SpringDataJpaMemberRepository이 등록이 된다.
		this.memberRepository = memberRepository;
	}
	
	@Bean
	public MemberService memberService() {
		return new MemberService(memberRepository);
	}

//	@Bean
//	public MemberRepository memberRepository() {
//		// return new MemoryMemberRepository();
//		// return new JdbcMemberRepository(dataSource);
//		// return new JdbcTemplateMemberRepository(dataSource);
//		return new JpaMemberRepository(em);
//	}
}
