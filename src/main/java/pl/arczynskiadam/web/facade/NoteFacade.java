package pl.arczynskiadam.web.facade;

import java.util.Collection;
import java.util.Set;

import pl.arczynskiadam.core.model.NoteModel;
import pl.arczynskiadam.core.model.RegisteredUserModel;
import pl.arczynskiadam.web.data.DateFilterData;
import pl.arczynskiadam.web.data.NotesPaginationData;
import pl.arczynskiadam.web.form.NoteForm;

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
	public void deleteNotes(int[] ids);
	public void deleteNotes(Collection<Integer> ids);
	public void deleteNotes(RegisteredUserModel user);
	public NoteModel findNoteById(int id);
	public boolean hasCurrentUserRightsToNote(int noteId);
	public void removePaginationDataFromSession();
	public Set<Integer> convertSelectionsToNotesIds(Collection<String> selections);
	public Set<String> convertNotesIdsToSelections(Collection<Integer> ids);
	public void clearDateFilter(String mode);
}
