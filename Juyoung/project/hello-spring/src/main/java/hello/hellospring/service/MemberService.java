package hello.hellospring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;

@Transactional
public class MemberService {
	
	// member 레파지토리 선언
	private final MemberRepository memberRepository;
	
	public MemberService (MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	/**
	 * 회원가입
	 */
	public Long join(Member member) {
		
		validateDuplicateMember(member); // 중복 회원 검증
		
		memberRepository.save(member);
		return member.getId();
		
	}

	// validateDuplicateMember : extract Method를 통해 생성된 함수 : Alt+shift+M
	private void validateDuplicateMember(Member member) {
		memberRepository.findByName(member.getName()).ifPresent(m -> {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		});
	}

	
	/**
	 * 전체 회원 조회
	 */
	public List<Member> findMembers() {
		return memberRepository.findAll();
	}

	
	/**
	 *  회원 ID 기반 회원 조회
	 */
	public Optional<Member> findOne(Long memberId) {
		return memberRepository.findById(memberId);
	}
}