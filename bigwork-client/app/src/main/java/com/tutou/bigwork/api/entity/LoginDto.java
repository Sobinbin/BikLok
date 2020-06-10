package com.tutou.bigwork.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Sobinbin
 */
public class LoginDto {

    @SerializedName("errno")
    public Integer errno;

    @SerializedName("token")
    public String token;

    @SerializedName("errmsg")
    public String errmsg;

}
