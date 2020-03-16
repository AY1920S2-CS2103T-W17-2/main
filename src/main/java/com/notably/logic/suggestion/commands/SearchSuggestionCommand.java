package com.notably.logic.suggestion.commands;

import java.util.ArrayList;
import java.util.List;

import com.notably.commons.core.path.Path;
import com.notably.logic.suggestion.stubs.SuggestionListStub;
import com.notably.model.Model;
import com.notably.model.suggestion.SuggestionItem;
import com.notably.model.suggestion.SuggestionItemImpl;

/**
 * Represents a suggestion command object to search a note.
 */
public class SearchSuggestionCommand implements SuggestionCommand {
    private Path path;

    public SearchSuggestionCommand(Path path) {
        this.path = path;
    }

    @Override
    public void execute(Model model) {
        List<String> suggestionList = getSuggestionList(path);
        List<SuggestionItem> suggestions = new ArrayList<>();

        for (String suggestion : suggestionList) {
            Runnable action = () -> {
                model.setCommandInputText("search " + suggestion);
            };
            SuggestionItem suggestionItem = new SuggestionItemImpl(suggestion, action);
            suggestions.add(suggestionItem);
        }

        model.setSuggestions(suggestions);
    }

    private List<String> getSuggestionList(Path path) {
        List<String> paths = path.getComponents();

        // TODO: traverse tree. For now will just use stubs.
        SuggestionListStub suggestionListStub = new SuggestionListStub();
        List<String> suggestionList = suggestionListStub.getSuggestionList();
        return suggestionList;
    }
}
