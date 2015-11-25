package pl.arczynskiadam.notesmanager.core.dao.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import pl.arczynskiadam.notesmanager.core.model.RegisteredUserModel;
import pl.arczynskiadam.notesmanager.core.model.UserModel;

public class UserSpecs {
	
	public static Specification<UserModel> registered() {
		return new Specification<UserModel>() {
            @Override
            public Predicate toPredicate(Root<UserModel> userRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
            	final Root<UserModel> person = query.from(UserModel.class);
                return cb.equal(person.type(), cb.literal(RegisteredUserModel.class));
            }
		};
	}
	
	public static Specification<UserModel> anonymous() {
		return Specifications.not(registered());
	}
}
