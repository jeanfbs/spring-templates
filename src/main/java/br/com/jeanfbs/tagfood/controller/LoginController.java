package br.com.jeanfbs.tagfood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

//@Controller
public class LoginController {

	
	@RequestMapping(value="login")
	public ModelAndView home(){
		ModelAndView view = new ModelAndView("login");
		
		return view;
	}
}
