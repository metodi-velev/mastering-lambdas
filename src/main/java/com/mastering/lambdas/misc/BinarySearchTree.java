package com.mastering.lambdas.misc;

import java.util.Scanner;

public class BinarySearchTree {

    private static class Node {
        Node left, right;
        int data;

        Node(int newData) {
            left = right = null;
            data = newData;
        }
    }

    private static Node insert(Node node, int data) {
        if (node == null) {
            node = new Node(data);
        } else {
            if (data <= node.data) {
                node.left = insert(node.left, data);
            } else {
                node.right = insert(node.right, data);
            }
        }
        return (node);
    }

    public static void main(String[] args) throws Exception {
        //Scanner in = new Scanner(System.in);
        Scanner in = new Scanner(System.in);
        Node _root;
        int root_i = 0, root_cnt = 0, root_num = 0;
        root_cnt = in.nextInt();
        _root = null;
        for (root_i = 0; root_i < root_cnt; root_i++) {
            root_num = in.nextInt();
            if (root_i == 0)
                _root = new Node(root_num);
            else
                insert(_root, root_num);
        }

        int q = in.nextInt();

        for (int i = 0; i < q; i++) {
            int _x = in.nextInt();
            System.out.println(isPresent(_root, _x));
        }

        return;
    }

    private static int isPresent(Node root, int val) {
        //10 20 2 18 4 16 5 11 3 15 7 8 18 15 21 11 2 22 16 23
        //1 1 0 1 1 0 1 0
/* For your reference
class Node {
		Node left, right;
		int data;
                Node(int newData) {
			left = right = null;
			data = newData;
		}
	}
*/

        if (root == null) {
            return 0;
        }

        if (root.data == val) {
            return 1;
        }

        if (val > root.data) {
            return isPresent(root.right, val);
        } else {
            return isPresent(root.left, val);
        }
    }
}