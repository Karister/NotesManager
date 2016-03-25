package pl.arczynskiadam.notesmanager.core.service;

import java.util.Collection;

import org.springframework.data.domain.Page;

import pl.arczynskiadam.notesmanager.core.model.NoteModel;
import pl.arczynskiadam.notesmanager.web.data.DateFilterData;
import pl.arczynskiadam.notesmanager.web.data.NotesPaginationData;

public interface NoteService {
	
	public void addNoteForRegisteredUser(NoteModel note, String userNick);
	public void addNoteForAnonymousUser(NoteModel note, String userNick);
	public void updateNote(NoteModel note);
	public Page<NoteModel> listNotes(int pageId, int pageSize, String sortCol, boolean asc);
	public Page<NoteModel> listNotesByDateFilter(int pageId, int pageSize, String sortCol, boolean asc, DateFilterData dateFilter);
	public void deleteNotes(Collection<Integer> ids);
	public void deleteUserNotes(int userId);
	public NoteModel findNoteById(int id);
	public int getNotesCountForRegisteredUser(String userNick);
}
