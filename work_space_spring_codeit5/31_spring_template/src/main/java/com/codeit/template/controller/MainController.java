package com.codeit.template.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.*;


@Controller
public class MainController {

	@RequestMapping("/")
	public String indexPage() {
		return "main";
	}
	
	@GetMapping("/thymeleaf")
	public String thymeleafPage(Model model) {
		model.addAttribute("str", "Hello Spring Boot World!!!");
		model.addAttribute("str2", "<b>Hello Spring Boot World!!!</b>");
		model.addAttribute("name", "홍길동");
		model.addAttribute("date", new Date());
		
		List<String> list = new ArrayList<>();
		list.add("문자열1");
		list.add("문자열2");
		list.add("문자열3");
		list.add("문자열4");
		model.addAttribute("list", list);
		
		Map<String, String> map = new HashMap<>();
		map.put("id", "test1234");
		map.put("name", "홍길동");
		model.addAttribute("map", map);
		
		Member member = new Member("test1", "홍길동", 31, "남", "서울시", List.of("Java","C++"), null);
		Member member1 = new Member("test2", "최길순", 32, "여", "서울시", List.of("Javascript","C++"), null);
		Member member2 = new Member("test3", "박길동", 33, "남", "서울시", List.of("C","C++"), null);
		Member member3 = new Member("test4", "김길동", 34, "남", "서울시", List.of("Python","Java","C++"), null);
		model.addAttribute("member", member);
		
		List<Member> memberList = new ArrayList<>();
		memberList.add(member);
		memberList.add(member1);
		memberList.add(member2);
		memberList.add(member3);
		model.addAttribute("memberList", memberList);

		return "thymeleaf/index";
	}
	
	@GetMapping("/request")
	@PostMapping("/request")
	public String mainRequest(Model model, String value1, String value2, String values[]) {
		model.addAttribute("value1", value1);
		model.addAttribute("value2", value2);
		model.addAttribute("values", values);
		return "thymeleaf/request";
	}
	
	
	@PostMapping("/join")
	public String memberJoin(Model model, @ModelAttribute Member member) {
//		model.addAttribute("member", member); // @ModelAttribute로 view 바로 넘길수 있다.
		return "thymeleaf/memberView";
	}
	
	
	
}











