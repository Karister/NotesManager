package pl.arczynskiadam.notesmanager.web.tag.navigation;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UrlPathHelper;
import pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants;

import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.ASCENDING_PARAM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.SORT_COLUMN_PARAM;
import static pl.arczynskiadam.notesmanager.web.controller.constants.GlobalControllerConstants.RequestParams.THEME_PARAM;

public class SortHeaderTag extends SimpleTagSupport {

    private static final Logger log = Logger.getLogger(SortHeaderTag.class);

    String sortColumn;
    String ascImgUrl;
    String descImgUrl;
    String divClass;
    Integer imgSize;

    public void doTag() throws JspException, IOException {

        StringWriter sw = new StringWriter();
        getJspBody().invoke(sw);

        String divOpen = String.format("<div class=\"%s\">", divClass);
        String divClose = "</div>";
        String sortAsc = getAscSortHtml();
        String sortDesc = getDescSortHtml();

        StringBuilder sb = new StringBuilder();
        sb.append(divOpen)
                .append(sw.toString())
                .append(sortAsc)
                .append(sortDesc)
                .append(divClose);

        log.debug("writting output: " + sb.toString());
        getJspContext().getOut().println(sb.toString());
    }

    private String getAscSortHtml() {
        return String.format("<a href=\"%s\">"
                + "<img src=\"%s\" width=\"%d\" height=\"%d\"/>"
                + "</a>", getAscSortUrl(), ascImgUrl, imgSize, imgSize);
    }

    private String getDescSortHtml() {
        return String.format("<a href=\"%s\">"
                + "<img src=\"%s\" width=\"%d\" height=\"%d\"/>"
                + "</a>", getDescSortUrl(), descImgUrl, imgSize, imgSize);
    }

    private String getAscSortUrl() {
        return getSortUrl(true);
    }

    private String getDescSortUrl() {
        return getSortUrl(false);
    }

    private String getSortUrl(boolean asc) {
        String requestUrl = getCurrentUrl();
        return UriBuilder
                .fromUri(requestUrl)
                .replaceQueryParam(SORT_COLUMN_PARAM, sortColumn)
                .replaceQueryParam(ASCENDING_PARAM, asc)
                .replaceQueryParam(THEME_PARAM)
                .build()
                .toString();
    }

    private String getCurrentUrl() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getContextPath()
                + new UrlPathHelper().getOriginatingServletPath(request)
                + "?"
                + request.getQueryString();
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getAscImgUrl() {
        return ascImgUrl;
    }

    public void setAscImgUrl(String ascImgUrl) {
        this.ascImgUrl = ascImgUrl;
    }

    public String getDescImgUrl() {
        return descImgUrl;
    }

    public void setDescImgUrl(String descImgUrl) {
        this.descImgUrl = descImgUrl;
    }

    public Integer getImgSize() {
        return imgSize;
    }

    public void setImgSize(Integer imgSize) {
        this.imgSize = imgSize;
    }

    public String getDivClass() {
        return divClass;
    }

    public void setDivClass(String divClass) {
        this.divClass = divClass;
    }
}