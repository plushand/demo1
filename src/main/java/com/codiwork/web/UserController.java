package com.codiwork.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codiwork.domain.User;
import com.codiwork.domain.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {
	private List<User> users = new ArrayList<User>();
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/loginForm")
	public String loginForm(){
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(String userId, String password, HttpSession session){
		User user = userRepository.findByUserId(userId);
		if (user == null) {
			return "redirect:/users/loginForm";
		}
		if (!user.matchPassword(password)){
			return "redirect:/users/loginForm";
		}

		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user);
		
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session){
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		return "redirect:/";
	}
	
	@GetMapping("/form")
	public String form(){
		return "/user/form";
	}
	
	@PostMapping("")
	public String create(User user){
		userRepository.save(user);
		return "redirect:/users";
	}
	
	@GetMapping("")
	public String list(Model model){
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}

	@GetMapping("{id}/form")
	public String form(@PathVariable Long id, Model model, HttpSession session){
		if (!HttpSessionUtils.isLoginUser(session)){
			return "redirect:/users/loginForm";
		}
		User sessionedUser = HttpSessionUtils.getUserFormSession(session);
		if(!sessionedUser.matchId(id)){
			throw new IllegalStateException("자기 자신의 정보만 수정할 수 있습니다.");
		}
		model.addAttribute("user", userRepository.findOne(sessionedUser.getId()));
		return "/user/updateForm";
	}
	
	@PutMapping("/{id}")
	public String update(@PathVariable Long id, User updatedUser, HttpSession session){
		if (!HttpSessionUtils.isLoginUser(session)){
			return "redirect:/users/loginForm";
		}
		User sessionedUser = HttpSessionUtils.getUserFormSession(session);
		if(!sessionedUser.matchId(id)){
			throw new IllegalStateException("자기 자신의 정보만 수정할 수 있습니다.");
		}
		User user = userRepository.findOne(id);
		user.update(updatedUser);
		userRepository.save(user);
		return "redirect:/users";
	}
}
