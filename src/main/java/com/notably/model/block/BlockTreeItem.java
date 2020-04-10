package com.notably.model.block;

import java.util.List;

import com.notably.commons.path.AbsolutePath;

import javafx.scene.control.TreeItem;

/**
 * API of the BlockTreeItem component.
 *
 * A BlockTreeItem is a single node in a BlockTree.
 * It contains a block as well as information on its parent and children items.
 */
public interface BlockTreeItem extends Block {
    /**
     * Gets the {@code TreeItem<Block>} representation of the BlockTreeItem.
     */
    TreeItem<Block> getTreeItem();

    /**
     * Gets the Block content contained in a BlockTreeItem.
     */
    Block getBlock();

    /**
     * Gets the parent block of a block, if possible.
     * Returns an {@code Optional.empty()} if the block is the root block.
     */
    BlockTreeItem getBlockParent();

    /**
     * Gets a list of children blocks of a block.
     */
    List<BlockTreeItem> getBlockChildren();

    /**
     * Replaces all the children of the block with a new list of children.
     */
    void setBlockChildren(List<BlockTreeItem> newChildren);

    /**
     * Finds a child block of a block, with the given input.
     */
    BlockTreeItem getBlockChild(Title title);

    /**
     * Replaces a child block of a block, that matches the title, with a new child block.
     */
    void setBlockChild(Title title, Block newBlock);

    /**
     * Adds a single new child to a block.
     */
    void addBlockChild(Block newBlock);

    /**
     * Removes a specified child block from a block.
     * @param toRemove
     */
    void removeBlockChild(Block toRemove);

    /**
     * Gets the absolute path of the current block in the tree.
     */
    AbsolutePath getAbsolutePath();

    /**
     * Checks if a block is a root block.
     */
    boolean isRootBlock();
}
