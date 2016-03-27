package pl.arczynskiadam.notesmanager.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NoteListUrlFilter implements Filter {
    final String SELF_REDIRECTED = "self-redirected";

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String requestUrl = request.getRequestURI();
        if (request.getQueryString() != null) {
            requestUrl += "?" + request.getQueryString();
        }
        String referer = request.getHeader("referer");

        if (referer == null || (request).getSession().getAttribute(SELF_REDIRECTED) != null) {
            ((HttpServletRequest) req).getSession().removeAttribute(SELF_REDIRECTED);
            chain.doFilter(req, res);
        } else {
            String newUrl = requestUrl;

            for (Map.Entry<String, String> entry : getQueryParams(referer).entrySet()) {
                if (!request.getParameterMap().containsKey(entry.getKey())) {
                    newUrl = UriBuilder.fromUri(newUrl).
                            replaceQueryParam(entry.getKey(), getQueryParams(referer).get(entry.getKey()))
                            .build()
                            .toString();
                }
            }

            ((HttpServletRequest) req).getSession().setAttribute(SELF_REDIRECTED, true);
            ((HttpServletResponse) res).sendRedirect(newUrl);
        }
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

    @Override
    public void destroy() {
    }
}