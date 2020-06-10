package com.tutou.bigwork.api.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author Sobinbin
 */
public class CheckFavDto {

    @SerializedName("errno")
    public Integer errno;

    @SerializedName("videoIdList")
    public List<String> videoIdList;

    @SerializedName("errmsg")
    public String errmsg;

}
