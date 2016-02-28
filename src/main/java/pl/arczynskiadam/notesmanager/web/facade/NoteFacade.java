package pl.arczynskiadam.notesmanager.web.facade;

import java.util.Collection;

import pl.arczynskiadam.notesmanager.core.model.NoteModel;
import pl.arczynskiadam.notesmanager.web.data.DateFilterData;
import pl.arczynskiadam.notesmanager.web.data.NotesPaginationData;
import pl.arczynskiadam.notesmanager.web.form.NoteForm;

public interface NoteFacade {
	public void addNewNote(NoteForm noteData);
	public void editNote(NoteForm noteData);
	public NotesPaginationData listNotes(int pageNumber, int pageSize, String sortCol, boolean sortAsc, DateFilterData dateFilterData);
	public int getNotesCountForRegisteredUser(String userNick);
	public void deleteNote(int id);
	public void deleteNotes(Collection<Integer> ids);
	public void deleteAllNotesForCurrentUser();
	public NoteModel findNoteById(int id);
	public boolean hasCurrentUserRightsToNote(int noteId);
	public boolean isNoteCreatedByAnonymousAuthor(int noteId);
}
