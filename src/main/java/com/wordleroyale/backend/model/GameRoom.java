package com.wordleroyale.backend.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class GameRoom {
    private final String roomId;
    private final Map<String, Player> players = new LinkedHashMap<>();

    private boolean started = false;
    private String hostPlayerId;

    public GameRoom(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() { return roomId; }

    public boolean isStarted() { return started; }

    public String getHostPlayerId() { return hostPlayerId; }

    public void setHostPlayerId(String hostPlayerId) {
        this.hostPlayerId = hostPlayerId;
    }

    public void start() {
        this.started = true;
    }

    public Player addPlayer(Player player) {
        if (started) {
            throw new IllegalStateException("Game already started");
        }
        players.put(player.getId(), player);
        return player;
    }

    public Player getPlayer(String playerId) {
        return players.get(playerId);
    }

    public Collection<Player> getPlayers() {
        return players.values();
    }
}
