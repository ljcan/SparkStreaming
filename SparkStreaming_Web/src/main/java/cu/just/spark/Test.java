package cu.just.spark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@RestController
public class Test {
	@RequestMapping("/hello")
	public String hello() {
		return "hello ";
	}
	
	@RequestMapping("/frist")
	public ModelAndView firstDemo() {
		return new ModelAndView("test");
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Test.class, args);
	}

}
