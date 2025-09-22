package com.ink.unforgiving.commands;

import com.ink.unforgiving.configs.ConfigCache;
import com.ink.unforgiving.configs.ConfigKOS;
import com.ink.unforgiving.maths.IVector2;
import com.ink.unforgiving.types.FormattedPlayer;
import com.ink.unforgiving.types.PlayerPriorities;
import com.ink.unforgiving.utils.Messaging;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

import java.util.*;

// TODO: Make use of ChatComponentTranslation to turn all text into translatables.

// TODO: Re-read code

public class CommandKOS extends CommandBase {
  @Override
  public String getCommandName() {
    return "kos";
  }
  
  @Override
  public int getRequiredPermissionLevel() {
    return 0;
  }
  
  @Override
  public String getCommandUsage(ICommandSender sender) {
    return "/kos <add|assign|get|list|reload|remove|toggle> [player] [type] [reason]";
  }
  
  @Override
  public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
    try {
      // Check for arguments
      if (args.length != 0) {
        
        if (args.length == 1) {
          // All values of CommandKOSArguments
          List<String> arguments = new ArrayList<String>();
          for (CommandKOSArguments argument : CommandKOSArguments.values())
            arguments.add(argument.toString());
          return getListOfStringsMatchingLastWord(args, arguments);
        }
        switch (CommandKOSArguments.valueOf(args[0].toUpperCase())) {
          case ADD:
            // Arguments
            switch (args.length) {
              case 2:
                return find_closest(get_players(), args[1]);
              case 3:
                return find_closest(Arrays.asList("ENEMY", "FRIEND", "TRUCE"), args[2]);
              case 4:
                return null;
            }
          case ASSIGN:
            // Arguments
            switch (args.length) {
              case 2:
                // All values of CommandKOSIdentifiers
                List<String> identifiers = new ArrayList<String>();
                for (CommandKOSIdentifiers identifier : CommandKOSIdentifiers.values())
                  identifiers.add(identifier.toString());
                return find_closest(identifiers, args[1]);
              case 3:
                // Depends on the identifier
                switch (CommandKOSIdentifiers.valueOf(args[1].toUpperCase())) {
                  case NAME:
                    // Return KOS players' names
                    List<FormattedPlayer> kos = ConfigKOS.get_instance().get_players();
                    List<String> players = new ArrayList<String>();
                    for (FormattedPlayer player : kos) players.add(player.name);
                    return find_closest(players, args[2]);
                  case UUID:
                    // Return KOS players' UUIDs
                    List<FormattedPlayer> kos2 = ConfigKOS.get_instance().get_players();
                    List<String> players2 = new ArrayList<String>();
                    for (FormattedPlayer player : kos2) players2.add(player.uuid.toString());
                    return find_closest(players2, args[2]);
                }
              case 4:
                // All values of CommandKOSAssignments
                List<String> assignments = new ArrayList<String>();
                for (CommandKOSAssignments assignment : CommandKOSAssignments.values())
                  assignments.add(assignment.toString());
                return find_closest(assignments, args[3]);
              case 5:
                // Depends on the assignment
                switch (CommandKOSAssignments.valueOf(args[3].toUpperCase())) {
                  case NAME:
                  case UUID:
                  case REASON:
                    return null;
                  case TYPE:
                    // All values of PlayerPriorities
                    List<String> types = new ArrayList<String>();
                    for (PlayerPriorities type : PlayerPriorities.values()) types.add(type.toString());
                    return find_closest(types, args[4]);
                }
            }
          case GET:
          case LIST:
          case REMOVE:
            // Get all KOS players
            List<FormattedPlayer> kos = ConfigKOS.get_instance().get_players();
            List<String> players = new ArrayList<String>();
            for (FormattedPlayer player : kos) players.add(player.name);
            return players;
          case OFFSET:
            // Arguments
            switch (args.length) {
              case 2:
                // Get value for X
                return find_closest(Arrays.asList(ConfigKOS.get_instance().get_render_offset().x + "", "-"), args[1]);
              case 3:
                // Get value for Y
                return find_closest(Arrays.asList(ConfigKOS.get_instance().get_render_offset().y + "", "-"), args[2]);
            }
          case TOGGLE:
            // All values of CommandKOSToggles
            List<String> toggles = new ArrayList<String>();
            for (CommandKOSToggles toggle : CommandKOSToggles.values()) toggles.add(toggle.toString());
            return find_closest(toggles, args[1]);
          default:
            return null;
        }
      } else { throw new Exception(); }
    } catch (Exception e) {
      // All values of CommandKOSArguments
      List<String> arguments = new ArrayList<String>();
      for (CommandKOSArguments argument : CommandKOSArguments.values())
        arguments.add(argument.toString());
      return getListOfStringsMatchingLastWord(args, arguments);
    }
  }
  
  @Override
  public void processCommand(ICommandSender sender, String[] args) {
    // Check for arguments
    if (args.length != 0) {
      // Find command argument
      CommandKOSArguments command;
      try {
        command = CommandKOSArguments.valueOf(args[0].toUpperCase());
      } catch (IllegalArgumentException e) {
        Messaging.send_error_message("Invalid command. Usage: " + getCommandUsage(sender));
        return;
      }
      
      // Process command
      switch (command) {
        case HELP:
          Messaging.send_chat_message("Help for /kos command:");
          Messaging.send_chat_message("/kos add [player] [type] [reason] - Add a player to the KOS list");
          Messaging.send_chat_message("/kos assign [identifierType] [identifier] [assignmentType] " + "[assignment] " + "-" + " Assign a player to a group");
          Messaging.send_chat_message("/kos get [player] - Get a player's KOS status");
          Messaging.send_chat_message("/kos help - Display this message");
          Messaging.send_chat_message("/kos list - List all KOS players");
          Messaging.send_chat_message("/kos offset [player] [x] [z] - The rendering offset of the list");
          Messaging.send_chat_message("/kos reload - Reload the KOS list's cache");
          Messaging.send_chat_message("/kos remove [player] - Remove a player from the KOS list");
          Messaging.send_chat_message("/kos toggle [toggle] - Toggle a setting");
          return;
        case ADD:
          if (add_player(sender, Arrays.copyOfRange(args, 1, args.length))) return;
          break;
        case ASSIGN:
          if (assign_player(sender, Arrays.copyOfRange(args, 1, args.length))) return;
          break;
        case GET:
          if (get_player(sender, Arrays.copyOfRange(args, 1, args.length))) return;
          break;
        case LIST:
          if (list_players(sender, Arrays.copyOfRange(args, 1, args.length))) return;
          break;
        case OFFSET:
          if (handle_offset(sender, Arrays.copyOfRange(args, 1, args.length))) return;
        case RELOAD:
          if (reload_players(sender, Arrays.copyOfRange(args, 1, args.length))) return;
          break;
        case REMOVE:
          if (remove_player(sender, Arrays.copyOfRange(args, 1, args.length))) return;
          break;
        case TOGGLE:
          if (toggle_player(sender, Arrays.copyOfRange(args, 1, args.length))) return;
          break;
      }
      Messaging.send_error_message("Invalid command. Usage: " + getCommandUsage(sender));
    } else {
      Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
    }
  }
  
  private List<String> find_closest(List<String> strings, String arg) {
    // Sort alphabetically
    Collections.sort(strings);
    
    // Remove elements alphabetically before the first element
    while (strings.size() > 0 && strings.get(0).compareTo(arg) < 0) strings.remove(0);
    
    return strings;
  }
  
  private boolean assign_player(ICommandSender sender, String[] args) {
    // Check for arguments
    if (args.length != 4) {
      Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
      return true;
    }
    
    // Parse arguments
    CommandKOSIdentifiers identifierType;
    String identifier = args[1];
    CommandKOSAssignments assignmentType;
    String assignment = args[3];
    
    try {
      identifierType = CommandKOSIdentifiers.valueOf(args[0].toUpperCase());
      assignmentType = CommandKOSAssignments.valueOf(args[2].toUpperCase());
    } catch (IllegalArgumentException e) {
      Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
      return true;
    }
    
    // Find player
    FormattedPlayer player = null;
    switch (identifierType) {
      case NAME:
        player = FormattedPlayer.find(identifier, ConfigKOS.get_instance().get_players());
        break;
      case UUID:
        try {
          player = FormattedPlayer.find(UUID.fromString(identifier), ConfigKOS.get_instance().get_players());
        } catch (IllegalArgumentException e) {
          Messaging.send_error_message("Invalid UUID. Usage: " + getCommandUsage(sender));
          return true;
        }
        break;
      default:
        Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
        return true;
    }
    
    // Check if player is on KOS list
    if (player == null) {
      Messaging.send_error_message("Player '" + identifier + "' is not on KOS list.");
      return true;
    }
    
    // Assign player
    switch (assignmentType) {
      case NAME:
        // Remove player from KOS list
        ConfigKOS.get_instance().remove_player(player);
        
        // Add player to KOS list with new name
        try {
          ConfigKOS.get_instance().add_player(new FormattedPlayer(player.uuid, player.type, assignment,
              player.reason), true);
        } catch (Exception e) { e.printStackTrace(); }
        
        // Notify
        Messaging.send_chat_message(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD + player.name + EnumChatFormatting.GREEN + "' renamed to '" + EnumChatFormatting.GOLD + assignment + EnumChatFormatting.GREEN + "'.");
        return true;
      case REASON:
        // Remove player from KOS list
        ConfigKOS.get_instance().remove_player(player);
        
        // Add player to KOS list with new reason
        try {
          ConfigKOS.get_instance().add_player(new FormattedPlayer(player.uuid, player.type, player.name, assignment),
              true);
        } catch (Exception e) { e.printStackTrace(); }
        
        // Notify
        Messaging.send_chat_message(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD + player.name + EnumChatFormatting.GREEN + "' reason changed to '" + EnumChatFormatting.GOLD + assignment + EnumChatFormatting.GREEN + "'.");
        return true;
      case TYPE:
        // Remove player from KOS list
        ConfigKOS.get_instance().remove_player(player);
        
        // Add player to KOS list with new type
        try {
          ConfigKOS.get_instance().add_player(new FormattedPlayer(player.uuid,
              PlayerPriorities.valueOf(assignment.toUpperCase()), player.name, player.reason), true);
        } catch (Exception e) { e.printStackTrace(); }
        
        // Notify
        Messaging.send_chat_message(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD + player.name + EnumChatFormatting.GREEN + "' type changed to '" + EnumChatFormatting.GOLD + assignment + EnumChatFormatting.GREEN + "'.");
        return true;
      case UUID:
        // Remove player from KOS list
        ConfigKOS.get_instance().remove_player(player);
        
        // Add player to KOS list with new UUID
        try {
          ConfigKOS.get_instance().add_player(new FormattedPlayer(UUID.fromString(assignment), player.type,
              player.name, player.reason), true);
        } catch (Exception e) { e.printStackTrace(); }
        
        // Notify
        Messaging.send_chat_message(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD + player.name + EnumChatFormatting.GREEN + "' UUID changed to '" + EnumChatFormatting.GOLD + assignment + EnumChatFormatting.GREEN + "'.");
        return true;
      default:
        Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
        return true;
    }
  }
  
  private boolean handle_offset(ICommandSender sender, String[] args) {
    // Check for arguments
    if (args.length != 2) {
      Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
      return true;
    }
    
    // Parse arguments
    int x, y = -1;
    try {
      x = args[0].equals("-") ? ConfigKOS.get_instance().get_render_offset().x : Integer.parseInt(args[0]);
      y = args[1].equals("-") ? ConfigKOS.get_instance().get_render_offset().y : Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
      return true;
    }
    
    // Set offset
    if (x != -1)
      ConfigKOS.get_instance().set_render_offset(new IVector2(x, ConfigKOS.get_instance().get_render_offset().y));
    if (y != -1)
      ConfigKOS.get_instance().set_render_offset(new IVector2(ConfigKOS.get_instance().get_render_offset().x, y));
    
    // Notify
    Messaging.send_chat_message(EnumChatFormatting.GREEN + "Render offset set to: " + EnumChatFormatting.GOLD + x + EnumChatFormatting.GREEN + ", " + EnumChatFormatting.GOLD + y);
    return true;
  }
  
  private boolean reload_players(ICommandSender sender, String[] args) {
    // Check for arguments
    System.out.println(args.length);
    if (args.length != 0) {
      Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
      return true;
    }
    
    // Check if there are any players to reload
    int count = 0;
    Messaging.send_chat_message(EnumChatFormatting.GREEN + "Players reloaded from cache:");
    for (NetworkPlayerInfo player : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
      if (ConfigCache.get_instance().get_cached_players().contains(player.getGameProfile().getName())) {
        // Re-add player to KOS list with updated UUID
        try {
          ConfigKOS.get_instance().add_player(ConfigKOS.get_instance().remove_player(player.getGameProfile().getId())
              , false);
        } catch (Exception e) { e.printStackTrace(); }
        // Notify
        Messaging.send_chat_message(EnumChatFormatting.GREEN + " - " + EnumChatFormatting.GOLD + player.getGameProfile().getName() + EnumChatFormatting.GREEN + " : " + EnumChatFormatting.GOLD + player.getGameProfile().getId());
        count++;
      }
    }
    
    // Notify
    Messaging.send_chat_message(EnumChatFormatting.GREEN + " - " + EnumChatFormatting.GOLD + count + EnumChatFormatting.GREEN + " players reloaded.");
    
    return true;
  }
  
  private boolean remove_player(ICommandSender sender, String[] args) {
    // Check for arguments
    if (args.length != 1) {
      Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
      return true;
    }
    
    // Parse arguments
    String playerName = args[0];
    
    // Find player from KOS list
    FormattedPlayer player = FormattedPlayer.find(playerName, ConfigKOS.get_instance().get_players());
    
    // Check if player is on KOS list
    if (player == null) {
      Messaging.send_error_message("Player '" + playerName + "' is not on KOS list.");
      return true;
    }
    
    // Remove player from KOS list
    ConfigKOS.get_instance().remove_player(player);
    Messaging.send_chat_message(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD + playerName + EnumChatFormatting.GREEN + "' removed from KOS list.");
    
    // Check if player is in cache
    if (ConfigCache.get_instance().get_cached_players().contains(playerName)) {
      // Remove player from cache
      ConfigCache.get_instance().remove_cached_player(playerName);
      Messaging.send_chat_message(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD + playerName + EnumChatFormatting.GREEN + "' removed from cache.");
    }
    return true;
  }
  
  private boolean toggle_player(ICommandSender sender, String[] args) {
    // Check for arguments
    if (args.length == 0) {
      // Give a list of toggles and their state
      Messaging.send_chat_message(EnumChatFormatting.GREEN + "Toggles:");
      Messaging.send_chat_message(EnumChatFormatting.GREEN + " - Render Truces: " + (ConfigKOS.get_instance().get_rendered_types().contains(PlayerPriorities.TRUCE) ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"));
      Messaging.send_chat_message(EnumChatFormatting.GREEN + " - Render Friends: " + (ConfigKOS.get_instance().get_rendered_types().contains(PlayerPriorities.FRIEND) ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"));
      Messaging.send_chat_message(EnumChatFormatting.GREEN + " - Render Enemies: " + (ConfigKOS.get_instance().get_rendered_types().contains(PlayerPriorities.ENEMY) ? EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"));
      return true;
    } else if (args.length != 1) {
      Messaging.send_error_message("Invalid arguments. Usage: " + getCommandUsage(sender));
      return true;
    }
    
    // Parse arguments
    CommandKOSToggles type;
    try {
      type = CommandKOSToggles.valueOf(args[0].toUpperCase());
    } catch (IllegalArgumentException e) {
      Messaging.send_error_message("Invalid toggle type. Usage: " + getCommandUsage(sender));
      return true;
    }
    
    // Process toggle
    switch (type) {
      case RENDER_TRUCES:
        // Toggle the rendering of truces
        if (ConfigKOS.get_instance().get_rendered_types().contains(PlayerPriorities.TRUCE)) {
          ConfigKOS.get_instance().get_rendered_types().remove(PlayerPriorities.TRUCE);
          Messaging.send_chat_message(EnumChatFormatting.GREEN + "Truces will no longer be rendered.");
        } else {
          ConfigKOS.get_instance().get_rendered_types().add(PlayerPriorities.TRUCE);
          Messaging.send_chat_message(EnumChatFormatting.GREEN + "Truces will now be rendered.");
        }
        return true;
      case RENDER_FRIENDS:
        // Toggle the rendering of friends
        if (ConfigKOS.get_instance().get_rendered_types().contains(PlayerPriorities.FRIEND)) {
          ConfigKOS.get_instance().get_rendered_types().remove(PlayerPriorities.FRIEND);
          Messaging.send_chat_message(EnumChatFormatting.GREEN + "Friends will no longer be rendered.");
        } else {
          ConfigKOS.get_instance().get_rendered_types().add(PlayerPriorities.FRIEND);
          Messaging.send_chat_message(EnumChatFormatting.GREEN + "Friends will now be rendered.");
        }
        return true;
      case RENDER_ENEMIES:
        // Toggle the rendering of enemies
        if (ConfigKOS.get_instance().get_rendered_types().contains(PlayerPriorities.ENEMY)) {
          ConfigKOS.get_instance().get_rendered_types().remove(PlayerPriorities.ENEMY);
          Messaging.send_chat_message(EnumChatFormatting.GREEN + "Enemies will no longer be rendered.");
        } else {
          ConfigKOS.get_instance().get_rendered_types().add(PlayerPriorities.ENEMY);
          Messaging.send_chat_message(EnumChatFormatting.GREEN + "Enemies will now be rendered.");
        }
        return true;
      default:
        return false;
    }
  }
}
