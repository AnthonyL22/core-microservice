package com.pwc.core.framework.util;

import com.pwc.core.framework.FrameworkConstants;
import com.pwc.core.framework.controller.WebServiceController;
import com.pwc.core.framework.data.CommonField;
import com.pwc.core.framework.processors.rest.WebServiceProcessor;
import com.pwc.core.framework.type.GetNadaEmail;
import com.pwc.core.framework.type.MailSacEmail;
import io.restassured.path.json.JsonPath;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static com.pwc.core.framework.util.JsonUtils.convertMapToObject;
import static com.pwc.core.framework.util.WebElementUtils.pauseForMilliseconds;
import static com.pwc.logging.service.LoggerService.LOG;

public class EmailUtils {

    private static final String MAILSAC_URL = "https://mailsac.com/api/";
    private static final String GETNADA_URL = "https://getnada.com/api/v1/";
    private static WebServiceProcessor webServiceProcessorInstance = null;

    protected EmailUtils() {
        webServiceProcessorInstance = new WebServiceController();
    }

    /**
     * Get all emails for a particular email address.
     *
     * @param emailAddress email address
     * @return list of all emails for an email address
     */
    public static List<MailSacEmail> getAllMailSacEmailsByAddress(final String emailAddress) {

        List<MailSacEmail> allEmails = new ArrayList<>();
        try {
            JsonPath response = (JsonPath) executeGet(MAILSAC_URL, String.format("%s/%s/%s", CommonField.ADDRESSES.getField(), emailAddress, CommonField.MESSAGES.getField()));
            JsonPath entity = new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
            List<HashMap> inboxList = entity.get();
            inboxList.forEach(email -> {
                MailSacEmail emailResponse = (MailSacEmail) convertMapToObject(email, MailSacEmail.class);
                allEmails.add(emailResponse);
            });
            LOG(true, "Found %s emails for email address='%s'", inboxList.size(), emailAddress);
        } catch (Exception e) {
            LOG(true, "Unable to retrieve emails due to %s", e);
        }
        return allEmails;
    }

    /**
     * Get first N number of emails for a particular email address.
     *
     * @param numberOfEmails email quantity
     * @param emailAddress   email address
     * @return list of all emails for an email address
     */
    public static List<GetNadaEmail> getGetNadaEmailsByAddress(final int numberOfEmails, final String emailAddress) {

        List<GetNadaEmail> allEmails = new ArrayList<>();
        try {
            JsonPath response = (JsonPath) executeGet(GETNADA_URL, String.format("%s/%s", CommonField.INBOXES.getField(), emailAddress));
            JsonPath entity = new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
            List<HashMap> messageList = entity.get(CommonField.MSGS.getField());
            if (CollectionUtils.isNotEmpty(messageList)) {
                IntStream.range(0, numberOfEmails).forEach(index -> {
                    GetNadaEmail emailResponse = (GetNadaEmail) convertMapToObject(messageList.get(index), GetNadaEmail.class);
                    allEmails.add(emailResponse);
                });
            }
            LOG(true, "Found %s emails for email address='%s'", messageList.size(), emailAddress);
        } catch (Exception e) {
            LOG(true, "Unable to retrieve emails due to %s", e);
        }
        return allEmails;
    }

    /**
     * Get fully hydrated email found by UID from GetNada.
     *
     * @param email GetNada base email
     * @return fully hydrated email
     */
    public static GetNadaEmail getAllGetNadaEmailsById(GetNadaEmail email) {

        JsonPath response = (JsonPath) executeGet(GETNADA_URL, String.format("%s/%s", CommonField.MESSAGES.getField(), email.getUid()));
        JsonPath entity = new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
        email.setHtml(entity.get(CommonField.HTML.getField()));
        return email;
    }

    /**
     * Get an email message by finding a matching SUBJECT within 2 minutes from MailSac api.
     *
     * @param emailAddress    email address to get inbox from
     * @param expectedSubject expected subject
     * @return first email msg that matches the expected subject text
     */
    public static MailSacEmail getMailSacEmailMessageBySubject(final String emailAddress, final String expectedSubject) {

        int retryCount = 1;
        MailSacEmail email = null;
        int maxAmountOfAttempts = 120;

        while (null == email && maxAmountOfAttempts > 0) {
            List<MailSacEmail> emails = getAllMailSacEmailsByAddress(emailAddress);
            if (emails.size() > 0) {
                for (MailSacEmail emailToAnalyze : emails) {
                    if (StringUtils.containsIgnoreCase(emailToAnalyze.getSubject(), expectedSubject)) {
                        email = emailToAnalyze;
                        break;
                    }
                }
            }
            pauseForMilliseconds(1000);
            maxAmountOfAttempts--;
            retryCount++;
            LOG(true, "Email not delivered yet. Retry %s time. %s attempt(s) left before failure", retryCount, maxAmountOfAttempts);
        }
        return email;
    }

