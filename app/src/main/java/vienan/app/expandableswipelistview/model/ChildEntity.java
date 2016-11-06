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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChildEntity that = (ChildEntity) o;

        if (isOpen != that.isOpen) return false;
        return childTitle.equals(that.childTitle);

    }

    @Override
    public int hashCode() {
        int result = childTitle.hashCode();
        result = 31 * result + (isOpen ? 1 : 0);
        return result;
    }
}
