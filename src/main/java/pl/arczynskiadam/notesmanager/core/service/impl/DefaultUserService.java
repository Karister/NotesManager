package pl.arczynskiadam.notesmanager.core.service.impl;

import javax.annotation.Resource;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import pl.arczynskiadam.notesmanager.core.dao.NoteRepository;
import pl.arczynskiadam.notesmanager.core.dao.UserRepository;
import pl.arczynskiadam.notesmanager.core.model.AnonymousUserModel;
import pl.arczynskiadam.notesmanager.core.model.RegisteredUserModel;
import pl.arczynskiadam.notesmanager.core.service.UserService;

@Service("userService")
public class DefaultUserService implements UserService {

	@Resource
	private UserRepository userDao;
	
	@Resource
	private NoteRepository notesDao;
	
	@Override
	public RegisteredUserModel findRegisteredUserByNick(String nick) {
		return userDao.findRegisteredUserByNick(nick);
	}
	
	@Override
	public AnonymousUserModel findAnonymousUserByNick(String nick) {
		return userDao.findAnonymousdUserByNick(nick);
	}
	
	@Override
	public RegisteredUserModel getCurrentUser() {
	    SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    if (authentication != null) {
	        Object principal = authentication.getPrincipal();
	        return principal instanceof UserDetails ? findRegisteredUserByNick(((UserDetails)principal).getUsername()) : null;
	    }
	    return null;
	}
	
	@Override
	public boolean isCurrentUserAnonymous() {
		return getCurrentUser() == null;
	}
	
	@Override
	public void registerUser(RegisteredUserModel user) {
		userDao.save(user);
	}
	
	@Override
	public boolean isEmailAvailable(String email) {
		return userDao.findRegisteredUserByEmail(email) == null;
	}
	
	@Override
	public boolean isNickAvailable(String nick) {
		return userDao.findRegisteredUserByNick(nick) == null;
	}
}
