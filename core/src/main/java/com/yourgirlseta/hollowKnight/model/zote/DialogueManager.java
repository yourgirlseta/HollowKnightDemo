package com.yourgirlseta.hollowKnight.model.zote;

import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.enums.DialogueState;
import com.yourgirlseta.hollowKnight.model.enums.Language;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;

public class DialogueManager {
    private final SettingsManager settingsManager;
    private Array<DialogueLine> lines;

    private Array<DialogueLine> introLines;

    private Array<DialogueLine> precepts;

    private Array<DialogueLine> angryLines;

    private int currentIndex = 0;

    private boolean active = false;

    private DialogueState state =
        DialogueState.INTRO;

    public DialogueManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
        loadDialogues();
    }

    public void reloadLanguage() {

        boolean wasActive = active;

        active = false;

        loadDialogues();

        if (wasActive) {

            startDialogue();
        }
    }

    private void loadDialogues() {
        if (introLines == null)
            introLines = new Array<>();
        else
            introLines.clear();

        if (precepts == null)
            precepts = new Array<>();
        else
            precepts.clear();

        if (angryLines == null)
            angryLines = new Array<>();
        else
            angryLines.clear();

        if (isFrench()) {

            introLines.add(new DialogueLine("Je suis Zote le Puissant !"));
            introLines.add(new DialogueLine("Un chevalier de grande renommée !"));

            precepts.add(new DialogueLine("Précepte Un : Toujours gagner."));
            precepts.add(new DialogueLine("Précepte Deux : Ne jamais les laisser se moquer de toi."));
            precepts.add(new DialogueLine("Précepte Trois : Toujours être reposé."));

            angryLines.add(new DialogueLine("Comment oses-tu me frapper ?!"));
            angryLines.add(new DialogueLine("Affronte la colère de Zote !"));

        } else {

            introLines.add(new DialogueLine("I am Zote the Mighty!"));
            introLines.add(new DialogueLine("A knight of great renown!"));

            precepts.add(new DialogueLine("Precept One: Always Win."));
            precepts.add(new DialogueLine("Precept Two: Never Let Them Laugh At You."));
            precepts.add(new DialogueLine("Precept Three: Always Be Rested."));

            angryLines.add(new DialogueLine("How dare you strike me?!"));
            angryLines.add(new DialogueLine("Face the wrath of Zote!"));
        }
    }

    public Language getLanguage() {

        return settingsManager
            .getSettingsData()
            .getLanguage();
    }

    public boolean isFrench() {

        return getLanguage()
            == Language.FRENCH;
    }

    public void startDialogue() {

        active = true;

        currentIndex = 0;

        switch (state) {

            case INTRO:
                lines = introLines;
                break;

            case PRECEPTS:

                lines = precepts;

                lines.shuffle();

                break;

            case ANGRY:
                lines = angryLines;
                break;
        }
    }

    public void nextLine() {

        currentIndex++;

        if (currentIndex >= lines.size) {

            active = false;

            if (
                state == DialogueState.INTRO
            ) {

                state =
                    DialogueState.PRECEPTS;
            }

            else if (
                state == DialogueState.ANGRY
            ) {

                state =
                    DialogueState.PRECEPTS;
            }
        }
    }

    public void setState(
        DialogueState dialogueState
    ) {

        this.state =
            dialogueState;
    }

    public boolean isActive() {

        return active;
    }

    public DialogueLine getCurrentLine() {

        return lines.get(currentIndex);
    }
}
