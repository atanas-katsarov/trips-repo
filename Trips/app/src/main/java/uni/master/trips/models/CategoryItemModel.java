package uni.master.trips.models;

public class CategoryItemModel {
    private String categoryName;
    private String picUrl;

    public CategoryItemModel(String categoryName, String picUrl) {
        this.categoryName = categoryName;
        this.picUrl = picUrl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
