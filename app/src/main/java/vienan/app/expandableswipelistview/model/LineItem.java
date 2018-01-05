package vienan.app.expandableswipelistview.model;

/**
 * 页面描述：
 * <p>
 * Created by ditclear on 2016/12/17.
 */

public class LineItem {

    private String content;
    private boolean isLeft;


    public LineItem(String content, boolean isLeft) {
        this.content = content;
        this.isLeft = isLeft;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }
}
