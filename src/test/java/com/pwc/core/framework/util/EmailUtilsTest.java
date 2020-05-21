package com.pwc.core.framework.util;

import com.pwc.core.framework.type.GetNadaEmail;
import com.pwc.core.framework.type.MailSacEmail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class EmailUtilsTest {

    private static final String MAILSAC_EMAIL_ADDRESS = "pwc@mailsac.com";
    private static final String MAILSAC_TEST_EMAIL_SUBJECT = "test subject";

    private static final String GETNADA_EMAIL_ADDRESS = "pwc@getnada.com";
    private static final String GETNADA_EMAIL_ID = "S28VoqvcHEzgLe9UlzGmptpZgsbwtm";

    @Test
    public void getAllMailSacEmailsByAddressTest() {
        List<MailSacEmail> emails = EmailUtils.getAllMailSacEmailsByAddress(MAILSAC_EMAIL_ADDRESS);
        Assert.assertNotNull(emails);
        Assert.assertTrue(emails.size() >= 0);
    }

    @Test
    public void getGetNadaEmailsByAddressTest() {
        List<GetNadaEmail> emails = EmailUtils.getGetNadaEmailsByAddress(1, GETNADA_EMAIL_ADDRESS);
        Assert.assertNotNull(emails);
        Assert.assertTrue(emails.size() >= 0);
    }

    @Test (expected = Exception.class)
    public void getGetNadaEmailByIdTest() {
        GetNadaEmail emailToFind = new GetNadaEmail();
        emailToFind.setUid(GETNADA_EMAIL_ID);
        GetNadaEmail emailFound = EmailUtils.getAllGetNadaEmailsById(emailToFind);
        Assert.assertEquals(emailToFind.getUid(), emailFound.getUid());
    }

}
