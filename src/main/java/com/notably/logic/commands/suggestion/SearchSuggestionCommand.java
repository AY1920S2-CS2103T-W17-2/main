package com.notably.logic.commands.suggestion;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import com.notably.commons.path.AbsolutePath;
import com.notably.logic.commands.OpenCommand;
import com.notably.logic.commands.exceptions.CommandException;
import com.notably.model.Model;
import com.notably.model.block.BlockTree;
import com.notably.model.block.BlockTreeItem;
import com.notably.model.suggestion.SuggestionItem;
import com.notably.model.suggestion.SuggestionItemImpl;

/**
 * Represents a suggestion command object to search through all the notes based on keyword.
 */
public class SearchSuggestionCommand implements SuggestionCommand {
    public static final String COMMAND_WORD = "search";
    private static final String RESPONSE_MESSAGE = "Search through all notes based on keyword";

    private String keyword;

    public SearchSuggestionCommand(String keyword) {
        Objects.requireNonNull(keyword);
        this.keyword = keyword;
    }

    @Override
    public void execute(Model model) {
        // Nullity check
        Objects.requireNonNull(model);

        // Set response text
        model.setResponseText(RESPONSE_MESSAGE);

        // Set suggestions
        List<SuggestionItem> suggestions = traverseTree(model);

        model.setSuggestions(suggestions);
    }

    /**
     * Traverses the whole block tree to get the blocks which contains the keyword.
     * @param model The app's model.
     * @return List of SuggestionItem with a display text of the block's path and action of opening the block.
     */
    private List<SuggestionItem> traverseTree(Model model) {
        Queue<AbsolutePath> pathQueue = new LinkedList<>();
        pathQueue.offer(AbsolutePath.fromString("/"));

        List<SuggestionItem> suggestions = new ArrayList<>();

        while (!pathQueue.isEmpty()) {
            AbsolutePath currentPath = pathQueue.poll();
            BlockTree blockTree = model.getBlockTree();

            List<BlockTreeItem> childrenBlocks = blockTree.get(currentPath).getBlockChildren();
            List<AbsolutePath> childrenPaths = childrenBlocks
                    .stream()
                    .map(item -> {
                        List<String> combinedComponents = new ArrayList<>(currentPath.getComponents());
                        combinedComponents.add(item.getTitle().getText());
                        AbsolutePath absolutePath = AbsolutePath.fromComponents(combinedComponents);

                        BlockTreeItem blockTreeItem = blockTree.get(absolutePath);
                        String blockBody = blockTreeItem.getBody().getText();

                        /* If a blockBody contains the keyword, we create SuggestionItem by
                           counting the keyword's number of occurrences in the blockBody, setting display text,
                           and setting action to open that particular block when the user chooses that suggestion. */
                        String bodyLowerCase = blockBody.toLowerCase();
                        if (bodyLowerCase.contains(keyword.toLowerCase())) {
                            String[] blockBodies = bodyLowerCase.split(keyword, -1);
                            int frequency = blockBodies.length - 1;
                            String displayText = absolutePath.getStringRepresentation();
                            Runnable action = () -> {
                                try {
                                    OpenCommand openCommand = new OpenCommand(absolutePath);
                                    openCommand.execute(model);
                                } catch (CommandException ex) {
                                    /* notes suggested will definitely be able to be opened,
                                       as the block actually exists.
                                       AssertionError would never be thrown */
                                    throw new AssertionError(ex.getMessage());
                                }
                            };
                            SuggestionItem suggestionItem = new SuggestionItemImpl(displayText, frequency, action);
                            suggestions.add(suggestionItem);
                        }
                        return absolutePath;
                    })
                    .collect(Collectors.toList());
            pathQueue.addAll(childrenPaths);
        }
        return suggestions;
    }
}
