package qcom.itlegal.ipit.framework.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import qcom.itlegal.ipit.framework.command.WebServiceCommand;
import qcom.itlegal.ipit.framework.data.Credentials;
import qcom.itlegal.ipit.framework.processors.rest.WebServiceProcessor;

@Component
public class WebServiceController extends WebServiceProcessor {

    @Value("${web.services.url}")
    private String url;

    @Value("${web.services.user}")
    private String user;

    @Value("${web.services.password}")
    private String password;

    /**
     * Web service execution based on web service URL defined in automation.properties
     *
     * @param credentials   user credentials
     * @param command       web service command
     * @param pathParameter web service path parameter(s)
     * @param parameterMap  web service parameter map
     * @return web service response
     */
    public Object webServiceAction(final Credentials credentials, final WebServiceCommand command, final Object pathParameter, final Object parameterMap) {
        if (credentials != null) {
            if (pathParameter == null && parameterMap == null) {
                return execute(url, credentials.getUsername(), credentials.getPassword(), command);
            } else if (parameterMap == null && pathParameter != null) {
                return execute(url, credentials.getUsername(), credentials.getPassword(), command, pathParameter);
            } else {
                return execute(url, credentials.getUsername(), credentials.getPassword(), command, pathParameter, parameterMap);
            }
        } else {
            if (pathParameter == null && parameterMap == null) {
                return execute(url, user, password, command);
            } else if (parameterMap == null && pathParameter != null) {
                return execute(url, user, password, command, pathParameter);
            } else {
                return execute(url, user, password, command, pathParameter, parameterMap);
            }
        }
    }

    /**
     * Web service execution based on web service URL defined in automation.properties.  Typically, accessing a
     * direct url
     *
     * @param credentials user credentials
     * @param url         web service url segment
     * @return web service response
     */
    public Object webServiceAction(final Credentials credentials, final String url) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(StringUtils.appendIfMissing(this.url, "/", "/"));
        urlBuilder.append(StringUtils.removeStart(url, "/"));
        if (credentials != null) {
            return execute(urlBuilder.toString(), credentials.getUsername(), credentials.getPassword());
        } else {
            return execute(urlBuilder.toString(), user, password);
        }
    }

    /**
     * Http execution based on a direct url
     *
     * @param credentials user credentials
     * @param url         user defined url
     * @return web service response
     */
    public Object httpServiceAction(final Credentials credentials, String url) {

        if (!StringUtils.containsIgnoreCase(url, "http")) {
            StringBuilder urlBuilder = new StringBuilder();
            urlBuilder.append(this.url);
            urlBuilder.append(url);
            url = urlBuilder.toString();
        }

        if (credentials != null) {
            return execute(url, credentials.getUsername(), credentials.getPassword());
        } else {
            return execute(url, user, password);
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
