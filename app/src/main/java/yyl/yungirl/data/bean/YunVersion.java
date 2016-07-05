package yyl.yungirl.data.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yinyiliang on 2016/7/5 0005.
 */
public class YunVersion {

    @SerializedName("name") public String name;
    @SerializedName("version") public String version;
    @SerializedName("changelog") public String changelog;
    @SerializedName("updated_at") public int updatedAt;
    @SerializedName("versionShort") public String versionShort;
    @SerializedName("build") public String build;
    @SerializedName("install_url") public String installUrl;
    @SerializedName("direct_install_url") public String directInstallUrl;
    @SerializedName("update_url") public String updateUrl;
    /**
     * fsize : 2626655
     */
    @SerializedName("binary") public BinaryEntity binary;

    public static class BinaryEntity {
        @SerializedName("fsize") public int fsize;
    }
}
