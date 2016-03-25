package pl.arczynskiadam.notesmanager.web.data;

import static pl.arczynskiadam.notesmanager.web.facade.constants.FacadesConstants.Defaults.Pagination.DEFAULT_MAX_LINKED_PAGES;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NotesPaginationData {
	{
		maxLinkedPages = DEFAULT_MAX_LINKED_PAGES;
		selectedNotesIds = new HashSet<>();
		deadlineFilter = new DateFilterData();
	}
	
	private Set<Integer> selectedNotesIds;
	private DateFilterData deadlineFilter;
	private int maxLinkedPages;
	private int currentPage;
	private int pageSize;
	private int totalPages;
	private long totalNotes;
	private List<NoteData> notes;
	private String sortColumn;
	private boolean sortAscending;
	private boolean lastPage;
	private boolean firstPage;
	
	public int getFirstLinkedPage()
	{
		if (maxLinkedPages > totalPages) {
			return 0;
		}
		int begin = currentPage - maxLinkedPages / 2;
		int offset = Math.max(0, (currentPage + maxLinkedPages / 2) - (totalPages - 1));
		int firstLinkedPage = begin - offset;
		return Math.max(0, firstLinkedPage);
	}

	public int getLastLinkedPage()
	{
		if (maxLinkedPages > totalPages) {
			return totalPages - 1;
		}
		int end = currentPage + maxLinkedPages / 2;
		int offset = Math.max(0, - (currentPage - maxLinkedPages / 2));
		int lastLinkedPage = end + offset;
		return Math.min(currentPage - 1, lastLinkedPage);
	}

	public Set<Integer> getSelectedNotesIds() {
		return selectedNotesIds;
	}
	public void setSelectedNotesIds(Set<Integer> selectedNotesIds) {
		this.selectedNotesIds = selectedNotesIds;
	}
	public DateFilterData getDeadlineFilter() {
		return deadlineFilter;
	}
	public void setDeadlineFilter(DateFilterData deadlineFilter) {
		this.deadlineFilter = deadlineFilter;
	}
	public int getMaxLinkedPages() {
		return maxLinkedPages;
	}
	public void setMaxLinkedPages(int maxLinkedPages) {
		this.maxLinkedPages = maxLinkedPages;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public List<NoteData> getNotes() {
		return notes;
	}
	public void setNotes(List<NoteData> notes) {
		this.notes = notes;
	}
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public boolean isSortAscending() {
		return sortAscending;
	}
	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotalNotes() {
		return totalNotes;
	}
	public void setTotalNotes(long totalNotes) {
		this.totalNotes = totalNotes;
	}
	public boolean isLastPage() {
		return lastPage;
	}
	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}
	public boolean isFirstPage() {
		return firstPage;
	}
	public void setFirstPage(boolean firstPage) {
		this.firstPage = firstPage;
	}
}