package pl.arczynskiadam.notesmanager.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import pl.arczynskiadam.notesmanager.core.model.AnonymousUserModel;
import pl.arczynskiadam.notesmanager.core.model.RegisteredUserModel;
import pl.arczynskiadam.notesmanager.core.model.UserModel;
import pl.arczynskiadam.notesmanager.core.model.UserPreferencesModel;

public interface UserPreferencesRepository extends JpaRepository<UserPreferencesModel, Integer> {
	
	@Query("FROM UserPreferencesModel prefs WHERE prefs.user.id = :id ")
    public UserPreferencesModel findPreferencesForUser();

}
