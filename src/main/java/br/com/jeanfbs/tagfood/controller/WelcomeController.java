package br.com.jeanfbs.tagfood.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WelcomeController {


	@RequestMapping(value="/",method=RequestMethod.GET)
	public ModelAndView hello(){
		
		ModelAndView view = new ModelAndView("home");
		view.addObject("message","Java Set :)");
		return view;
	}

}