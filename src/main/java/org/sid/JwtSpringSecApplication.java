package org.sid;

import java.util.stream.Stream;

import org.apache.tomcat.jni.User;
import org.sid.dao.TaskRepository;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.sid.entities.Task;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class JwtSpringSecApplication implements CommandLineRunner {
	
	@Autowired
	private TaskRepository tr;
	@Autowired
	private AccountService as;

	public static void main(String[] args) {
		SpringApplication.run(JwtSpringSecApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder getBCryptPasswordEncoder () {
		return new BCryptPasswordEncoder();
	}
	
	
	@Override
	public void run(String... args) throws Exception {
		
		as.saveUser(new AppUser("admin","123",null));
		as.saveUser(new AppUser("user","123",null));
		as.saveRole(new AppRole("ADMIN"));
		as.saveRole(new AppRole("USER"));
		as.addRoleToUser("admin", "ADMIN");
		as.addRoleToUser("admin", "USER");
		as.addRoleToUser("user", "USER");
		
		Stream.of("T1","T2","T3").forEach(t-> {
			tr.save(new Task(null,t));
		});
		tr.findAll().forEach(t->System.out.println(t.getTaskName()));
	}
}
