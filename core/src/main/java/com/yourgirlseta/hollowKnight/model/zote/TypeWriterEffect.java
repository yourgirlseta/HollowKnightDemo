package com.yourgirlseta.hollowKnight.model.zote;

public class TypeWriterEffect {

    private String fullText = "";

    private String visibleText = "";

    private float timer = 0f;

    private int index = 0;

    private float speed = 0.03f;

    public void start(
        String text
    ) {

        fullText = text;

        visibleText = "";

        timer = 0f;

        index = 0;
    }

    public void update(
        float delta
    ) {

        timer += delta;

        if (
            timer >= speed
                &&
                index < fullText.length()
        ) {

            visibleText +=
                fullText.charAt(index);

            index++;

            timer = 0f;
        }
    }

    public String getVisibleText() {

        return visibleText;
    }

    public boolean isFinished() {

        return index >= fullText.length();
    }

    public void finishInstantly() {

        visibleText =
            fullText;

        index =
            fullText.length();
    }
}
