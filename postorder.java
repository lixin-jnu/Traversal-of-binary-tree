import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * 二叉树的后序遍历:
 * 1.递归解法:时间复杂度O(n),空间复杂度O(n);
 * 2.迭代解法:时间复杂度O(n),空间复杂度O(n);
 * 3.Morris遍历:时间复杂度O(n),空间复杂度O(1);
 */

public class postorder {

    private List<Integer> res = new ArrayList<>();

    /*
     * 1.递归DFS解法:先左子树,再右子树,最后输出
     */
    public List<Integer> postorderTraversal_Recursion(TreeNode root) {
        dfs(root);
        return res;
    }

    private void dfs(TreeNode node) {
        if (node == null) {
            return;
        }
        dfs(node.left);
        dfs(node.right);
        res.add(node.val);
    }

    /*
     * 2.迭代解法
     */
    public List<Integer> postorderTraversal_Iteration(TreeNode root) {
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode prev = null;
        while (root != null || !stack.isEmpty()) {
            //root==null时不进行while循环:来自下面的if,上一轮右子树为空或是右子树已遍历
            while (root != null) {//一直遍历到该节点的最左子节点的左子节点(null)
                stack.addFirst(root);
                root = root.left;
            }
            /*
             * 这时出栈的元素有两种情况:
             * 1.一直遍历到的最左子节点;
             * 2.新弹出一个已存储的节点;
             */
            root = stack.removeFirst();
            if (root.right == null || root.right == prev) {//右子树为空或是右子树已遍历
                res.add(root.val);//加入该节点
                prev = root;//标记该节点已遍历
                root = null;//root置为null,下一轮可以从栈中弹出新节点
            } else {
                stack.addFirst(root);//因为右子树存在,把弹出的节点再放回去
                root = root.right;//转到右子节点
            }
        }
        return res;
    }

    /*
     * 3.Morris遍历解法
     */
    public List<Integer> postorderTraversal_Morris(TreeNode root) {
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
                    //res.add(n1.val);//前序遍历
                    n2.right = n1;//找到当前节点的前驱节点,将前驱节点的右指针指向当前节点
                    n1 = n1.left;//去遍历左子树
                    continue;
                } else {//情况2
                    //res.add(n1.val);//中序遍历
                    n2.right = null;//打断前驱节点和当前节点的关系,防止死循环
                    addPath(n1.left);
                }
            }
            n1 = n1.right;
        }
        addPath(root);//最后把根节点开头的路径加进去
        return res;
    }

    //添加该节点+它一直延伸的右子节点路径进入答案(路径中节点的左子树早已加入答案)
    private void addPath(TreeNode node) {
        int count = 0;
        while (node != null) {//一直找到node节点的最右子节点
            ++count;//记录个数
            res.add(node.val);//暂时添加到答案中
            node = node.right;
        }
        //left:上次添加完后的第一个位置
        //right:List容器最后一个位置
        int left = res.size() - count, right = res.size() - 1;
        while (left < right) {//将添加的子节点反序过来(在已添加左子树的情况下,将原本的根->右转变为右->根)
            int temp = res.get(left);
            res.set(left, res.get(right));
            res.set(right, temp);
            left++;
            right--;
        }
    }

}
