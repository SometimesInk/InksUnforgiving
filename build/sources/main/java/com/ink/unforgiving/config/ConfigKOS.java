package com.ink.unforgiving.config;

import com.ink.unforgiving.UnforgivingMod;
import com.ink.unforgiving.maths.IVector2;
import com.ink.unforgiving.type.ListedPlayer;
import com.ink.unforgiving.type.ListedPlayerType;
import ibxm.Player;
import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.*;

public class ConfigKOS extends Config {
    public static ConfigKOS INSTANCE = new ConfigKOS();

    private Property unforgivenPlayersProperty;
    private List<ListedPlayer> unforgivenPlayers = new ArrayList<ListedPlayer>();

    private Property renderedTypesProperty;
    private List<ListedPlayerType> renderedTypes = new ArrayList<ListedPlayerType>();

    private Property renderOffsetProperty;
    private IVector2 renderOffset = new IVector2(0, 0);

    @Override
    public String fileName() {
        return "kos";
    }

    public void reload() {
        // Get properties from file
        unforgivenPlayersProperty = config.get("Unforgiving", "Unforgiven_Players", new String[0]);
        String[] list = unforgivenPlayersProperty.getStringList();

        List<String> typeList = new ArrayList<String>();
        for (ListedPlayerType type : ListedPlayerType.values()) {
            typeList.add(type.toString());
        }
        renderedTypesProperty = config.get("Unforgiving", "Rendered_Types", typeList.toArray(new String[0]));

        renderOffsetProperty = config.get("Unforgiving", "Render_Offset", new int[] {0, 0});

        // Populate the lists
        unforgivenPlayers.clear();
        for(String s1 : list) unforgivenPlayers.add(new ListedPlayer(s1));

        renderedTypes.clear();
        for(String s2 : typeList) renderedTypes.add(ListedPlayerType.valueOf(s2));

        int[] offset = renderOffsetProperty.getIntList();
        renderOffset = new IVector2(offset[0], offset[1]);

        // Save the list
        save();
    }

    public List<ListedPlayer> getUnforgivenPlayers() {
        return unforgivenPlayers;
    }

    private void setUnforgivenPlayers(List<ListedPlayer> unforgivenPlayers) {
        this.unforgivenPlayers = unforgivenPlayers;

        // Set config
        List<String> list = new ArrayList<String>();
        for(ListedPlayer p : unforgivenPlayers) list.add(p.toString());
        unforgivenPlayersProperty.set(list.toArray(new String[0]));

        // Save config
        save();
    }

    public void addUnforgivenPlayer(ListedPlayer player, boolean checkForDuplicates) throws Exception {
        List<ListedPlayer> unforgivenPlayers = getUnforgivenPlayers();

        // Check if UUID is null
        if(player.getUUID() == null) {
            // Add to cache
            ConfigCache.getInstance().addCachedPlayer(player.getName());
        }

        // Check if player is already in the KOS
        if(checkForDuplicates && ListedPlayer.contains(player.getName(), unforgivenPlayers))
            throw new Exception("Player is already on the unforgiven list");

        unforgivenPlayers.add(player);
        setUnforgivenPlayers(unforgivenPlayers);
    }

    public ListedPlayer removeUnforgivenPlayer(ListedPlayer player) {
        List<ListedPlayer> unforgivenPlayers = getUnforgivenPlayers();
        for(ListedPlayer p : unforgivenPlayers)
            if (p.toString().equals(player.toString())) {
                unforgivenPlayers.remove(p);
                setUnforgivenPlayers(unforgivenPlayers);
                return p;
            }
        return null;
    }

    /**
     * Removes a player from the unforgiven list by UUID
     */
    public void removeUnforgivenPlayer(UUID uuid) {
        List<ListedPlayer> unforgivenPlayers = getUnforgivenPlayers();
        for(ListedPlayer p : unforgivenPlayers) {
            if(p.getUUID().equals(uuid)) {
                unforgivenPlayers.remove(p);
                break;
            }
        }
        setUnforgivenPlayers(unforgivenPlayers);
    }

    public List<ListedPlayerType> getRenderedTypes() {
        return renderedTypes;
    }

    public void setRenderedTypes(List<ListedPlayerType> renderedTypes) {
        this.renderedTypes = renderedTypes;

        // Set config
        List<String> list = new ArrayList<String>();
        for(ListedPlayerType p : renderedTypes) list.add(p.toString());
        renderedTypesProperty.set(list.toArray(new String[0]));

        // Save config
        save();
    }

    public IVector2 getRenderOffset() {
        return renderOffset;
    }

    public void setRenderOffset(IVector2 renderOffset) {
        this.renderOffset = renderOffset;

        // Set config
        renderOffsetProperty.set(new int[] {renderOffset.x, renderOffset.y});

        // Save config
        save();
    }

    public static ConfigKOS getInstance() {
        return INSTANCE;
    }
}
