package pl.arczynskiadam.notesmanager.core.service.impl;

import static pl.arczynskiadam.notesmanager.core.service.constants.ServiceConstants.Session.Attributes.NOTES_PAGINATION;

import java.time.LocalDateTime;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.arczynskiadam.notesmanager.core.dao.NoteRepository;
import pl.arczynskiadam.notesmanager.core.dao.specs.NoteSpecs;
import pl.arczynskiadam.notesmanager.core.model.AnonymousUserModel;
import pl.arczynskiadam.notesmanager.core.model.NoteModel;
import pl.arczynskiadam.notesmanager.core.model.RegisteredUserModel;
import pl.arczynskiadam.notesmanager.core.service.NoteService;
import pl.arczynskiadam.notesmanager.core.service.SessionService;
import pl.arczynskiadam.notesmanager.core.service.UserService;
import pl.arczynskiadam.notesmanager.web.data.DateFilterData;
import pl.arczynskiadam.notesmanager.web.data.NotesPaginationData;

@Service
public class DefaultNoteService implements NoteService {
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(DefaultNoteService.class);
	
	@Resource
	private NoteRepository noteDAO;
	
	@Autowired(required = true)
	private UserService userService;
	
	@Override
	@Transactional
	public void addNoteForRegisteredUser(NoteModel note, String userNick) {
		RegisteredUserModel user = userService.findRegisteredUserByNick(userNick);
		user.addNote(note);
		saveNewNote(note);
	}

	@Override
	@Transactional
	public void addNoteForAnonymousUser(NoteModel note, String userNick) {
		AnonymousUserModel anonymous = userService.findAnonymousUserByNick(userNick);
		if (anonymous == null) {
			anonymous = new AnonymousUserModel();
			anonymous.setNick(userNick);
		}
		
		anonymous.addNote(note);
		saveNewNote(note);
	}
	
	private void saveNewNote(NoteModel note) {
		if (note.getDateCreated() == null) {
			note.setDateCreated(LocalDateTime.now());
		}
		noteDAO.save(note);
	}
	
	@Override
	public void updateNote(NoteModel note) {
		RegisteredUserModel currentUser = userService.getCurrentUser();
		if (!note.getAuthor().equals(currentUser)) {
			throw new IllegalStateException("global.error.userNotOwnerOfNote");
		}
		note.setLastModified(LocalDateTime.now());
		noteDAO.save(note);
	}

	@Override
	@Transactional
	public Page<NoteModel> listNotes(int pageId, int pageSize, String sortCol, boolean asc) {
		RegisteredUserModel currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return listNotesForAnonymousUser(pageId, pageSize, sortCol, asc);
		}
		return ListNotesForRegisteredUser(pageId, pageSize, sortCol, asc, currentUser);
	}

	private Page<NoteModel> ListNotesForRegisteredUser(int pageId, int pageSize, String sortCol, boolean asc, RegisteredUserModel currentUser) {
		return noteDAO.findAll(NoteSpecs.forUser(currentUser),
				constructPageSpecification(keepPageNumberInRange(pageId, pageSize, currentUser), pageSize, sortCol, asc));
	}
	
	private Page<NoteModel> listNotesForAnonymousUser(int pageId, int pageSize, String sortCol, boolean asc) {
		Page<NoteModel> notes = noteDAO.findAll(NoteSpecs.anonymous(),
				constructPageSpecification(keepPageNumberInRange(pageId, pageSize), pageSize, sortCol, asc));
		for (NoteModel note : notes.getContent()) {
			note.getAuthor().getNick();
		}
		return notes;
	}

	private int keepPageNumberInRange(int pageId, int pageSize, RegisteredUserModel currentUser) {
		int notesCount = (int) noteDAO.count(NoteSpecs.forUser(currentUser));
		if (pageId > notesCount / pageSize) {
			pageId = notesCount / pageSize;
		}
		return pageId;
	}
	
	private int keepPageNumberInRange(int pageId, int pageSize) {
		int notesCount = (int) noteDAO.count(NoteSpecs.anonymous());
		if (pageId > notesCount / pageSize) {
			pageId = notesCount / pageSize;
		}
		return pageId;
	}
	
	@Override
	@Transactional
	public Page<NoteModel> listNotesByDateFilter(int pageId, int pageSize, String sortCol, boolean asc, DateFilterData dateFilter) {
		RegisteredUserModel currentUser = userService.getCurrentUser();
		
		Specifications<NoteModel> spec = null;
		if (currentUser == null) {
			spec = Specifications.where(NoteSpecs.anonymous());
		} else {
			spec = Specifications.where(NoteSpecs.forUser(currentUser));
		}
		
		if (dateFilter.getFrom() != null) {
			spec = spec.and(NoteSpecs.from(dateFilter.getFrom()));
		}
		if (dateFilter.getTo() != null) {
			spec = spec.and(NoteSpecs.to(dateFilter.getTo()));
		}
		
		Page<NoteModel> notes = noteDAO.findAll(spec, constructPageSpecification(pageId, pageSize, sortCol, asc));
		if (currentUser == null) {
			for (NoteModel note : notes.getContent()) {
				note.getAuthor().getNick();
			}
		}
		return notes;
	}

	@Override
	@Transactional
	public int getNotesCountForRegisteredUser(String userNick) {
		RegisteredUserModel user = userService.findRegisteredUserByNick(userNick);
		return (int) noteDAO.count(NoteSpecs.forUser(user));
	}
	
	@Override
	@Transactional
	public void deleteNotes(Collection<Integer> ids) {
		noteDAO.deleteByIds(ids);
	}
	
	@Override
	@Transactional
	public void deleteUserNotes(int userId) {
		noteDAO.deleteByUserId(userId);
	}
	
	@Override
	@Transactional
	public NoteModel findNoteById(int id) {
		NoteModel note = noteDAO.findOne(id);
		if (note != null) {
			note.getAuthor().getNick();
		}
		return note;
	}
	
	private Pageable constructPageSpecification(int pageIndex, int pageSize, String sortCol, boolean asc) {
		Sort sort = new Sort(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortCol);
		Pageable pageSpecification = new PageRequest(pageIndex, pageSize, sort);
		return pageSpecification;
	}
	
	public void setNoteDAO(NoteRepository noteDAO) {
		this.noteDAO = noteDAO;
	}
}
