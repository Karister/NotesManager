package pl.arczynskiadam.notesmanager.web.facade;

import java.util.Collection;

import pl.arczynskiadam.notesmanager.core.model.NoteModel;
import pl.arczynskiadam.notesmanager.web.data.DateFilterData;
import pl.arczynskiadam.notesmanager.web.data.NotesPaginationData;
import pl.arczynskiadam.notesmanager.web.form.NoteForm;

public interface NoteFacade {
	public NotesPaginationData prepareNotesPaginationData();
	public NotesPaginationData updatePageNumber(int pageNumber);
	public NotesPaginationData updatePageSize(int pageSize);
	public NotesPaginationData updateSort(String sortColumn, boolean ascending);
	public NotesPaginationData updateDateFilter(DateFilterData dateFilter);
	public void addNewNote(NoteForm noteData);
	public void editNote(NoteForm noteData);
	public int getNotesCountForRegisteredUser(String userNick);
	public void deleteNote(int id);
	public void deleteNotes(Collection<Integer> ids);
	public void deleteAllNotesForCurrentUser();
	public NoteModel findNoteById(int id);
	public boolean hasCurrentUserRightsToNote(int noteId);
	public boolean isNoteCreatedByAnonymousAuthor(int noteId);
	public void removePaginationDataFromSession();
	public void clearDateFilter(String mode);
}
