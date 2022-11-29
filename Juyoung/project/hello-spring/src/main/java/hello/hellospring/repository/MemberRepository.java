package hello.hellospring.repository;

import java.util.List;
import java.util.Optional;

import hello.hellospring.domain.Member;

public interface MemberRepository {
	Member save(Member member);				// 회원 저장

	Optional<Member> findById(Long id);			// id 값으로 id 찾음
	Optional<Member> findByName(String name);	// name 값으로 name 찾음

	List<Member> findAll();						// findAll 하면 지금까지 저장된 모든 회원 List를 반환한다.
}
