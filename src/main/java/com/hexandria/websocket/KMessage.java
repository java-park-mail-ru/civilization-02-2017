package com.hexandria.websocket;

/**
 * Created by root on 25.04.17.
 */
import org.jetbrains.annotations.NotNull;

/**
 * Created by Solovyev on 06/04/16.
 */
@SuppressWarnings("NullableProblems")
public class
KMessage {
    @NotNull
    private String type;
    @NotNull
    private String content;
    //Here could be your versioning system
    //private int version = VERSION;

    @NotNull
    public String getType() {
        return type;
    }
    @NotNull
    public String getContent() {
        return content;
    }

    public KMessage() {
    }

    public KMessage(@NotNull String type, @NotNull String content) {
        this.type = type;
        this.content = content;
    }

    public KMessage(@NotNull Class clazz, @NotNull String content) {
        //noinspection ConstantConditions
        this(clazz.getName(), content);
    }
}
