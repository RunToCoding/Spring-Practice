package hello.hellospring.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import hello.hellospring.domain.Member;

public class MemoryMemberRepository implements MemberRepository {
	
	// 임시 member 내용을 저장할 객체 선언
	private static Map<Long, Member> store = new HashMap<>();	// key (회원ID) : Value (회원멤버)
	private static long sequence = 0L;							// key값 생성

	@Override
	public Member save(Member member) {
		member.setId(++sequence);			// ID : seq + 1
		store.put(member.getId(), member);	// store에 key:value 형식으로 저장
		return member;
	}

	@Override
	public Optional<Member> findById(Long id) {
		/**
		 * store에서 결과 꺼내서 반환
		 * 만약 반환 값이 NULL -> Optional을 사용해 반환값 Null을 던져 Client에서 처리 가능
		 */
		return Optional.ofNullable(store.get(id));
	}

	@Override
	public Optional<Member> findByName(String name) {
		/**
		 * member -> member.getName().equals(name) : 파라미터name이 Member 객체가 가진 name과 같은 객체 반환
		 */
		return store.values().stream()
				.filter(member -> member.getName().equals(name))
				.findAny();
	}

	@Override
	public List<Member> findAll() {
		/** 
		 *	 store.values()하면 모든 값 반환
		 */
		return new ArrayList<>(store.values());
	}
	
	
	public void clearStore() {
		store.clear();
	}
	
	
}
