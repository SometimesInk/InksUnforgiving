package com.ink.unforgiving.config;

import com.ink.unforgiving.type.ListedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.common.config.*;
import org.lwjgl.Sys;

import java.util.*;

public class ConfigCache extends Config {
    public static ConfigCache INSTANCE = new ConfigCache();

    private Property cachedPlayersProperty;
    private List<String> cachedPlayers = new ArrayList<String>();

    @Override
    public String fileName() {
        return "kosCache";
    }

    @Override
    public void reload() {
        // Get properties from
        cachedPlayersProperty = config.get("Unforgiving", "Unforgiven_Players", new String[0]);

        // Populate the list
        cachedPlayers.clear();
        cachedPlayers.addAll(Arrays.asList(cachedPlayersProperty.getStringList()));

        // Save the list
        save();
    }

    public void addCachedPlayer(String player) {
        // Get point-less list
        List<String> cachedPlayers = getCachedPlayers();

        // Set variable
        cachedPlayers.add(player);

        // Save
        setCachedPlayers(cachedPlayers);
    }

    public void removeCachedPlayer(String player) {
        // Get point-less list
        List<String> cachedPlayers = getCachedPlayers();

        // Set variable
        cachedPlayers.remove(player);

        // Save
        setCachedPlayers(cachedPlayers);
    }

    public List<String> getCachedPlayers() {
        return cachedPlayers;
    }

    public void setCachedPlayers(List<String> players) {
        // Set variable
        this.cachedPlayers = players;

        // Set config
        cachedPlayersProperty.set(players.toArray(new String[0]));

        // Save config
        save();
    }

    public void reloadPlayers() {
        // Check if there are any players to reload
        for (NetworkPlayerInfo player : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            if (ConfigCache.getInstance().getCachedPlayers().contains(player.getGameProfile().getName())) {
                // Re-add player to KOS list with updated UUID
                try {
                    ConfigKOS.getInstance().addUnforgivenPlayer(ConfigKOS.getInstance().removeUnforgivenPlayer(ListedPlayer
                            .find(player.getGameProfile().getName(), ConfigKOS.getInstance().getUnforgivenPlayers()))
                            .setUUID(player.getGameProfile().getId()), false);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    public static ConfigCache getInstance() {
        return INSTANCE;
    }
}
