package com.example.demo.extra.others;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 二叉树
 * 完满二叉树：除了叶子结点之外的每一个结点都有两个孩子结点
 * 完全二叉树：除了最后一层之外的其他每一层都被完全填充，并且所有结点都保持向左对齐
 * 完美二叉树：除了叶子结点之外的每一个结点都有两个孩子，每一层(当然包含最后一层)都被完全填充
 * <p>
 * 遍历
 * 前序：先访问根结点，再访问左子树，最后访问右子树
 * 中序：先访问左子树，再访问根结点，最后访问右子树
 * 后序：先访问左子树，再访问右子树，最后访问根结点
 * 层序：按层，从左到右依次访问
 */
public class BinaryTree {
    private static final String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static List<Node> nodeList = null;
    // 计算幂
    private static int caculateNum = 0;

    public static void main(String[] args) {
        //初始化树
        createTree();
        //自定义root
        int root = 2;
        Node nodeRoot = nodeList.get(root);
        System.out.println("root = " + nodeList.get(root).getData());
        //去掉root前元素
        String[] total = getTree();
        if (root > 0) {
            for (int i = 0; i < root; i++) {
                total[i] = " ";
            }
        }
        //画出树结构
        drawTree(total);
        //前序
        System.out.print("前序：");
        preOrder(nodeRoot);
        System.out.println();
        //中序
        System.out.print("中序：");
        inOrder(nodeRoot);
        System.out.println();
        //后序
        System.out.print("后序：");
        postOrder(nodeRoot);
        System.out.println();
        //层序
        System.out.print("层序：");
        sequence(nodeRoot);
    }

    /**
     * 初始化树
     */
    public static void createTree() {
        nodeList = new LinkedList<>();
        // 将arr转化成node存储
        for (int i = 0; i < arr.length; i++) {
            nodeList.add(new Node(arr[i]));
        }
        // 子元素赋值
        for (int j = 0; j < arr.length / 2; j++) {
            // 左子树
            nodeList.get(j).leftChild = nodeList.get(j * 2 + 1);
            // 右子树，最后一个元素当arr长度为奇数才有
            if (j < arr.length / 2 - 1 || (j == arr.length / 2 - 1 && arr.length % 2 == 1))
                nodeList.get(j).rightChild = nodeList.get(j * 2 + 2);
        }
    }

    /**
     * 前序
     */
    public static void preOrder(Node node) {
        if (node == null) return;
        System.out.print(node.data + " ");
        preOrder(node.getLeftChild());
        preOrder(node.getRightChild());
    }

    /**
     * 中序
     */
    public static void inOrder(Node node) {
        if (node == null) return;
        inOrder(node.getLeftChild());
        System.out.print(node.data + " ");
        inOrder(node.getRightChild());
    }

    /**
     * 后序
     */
    public static void postOrder(Node node) {
        if (node == null) return;
        postOrder(node.getLeftChild());
        postOrder(node.getRightChild());
        System.out.print(node.data + " ");
    }

    /**
     * 层序
     */
    public static void sequence(Node node) {
        String[] tree = getTree();
        for (int i = nodeList.indexOf(node); i < tree.length; i++) {
            System.out.print(tree[i] + " ");
        }
        System.out.println();
    }

    /**
     * 获取树的数组
     */
    public static String[] getTree() {
        String[] arr = new String[nodeList.size()];
        for (int i = 0; i < nodeList.size(); i++) {
            arr[i] = nodeList.get(i).getData();
        }
        return arr;
    }

    /**
     * 画出二叉树图谱
     */
    public static void drawTree(String[] arr) {
        int row = caculateRow(arr.length);
        System.out.println("row = " + row);
        System.out.println("-- Tree construction --");
        draw(arr, row);
        System.out.println("-- Tree construction --");
    }

    /**
     * 计算距离最近的且大于的值
     */
//    private static int max2pow() {
//        int n = arr.length - 1;
//        n |= n >>> 1;
//        n |= n >>> 2;
//        n |= n >>> 4;
//        n |= n >>> 8;
//        n |= n >>> 16;
//        return n + 1;
//    }

    /**
     * 计算行
     */
    private static int caculateRow(int max) {
        do {
            ++caculateNum;
        } while ((max /= 2) >= 2);
        return ++caculateNum;
    }

    /**
     * 根据行画树
     */
    private static void draw(String[] arr, int row) {
        int count = 0;
        int rowTemp = 1;
        int gap;
        for (int i = 1; i < arr.length + 1; i++) {
            if (count == 0)
                gap = (int) Math.pow(2, row - rowTemp) - 1;
            else
                gap = (int) Math.pow(2, row + 1 - rowTemp) - 1;
            printSpace(gap);
            System.out.print(arr[i - 1]);

            count++;
            if (count == Math.pow(2, rowTemp - 1)) {
                count = 0;
                rowTemp++;
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * 打印空格
     */
    private static void printSpace(int count) {
        for (int i = 0; i < count; i++) {
            System.out.print(" ");
        }
    }

}

/**
 * 树节点
 */
@Data
class Node {
    String data;
    Node leftChild = null;
    Node rightChild = null;

    public Node(String data) {
        this.data = data;
    }
}