## 二叉树的遍历

### 0 相关资源
1. Leetcode题目链接：二叉树的[前序遍历](https://leetcode-cn.com/problems/binary-tree-preorder-traversal/)，[中序遍历](https://leetcode-cn.com/problems/binary-tree-inorder-traversal/)和[后序遍历](https://leetcode-cn.com/problems/binary-tree-postorder-traversal/)；
2. 源代码：包含二叉树**三种遍历方式**的**三种解法**的[GitHub仓库](https://github.com/lixin-jnu/Traversal-of-binary-tree)；
3. CSDN博客：[二叉树的遍历](https://blog.csdn.net/JAVA_XIN_BOY/article/details/124275913)。

### 1 递归解法
三种遍历方式的递归解法是最容易想到的解决方案，主要采用DFS深度优先遍历的思想，在递归函数中进行**先左子树后右子树**的深度遍历，唯一的不同就是**当前节点的输出位置**，代码如下。
>时空复杂度均为O(n)。

```java
private List<Integer> res = new ArrayList<>();

public List<Integer> Traversal_Recursion(TreeNode root) {
    dfs(root);
    return res;
}

private void dfs(TreeNode node) {
    if (node == null) {
        return;
    }
    /*
     * 先序遍历：先根节点输出，再左子树递归，最后右子树递归。
     */
    res.add(node.val);
    dfs(node.left);
    dfs(node.right);
    /*
     * 中序遍历：先左子树递归，再根节点输出，最后右子树递归。
     * dfs(node.left);
     * res.add(node.val);
     * dfs(node.right);
     */
    /*
     * 后序遍历：先左子树递归，再右子树递归，最后根节点输出。
     * dfs(node.left);
     * dfs(node.right);
     * res.add(node.val);
     */
}
```
### 2 迭代解法
迭代解法主要依靠**栈stack**这种数据结构的帮助，最终节点的出栈顺序即为相应的遍历顺序（后序遍历略有不同），下面针对每种遍历方式进行详细说明。
>时空复杂度也均为O(n)。

1. **先序遍历**：先序遍历先将根节点入栈，如果栈不为空，每次循环从栈中取出一个节点并输出，再将该节点的左右子节点（如果存在的话）放入栈中，这里需要注意的是先将右子节点入栈，再将左子节点入栈，从而保证先序遍历根→左→右的遍历顺序。

```java
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
```

2. **中序遍历**：中序遍历每次循环都需要先找到当前root节点的最左子节点，期间需要将寻找路径中的节点依次放入栈中，找到后直接输出该最左子节点即可（它没有左子节点了），然后转到右子节点进行下一轮循环。但是每轮循环也存在一开始root直接为null的情况，这意味着上轮循环中的root节点的右子节点为null，此时只需要从栈中再取出一个节点加入到答案中即可，因为取出的节点的左子树已遍历完毕，然后同理转到当前节点的右子节点进行下一轮循环。

```java
public List<Integer> inorderTraversal_Iteration(TreeNode root) {
    Deque<TreeNode> stack = new LinkedList<>();
    while (root != null || !stack.isEmpty()) {
        //先一股脑找到该节点的最左子节点
        while (root != null) {
            stack.addFirst(root);//期间将路径中的节点压入栈中
            root = root.left;
        }
        //以上while循环也存在root直接为null的情况,代表上轮循环的root节点的右子节点为null
        //这时从栈中再取出一个节点加入到答案中即可,取出的节点的左子树已遍历完毕
        root = stack.removeFirst();
        res.add(root.val);
        root = root.right;//转到右子节点
    }
    return res;
}
```

3. **后序遍历**：后序遍历一开始和中序遍历类似，每次循环都需要先找到当前root节点的最左子节点，期间将寻找路径中的节点依次放入栈中，但是当找到最左子节点后，不能立刻输出答案，需要先保证找到的最左子节点的右子树为null或已遍历时才可以输出答案，否则就要转到右子树去遍历（当前节点放回栈中），等待右子树遍历后，再去输出当前节点。同理每轮循环也存在一开始root直接为null的情况，这意味着上轮循环中的root节点的右子节点为null或已遍历，此时当前节点就能加入到答案中了。

```java
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
```

### 3 Morris解法
有一种巧妙的方法可以在线性时间内，只占用常数空间来实现前序、中序和后续遍历。这种方法由J.H.Morris在1979年的论文《Traversing Binary Trees Simply and Cheaply》中首次提出，因此被称为Morris遍历。

>时间复杂度O(n)，空间复杂度O(1)。

Morris遍历的核心思想是利用树的大量空闲指针，实现空间开销的极限缩减，其遍历规则总结如下：

1. 新建临时节点，令该节点为root；
2. 如果当前节点的左子节点为空，遍历当前节点的右子节点；
3. 如果当前节点的左子节点不为空，在当前节点的左子树中找到最右节点作为当前节点的**前驱节点**；
4. **如果前驱节点的右子节点为空**，将前驱节点的右子节点设置为当前节点，当前节点更新为当前节点的左子节点；**如果前驱节点的右子节点为当前节点**，将它的右子节点重新设为空，当前节点更新为当前节点的右子节点；
5. 重复步骤2和步骤3(4)，直到遍历结束。

如下图所示，给出了一个Morris遍历的例子：<br>
![在这里插入图片描述](https://img-blog.csdnimg.cn/61834edd4a5045439274d436d14bc8de.png)<br>
在Morris遍历的过程中，在合适的位置输出节点的值即可完成二叉树的前序、中序和后序遍历：
- **前序遍历**：在每次循环中，以下两种情况成立时输出当前节点即是前序遍历顺序。
	1. 如果当前节点的左子树为空；
	2. 如果当前节点的左子树不为空，且左子树的最右节点的右指针为空（第一次遍历到）；
- **中序遍历**：在每次循环中，以下两种情况成立时输出当前节点即是中序遍历顺序。
	1. 如果当前节点的左子树为空；
	2. 如果当前节点的左子树不为空，且左子树的最右节点的右指针指向当前节点（第二次遍历到）；
- **后序遍历**：在每次循环中，如果当前节点的左子树不为空，且左子树的最右节点的右指针指向当前节点（第二次遍历到）时，将当前节点的左子节点（+以该左子节点为根节点一直延伸的右子节点路径）**反序**后添加到答案中，最后不要忘记在整个循环结束后，在对总的根节点也进行类似的操作后即为后序遍历顺序。

三种遍历方式的Morris实现参见文章开头给出的GitHub仓库（本仓库代码）。
	
