package messaging_service;

import java.util.*;

public class DeliveryReceiptEngine {

    // Internal node structure of the AVL Tree
    public static class AVLNode {
        long timestamp; // Primary index key
        int height;
        AVLNode left, right;

        public AVLNode(long timestamp) {
            this.timestamp = timestamp;
            this.height = 1;
        }
    }

    private AVLNode root;

    // Helper method to securely fetch current node height
    private int height(AVLNode node) {
        return (node == null) ? 0 : node.height;
    }

    // Evaluates height imbalances between subtrees
    private int getBalanceFactor(AVLNode node) {
        return (node == null) ? 0 : height(node.left) - height(node.right);
    }

    // Right rotation utility for Left-Left (LL) structural imbalances
    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x; // New local root node
    }

    // Left rotation utility for Right-Right (RR) structural imbalances
    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y; // New local root node
    }

    /**
     * Inserts a new receipt timestamp into the AVL tree with automatic balancing.
     * @param timestamp Unique transaction millisecond epoch value
     */
    public void insert(long timestamp) {
        root = insertRecursive(root, timestamp);
    }

    private AVLNode insertRecursive(AVLNode node, long timestamp) {
        // 1. Standard BST insertion logic
        if (node == null) {
            return new AVLNode(timestamp);
        }

        if (timestamp < node.timestamp) {
            node.left = insertRecursive(node.left, timestamp);
        } else if (timestamp > node.timestamp) {
            node.right = insertRecursive(node.right, timestamp);
        } else {
            return node; // Duplicate timestamps ignored
        }

        // 2. Update height metrics of parent node
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // 3. Evaluate balance factor to check for imbalances
        int balance = getBalanceFactor(node);

        // Left-Left (LL) Imbalance Case
        if (balance > 1 && timestamp < node.left.timestamp) {
            return rightRotate(node);
        }

        // Right-Right (RR) Imbalance Case
        if (balance < -1 && timestamp > node.right.timestamp) {
            return leftRotate(node);
        }

        // Left-Right (LR) Imbalance Case
        if (balance > 1 && timestamp > node.left.timestamp) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right-Left (RL) Imbalance Case
        if (balance < -1 && timestamp < node.right.timestamp) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    /**
     * Traverses down the leftmost edge to locate the oldest pending record.
     * Time Complexity: O(log n)
     * @return Minimum timestamp value stored within the tree layout
     */
    public long findOldestPending() {
        if (root == null) {
            throw new NoSuchElementException("The active receipt queue is empty.");
        }

        AVLNode curr = root;
        int hops = 0;
        while (curr.left != null) {
            curr = curr.left;
            hops++;
        }
        System.out.println("  Executed Leftmost descent traversal in: " + hops + " pointer hops.");
        return curr.timestamp;
    }

    public int getTreeHeight() {
        return height(root);
    }

    public static void main(String[] args) {
        DeliveryReceiptEngine avl = new DeliveryReceiptEngine();

        System.out.println("=== WHATSAPP SYSTEM TESTING RECEIPT PIPELINE ===");
        System.out.println("Simulating strictly monotonic incoming data sequence...");

        // Injecting an increasing sequence of mock timestamps
        long baseTimestamp = 1717488000000L; // Mock epoch base
        for (int i = 1; i <= 10; i++) {
            avl.insert(baseTimestamp + (i * 1000));
        }

        System.out.println("\nTree Performance Metrics Metrics:");
        System.out.println("  Total Items Handled: 10 records");
        System.out.println("  Final AVL Calculated Height: " + avl.getTreeHeight() + " levels");

        long oldest = avl.findOldestPending();
        System.out.println("  Resolved Oldest Target Value entry: " + oldest);
    }
}
