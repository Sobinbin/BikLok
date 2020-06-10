package com.tutou.bigwork.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Sobinbin
 */
public class ToggleFavDto {

    @SerializedName("errno")
    public Integer errno;

    @SerializedName("isFav")
    public Boolean isFav;

    @SerializedName("errmsg")
    public String errmsg;

}
