package com.notably.logic.commands.suggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.notably.commons.path.AbsolutePath;
import com.notably.model.Model;
import com.notably.model.block.BlockTreeItem;
import com.notably.model.suggestion.SuggestionItem;
import com.notably.model.suggestion.SuggestionItemImpl;

/**
 * Represents a suggestion command object to open a note.
 */
public class OpenSuggestionCommand implements SuggestionCommand {
    public static final String COMMAND_WORD = "open";
    public static final String COMMAND_SHORTHAND = "o";

    private static final String RESPONSE_MESSAGE = "Open a note";

    private AbsolutePath path;
    private String title;

    public OpenSuggestionCommand(AbsolutePath path, String title) {
        Objects.requireNonNull(path);
        Objects.requireNonNull(title);
        this.path = path;
        this.title = title;
    }

    @Override
    public void execute(Model model) {
        // Nullity check
        Objects.requireNonNull(model);

        // Set response text
        model.setResponseText(RESPONSE_MESSAGE);

        // Set suggestions
        List<AbsolutePath> possiblePaths = getPossiblePaths(model);
        List<SuggestionItem> suggestions = getSuggestions(possiblePaths, model);

        model.setSuggestions(suggestions);
    }

    /**
     * Gets the list of possible absolute paths, based on the user's input path.
     * @param model The app's model.
     * @return List of possible absolute paths, based on the user's input path.
     */
    private List<AbsolutePath> getPossiblePaths(Model model) {
        Objects.requireNonNull(model);

        List<AbsolutePath> possiblePaths = new ArrayList<>();

        BlockTreeItem source = model.getBlockTree().get(path);
        List<String> components = path.getComponents();

        possiblePaths.add(path);

        List<BlockTreeItem> children = source.getBlockChildren();
        if (children.size() != 0) {
            for (BlockTreeItem child : children) {
                getChildDfs(child, components, possiblePaths);
            }
        }

        return possiblePaths;
    }

    /**
     * Traverses the tree block starting from the block with the path that the user inputs.
     * @param curr Current BlockTreeItem.
     * @param components The components of the current path.
     * @param possiblePaths The list of possible paths.
     */
    private void getChildDfs(BlockTreeItem curr, List<String> components,
        List<AbsolutePath> possiblePaths) {
        List<String> newComponents = new ArrayList<>();
        newComponents.addAll(components);
        newComponents.add(curr.getTitle().getText());

        List<BlockTreeItem> children = curr.getBlockChildren();

        if (children.size() == 0) { // if last element in THAT current path, add curr path to list of possible paths.
            AbsolutePath newPath = AbsolutePath.fromComponents(newComponents);
            possiblePaths.add(newPath);
        } else {
            for (BlockTreeItem child : children) {
                getChildDfs(child, newComponents, possiblePaths);
            }
        }
    }

    private List<SuggestionItem> getSuggestions(List<AbsolutePath> possiblePaths, Model model) {
        Objects.requireNonNull(possiblePaths);
        Objects.requireNonNull(model);

        return possiblePaths.stream()
                .map(path -> {
                    String displayText = path.getStringRepresentation();
                    String updatedInput = (model.getInput()).replace(title, displayText);
                    Runnable action = () -> {
                        model.setInput(updatedInput);
                    };
                    return new SuggestionItemImpl(displayText, action);
                })
                .collect(Collectors.toList());
    }
}
