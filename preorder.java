package 树.traversal;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 二叉树的前序遍历:
 * 1.递归解法:时间复杂度O(n),空间复杂度O(n);
 * 2.迭代解法:时间复杂度O(n),空间复杂度O(n);
 * 3.Morris遍历:时间复杂度O(n),空间复杂度O(1);
 */

public class preorder {

    private List<Integer> res = new ArrayList<>();

    /*
     * 1.递归DFS解法:输出,先左子树,再右子树
     */
    public List<Integer> preorderTraversal_Recursion(TreeNode root) {
        dfs(root);
        return res;
    }

    private void dfs(TreeNode node) {
        if (node == null) {
            return;
        }
        res.add(node.val);
        dfs(node.left);
        dfs(node.right);
    }

    /*
     * 2.迭代解法:栈->输出,先加右子树,再加左子树
     */
    public List<Integer> preorderTraversal_Iteration(TreeNode root) {
        if (root == null) {
            return res;
        }
        Deque<TreeNode> stack = new LinkedList<>();
        stack.addFirst(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.removeFirst();
            res.add(node.val);
            //注意这里要先加右子节点,再加左子节点,这样出栈的时候才是先左后右
            if (node.right != null) {
                stack.addFirst(node.right);
            }
            if (node.left != null) {
                stack.addFirst(node.left);
            }
        }
        return res;
    }

    /*
     * 3.Morris遍历解法
     */
    public List<Integer> preorderTraversal_Morris(TreeNode root) {
        if (root == null) {
            return res;
        }
        TreeNode n1 = root, n2 = null;
        while (n1 != null) {//n1表示当前节点
            n2 = n1.left;//n2表示当前节点的左子节点
            if (n2 != null) {//如果当前节点的左子节点不为空,去寻找当前节点的前驱节点,即左子树的最右节点
                /*
                 * 当遍历当前节点的左子节点时,有两种情况:
                 * 1.正常第1次遍历;
                 * 2.第2次遍历:通过当前节点的左子树的最右节点又回到了当前节点;
                 */
                while (n2.right != null && n2.right != n1) {
                    /*
                     * 第1个判断条件:正常寻找最右节点;
                     * 第2个判断条件:获取已经是第2次遍历的信息,直接去遍历当前节点的右子节点;
                     */
                    n2 = n2.right;
                }
                if (n2.right == null) {//正常情况1
                    res.add(n1.val);
                    n2.right = n1;//找到当前节点的前驱节点,将前驱节点的右指针指向当前节点
                    n1 = n1.left;//去遍历左子树
                    continue;
                } else {//情况2
                    //res.add(n1.val);//中序遍历
                    n2.right = null;//打断前驱节点和当前节点的关系,防止死循环
                }
            } else {//如果当前节点的左子节点为空,当前节点加入答案,然后去遍历当前节点的右子节点
                res.add(n1.val);
            }
            n1 = n1.right;
        }
        return res;
    }

}
