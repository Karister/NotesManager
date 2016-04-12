package pl.arczynskiadam.notesmanager.web.filter;

import com.google.common.collect.Lists;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.*;
import static pl.arczynskiadam.notesmanager.web.controller.constants.NoteControllerConstants.RequestParams.DEADLINE_PARAM;

public class NoteListUrlFilter implements Filter {
    private final static String SELF_REDIRECTED = "self-redirected";

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String requestUrl = request.getRequestURI();
        if (request.getQueryString() != null) {
            requestUrl += "?" + request.getQueryString();
        }

        if (shouldModifyUrl(request)) {
            ((HttpServletRequest) req).getSession().removeAttribute(SELF_REDIRECTED);
            chain.doFilter(req, res);
        } else {
            String newUrl = buildUrl(request, requestUrl);

            request.getSession().setAttribute(SELF_REDIRECTED, true);
            response.sendRedirect(newUrl);
        }
    }

    private boolean shouldModifyUrl(HttpServletRequest request) {
        String referrer = getReferrer(request);
        return referrer == null || (request).getSession().getAttribute(SELF_REDIRECTED) != null;
    }

    private String buildUrl(HttpServletRequest request, String requestUrl) {
        String referrer = getReferrer(request);
        String newUrl = requestUrl;

        Map<String, String> queryParams = getQueryParams(referrer);
        queryParams = filterSearchParams(queryParams);

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (!request.getParameterMap().containsKey(entry.getKey())) {
                newUrl = UriBuilder.fromUri(newUrl).
                        replaceQueryParam(entry.getKey(), queryParams.get(entry.getKey()))
                        .build()
                        .toString();
            }
        }
        return newUrl;
    }

    private Map<String, String> getQueryParams(String url) {
        if (!url.contains("?")) {
            return Collections.EMPTY_MAP;
        }
        String queryString = url.split("\\?")[1];
        Map<String, String> params = new HashMap<>();
        for (String paramKeyValue : queryString.split("&")) {
            params.put(paramKeyValue.split("=")[0], paramKeyValue.split("=")[1]);
        }
        return params;
    }

    private Map<String, String> filterSearchParams(Map<String, String> map) {
        List<String> expectedParams = Lists.newArrayList(PAGE_SIZE_PARAM, PAGE_NUMBER_PARAM, SORT_COLUMN_PARAM, ASCENDING_PARAM, DEADLINE_PARAM);
        return map.entrySet().stream().filter(e -> expectedParams.contains(e.getKey())).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
    }

    private String getReferrer(HttpServletRequest request) {
        return request.getHeader("referer");
    }

    @Override
    public void destroy() {
    }
}