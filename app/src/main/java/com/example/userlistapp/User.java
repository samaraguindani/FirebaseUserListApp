package com.example.userlistapp;

public class User {
    private String name;

    public User() {
        // Construtor vazio obrigatório para o Firestore
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
