package com.github.aandrosov.tkinter.server.entity;

import com.github.aandrosov.tkinter.server.database.Column;
import com.github.aandrosov.tkinter.server.database.Table;

@Table("message")
public class MessageEntity extends Entity {

    @Column("author_id")
    private long authorId;

    @Column("destination_id")
    private long destinationId;

    @Column("text")
    private String text;

    public MessageEntity() {

    }

    public MessageEntity(long id, long authorId, long destinationId, String text) {
        setId(id);
        this.authorId = authorId;
        this.destinationId = destinationId;
        this.text = text;
    }

    public MessageEntity(long authorId, long destinationId, String text) {
        this(0, authorId, destinationId, text);
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + getId() +
                ",\"author_id\":" + authorId +
                ",\"destination_id\":" + destinationId +
                ",\"text\":\"" + text + "\"}";
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
