package br.com.insideup.controller;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Transactional("transactionManager")
public class TestRestController {

	@PersistenceContext(unitName="entityManager")
	private EntityManager manager;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST,
		    consumes = {"multipart/form-data"})
		@ResponseBody
		public boolean upload(
				@RequestPart("json") @Valid String json,
		        @RequestPart("file") @Valid @NotNull @NotBlank MultipartFile file,
		        HttpServletResponse response) throws IOException {
		
		    System.out.println(file.getOriginalFilename());
		    System.out.println(file.getBytes().length);
		    System.out.println(json);
		    response.setHeader("Access-Control-Allow-Origin", "*");
		    return true;
		}
	
}
