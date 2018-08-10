package com.egloffgroup.flashchatnewfirebase;

public class InstantMessage {
    private String mMessage;
    private String mAuthor;

    public InstantMessage() {
    }

    public InstantMessage(String message, String author) {
        mMessage = message;
        mAuthor = author;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }
}
