package com.ink.unforgiving.commands;

import com.ink.unforgiving.configs.ConfigKOS;
import com.ink.unforgiving.types.FormattedPlayer;
import com.ink.unforgiving.utils.Messaging;
import net.minecraft.command.ICommandSender;

public class SubcommandList extends Subcommand {
  @Override
  protected String get_name() {
    return "list";
  }
  
  @Override
  public void execute(ICommandSender sender, String[] args) {
    if (args.length != 1) {
      Messaging.send("command.unforgiving.kos.list.usage");
      return;
    }
    
    Messaging.send("command.unforgiving.kos.list.header", ConfigKOS.get_instance().get_players().size());
    
    for (FormattedPlayer player : ConfigKOS.get_instance().get_players())
      Messaging.send("command.unforgiving.kos.list.entry", player.type.get_color(), player.name, player.reason);
  }
}
