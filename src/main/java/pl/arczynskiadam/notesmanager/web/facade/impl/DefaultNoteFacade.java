package pl.arczynskiadam.notesmanager.web.facade.impl;

import static java.util.stream.Collectors.toList;
import static pl.arczynskiadam.notesmanager.web.facade.constants.FacadesConstants.Defaults.Pagination.ANONYMOUS_USER_DEFAULT_SORT_COLUMN;
import static pl.arczynskiadam.notesmanager.web.facade.constants.FacadesConstants.Defaults.Pagination.REGISTERED_USER_DEFAULT_SORT_COLUMN;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

import pl.arczynskiadam.notesmanager.core.model.NoteModel;
import pl.arczynskiadam.notesmanager.core.model.UserModel;
import pl.arczynskiadam.notesmanager.core.service.NoteService;
import pl.arczynskiadam.notesmanager.core.service.UserService;
import pl.arczynskiadam.notesmanager.web.data.DateFilterData;
import pl.arczynskiadam.notesmanager.web.data.NoteData;
import pl.arczynskiadam.notesmanager.web.data.NotesPaginationData;
import pl.arczynskiadam.notesmanager.web.facade.NoteFacade;
import pl.arczynskiadam.notesmanager.web.form.NoteForm;

@Component
public class DefaultNoteFacade implements NoteFacade {

	@Autowired(required = true)
	private NoteService noteService;
	
	@Autowired(required = true)
	private UserService userService;
	
	@Autowired(required = true)
	@Qualifier("noteMapper")
	Mapper noteMapper;

	@Override
	public int getNotesCountForRegisteredUser(String userNick) {
		return noteService.getNotesCountForRegisteredUser(userNick);
	}
	
	@Override
	public void addNewNote(NoteForm noteData) {
		NoteModel newNote = createNewNote(noteData);
		
		if (userService.isCurrentUserAnonymous()) {
			noteService.addNoteForAnonymousUser(newNote, noteData.getAuthor());
		} else {
			noteService.addNoteForRegisteredUser(newNote, noteData.getAuthor());
		}
	}

	private NoteModel createNewNote(NoteForm noteData) {
		NoteModel note = new NoteModel();
		note.setTitle(noteData.getTitle());
		note.setContent(noteData.getContent());
		note.setDeadline(noteData.getDeadline());
		note.setLongitude(noteData.getLongitude());
		note.setLatitude(noteData.getLatitude());
		note.setDateCreated(LocalDateTime.now());
		return note;
	}
	
	@Override
	@Transactional
	public NotesPaginationData listNotes(int pageNumber, int pageSize, String sortCol, boolean sortAsc)
	{
		NotesPaginationData notesPaginationData = new NotesPaginationData();
		notesPaginationData.setCurrentPage(pageNumber);
		notesPaginationData.setPageSize(pageSize);
		notesPaginationData.setSortColumn(sortCol);
		notesPaginationData.setSortAscending(sortAsc);
		
		Page<NoteModel> notesPage = buildPage(pageNumber, pageSize, sortCol, sortAsc, new DateFilterData());
				
		notesPaginationData.setNotes(notesPage.getContent().stream().map(e -> noteMapper.map(e, NoteData.class)).collect(toList()));
		notesPaginationData.setTotalPages(notesPage.getTotalPages());
		notesPaginationData.setTotalNotes(notesPage.getTotalElements());
		notesPaginationData.setFirstPage(notesPage.isFirstPage());
		notesPaginationData.setLastPage(notesPage.isLastPage());
		
		return notesPaginationData;
	}
	
	private Page<NoteModel> buildPage(Integer pageNumber, Integer pageSize, String sortCol, boolean sortAsc, DateFilterData dateFilter) {
		Page<NoteModel> page = null;
		if (dateFilter.isActive()) {
			page = noteService.listNotesByDateFilter(pageNumber, pageSize, sortCol, sortAsc, dateFilter);
		} else {
			page = noteService.listNotes(pageNumber, pageSize, sortCol, sortAsc);
		}
		return page;
	}
	
	private String resolveDefaultSortColumn()
	{
		return userService.isCurrentUserAnonymous() ? ANONYMOUS_USER_DEFAULT_SORT_COLUMN : REGISTERED_USER_DEFAULT_SORT_COLUMN;
	}
	
	private void clearSelectedNotesIds(NotesPaginationData pagesData) {
		pagesData.setSelectedNotesIds(Collections.<Integer> emptySet());
	}
	
	@Override
	public NoteModel findNoteById(int id) {
		return noteService.findNoteById(id);
	}

	@Override
	public boolean hasCurrentUserRightsToNote(int noteId) {
		UserModel currentUser = userService.getCurrentUser();
		if (currentUser == null) {
			return false;
		}
		
		NoteModel noteToEdit = noteService.findNoteById(noteId);
		if (noteToEdit == null)	{
			return false;
		}
		
		if (!noteToEdit.getAuthor().equals(currentUser))
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean isNoteCreatedByAnonymousAuthor(int noteId) {
		return noteService.findNoteById(noteId).getAuthor().isAnonymous();
	}
	
	@Override
	public void editNote(NoteForm noteData) {
		NoteModel note = noteService.findNoteById(noteData.getId());
		if (note == null)
		{
			throw new IllegalArgumentException("Note with given id does not exist");
		}
		
		note.setTitle(noteData.getTitle());
		note.setContent(noteData.getContent());
		note.setDeadline(noteData.getDeadline());
		note.setLatitude(noteData.getLatitude());
		note.setLongitude(noteData.getLongitude());
		noteService.updateNote(note);
	}
	
	@Override
	public void deleteNote(int id) {
		deleteNotes(Sets.newHashSet(Integer.valueOf(id)));
	}
	
	@Override
	public void deleteNotes(Collection<Integer> ids) {
		noteService.deleteNotes(ids);
	}
	
	@Override
	public void deleteAllNotesForCurrentUser() {
		int currentuserId = userService.getCurrentUser().getId();
		noteService.deleteUserNotes(currentuserId);
	}
}
