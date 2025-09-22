package com.ink.unforgiving.commands;

import com.ink.unforgiving.configs.ConfigCache;
import com.ink.unforgiving.configs.ConfigKOS;
import com.ink.unforgiving.types.FormattedPlayer;
import com.ink.unforgiving.types.PlayerPriorities;
import com.ink.unforgiving.utils.Messaging;
import com.ink.unforgiving.utils.PlayerManager;
import net.minecraft.command.ICommandSender;

import java.util.UUID;

public class SubcommandAdd extends Subcommand {
  @Override
  protected String get_name() {
    return "add";
  }
  
  @Override
  public void execute(ICommandSender sender, String[] args) {
    if (args.length <= 2) {
      Messaging.send("command.unforgiving.kos.add.usage");
      return;
    }
    
    // Get args
    String playerName = args[1];
    
    PlayerPriorities type;
    try {
      type = PlayerPriorities.valueOf(args[2].toUpperCase());
    } catch (Exception e) {
      Messaging.send("command.unforgiving.kos.add.type", args[2]);
      return;
    }
    
    // TODO: Clean this up
    StringBuilder reason = new StringBuilder();
    if (args.length != 3) for (int i = 3; i < args.length; i++)
      reason.append(args[i]).append(" ");
    else reason.append("[none]");
    
    // Add player to KOS list
    UUID playerUUID = PlayerManager.find_player_uuid(playerName);
    if (playerUUID != null) {
      if (!ConfigKOS.get_instance().add_player(new FormattedPlayer(playerUUID, type, playerName, reason.toString()),
          true)) {
        Messaging.send("command.unforgiving.kos.add.duplicate", playerName);
        return;
      }
      
      Messaging.send("command.unforgiving.kos.add.success", playerName);
      return;
    }
    
    // Cache player
    if (!ConfigCache.get_instance().add_cached_player(playerName, true)) {
      Messaging.send("command.unforgiving.kos.add.duplicate", playerName);
      return;
    }
    
    ConfigKOS.get_instance().add_player(new FormattedPlayer(null, type, playerName, reason.toString()), false);
    Messaging.send("command.unforgiving.kos.add.cache", playerName);
  }
}
