package com.tutou.bigwork.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Sobinbin
 */
public class RegisterDto {

    @SerializedName("errno")
    public Integer errno;

    @SerializedName("errmsg")
    public String errmsg;
}
