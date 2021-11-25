package com.github2.input.classnamesuggestion.dropdowntextsuggestion.suggestionclient.impl;

import com.github2.input.classnamesuggestion.dropdowntextsuggestion.suggestionclient.SuggestionClient;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.function.Function;

public class TextComponentWordSuggestionClient implements SuggestionClient<JTextComponent> {
    private Function<String, List<String>> suggestionProvider;

    public TextComponentWordSuggestionClient(Function<String, List<String>> suggestionProvider) {
        this.suggestionProvider = suggestionProvider;
    }

    @Override
    public Point getPopupLocation(JTextComponent invoker) {
        int caretPosition = invoker.getCaretPosition();
        try {
            Rectangle2D rectangle2D = invoker.modelToView(caretPosition);
            return new Point((int) rectangle2D.getX(), (int) (rectangle2D.getY() + rectangle2D.getHeight()));
        } catch (BadLocationException e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public void setSelectedText(JTextComponent tp, String selectedValue) {
        int cp = tp.getCaretPosition();
        try {
            if (cp == 0 || tp.getText(cp - 1, 1).trim().isEmpty()) {
                tp.getDocument().insertString(cp, selectedValue, null);
            } else {
                int previousWordIndex = Utilities.getPreviousWord(tp, cp);
                String text = tp.getText(previousWordIndex, cp - previousWordIndex);
                if (selectedValue.startsWith(text)) {
                    tp.getDocument().insertString(cp, selectedValue.substring(text.length()), null);
                } else {
                    tp.getDocument().insertString(cp, selectedValue, null);
                }
            }
        } catch (BadLocationException e) {
            System.err.println(e);
        }
    }

    @Override
    public List<String> getSuggestions(JTextComponent tp) {
        try {
            int cp = tp.getCaretPosition();
            if (cp != 0) {
                String text = tp.getText(cp - 1, 1);
                if (text.trim().isEmpty()) {
                    return null;
                }
            }
            int previousWordIndex = Utilities.getPreviousWord(tp, cp);
            String text = tp.getText(previousWordIndex, cp - previousWordIndex);
            return suggestionProvider.apply(text.trim());
        } catch (BadLocationException e) {
            System.err.println(e);
        }
        return null;
    }
}
