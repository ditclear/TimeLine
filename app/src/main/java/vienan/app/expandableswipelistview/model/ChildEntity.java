package vienan.app.expandableswipelistview.model;

/**
 * Created by vienan on 2015/9/17.
 */
public class ChildEntity {
    private String childTitle;

    public boolean isOpen;

    public ChildEntity(String childTitle) {
        this.childTitle = childTitle;
    }

    public String getChildTitle() {
        return childTitle;
    }

}
