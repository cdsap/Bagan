package com.cdsap.bagan


/*

     1
  2     3

4   5
 */

fun main() {

    val node4 = Node(null, null, 5)
    val node3 = Node(null, null, 4)
    val node2 = Node(node3, node4, 2)
    val node1 = Node(null, null, 3)
    val nodeRoot = Node(node2, node1, 1)

    println("traverseInorder in order")
    traverseInorder(nodeRoot)

    println("traverseInorder in pre-order")
    traversePreorder(nodeRoot)

    println("traverseInorder in post-order")
    traversePostorder(nodeRoot)


    val nodex1 = Node(null, null, 4)
    val nodex2 = Node(null, null, 4)
    val nodexRoot = Node(nodex1, nodex2, 2)
    println(isSubtree(nodeRoot, nodexRoot))
}


fun areIdentical(root1: Node?, root2: Node?): Boolean {

    if (root1 == null && root2 == null)
        return true

    return if (root1 == null || root2 == null) false else root1.value === root2.value
            && areIdentical(root1.left, root2.left)
            && areIdentical(root1.right, root2.right)

}

/* This function returns true if S is a subtree of T, otherwise false */
fun isSubtree(T: Node?, S: Node?): Boolean {
    /* base cases */
    if (S == null)
        return true

    if (T == null)
        return false

    /* Check the tree with root as current node */
    return if (areIdentical(T, S)) true
    else isSubtree(T.left, S) || isSubtree(T.right, S)

    /* If the tree with root as current node doesn't match then
           try left and right subtrees one by one */
}

fun traverseInorder(root: Node?) {
    // inorder
    if (root == null) {
        return
    }
    println(root.value)
    traverseInorder(root.left)
    traverseInorder(root.right)

    // preorder

    // posr order
}

fun traversePreorder(root: Node?) {
    if (root == null) {
        return
    }
    traversePreorder(root.left)
    println(root.value)
    traversePreorder(root.right)

}

fun traversePostorder(root: Node?) {
    if (root == null) {
        return
    }
    traversePostorder(root.left)
    traversePostorder(root.right)
    println(root.value)

}


class Node(val left: Node?, val right: Node?, val value: Int)

class BullShot {

}