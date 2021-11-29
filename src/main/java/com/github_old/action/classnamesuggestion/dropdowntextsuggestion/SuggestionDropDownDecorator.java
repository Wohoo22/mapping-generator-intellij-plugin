package com.github_old.action.classnamesuggestion.dropdowntextsuggestion;

import com.github_old.action.classnamesuggestion.dropdowntextsuggestion.suggestionclient.SuggestionClient;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import static java.awt.event.KeyEvent.*;

public class SuggestionDropDownDecorator<C extends JComponent> {
    private final C invoker;
    private final SuggestionClient<C> suggestionClient;
    private JPopupMenu popupMenu;
    private JList<String> listComp;
    DefaultListModel<String> listModel;
    private boolean disableTextEvent;

    public SuggestionDropDownDecorator(C invoker, SuggestionClient<C> suggestionClient) {
        this.invoker = invoker;
        this.suggestionClient = suggestionClient;
    }

    public static <C extends JComponent> void decorate(C component, SuggestionClient<C> suggestionClient) {
        SuggestionDropDownDecorator<C> d = new SuggestionDropDownDecorator<>(component, suggestionClient);
        d.init();
    }

    public void init() {
        initPopup();
        initSuggestionCompListener();
        initInvokerKeyListeners();
    }

    private void initPopup() {
        popupMenu = new JPopupMenu();
        listModel = new DefaultListModel<>();
        listComp = new JList<>(listModel);
        listComp.setBorder(BorderFactory.createEmptyBorder(0, 2, 5, 2));
        listComp.setFocusable(false);
        popupMenu.setFocusable(false);
        popupMenu.add(listComp);
    }

    private void initSuggestionCompListener() {
        if (invoker instanceof JTextComponent) {
            JTextComponent tc = (JTextComponent) invoker;
            tc.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    update(e);
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update(e);
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update(e);
                }

                private void update(DocumentEvent e) {
                    if (disableTextEvent) {
                        return;
                    }
                    SwingUtilities.invokeLater(() -> {
                        List<String> suggestions = suggestionClient.getSuggestions(invoker);
                        if (suggestions != null && !suggestions.isEmpty()) {
                            showPopup(suggestions);
                        } else {
                            popupMenu.setVisible(false);
                        }
                    });
                }
            });
        }//todo init invoker components other than text components
    }

    private void showPopup(List<String> suggestions) {
        listModel.clear();
        suggestions.forEach(listModel::addElement);
        Point p = suggestionClient.getPopupLocation(invoker);
        if (p == null) {
            return;
        }
        popupMenu.pack();
        listComp.setSelectedIndex(0);
        popupMenu.show(invoker, (int) p.getX(), (int) p.getY());
    }

    private void initInvokerKeyListeners() {
        //not using key inputMap cause that would override the original handling
        invoker.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == VK_ENTER) {
                    selectFromList(e);
                } else if (e.getKeyCode() == VK_UP) {
                    moveUp(e);
                } else if (e.getKeyCode() == VK_DOWN) {
                    moveDown(e);
                } else if (e.getKeyCode() == VK_ESCAPE) {
                    popupMenu.setVisible(false);
                }
            }
        });
    }

    private void selectFromList(KeyEvent e) {
        if (popupMenu.isVisible()) {
            int selectedIndex = listComp.getSelectedIndex();
            if (selectedIndex != -1) {
                popupMenu.setVisible(false);
                String selectedValue = listComp.getSelectedValue();
                disableTextEvent = true;
                suggestionClient.setSelectedText(invoker, selectedValue);
                disableTextEvent = false;
                e.consume();
            }
        }
    }

    private void moveDown(KeyEvent keyEvent) {
        if (popupMenu.isVisible() && listModel.getSize() > 0) {
            int selectedIndex = listComp.getSelectedIndex();
            if (selectedIndex < listModel.getSize()) {
                listComp.setSelectedIndex(selectedIndex + 1);
                keyEvent.consume();
            }
        }
    }

    private void moveUp(KeyEvent keyEvent) {
        if (popupMenu.isVisible() && listModel.getSize() > 0) {
            int selectedIndex = listComp.getSelectedIndex();
            if (selectedIndex > 0) {
                listComp.setSelectedIndex(selectedIndex - 1);
                keyEvent.consume();
            }
        }
    }
}