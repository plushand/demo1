package com.codiwork.web;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codiwork.domain.Answer;
import com.codiwork.domain.AnswerRepository;
import com.codiwork.domain.Question;
import com.codiwork.domain.QuestionRepository;
import com.codiwork.domain.Result;
import com.codiwork.domain.User;

@RestController
@RequestMapping("/api/questions/{questionId}/answers")
public class ApiAnswerController {
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	@PostMapping("")
	public Answer create(@PathVariable Long questionId, String contents, HttpSession session){
		if (!HttpSessionUtils.isLoginUser(session)){
			return null;
		}
		User loginUser = HttpSessionUtils.getUserFormSession(session);
		Question question = questionRepository.findOne(questionId);
		Answer answer = new Answer(loginUser, question, contents);
		question.addAnswer();
		return answerRepository.save(answer);
	}
	
	@DeleteMapping("/{id}")
	public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session){
		if(!HttpSessionUtils.isLoginUser(session)){
			return Result.fail("로그인해야 합니다.");
		}
		Answer answer = answerRepository.findOne(id);
		User loginUser = HttpSessionUtils.getUserFormSession(session);
		if(!answer.isSameWriter(loginUser)){
			return Result.fail("자신의 답글만 지울 수 있습니다.");
		}
		answerRepository.delete(id);
		Question question = questionRepository.findOne(questionId);
		question.deleteAnswer();
		questionRepository.save(question);
		return Result.ok();
	}
}
