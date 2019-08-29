package com.example.demo.extra.others;

import java.util.Arrays;

/**
 * 各种排序算法
 * 从小到大排序
 */
public class SortArithmetic {

    public static void main(String[] args) {
        int[] sort = {2, 6, 1, 2, 3, 9, 6, 0, 2, 1, 1};
        System.out.println("原始数组：" + Arrays.toString(sort));
        int[] selectSort = selectSort(sort);
        System.out.println("选择排序：" + Arrays.toString(selectSort));
        int[] bubbleSort = bubbleSort(sort);
        System.out.println("冒泡排序：" + Arrays.toString(bubbleSort));
        int[] fastSort = fastSort(sort);
        System.out.println("快速排序：" + Arrays.toString(fastSort));
    }

    /**
     * 选择排序
     */
    public static int[] selectSort(int[] arg) {
        int[] sort = arg.clone();
        int t;
        for (int i = 0; i < sort.length - 1; i++) {
            for (int j = i + 1; j < sort.length; j++) {
                if (sort[i] > sort[j]) {
                    t = sort[i];
                    sort[i] = sort[j];
                    sort[j] = t;
                }
            }
        }
        return sort;
    }

    /**
     * 冒泡排序
     */
    public static int[] bubbleSort(int[] arg) {
        int[] sort = arg.clone();
        int t;
        for (int i = 0; i < sort.length - 1; i++) {
            for (int j = 0; j < sort.length - i - 1; j++) {
                if (sort[j] > sort[j + 1]) {
                    t = sort[j];
                    sort[j] = sort[j + 1];
                    sort[j + 1] = t;
                }
            }
        }
        return sort;
    }

    /**
     * 快速排序
     */
    public static int[] fastSort(int[] arg) {
        int[] sort = arg.clone();
        fast(sort, 0, sort.length - 1);
        return sort;
    }

    private static void fast(int[] sort, int low, int high) {
        //设置退出
        if (low > high) return;
        //初始化
        int i = low;
        int j = high;
        int key = sort[low];

        //开始排序
        while (i < j) {
            //从右往左查找
            while (i < j && sort[j] >= key) j--;
            //从左往右查找
            while (i < j && sort[i] <= key) i++;
            //交换左右值
            if (i < j) {
                int t = sort[i];
                sort[i] = sort[j];
                sort[j] = t;
            }
        }

        //交换key
        sort[low] = sort[i];
        sort[i] = key;

        //递归左右二分排序
        fast(sort, low, i - 1);
        fast(sort, i + 1, high);
    }

}
