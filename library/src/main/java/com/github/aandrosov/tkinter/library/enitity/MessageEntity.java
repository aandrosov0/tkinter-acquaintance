package com.github.aandrosov.tkinter.library.enitity;

import com.google.gson.annotations.SerializedName;

public class MessageEntity extends Entity {

    @SerializedName("author_id")
    private long authorId;

    @SerializedName("destination_id")
    private long destinationId;

    private String text;

    public MessageEntity() {
        super(0);
    }

    public MessageEntity(long id, long authorId, long destinationId, String text) {
        super(id);
        this.authorId = authorId;
        this.destinationId = destinationId;
        this.text = text;
    }

    public MessageEntity(long authorId, long destinationId, String text) {
        this(0, authorId, destinationId, text);
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
