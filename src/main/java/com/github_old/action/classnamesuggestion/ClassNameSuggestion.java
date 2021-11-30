package com.github_old.action.classnamesuggestion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ClassNameSuggestion {

    private final List<String> classNames;

    public ClassNameSuggestion(List<String> classNames) {
        this.classNames = classNames;
    }

    public List<String> getSuggestions(String inp) {
        List<String> res = new ArrayList<>();

        if (inp == null || inp.isEmpty() || inp.isBlank()) return res;

        for (String className : classNames) {
            if (res.size() >= 10) break;
            if (className.contains(inp))
                res.add(className);
        }
        res.sort(Comparator.comparingInt(s -> s.length()));
        return res;
    }
}