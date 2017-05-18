package model;

/**
 * Created by Will on 08/03/2017.
 */

public class MyNote {

    //Create our instant variable
    public String title;
    public String content;
    public String recordDate;
    public int itemId;

    //Getting and Setter for our instance variable
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }
}
