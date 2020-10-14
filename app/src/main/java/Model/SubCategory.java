package Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubCategory implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("parent")
    @Expose
    private String parent;
    @SerializedName("leval")
    @Expose
    private String leval;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image2")
    @Expose
    private String image2;
    @SerializedName("image2_status")
    @Expose
    private String image2Status;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("Count")
    @Expose
    private String count;
    @SerializedName("PCount")
    @Expose
    private String pCount;
    private final static long serialVersionUID = -5216828902603837475L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getLeval() {
        return leval;
    }

    public void setLeval(String leval) {
        this.leval = leval;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage2Status() {
        return image2Status;
    }

    public void setImage2Status(String image2Status) {
        this.image2Status = image2Status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPCount() {
        return pCount;
    }

    public void setPCount(String pCount) {
        this.pCount = pCount;
    }
}
