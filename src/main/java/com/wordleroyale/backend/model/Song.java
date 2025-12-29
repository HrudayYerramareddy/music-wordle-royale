package com.wordleroyale.backend.model;

public class Song {
    private final String id;
    private final String title;
    private final String artist;

    public Song(String id, String title, String artist) {
        this.id = id;
        this.title = title;
        this.artist = artist;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
}
