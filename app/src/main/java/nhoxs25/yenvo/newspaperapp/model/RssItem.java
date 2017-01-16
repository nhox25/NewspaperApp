package nhoxs25.yenvo.newspaperapp.model;

/**
 * Created by yenvo on 10/01/2017.
 */
public class RssItem {
    private String title;
    private String img;
    private String date;
    private String link;
    public RssItem(){
        this.title = null;
        this.img = null;
        this.date = null;
        this.link = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
