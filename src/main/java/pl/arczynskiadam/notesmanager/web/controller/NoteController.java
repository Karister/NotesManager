package pl.arczynskiadam.notesmanager.web.controller;

import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.Misc.HASH;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.ModelAttrKeys.Form.SELECTED_CHECKBOXES_FORM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.ASCENDING_PARAM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.PAGE_NUMBER_PARAM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.PAGE_SIZE_PARAM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.SORT_COLUMN_PARAM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.NoteControllerConstants.ModelAttrKeys.Form.DATE_FILTER_FORM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.NoteControllerConstants.ModelAttrKeys.View.PAGINATION;
import static pl.arczynskiadam.notesmanager.web.controller.constants.NoteControllerConstants.URLs.SHOW_NOTES;
import static pl.arczynskiadam.notesmanager.web.controller.constants.NoteControllerConstants.URLs.SHOW_NOTES_FULL;
import static pl.arczynskiadam.notesmanager.web.facade.constants.FacadesConstants.Defaults.Pagination.ANONYMOUS_USER_DEFAULT_SORT_COLUMN;
import static pl.arczynskiadam.notesmanager.web.facade.constants.FacadesConstants.Defaults.Pagination.DEFAULT_ENTRIES_PER_PAGE;
import static pl.arczynskiadam.notesmanager.web.facade.constants.FacadesConstants.Defaults.Pagination.DEFAULT_FIRST_PAGE;
import static pl.arczynskiadam.notesmanager.web.facade.constants.FacadesConstants.Defaults.Pagination.REGISTERED_USER_DEFAULT_SORT_COLUMN;
import static pl.arczynskiadam.notesmanager.web.controller.constants.NoteControllerConstants.Pages.*;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.arczynskiadam.notesmanager.utils.collections.Utils;
import pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants;
import pl.arczynskiadam.notesmanager.web.controller.constants.NoteControllerConstants;
import pl.arczynskiadam.notesmanager.web.data.DateFilterData;
import pl.arczynskiadam.notesmanager.web.data.NotesPaginationData;
import pl.arczynskiadam.notesmanager.web.facade.NoteFacade;
import pl.arczynskiadam.notesmanager.web.facade.UserFacade;
import pl.arczynskiadam.notesmanager.web.form.DateFilterForm;
import pl.arczynskiadam.notesmanager.web.form.SelectedCheckboxesForm;
import pl.arczynskiadam.notesmanager.web.form.validation.DateFilterValidator;
import pl.arczynskiadam.notesmanager.web.form.validation.SelectedCheckboxesValidator;
import pl.arczynskiadam.notesmanager.web.messages.GlobalMessages;
import pl.arczynskiadam.notesmanager.web.tag.navigation.BreadcrumbsItem;

@Controller
@RequestMapping(value = NoteControllerConstants.URLs.MANAGER)
public class NoteController extends AbstractController {
	
	private static final Logger log = Logger.getLogger(NoteController.class);
	
	@Autowired(required = true)
	private NoteFacade noteFacade;
	
	@Autowired(required = true)
	private UserFacade userFacade;
	
	@Resource(name="selectedNotesValidator")
    private SelectedCheckboxesValidator selectedCheckboxesValidator;
	
	@Autowired(required=true)
	DateFilterValidator dateFilterValidator;
	
	@Resource(name="notesPageSizes")
	List<Integer> notesPageSizes;
	
	@InitBinder(SELECTED_CHECKBOXES_FORM) //argument = command/modelattr name
		public void initSelectedCheckboxesValidator(WebDataBinder binder) {
		binder.addValidators(selectedCheckboxesValidator);
	}
	
	@InitBinder(DATE_FILTER_FORM) //argument = command/modelattr name
	public void initDateFilterValidator(WebDataBinder binder) {
		binder.addValidators(dateFilterValidator);
	}
	
//	@RequestMapping(value = SHOW_NOTES, method = RequestMethod.GET, params = {
//			"!"+CLEAR_DATE_FILTER_PARAM, "!"+DATE_FILTER_FROM, "!"+DATE_FILTER_TO,
//			"!"+PAGE_NUMBER_PARAM, "!"+PAGE_SIZE_PARAM, "!"+SORT_COLUMN_PARAM, "!"+ASCENDING_PARAM,})
//	public String listNotes(HttpServletRequest request,	final Model model) {
//		
//		NotesPaginationData paginationData = noteFacade.prepareNotesPaginationData();
//		model.addAttribute(NoteControllerConstants.ModelAttrKeys.View.PAGINATION, paginationData);
//		
//		preparePage(paginationData, model);
//		populateModelWithEntriesPerPage(model);
//		displayInfoIfNoNotesFound(model, paginationData);
//		
//		return NoteControllerConstants.Pages.NOTES_LISTING_PAGE;
//	}
	
