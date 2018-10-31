package org.sid.service;

import org.sid.dao.RoleRepository;
import org.sid.dao.UserRepository;
import org.sid.entities.AppRole;
import org.sid.entities.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class AccountServiceImpl implements AccountService {

	@Autowired
	private BCryptPasswordEncoder bpe;
	
	@Autowired
	private UserRepository ur;
	@Autowired
	private RoleRepository rr;
	
	@Override
	public AppUser saveUser(AppUser user) {
		String hashPwd = bpe.encode(user.getPassword());
		user.setPassword(hashPwd);
		return ur.save(user);
	}

	@Override
	public AppRole saveRole(AppRole role) {
		// TODO Auto-generated method stub
		return rr.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		AppRole role = rr.findByRolename(rolename);
		AppUser user = ur.findByUsername(username);
		user.getRoles().add(role);
		
	}

	@Override
	public AppUser findUserByUsername(String username) {
		// TODO Auto-generated method stub
		return ur.findByUsername(username);
	}

}
