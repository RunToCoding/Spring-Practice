package hello.hellospring.repository;


import hello.hellospring.domain.Member;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {
	MemoryMemberRepository repository = new MemoryMemberRepository();
	
	@AfterEach				// 모든 케이스 함수들이 돌고 나서 이 함수를 돌게 만든다.
	public void afterEach() {	// 함수는 repository를 비우는 함수이다.
		repository.clearStore();
	}

	@Test
	public void save() {
		// given
		Member member = new Member();
		member.setName("spring");
		// when
		repository.save(member);
		// then
		Member result = repository.findById(member.getId()).get();
		assertThat(member).isEqualTo(result);		//assert
	}
	
	@Test
	public void findByName() {
		// given
		Member member1 = new Member();
		member1.setName("spring1");
		repository.save(member1);
		
		Member member2 = new Member();
		member2.setName("spring2");
		repository.save(member2);
		
		// when
		Member result = repository.findByName("spring2").get();
		
		// then
		assertThat(result).isEqualTo(member1);
	}
	
	@Test
	public void findAll() {
		// given
		Member member1 = new Member();
		member1.setName("spring1");
		repository.save(member1);
		Member member2 = new Member();
		member2.setName("spring2");
		repository.save(member2);
		
		// when
		List<Member> result = repository.findAll();
		// then
		assertThat(result.size()).isEqualTo(2);
	}
}