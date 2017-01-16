package nhoxs25.yenvo.newspaperapp.model;

/**
 * Created by yenvo on 10/01/2017.
 */
public class ItemSlideMenu {
    private int imgId;
    private String title;

    public ItemSlideMenu(int imgId, String title) {
        this.imgId = imgId;
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImgId() {

        return imgId;
    }

    public String getTitle() {
        return title;
    }
}
