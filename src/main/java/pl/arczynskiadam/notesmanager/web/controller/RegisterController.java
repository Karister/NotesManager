package pl.arczynskiadam.notesmanager.web.controller;

import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.Misc.HASH;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.Prefixes.REDIRECT_PREFIX;
import static pl.arczynskiadam.notesmanager.web.controller.constants.NoteControllerConstants.URLs.SHOW_NOTES_FULL;
import static pl.arczynskiadam.notesmanager.web.controller.constants.RegisterControllerConstants.ModelAttrKeys.Form.REGISTER_FORM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.RegisterControllerConstants.Pages.REGISTER_PAGE;
import static pl.arczynskiadam.notesmanager.web.controller.constants.RegisterControllerConstants.URLs.REGISTER;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pl.arczynskiadam.notesmanager.web.controller.constants.RegisterControllerConstants;
import pl.arczynskiadam.notesmanager.web.facade.UserFacade;
import pl.arczynskiadam.notesmanager.web.form.RegisterForm;
import pl.arczynskiadam.notesmanager.web.messages.GlobalMessages;
import pl.arczynskiadam.notesmanager.web.tag.navigation.BreadcrumbsItem;

@Controller
public class RegisterController extends AbstractController {

	@Resource
	private UserFacade userFacade;
	
	@Resource(name = "authenticationManager")
	private AuthenticationManager authMgr;
	
	@Resource(name = "userDetailsService")
	private UserDetailsService userDetailsService;
	
	private static final Logger log = Logger.getLogger(RegisterController.class);
	
	@RequestMapping(value = RegisterControllerConstants.URLs.REGISTER, method = RequestMethod.GET)
	public String register(Model model) {
		if (isUserLoggedIn()) {
			return REDIRECT_PREFIX + SHOW_NOTES_FULL;
		}
		
		model.addAttribute(RegisterControllerConstants.ModelAttrKeys.Form.REGISTER_FORM, new RegisterForm());
		addDefaultBreadcrumbsToModel(model);
		
		return REGISTER_PAGE;
	}
	
	@RequestMapping(value = REGISTER, method = RequestMethod.POST)
	public String performRegister(@Valid @ModelAttribute(REGISTER_FORM) RegisterForm form,
			BindingResult result,
			Model model,
			RedirectAttributes attrs) {
		
		if (result.hasErrors()) {
			addDefaultBreadcrumbsToModel(model);
			GlobalMessages.addErrorMessage("global.error.correctAll", model);
			return RegisterControllerConstants.Pages.REGISTER_PAGE;
		}
		
		userFacade.registerUser(form.getNick(), form.getEmail(), form.getPassword());
		GlobalMessages.addInfoFlashMessage("global.register.success", attrs);
		
		autoLogin(form, model, attrs);
		
		return REDIRECT_PREFIX + SHOW_NOTES_FULL;
	}

	private void autoLogin(RegisterForm form, Model model, RedirectAttributes attrs) {
		try {
			UserDetails userDetails = userDetailsService.loadUserByUsername(form.getNick());
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
					userDetails, form.getPassword(),
					userDetails.getAuthorities());
			
			authMgr.authenticate(auth);

			if (auth.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(auth);
				GlobalMessages.addInfoFlashMessage("global.login.auto", attrs);
			}
		} catch (Exception e) {
			log.error("Autologin failed");
		}
	}
	
	private void addDefaultBreadcrumbsToModel(Model model) {
		createBreadcrumpAndSaveToModel(model,
				new BreadcrumbsItem(getMessage("breadcrumbs.home"), SHOW_NOTES_FULL),
				new BreadcrumbsItem(getMessage("breadcrumbs.register"), HASH));
	}
}
