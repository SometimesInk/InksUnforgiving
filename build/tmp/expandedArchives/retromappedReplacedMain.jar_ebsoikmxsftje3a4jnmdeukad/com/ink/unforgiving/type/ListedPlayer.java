package com.ink.unforgiving.type;

import java.util.List;
import java.util.UUID;

public class ListedPlayer {
    private String name;
    private String reason;
    private UUID uuid;
    private ListedPlayerType type;

    public ListedPlayer(String name, String reason, UUID uuid, ListedPlayerType type) {
        this.name = name;
        this.reason = reason;
        this.uuid = uuid;
        this.type = type;
    }

    /**
     * @param formattedEntry Must be formatted as "uuid!type!name!reason"
     */
    public ListedPlayer(String formattedEntry) {
        String[] split = formattedEntry.split("!");
        this.uuid = split[0].equals("null") ? null : UUID.fromString(split[0]);
        this.type = ListedPlayerType.valueOf(split[1].toUpperCase());
        this.name = split[2];
        this.reason = split[3];
    }

    /**
     * @return String formatted as "uuid!type!name!reason"
     */
    @Override
    public String toString() {
        if(uuid == null) return "null!" + type.toString() + "!" + name + "!" + reason;
        return uuid.toString() + "!" + type.toString() + "!" + name + "!" + reason;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public UUID getUUID() {
        return uuid;
    }

    public ListedPlayerType getType() {
        return type;
    }

    public ListedPlayer setType(ListedPlayerType type) {
        this.type = type;
        return this;
    }

    public ListedPlayer setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public ListedPlayer setName(String name) {
        this.name = name;
        return this;
    }

    public ListedPlayer setUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public static ListedPlayer find(ListedPlayer player, List<ListedPlayer> list) {
        for(ListedPlayer listedPlayer : list) {
            if(listedPlayer.getUUID().equals(player.getUUID())) {
                return listedPlayer;
            }
        }
        return null;
    }

    public static ListedPlayer find(String name, List<ListedPlayer> list) {
        for(ListedPlayer listedPlayer : list) {
            if(listedPlayer.getName().equalsIgnoreCase(name)) {
                return listedPlayer;
            }
        }
        return null;
    }

    public static ListedPlayer find(UUID uuid, List<ListedPlayer> list) {
        if(uuid == null) return null;
        for(ListedPlayer listedPlayer : list) {
            if(listedPlayer.getUUID() == null) continue;
            if(listedPlayer.getUUID().equals(uuid)) return listedPlayer;
        }
        return null;
    }

    public static boolean contains(String name, List<ListedPlayer> list) {
        for(ListedPlayer listedPlayer : list) {
            if(listedPlayer.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(UUID uuid, List<ListedPlayer> list) {
        for(ListedPlayer listedPlayer : list) {
            if(listedPlayer.getUUID().equals(uuid)) {
                return true;
            }
        }
        return false;
    }
}
