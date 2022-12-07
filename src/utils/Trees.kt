package utils

import java.util.*

typealias Visitor<T> = (TreeNode<T>) -> Unit

data class TreeNode<T>(val value: T) {
    private var parent: TreeNode<T>? = null
    private val children: MutableList<TreeNode<T>> = mutableListOf()

    fun add(child: TreeNode<T>) {
        child.parent = this
        children.add(child)
    }

    fun isRoot() = parent == null

    fun getParent() = parent

    fun getRoot(): TreeNode<T> {
        if(this.isRoot())
            return this
        return parent!!.getRoot()
    }

    fun forEachLevelOrder(visit: Visitor<T>) {
        visit(this)
        val queue = LinkedList<TreeNode<T>>()
        children.forEach { queue.addLast(it) }

        var node = queue.poll()
        while (node != null) {
            visit(node)
            node.children.forEach { queue.addLast(it) }
            node = queue.poll()
        }
    }

    fun getNodes(): List<TreeNode<T>> {
        val result: MutableList<TreeNode<T>> = mutableListOf()
        forEachLevelOrder {
            result += it
        }
        return result
    }

    fun search(condition: (T) -> Boolean): TreeNode<T>? {
        var result: TreeNode<T>? = null

        forEachLevelOrder {
            if (condition(it.value)) {
                result = it
            }
        }

        return result
    }
}
