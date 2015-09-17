package vienan.app.expandableswipelistview.model;

import java.util.List;

/**
 * Created by vienan on 2015/9/17.
 */
public class GroupEntity {

    private String groupName;
    private List<ChildEntity> childEntities;

    public GroupEntity(String groupName) {
        this.groupName = groupName;
    }

    public List<ChildEntity> getChildEntities() {
        return childEntities;
    }

    public void setChildEntities(List<ChildEntity> childEntities) {
        this.childEntities = childEntities;
    }

    public String getGroupName() {
        return groupName;
    }
}