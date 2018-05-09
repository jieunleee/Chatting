package com.example.jieunlee.chatting;

/**
 * Created by jieunlee on 2018-05-04.
 */
//제발되라

public class Chat {

    public String email;
    public String text;

    public Chat() { //지우면 실행안됨.
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Chat(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