    /**
     * Get an email message by finding a matching SUBJECT within 2 minutes from GetNada api.
     *
     * @param numberOfEmails  number of emails to fetch
     * @param emailAddress    email address to get inbox from
     * @param expectedSubject expected subject
     * @return first email msg that matches the expected subject text
     */
    public static GetNadaEmail getGetNadaEmailMessageBySubject(final int numberOfEmails, final String emailAddress, final String expectedSubject) {

        int retryCount = 1;
        GetNadaEmail email = null;
        int maxAmountOfAttempts = 120;

        while (null == email && maxAmountOfAttempts > 0) {
            List<GetNadaEmail> emails = getGetNadaEmailsByAddress(numberOfEmails, emailAddress);
            if (emails.size() > 0) {
                for (GetNadaEmail emailToAnalyze : emails) {
                    if (StringUtils.containsIgnoreCase(emailToAnalyze.getS(), expectedSubject)) {
                        email = emailToAnalyze;
                        break;
                    }
                }
            }
            pauseForMilliseconds(1000);
            maxAmountOfAttempts--;
            retryCount++;
            LOG(true, "Email not delivered yet. Retry %s time. %s attempt(s) left before failure", retryCount, maxAmountOfAttempts);
        }
        return email;
    }

    /**
     * Fetch URL from email message HTML body.
     *
     * @param filterRegex     regex to match
     * @param emailAddress    user's email address
     * @param expectedSubject expected subject
     * @return matching URL
     */
    public static String getUrlFromEmail(final String filterRegex, final String emailAddress, final String expectedSubject) {

        MailSacEmail email = getMailSacEmailMessageBySubject(emailAddress, expectedSubject);
        JsonPath response = (JsonPath) executeGet(MAILSAC_URL, String.format("dirty/%s/%s", emailAddress, email.get_id()));
        String htmlTextResponse = response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString();
        return com.pwc.core.framework.util.StringUtils.getMatchFromRegex(filterRegex, htmlTextResponse, 0);
    }

    /**
     * Delete an email message by messageId.
     *
     * @param emailToDelete email message to delete
     */
    public static void deleteSingleMailSacEmailMessage(final MailSacEmail emailToDelete) {

        try {
            JsonPath response = (JsonPath) executeDelete(MAILSAC_URL,
                            String.format("%s/%s/%s/%s", CommonField.ADDRESSES.getField(), emailToDelete.getOriginalInbox(), CommonField.MESSAGES.getField(), emailToDelete.get_id()));
            JsonPath entity = new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
            String message = entity.get(CommonField.MESSAGE.getField());
            LOG(true, "%s (%s) - %s", emailToDelete.getOriginalInbox(), emailToDelete.get_id(), message);
        } catch (Exception e) {
            LOG(false, "Failed due to e=%s", e);
        }
    }

    /**
     * Delete first N number of email messages by unique UID.
     *
     * @param numberOfEmails number of emails to fetch
     * @param emailAddress   email address to delete all emails for
     */
    public static void deleteGetNadaEmailMessages(final int numberOfEmails, final String emailAddress) {

        try {
            List<GetNadaEmail> emailList = getGetNadaEmailsByAddress(numberOfEmails, emailAddress);
            AtomicInteger deletedCount = new AtomicInteger();
            emailList.forEach(emailToDelete -> {
                JsonPath response = (JsonPath) executeGet(GETNADA_URL, String.format("messages/%s", emailToDelete.getUid()));
                JsonPath entity = new JsonPath(response.get(FrameworkConstants.HTTP_ENTITY_KEY).toString());
                if (entity.get(CommonField.DELETED.getField())) {
                    deletedCount.getAndIncrement();
                }
            });
            LOG(true, "Deleted first %s messages for email '%s'", deletedCount.get(), emailAddress);
        } catch (Exception e) {
            LOG(false, "Failed due to e=%s", e);
        }
    }

    /**
     * Perform GET to Jira endpoint.
     *
     * @param baseUrl  base URL to use
     * @param endpoint web service endpoint descriptor
     * @return GET response
     */
    private static Object executeGet(String baseUrl, final String endpoint) {

        baseUrl = baseUrl + endpoint;
        LOG(true, "AUTHORIZED GET URI='%s'", baseUrl);
        Object wsResponse = null;

        int timeout = 3;
        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpGet http = new HttpGet(baseUrl);
            CloseableHttpResponse response = httpclient.execute(http);
            HttpEntity httpEntity = response.getEntity();
            wsResponse = EmailUtils.getWebServiceProcessorInstance().getWebServiceResponse(response, httpEntity, null);
        } catch (Exception e) {
            LOG(true, "Failed to perform GET to url='%s'", endpoint);
        }
        return wsResponse;
    }

    /**
     * Perform POST to Jira endpoint.
     *
     * @param baseUrl  base URL to use
     * @param endpoint web service endpoint descriptor
     * @return POST response
     */
    private static Object executeDelete(String baseUrl, final String endpoint) {

        baseUrl = baseUrl + endpoint;
        LOG(true, "AUTHORIZED DELETE URI='%s'", baseUrl);
        Object wsResponse = null;

        int timeout = 3;
        RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000).setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
        try (CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(config).build()) {
            HttpDelete http = new HttpDelete(baseUrl);
            http.setHeader("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
            CloseableHttpResponse response = httpclient.execute(http);
            HttpEntity httpEntity = response.getEntity();
            wsResponse = EmailUtils.getWebServiceProcessorInstance().getWebServiceResponse(response, httpEntity, null);
        } catch (Exception e) {
            LOG(true, "Failed to perform POST to url='%s'", endpoint);
        }
        return wsResponse;
    }

    /**
     * Singleton WebServiceProcessor provider.
     *
     * @return one WebServiceProcessor
     */
    private static WebServiceProcessor getWebServiceProcessorInstance() {

        if (null == webServiceProcessorInstance) {
            webServiceProcessorInstance = new WebServiceProcessor();
        }
        return webServiceProcessorInstance;
    }

}
