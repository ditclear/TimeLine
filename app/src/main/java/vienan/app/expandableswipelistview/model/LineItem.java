package vienan.app.expandableswipelistview.model;

/**
 * 页面描述：
 * <p>
 * Created by ditclear on 2016/12/17.
 */

public class LineItem {

    private String content;
    private boolean isTitle;

    public LineItem(String content, boolean isTitle) {
        this.content = content;
        this.isTitle = isTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }
}
