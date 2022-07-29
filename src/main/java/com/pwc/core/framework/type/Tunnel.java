package com.pwc.core.framework.type;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Tunnel {

    @SerializedName("creation_time")
    private int creationTime;
    private String id;
    private String owner;
    private String status;
    private String host;
    @SerializedName("tunnel_identifier")
    private String tunnelIdentifier;

}
