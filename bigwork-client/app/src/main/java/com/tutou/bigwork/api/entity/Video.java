package com.tutou.bigwork.api.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Sobinbin
 */

public class Video {

    @SerializedName("_id")
    public String id;

    @SerializedName("feedurl")
    public String url;

    @SerializedName("nickname")
    public String name;

    @SerializedName("description")
    public String description;

    @SerializedName("likecount")
    public int likeCount;

    @SerializedName("avatar")
    public String avatar;

}
