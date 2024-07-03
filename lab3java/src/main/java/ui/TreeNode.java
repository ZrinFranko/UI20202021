package ui;

import java.util.HashMap;
import java.util.Map;

public class TreeNode {

    private Feature feature;
    private boolean isLeaf;
    private Map<String,TreeNode> children;

    public TreeNode(Feature feature, Map<String,TreeNode> children){

        this.feature = feature;
        this.isLeaf = children.isEmpty() ? true : false;
        this.children = children;
    }

}
