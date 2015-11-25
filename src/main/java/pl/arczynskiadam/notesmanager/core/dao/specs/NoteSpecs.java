package pl.arczynskiadam.notesmanager.core.dao.specs;

import static org.springframework.data.jpa.domain.Specifications.not;

import java.time.LocalDate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import pl.arczynskiadam.notesmanager.core.model.NoteModel;
import pl.arczynskiadam.notesmanager.core.model.NoteModel_;
import pl.arczynskiadam.notesmanager.core.model.RegisteredUserModel;
import pl.arczynskiadam.notesmanager.core.model.UserModel;
import pl.arczynskiadam.notesmanager.core.model.UserModel_;

public class NoteSpecs {

	public static Specification<NoteModel> from(final LocalDate from)
	{
		return new Specification<NoteModel>() {
            @Override
            public Predicate toPredicate(Root<NoteModel> noteRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
            	Path<LocalDate> deadline = noteRoot.<LocalDate> get(NoteModel_.deadline);
                return cb.greaterThanOrEqualTo(deadline, from);
            }
		};
	}
	
	public static Specification<NoteModel> to(final LocalDate to)
	{
		return new Specification<NoteModel>() {
            @Override
            public Predicate toPredicate(Root<NoteModel> noteRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
            	Path<LocalDate> deadline = noteRoot.<LocalDate> get(NoteModel_.deadline);
                return cb.lessThanOrEqualTo(deadline, to);
            }
		};
	}
	
	public static Specification<NoteModel> registered()
	{
		return new Specification<NoteModel>() {
            @Override
            public Predicate toPredicate(Root<NoteModel> noteRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
            	final Subquery<Integer> personQuery = query.subquery(Integer.class);
				final Root<RegisteredUserModel> person = personQuery.from(RegisteredUserModel.class);
				final Join<RegisteredUserModel, NoteModel> notes = person.join(UserModel_.notes);
				personQuery.select(notes.<Integer> get(NoteModel_.id));
				return cb.in(noteRoot.get(NoteModel_.id)).value(personQuery);
            }
		};
	}
	
	public static Specification<NoteModel> anonymous()
	{
		return not(registered());
	}
	
	public static Specification<NoteModel> forUser(final UserModel user)
	{
		return new Specification<NoteModel>() {
            @Override
            public Predicate toPredicate(Root<NoteModel> noteRoot, CriteriaQuery<?> query, CriteriaBuilder cb) {
            	Path<UserModel> author = noteRoot.<UserModel> get(NoteModel_.author);
				return cb.equal(author, user);
            }
		};
	}
}
