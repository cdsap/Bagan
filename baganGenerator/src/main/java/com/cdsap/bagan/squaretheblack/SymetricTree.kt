package com.cdsap.bagan.squaretheblack

/**
 * Example:
 * var ti = TreeNode(5)
 * var v = ti.`val`
 * Definition for a binary tree node.
 * class TreeNode(var `val`: Int) {
 *     var left: TreeNode? = null
 *     var right: TreeNode? = null
 * }
 */
class Solution {
    fun isSymmetric(root: TreeNode?): Boolean {

        return isMirror(root, root)
    }

    fun isMirror(left: TreeNode?, right: TreeNode?): Boolean {
        if (left == null && right == null) {
            return true
        } else if (left == null || right == null) {
            return false
        }

        return left.ss == right.ss && isMirror(left.left, right.right) && isMirror(left.right, right.left)
    }
}


class TreeNode(var ss: Int) {
    var left: TreeNode? = null
    var right: TreeNode? = null
}


fun main() {
    val treeNode2 = TreeNode(2)
    val treeNode3 = TreeNode(3)

    val treeNode1 = TreeNode(1)
    treeNode1.left = treeNode2
    treeNode1.right = treeNode3

    println(Solution().isSymmetric(treeNode1))


}