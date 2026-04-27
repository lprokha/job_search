package kg.attractor.job_search.common;

import jakarta.servlet.http.HttpServletRequest;

public class UrlBuilder {
    private UrlBuilder() {
    }

    public static String getSiteUrl(HttpServletRequest request) {
        String siteUrl = request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(), "");
    }
}
