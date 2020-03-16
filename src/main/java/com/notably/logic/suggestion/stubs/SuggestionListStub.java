package com.notably.logic.suggestion.stubs;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hardcoded suggestion list object.
 */
public class SuggestionListStub {
    public SuggestionListStub() {

    }

    public List<String> getSuggestionList() {
        List<String> suggestionListStub = new ArrayList<>();
        suggestionListStub.add("/y2s2/cs2103t");
        suggestionListStub.add("/y2s2/cs2103t/IP");
        suggestionListStub.add("/y2s2/cs2103t/TP");
        suggestionListStub.add("/y2s2/cs2103t/Quiz");
        suggestionListStub.add("/y2s2/cs2101");

        return suggestionListStub;
    }
}
