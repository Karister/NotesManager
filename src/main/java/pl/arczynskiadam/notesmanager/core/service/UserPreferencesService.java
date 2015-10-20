package pl.arczynskiadam.notesmanager.core.service;

import pl.arczynskiadam.notesmanager.core.model.UserPreferencesModel;

public interface UserPreferencesService {
	public UserPreferencesModel getUserPreferencesForUser(int userId);
	public UserPreferencesModel buildDefaultUserPreferences();
}
