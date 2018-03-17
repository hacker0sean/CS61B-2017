package disc;

import org.junit.Test;
import java.util.Stack;
public class Tree {
    private TreeNode root;
    public static class TreeNode {
        public int num;
        public TreeNode left, right;
        public TreeNode(int num, TreeNode left, TreeNode right) {
            this.num = num;
            this.left = left;
            this.right = right;
        }
    }

    private static void swapNumbers(TreeNode t1, TreeNode t2) {
        int temp = t1.num;
        t1.num = t2.num;
        t2.num = temp;
    }

    private static void safePush(TreeNode t, Stack<TreeNode> s) {
        if (t != null) {
            s.push(t);
        }
    }

    public void flipHorizontally() {
        Stack<TreeNode> sta = new Stack<TreeNode>();
        safePush(root, sta);
        while (!sta.isEmpty()) {
            TreeNode temp = sta.pop();
            safePush(temp.left, sta);
            safePush(temp.right, sta);
            TreeNode ex = temp.left;
            temp.left = temp.right;
            temp.right = ex;
        }
    }

    public void traverseAndprint(TreeNode root) {
        if (root.left != null) traverseAndprint(root.left);
        System.out.print(root.num + " ");
        if (root.right != null) traverseAndprint(root.right);
    }

    @Test
    public void Test(){
        Tree testTree = new Tree();
        testTree.root = new TreeNode(7, null, null);
        testTree.root.left = new TreeNode(5, null, null);
        testTree.root.right = new TreeNode(6, null, null);
        testTree.root.left.left = new TreeNode(9, null, null);
        testTree.root.left.right = new TreeNode(2, null, null);
        testTree.root.left.right.left = new TreeNode(3, null, null);
        testTree.root.right.left = new TreeNode(4, null, null);
        testTree.root.right.left.right = new TreeNode(8, null, null);
        testTree.root.right.right = new TreeNode(9, null, null);
        traverseAndprint(testTree.root);
        testTree.flipHorizontally();
        System.out.println();
        traverseAndprint(testTree.root);
    }
}