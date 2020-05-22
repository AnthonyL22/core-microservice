package com.pwc.core.framework.type;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class MailSacEmail {

    private String cc;
    private String bcc;
    private String attachments;
    private String read;
    private String subject;
    private String ip;
    private String received;
    private String originalInbox;
    private String via;
    private List<String> labels;
    private String folder;
    private String size;
    private String domain;
    private List<HashMap> from;
    private List<String> links;
    private String _id;
    private List<HashMap> to;
    private String rtls;
    private String spam;
    private String inbox;
    private String savedBy;

    public MailSacEmail(String emailAddress) {
        this.originalInbox = emailAddress;
    }

}
