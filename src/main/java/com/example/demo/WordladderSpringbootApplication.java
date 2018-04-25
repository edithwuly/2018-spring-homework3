package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
@RestController
public class WordladderSpringbootApplication{
	
	private static Logger logger = LoggerFactory.getLogger(WordladderSpringbootApplication.class);
	
	@RequestMapping(value = "/Wordladder", method = RequestMethod.GET)
    public String test(@RequestParam(value="w1", defaultValue="code") String w1,@RequestParam(value="w2", defaultValue="data") String w2,@RequestParam(value="file", defaultValue="dictionary.txt") String file) 
	{
    	return new Wordladder(w1,w2,file).act();
    }

	public static void main(String[] args) {
		logger.info("Wordladder start");
		SpringApplication.run(WordladderSpringbootApplication.class, args);
		logger.info("Wordladder end");
	}
}