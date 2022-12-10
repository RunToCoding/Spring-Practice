package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {
	
	private final EntityManager em;	// JPA는 EntityManager를 통해 사용된다 -> Spring에서는 자동으로 이것까지 생성해준다.

	public JpaMemberRepository(EntityManager em) {
		this.em = em;
	}

	// 저장
	public Member save(Member member) {
		em.persist(member);	//persist : 영구저장함
		
		return member;
	}

	// 조회1
	public Optional<Member> findById(Long id) {
		Member member = em.find(Member.class, id);	// 조회
		
		return Optional.ofNullable(member); 
	}

	// 전체조회
	public List<Member> findAll() {
		// Member.class : 아래와 같은 경우는 DB 조회가 아닌 사실 Member 객체 조회를 의미한다.
		return em.createQuery("select m from Member m", Member.class)
				 .getResultList();
	}

	// 이름을 통한 조회
	public Optional<Member> findByName(String name) {
		List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
								.setParameter("name", name)
								.getResultList();
		return result.stream().findAny();
 }
}