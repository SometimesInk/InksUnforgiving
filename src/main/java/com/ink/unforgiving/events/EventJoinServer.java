package com.ink.unforgiving.events;

import com.ink.unforgiving.configs.ConfigCache;
import com.ink.unforgiving.utils.Messaging;
import com.ink.unforgiving.utils.PlayerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

public class EventJoinServer {
  // private List<UUID> players_last_join = new ArrayList<UUID>();

  @SubscribeEvent
  public void on_join_world(EntityJoinWorldEvent event) {
    if (event.entity instanceof EntityPlayer && Minecraft.getMinecraft().thePlayer != null) {
      PlayerManager.players_in_lobby = PlayerManager.get_players_in_lobby();

      /*
      // Get players that have left since last join
      players_last_join.removeAll(PlayerManager.players_in_lobby);

      if (players_last_join.size() > 1)
        Messaging.send_warning_message("More than one player has left within the same event, cannot call leave event " +
            "on both players.");
      else if (players_last_join.size() == 1) on_leave_world(event);

      players_last_join = PlayerManager.players_in_lobby;
      */

      try {
        ConfigCache.get_instance().reload_players();
      } catch (Exception e) {
        Messaging.send_error_message(Arrays.toString(e.getStackTrace()));
      }
    }
  }

  // private void on_leave_world(EntityJoinWorldEvent event) {}
}