package com.notably.model.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.notably.model.block.exceptions.DuplicateBlockException;
import com.notably.model.block.exceptions.NoSuchBlockException;

/**
 * Represents a Block in the Notably data structure.
 */
public class BlockImpl implements Block {
    private Block parent;
    private Title title;
    private Body body;
    private List<Block> children;

    /**
     * Initializes a block without the body.
     * Used when creating a root block or a block without the optional body argument.
     */
    public BlockImpl(Title title) {
        this(title, new Body(""));
    }

    /**
     * Initializes a block with body content.
     * Used when creating a block with additional body argument.
     */
    public BlockImpl(Title title, Body body) {
        Objects.requireNonNull(title);
        Objects.requireNonNull(body);
        this.title = title;
        this.body = body;
        this.children = new ArrayList<Block>();
    }

    /**
     * Static method to create and return a root block
     */
    public static Block createRootBlock() {
        return new BlockImpl(new Title("Root"));
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
        return Optional.ofNullable(parent);
    }

    @Override
    public List<Block> getChildren() {
        return this.children;
    }

    @Override
    public void setChildren(List<Block> newChildren) {
        Objects.requireNonNull(newChildren);
        this.children = newChildren;
    }

    @Override
    public Optional<Block> getChild(Title title) {
        Objects.requireNonNull(title);
        Optional<Block> child = this.children.stream()
            .filter(block -> title.equals(block.getTitle()))
            .findAny();
        return child;
    }

    @Override
    public void setChild(Title title, Block newBlock) throws NoSuchBlockException {
        Objects.requireNonNull(title);
        Objects.requireNonNull(newBlock);
        Optional<Block> child = this.children.stream()
            .filter(block -> title.equals(block.getTitle()))
            .findAny();
        if (child.isPresent()) {
            int childIndex = this.children.indexOf(child.get());
            this.children.set(childIndex, newBlock);
        } else {
            throw new NoSuchBlockException(title.getText());
        }
    }

    @Override
    public void addChild(Block block) throws DuplicateBlockException {
        boolean hasMatchingChild = this.children.stream()
            .anyMatch(child -> this.title.equals(child.getTitle()));
        if (hasMatchingChild) {
            throw new DuplicateBlockException();
        } else {
            this.children.add(block);
        }
    }

    @Override
    public void removeChild(Block toRemove) {
        this.children.remove(toRemove);
    }

    @Override
    public boolean isRoot() {
        return this.title.getText().equals("Root")
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