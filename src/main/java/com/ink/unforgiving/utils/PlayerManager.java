package com.ink.unforgiving.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerManager {
  public static List<UUID> players_in_lobby = new ArrayList<UUID>();
  
  public static List<UUID> get_players_in_lobby() {
    List<UUID> players = new ArrayList<UUID>();
    // Loop through players in the network.
    for (NetworkPlayerInfo playerInfo : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap())
      // Find player entity from network information.
      players.add(playerInfo.getGameProfile().getId());
    return players;
  }
  
  public static EntityPlayer get_player_from_uuid(UUID uuid) {
    return Minecraft.getMinecraft().theWorld.getPlayerEntityByUUID(uuid);
  }
  
  public static UUID find_player_uuid(String playerName) {
    try {
      NetworkPlayerInfo connectedPlayer = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(playerName);
      return connectedPlayer.getGameProfile().getId();
    } catch (NullPointerException e) {return null;}
  }
}