	@RequestMapping(value = SHOW_NOTES, method = RequestMethod.GET)
	public String listNotes(
			@RequestParam(PAGE_NUMBER_PARAM) Optional<Integer> pageNumber,
			@RequestParam(PAGE_SIZE_PARAM) Optional<Integer> pageSize,
			@RequestParam(SORT_COLUMN_PARAM) Optional<String> sortCol,
			@RequestParam(ASCENDING_PARAM) Optional<Boolean> sortAsc,
			HttpServletRequest request,	final Model model) {
		
		NotesPaginationData paginationData = buildPaginationData(pageNumber, pageSize, sortCol, sortAsc);
		preparePage(paginationData, model);
		
		return NOTES_LISTING_PAGE;
	}

	@RequestMapping(value = SHOW_NOTES, method = RequestMethod.GET, params = {DATE_FILTER_FROM, DATE_FILTER_TO})
	public String listNotesByDate(
			@RequestParam(PAGE_NUMBER_PARAM) Optional<Integer> pageNumber,
			@RequestParam(PAGE_SIZE_PARAM) Optional<Integer> pageSize,
			@RequestParam(SORT_COLUMN_PARAM) Optional<String> sortCol,
			@RequestParam(ASCENDING_PARAM) Optional<Boolean> sortAsc,
			@ModelAttribute(SELECTED_CHECKBOXES_FORM) SelectedCheckboxesForm selectedCheckboxesForm,
			@Valid @ModelAttribute(DATE_FILTER_FORM) DateFilterForm dateFilterForm,
			BindingResult result,
			HttpServletRequest request,
			final Model model) {
		
		NotesPaginationData paginationData = buildPaginationData(pageNumber, pageSize, sortCol, sortAsc);
			
		if (result.hasErrors()) {
			paginationData = buildPaginationData(pageNumber, pageSize, sortCol, sortAsc);
			
			Set<String> selections = Utils.mapIntSetToStringSet(paginationData.getSelectedNotesIds());
			selectedCheckboxesForm.setSelections(selections);
			
			for (ObjectError e : result.getAllErrors()) {
				if (ArrayUtils.contains(e.getCodes(), "DateFilter.dates.switched")) {
					GlobalMessages.addErrorMessage("DateFilter.dates.switched", model);
				}
				if (ArrayUtils.contains(e.getCodes(), "DateFilter.dates.empty")) {
					GlobalMessages.addErrorMessage("DateFilter.dates.empty", model);
				}
			}
		} 
		
		preparePage(paginationData, model);
		
		return NOTES_LISTING_PAGE;
	}

	private NotesPaginationData buildPaginationData(Optional<Integer> pageNumber, Optional<Integer> pageSize,
			Optional<String> sortCol, Optional<Boolean> sortAsc) {
		
		NotesPaginationData paginationData = noteFacade.listNotes(
				pageNumber.map(Function.identity()).orElse(DEFAULT_FIRST_PAGE),
				pageSize.map(Function.identity()).orElse(DEFAULT_ENTRIES_PER_PAGE),
				sortCol.map(Function.identity()).orElse(resolveSortColumn()),
				sortAsc.map(Function.identity()).orElse(Boolean.TRUE));
		
		return paginationData;
	}
	
	private void preparePage(NotesPaginationData paginationData, Model model)
	{
		populateModelWithPageData(paginationData, model);
		displayInfoIfNoNotesFound(model, paginationData);
		populateModelWithEntriesPerPage(model);
		
		Set<String> selections = Utils.mapIntSetToStringSet(paginationData.getSelectedNotesIds());
		SelectedCheckboxesForm selectedCheckboxesForm = new SelectedCheckboxesForm();
		selectedCheckboxesForm.setSelections(selections);
		model.addAttribute(SELECTED_CHECKBOXES_FORM, selectedCheckboxesForm);
		
		DateFilterForm dateFilterForm = new DateFilterForm();
		dateFilterForm.setFrom(paginationData.getDeadlineFilter().getFrom());
		dateFilterForm.setTo(paginationData.getDeadlineFilter().getTo());
		model.addAttribute(DATE_FILTER_FORM, dateFilterForm);
	}
	
	private String resolveSortColumn() {
		return userFacade.isCurrentUserAnonymous() ? ANONYMOUS_USER_DEFAULT_SORT_COLUMN : REGISTERED_USER_DEFAULT_SORT_COLUMN;
	}
	
