package org.sid.web;

import org.sid.entities.AppUser;
import org.sid.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRestController {
	
	@Autowired
	private AccountService as;
	
	@RequestMapping("/register")
	public AppUser register (@RequestBody RegisterForm rf) {
		if (! rf.getPassword().equals(rf.getRepassword())) throw new RuntimeException("Confirmer le mot de passe");
		AppUser u = as.findUserByUsername(rf.getUsername());
		if(u!=null) throw new RuntimeException("utilisateur existe déjà");
		AppUser user = new AppUser();
		user.setUsername(rf.getUsername());
		user.setPassword(rf.getPassword());
		as.saveUser(user);	
		as.addRoleToUser(rf.getUsername(), "USER");
		return user;
	}
	
	
	
}
