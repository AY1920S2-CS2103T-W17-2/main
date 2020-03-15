package com.notably.model.block;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a Block in the Notably data structure.
 */
public class BlockImpl implements Block{
    private List<Block> children;
    private Title title;
    private Body body;
    private Block parent;

    /**
     * Initializes a default block.
     * Used when creating a root block and creating a block without the optional body argument.
     */
    public BlockImpl(Block parent, Title title) {
        this.title = title;
        this.parent = parent;
    }

    /**
     * Initializes a block with body content.
     * Used when creating a block with additional body argument.
     */
    public BlockImpl(Block parent, Title title, Body body) {
        this.title = title;
        this.parent = parent;
        this.body = body;
    }

    @Override
	public Title getTitle() {
		return this.title;
    }

    @Override
	public Body getBody() {
		return this.body;
	}

    @Override
    public Optional<Block> getParent() {
        return Optional.of(parent);
    }

    @Override
    public List<Block> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(List<Block> newChildren) {
        this.children = newChildren;
    }

    @Override
    public Optional<Block> getChild(Title title) {
        Optional<Block> child = this.children.stream()
            .filter(block -> title.equals(block.getTitle()))
            .findAny();
        return child;
    }

    @Override
    public void setChild(Title title, Block newBlock) {
        Optional<Block> child = this.children.stream()
            .filter(block -> title.equals(block.getTitle()))
            .findAny();
        if (child.isPresent()) {
            int childIndex = this.children.indexOf(child.get());
            this.children.set(childIndex, newBlock);
        }
    }

    @Override
    public void addChild(Block block) {
        if (this.children == null) {
            this.children = new ArrayList<Block>();
        }
        this.children.add(block);
    }

    @Override
    public void removeChild(Block toRemove) {
        if (this.children.remove(toRemove) == false) {

        }
        if (this.children.isEmpty()) {
            this.children = null;
        }
    }

    @Override
    public boolean isRoot() {
        return this.title.getText().equals("root")
            && this.parent == null;
    }

    @Override
	public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Block)) {
            return false;
        }

        Block otherBlock = (Block) o;
        return otherBlock.getTitle().equals(this.getTitle())
            && otherBlock.getParent().equals(this.getParent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getParent(), this.getTitle());
    }
}