	private void displayInfoIfNoNotesFound(final Model model, NotesPaginationData pagination) {
		if(pagination.getNotes().size() == 0) {
			GlobalMessages.addWarningMessage("notes.listing.msg.noResults", model);
		}
	}
	
//	@RequestMapping(value = SHOW_NOTES, method = RequestMethod.GET, params = {CLEAR_DATE_FILTER_PARAM})
//	public String clearDateFilter(@RequestParam(CLEAR_DATE_FILTER_PARAM) String mode,
//			Model model, HttpServletRequest request) {
//		
//		noteFacade.clearDateFilter(mode);
//		noteFacade.updatePageNumber(DEFAULT_FIRST_PAGE);
//		
//		return listNotes(request, model);
//	}
//	
//	@RequestMapping(value = ADD_NOTE, method = RequestMethod.GET)
//	public String showNewNotePage(@ModelAttribute(NOTE_FORM) NoteForm note,
//			final Model model, final HttpServletRequest request) {
//		
//		createAddNotePageBreadcrumbs(model);
//		
//		return NoteControllerConstants.Pages.NEW_NOTE_PAGE;
//	}
//	
//	@RequestMapping(value = ADD_NOTE, method = RequestMethod.POST)
//	public String saveNewNote(@Validated(Default.class) @ModelAttribute(NOTE_FORM) NoteForm noteForm,
//			BindingResult bindinfgResult,
//			Model model,
//			RedirectAttributes attrs) {
//		
//		if (bindinfgResult.hasErrors()) {
//			createAddNotePageBreadcrumbs(model);
//			
//			GlobalMessages.addErrorMessage("global.error.correctAll", model);
//			
//			return NoteControllerConstants.Pages.NEW_NOTE_PAGE;
//		}
//		
//		noteFacade.addNewNote(noteForm);
//		GlobalMessages.addInfoFlashMessage("notes.addNew.msg.confirmation", attrs);
//		
//		return REDIRECT_PREFIX + SHOW_NOTES_FULL;
//	}
//
//	@RequestMapping(value = EDIT_NOTE, method = RequestMethod.GET)
//	public String showEditNotePage(@PathVariable("noteId") Integer noteId, @ModelAttribute(NOTE_FORM) NoteForm noteForm,
//			Model model, RedirectAttributes attrs)
//	{
//		if (!noteFacade.hasCurrentUserRightsToNote(noteId)) {
//			GlobalMessages.addErrorFlashMessage("global.error.permission" , attrs);
//			return REDIRECT_PREFIX + SHOW_NOTES_FULL;
//		}
//		
//		createEditNotePageBreadcrumbs(model);
//		prepopulateNoteForm(noteId, noteForm);
//		
//		return EDIT_NOTE_PAGE;
//	}
//	
//	private void prepopulateNoteForm(Integer noteId, NoteForm noteForm) {
//		NoteModel note = noteFacade.findNoteById(noteId);
//		noteForm.setId(noteId);
//		noteForm.setAuthor(note.getAuthor().getNick());
//		noteForm.setTitle(note.getTitle());
//		noteForm.setContent(note.getContent());
//		noteForm.setDeadline(LocalDate.from(note.getDeadline()));
//		noteForm.setLatitude(note.getLatitude());
//		noteForm.setLongitude(note.getLongitude());
//	}
//	
//	@RequestMapping(value = EDIT_NOTE, method = RequestMethod.POST)
//	public String updateNote(@ModelAttribute(NOTE_FORM) @Valid NoteForm noteForm,
//			BindingResult bindinfgResult, Model model, RedirectAttributes attrs)
//	{
//		if (bindinfgResult.hasErrors()) {
//			createEditNotePageBreadcrumbs(model);
//			
//			GlobalMessages.addErrorMessage("global.error.correctAll", model);
//			
//			return NoteControllerConstants.Pages.EDIT_NOTE_PAGE;
//		}
//		
//		noteFacade.editNote(noteForm);
//		GlobalMessages.addInfoFlashMessage("notes.edit.msg.confirmation", attrs);
//		
//		return REDIRECT_PREFIX + SHOW_NOTES_FULL;
//	}
//
//	@RequestMapping(value = DELETE_NOTE, method = RequestMethod.GET)
//	public String deleteNote(@PathVariable("noteId") Integer noteId, RedirectAttributes attrs) {
//	
//		if (!noteFacade.hasCurrentUserRightsToNote(noteId)) {
//			GlobalMessages.addErrorFlashMessage("global.error.permission" , attrs);
//			return REDIRECT_PREFIX + SHOW_NOTES_FULL;
//		}
//		
//		noteFacade.deleteNote(noteId);
//		GlobalMessages.addInfoFlashMessage("notes.delete.single.msg.confirmation" , attrs);
//		
//		return REDIRECT_PREFIX + SHOW_NOTES_FULL;
//	}
//	
//	@RequestMapping(value = SHOW_NOTES, method = RequestMethod.POST, params = {DELETE_PARAM})
//	public String deleteNotes(@RequestParam(value = GlobalControllerConstants.RequestParams.DELETE_PARAM) String delete,
//			@ModelAttribute(DATE_FILTER_FORM) DateFilterForm dateFilterForm,
//			@Valid @ModelAttribute(SELECTED_CHECKBOXES_FORM) SelectedCheckboxesForm selectedCheckboxesForm,	
//			BindingResult result,
//			Model model,
//			RedirectAttributes attrs) {
//		
//		String deletedNotesCount = null;
//		
//		if ("all".equals(delete)) {
//			deletedNotesCount = Integer.toString(noteFacade.getNotesCountForRegisteredUser(userFacade.getCurrentUser().getNick())); 
//			noteFacade.deleteAllNotesForCurrentUser();
//		} else if ("selected".equals(delete)) {
//			if (result.hasErrors()) {
//				NotesPaginationData pagination = noteFacade.prepareNotesPaginationData();
//				model.addAttribute(PAGINATION, pagination);
//				dateFilterForm.setFrom(pagination.getDeadlineFilter().getFrom());
//				dateFilterForm.setTo(pagination.getDeadlineFilter().getTo());
//				GlobalMessages.addErrorMessage("notes.delete.msg.nothingSelected", model);
//				populateModelWithEntriesPerPage(model);
//				return NOTES_LISTING_PAGE;
//			}
//			deletedNotesCount = Integer.toString(selectedCheckboxesForm.getSelections().size());
//			
//			Set<Integer> ids = Utils.mapStringSetToIntSet(selectedCheckboxesForm.getSelections());
//			noteFacade.deleteNotes(ids);
//		}
//		
//		GlobalMessages.addInfoFlashMessage("notes.delete.msg.confirmation", Collections.singletonList(deletedNotesCount), attrs);
//		
//		return REDIRECT_PREFIX + SHOW_NOTES_FULL;
//	}
//	
//	@RequestMapping(value = NoteControllerConstants.URLs.NOTE_DETAILS, method = RequestMethod.GET)
//	public String noteDetails(@PathVariable("noteId") Integer noteId, final Model model, RedirectAttributes attrs) {
//		
//		if (userFacade.isCurrentUserAnonymous() && !noteFacade.isNoteCreatedByAnonymousAuthor(noteId)) {
//			GlobalMessages.addErrorFlashMessage("global.edit.note.error" , attrs);
//			return REDIRECT_PREFIX + SHOW_NOTES_FULL;
//		}
//		
//		createViewNotePageBreadcrumbs(model);
//		
//		model.addAttribute(NOTE, noteFacade.findNoteById(noteId));
//		
//		return NOTE_DETAILS_PAGE;
//	}
//	
//	@ResponseStatus(HttpStatus.OK)
//	@RequestMapping(value = "/updateSelections.json", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public void noteSelected(@RequestBody SelectedCheckboxesForm selectedCheckboxesForm) {
//		log.debug("checboxes vals to update from ajax: " + selectedCheckboxesForm.toString());
//		
//		NotesPaginationData sessionPagesData = noteFacade.prepareNotesPaginationData();
//		Set<Integer> ids = Utils.mapStringSetToIntSet(selectedCheckboxesForm.getSelections());
//		sessionPagesData.setSelectedNotesIds(ids);
//	}
	
