/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.encrypt;

/**
 *
 * @author kdg
 */
public class Tree {

    final int depth;
    final int fanout;
    final int leaf_length;
    final int inner_length;
    final int digest_length;

    /**
     * @param fanout
     * @param depth
     * @param leaf_length size of data input for leaf nodes.
     * @param inner_length note this is used also as digest-length for non-root
     * nodes.
     * @param digest_length final hash out digest-length for the tree
     */
    public Tree(
            final int depth,
            final int fanout,
            final int leaf_length,
            final int inner_length,
            final int digest_length
    ) {
        this.fanout = fanout;
        this.depth = depth;
        this.leaf_length = leaf_length;
        this.inner_length = inner_length;
        this.digest_length = digest_length;
    }

    private Param treeParam() {
        return new Param().
                setDepth(depth).setFanout(fanout).setLeafLength(leaf_length).setInnerLength(inner_length);
    }

    /**
     * returns the Digest for tree node @ (depth, offset)
     */
    public final Digest getNode(final int depth, final int offset) {
        final Param nodeParam = treeParam().setNodeDepth(depth).setNodeOffset(offset).setDigestLength(inner_length);
        return Digest.newInstance(nodeParam);
    }

    /**
     * returns the Digest for root node
     */
    public final Digest getRoot() {
        final int depth = this.depth - 1;
        final Param rootParam = treeParam().setNodeDepth(depth).setNodeOffset(0L).setDigestLength(digest_length);
        return Digest.newInstance(rootParam);
    }
}
