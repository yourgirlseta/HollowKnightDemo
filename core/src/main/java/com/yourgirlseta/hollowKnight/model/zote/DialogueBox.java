package com.yourgirlseta.hollowKnight.model.zote;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DialogueBox {

    private BitmapFont font;

    private Texture background;

    private TypeWriterEffect writer;

    private DialogueManager dialogueManager;

    public DialogueBox(
        DialogueManager dialogueManager
    ) {

        this.dialogueManager =
            dialogueManager;

        font =
            new BitmapFont();

        background =
            new Texture(
                "hk-textbox-for-if-anybody-needs-it-to-make-silkpost-or-any-v0-1ytk0y8l6oyf1 (1).png"
            );

        writer =
            new TypeWriterEffect();
    }

    public void startCurrentLine() {

        writer.start(
            dialogueManager
                .getCurrentLine()
                .getText()
        );
    }

    public void update(
        float delta
    ) {

        writer.update(delta);
    }

    public void render(
        SpriteBatch batch
    ) {

        if (
            !dialogueManager.isActive()
        ) {
            return;
        }

        batch.draw(
            background,
            100,
            50,
            1700,
            250
        );

        font.draw(
            batch,
            writer.getVisibleText(),
            150,
            200
        );
    }

    public TypeWriterEffect getWriter() {

        return writer;
    }

    public boolean isFinished() {

        return writer.isFinished();
    }

    public void clear() {

        writer.start("");
    }
}
