package pl.arczynskiadam.notesmanager.web.facade.impl;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import pl.arczynskiadam.notesmanager.core.model.AnonymousUserModel;
import pl.arczynskiadam.notesmanager.core.model.RegisteredUserModel;
import pl.arczynskiadam.notesmanager.core.model.UserRoleModel;
import pl.arczynskiadam.notesmanager.core.service.UserPreferencesService;
import pl.arczynskiadam.notesmanager.core.service.UserService;
import pl.arczynskiadam.notesmanager.security.impl.EncodingHelper;
import pl.arczynskiadam.notesmanager.security.impl.SHA256SaltedPasswordEncoder;
import pl.arczynskiadam.notesmanager.security.impl.SaltGenerator;
import pl.arczynskiadam.notesmanager.web.SecurityConstants;
import pl.arczynskiadam.notesmanager.web.facade.UserFacade;

@Component
public class DefaultUserFacade implements UserFacade {

	@Resource(name = "userService")
	UserService userService;
	
	@Resource(name = "userPreferencesService")
	UserPreferencesService userPreferencesService;
	
	@Resource(name = "passwordEncoder")
	SHA256SaltedPasswordEncoder sha256Encoder;
	
	@Override
	public RegisteredUserModel getCurrentUser() {
		return userService.getCurrentUser();
	}
	
	@Override
	public AnonymousUserModel findAnonymousUserByNick(String nick) {
		return userService.findAnonymousUserByNick(nick);
	}
	
	@Override
	public boolean isCurrentUserAnonymous() {
		return userService.isCurrentUserAnonymous();
	}
	
	@Override
	public void registerUser(String nick, String email, String plainPassword) {
		RegisteredUserModel newUser = new RegisteredUserModel();
		newUser.setNick(nick);
		newUser.setEmail(email);
		newUser.setDateRegistered(LocalDateTime.now());
		newUser.setEnabled(true);
		newUser.setPasswordSalt(SaltGenerator.generateRandomSalt(16));
		newUser.setPasswordEncoding(SecurityConstants.DEFAULT_ENCODING);
		newUser.setPasswordHash(sha256Encoder.encode(EncodingHelper.buildPlainText(plainPassword, newUser.getPasswordSalt())));
		newUser.setEnabled(true);
		
		UserRoleModel role = new UserRoleModel();
		role.setRole("ROLE_USER");
		newUser.addUserRole(role);
		
		newUser.setUserPreferences(userPreferencesService.buildDefaultUserPreferences());
		
		userService.registerUser(newUser);
	}
}
