package pl.arczynskiadam.core.service.impl;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.arczynskiadam.core.dao.NotePredicates;
import pl.arczynskiadam.core.dao.NoteRepository;
import pl.arczynskiadam.core.model.NoteVO;
import pl.arczynskiadam.core.service.NoteService;
import pl.arczynskiadam.core.service.SessionService;
import pl.arczynskiadam.web.controller.NoteController;
import pl.arczynskiadam.web.controller.constants.NoteControllerConstants;
import pl.arczynskiadam.web.data.NotesPagesData;

@Service
public class DefaultNoteService implements NoteService {

	private static final Logger log = Logger.getLogger(DefaultNoteService.class);
	
	@Resource
	private NoteRepository noteDAO;
	
	@Autowired(required = true)
	private SessionService sessionService;
	
	@Override
	@Transactional
	public void addNote(NoteVO note) {
		noteDAO.save(note);
	}

	@Override
	@Transactional
	public Page<NoteVO> listNotes(int pageId, int pageSize, String sortCol, boolean asc) {
		return noteDAO.findAll(constructPageSpecification(pageId, pageSize, sortCol, asc));
	}
	
	@Override
	@Transactional
	public Page<NoteVO> listNotesFromDate(int pageId, int pageSize, String sortCol, boolean asc, Date date) {
		return noteDAO.findAll(NotePredicates.from(date), constructPageSpecification(pageId, pageSize, sortCol, asc));
	}

	@Override
	@Transactional
	public void deleteNote(int id) {
		noteDAO.delete(id);
	}
	
	@Override
	@Transactional
	public void deleteNotes(Set<Integer> ids) {
		noteDAO.deleteByIds(ids);
		
		NotesPagesData sessionPagination = retrievePagesDataFromSession();
		Page<NoteVO> sessionPage = sessionPagination.getPage();
		boolean deletedAllResultsFromLastPage = sessionPage.isLastPage() && ids.size() == sessionPage.getNumberOfElements();
		
		log.debug("page number = " + sessionPage.getNumber() + " / " + sessionPage.getTotalPages());
		
		if (deletedAllResultsFromLastPage) {
			Page<NoteVO> page = null;
			int pageIndexOffset = sessionPage.isFirstPage() ? 0 : 1;
			
			if (sessionPagination.getFromDate() == null) {
				page = listNotes(sessionPage.getNumber() - pageIndexOffset,
						sessionPage.getSize(),
						sessionPagination.getSortCol(),
						sessionPagination.isSortAscending());
			} else {
				page = listNotesFromDate(sessionPage.getNumber() - pageIndexOffset,
						sessionPage.getSize(),
						sessionPagination.getSortCol(),
						sessionPagination.isSortAscending(),
						sessionPagination.getFromDate());
			}
			sessionPagination.setPage(page);
			savePagesDataToSession(sessionPagination);
		}
	}
	
	@Override
	@Transactional
	public NoteVO findNoteById(int id) {
		return noteDAO.findOne(id);
	}
	
	public void setNoteDAO(NoteRepository noteDAO) {
		this.noteDAO = noteDAO;
	}
	
	@Override
	public void clearFromDateFilter() {
		retrievePagesDataFromSession().setFromDate(null);
	}
	
	@Override
	public void removePaginationDataFromSession() {
		sessionService.getCurrentSession().removeAttribute(NoteControllerConstants.ModelAttrKeys.View.Pagination);
	}
	
	@Override
	public NotesPagesData retrievePagesDataFromSession() {
		NotesPagesData sessionsPagesData = (NotesPagesData) sessionService.getCurrentSession().getAttribute(NoteControllerConstants.ModelAttrKeys.View.Pagination);
		if (sessionsPagesData == null) {
			return null;
		}
		return sessionsPagesData;
	}
	
	@Override
	public void savePagesDataToSession(NotesPagesData pagesData) {
		sessionService.getCurrentSession().setAttribute(NoteControllerConstants.ModelAttrKeys.View.Pagination, pagesData);
	}
	
	private Pageable constructPageSpecification(int pageIndex, int pageSize, String sortCol, boolean asc) {
		Sort sort = new Sort(asc ? Sort.Direction.ASC : Sort.Direction.DESC, sortCol);
        Pageable pageSpecification = new PageRequest(pageIndex, pageSize, sort);
        return pageSpecification;
    }
}