	private void populateModelWithEntriesPerPage(Model model) {
		model.addAttribute(NoteControllerConstants.ModelAttrKeys.View.PAGE_SIZES, notesPageSizes);
	}
	
	private void populateModelWithPageData(NotesPaginationData paginationData, Model model) {
		model.addAttribute(PAGINATION, paginationData);
	}
	
	private void createAddNotePageBreadcrumbs(Model model) {
		createBreadcrumpAndSaveToModel(model,
				new BreadcrumbsItem(getMessage("breadcrumbs.home"), SHOW_NOTES_FULL),
				new BreadcrumbsItem(getMessage("breadcrumbs.newNote"), HASH));
	}
	
	private void createEditNotePageBreadcrumbs(Model model) {
		createBreadcrumpAndSaveToModel(model,
				new BreadcrumbsItem(getMessage("breadcrumbs.home"), SHOW_NOTES_FULL),
				new BreadcrumbsItem(getMessage("breadcrumbs.editNote"), GlobalControllerConstants.Misc.HASH));
	}
	
	private void createViewNotePageBreadcrumbs(Model model) {
		createBreadcrumpAndSaveToModel(model,
				new BreadcrumbsItem(getMessage("breadcrumbs.home"), SHOW_NOTES_FULL),
				new BreadcrumbsItem(getMessage("breadcrumbs.noteDetails"), GlobalControllerConstants.Misc.HASH));
	}	
}