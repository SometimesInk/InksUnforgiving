package com.ink.unforgiving.commands;

import com.ink.unforgiving.configs.ConfigKOS;
import com.ink.unforgiving.types.FormattedPlayer;
import com.ink.unforgiving.utils.Messaging;
import net.minecraft.command.ICommandSender;

public class SubcommandGet extends Subcommand {
  @Override
  protected String get_name() {
    return "get";
  }
  
  @Override
  public void execute(ICommandSender sender, String[] args) {
    if (args.length != 2) {
      Messaging.send("command.unforgiving.kos.get.usage");
      return;
    }
    
    // Find player
    String playerName = args[1];
    FormattedPlayer player = FormattedPlayer.find(playerName, ConfigKOS.get_instance().get_players());
    
    if (player == null) {
      Messaging.send("command.unforgiving.kos.get.contain", playerName);
      return;
    }
    
    Messaging.send("command.unforgiving.kos.get.1", playerName);
    Messaging.send("command.unforgiving.kos.get.2", player.type.get_formatted_name());
    Messaging.send("command.unforgiving.kos.get.3", player.reason);
  }
}
