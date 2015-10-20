package pl.arczynskiadam.notesmanager.web.facade;

import pl.arczynskiadam.notesmanager.core.model.AnonymousUserModel;
import pl.arczynskiadam.notesmanager.core.model.RegisteredUserModel;

public interface UserFacade {
	public RegisteredUserModel getCurrentUser();
	public AnonymousUserModel findAnonymousUserByNick(String nick);
	public void registerUser(String nick, String email, String plainPassword);
	public boolean isCurrentUserAnonymous();
}
