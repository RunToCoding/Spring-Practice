package hello.hellospring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hello.hellospring.domain.Member;
import hello.hellospring.service.MemberService;

@Controller
public class MemberController {
	private final MemberService memberService;

	@Autowired
	public MemberController(MemberService memberService) {
		this.memberService = memberService; // 생성자를 통해 서비스 Controlle에 의존관계 주입
	}
	
	@GetMapping(value = "/members/new")
	public String createForm() {
		return "members/createMemberForm";
	}

	@PostMapping(value = "/members/new")
	public String create(MemberForm form) { // MemberForm form -> 폼 매핑해줘야 한다.
		Member member = new Member();
		member.setName(form.getName());	//form에서 GetName
		
		memberService.join(member);	//Service : 회원가입
		
		return "redirect:/";	// Home화면으로 redirect
	}
	
	@GetMapping(value = "/members")	//목록조회
	public String list(Model model) {
		List<Member> members = memberService.findMembers(); //findMember : 목록조회
		model.addAttribute("members", members);	// attributeName : members / attributeValue : members (서비스 결과)
		return "members/memberList";
	}
}
