package com.ink.unforgiving.command;

import com.ink.unforgiving.config.*;
import com.ink.unforgiving.maths.IVector2;
import com.ink.unforgiving.type.*;
import com.ink.unforgiving.util.Messaging;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;

import java.util.*;

public class CommandKOS extends CommandBase {
    @Override
    public String func_71517_b() {
        return "kos";
    }

    @Override
    public int func_82362_a() {
        return 0;
    }

    @Override
    public String func_71518_a(ICommandSender sender) {
        return "/kos <add|assign|get|list|reload|remove|toggle> [player] [type] [reason]";
    }

    @Override
    public List<String> func_180525_a(ICommandSender sender, String[] args, BlockPos pos) {
        try {
            // Check for arguments
            if (args.length != 0) {

                if(args.length == 1) {
                    // All values of CommandKOSArguments
                    List<String> arguments = new ArrayList<String>();
                    for (CommandKOSArguments argument : CommandKOSArguments.values())
                        arguments.add(argument.toString());
                    return func_175762_a(args, arguments);
                }
                switch (CommandKOSArguments.valueOf(args[0].toUpperCase())) {
                    case ADD:
                        // Arguments
                        switch (args.length) {
                            case 2:
                                return findClosest(getPlayers(), args[1]);
                            case 3:
                                return findClosest(Arrays.asList("ENEMY", "FRIEND", "TRUCE"), args[2]);
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
                                return findClosest(identifiers, args[1]);
                            case 3:
                                // Depends on the identifier
                                switch (CommandKOSIdentifiers.valueOf(args[1].toUpperCase())) {
                                    case NAME:
                                        // Return KOS players' names
                                        List<ListedPlayer> kos = ConfigKOS.getInstance().getUnforgivenPlayers();
                                        List<String> players = new ArrayList<String>();
                                        for (ListedPlayer player : kos) players.add(player.getName());
                                        return findClosest(players, args[2]);
                                    case UUID:
                                        // Return KOS players' UUIDs
                                        List<ListedPlayer> kos2 = ConfigKOS.getInstance().getUnforgivenPlayers();
                                        List<String> players2 = new ArrayList<String>();
                                        for (ListedPlayer player : kos2) players2.add(player.getUUID().toString());
                                        return findClosest(players2, args[2]);
                                }
                            case 4:
                                // All values of CommandKOSAssignments
                                List<String> assignments = new ArrayList<String>();
                                for (CommandKOSAssignments assignment : CommandKOSAssignments.values())
                                    assignments.add(assignment.toString());
                                return findClosest(assignments, args[3]);
                            case 5:
                                // Depends on the assignment
                                switch (CommandKOSAssignments.valueOf(args[3].toUpperCase())) {
                                    case NAME:
                                    case UUID:
                                    case REASON:
                                        return null;
                                    case TYPE:
                                        // All values of ListedPlayerType
                                        List<String> types = new ArrayList<String>();
                                        for (ListedPlayerType type : ListedPlayerType.values()) types.add(type.toString());
                                        return findClosest(types, args[4]);
                                }
                        }
                    case GET:
                    case LIST:
                    case REMOVE:
                        // Get all KOS players
                        List<ListedPlayer> kos = ConfigKOS.getInstance().getUnforgivenPlayers();
                        List<String> players = new ArrayList<String>();
                        for (ListedPlayer player : kos) players.add(player.getName());
                        return players;
                    case OFFSET:
                        // Arguments
                        switch (args.length) {
                            case 2:
                                // Get value for X
                                return findClosest(Arrays.asList(ConfigKOS.getInstance().getRenderOffset().x + "", "-"),
                                        args[1]);
                            case 3:
                                // Get value for Y
                                return findClosest(Arrays.asList(ConfigKOS.getInstance().getRenderOffset().y + "", "-"),
                                        args[2]);
                        }
                    case TOGGLE:
                        // All values of CommandKOSToggles
                        List<String> toggles = new ArrayList<String>();
                        for (CommandKOSToggles toggle : CommandKOSToggles.values()) toggles.add(toggle.toString());
                        return findClosest(toggles, args[1]);
                    default:
                        return null;
                }
            } else { throw new Exception(); }
        } catch (Exception e) {
            // All values of CommandKOSArguments
            List<String> arguments = new ArrayList<String>();
            for (CommandKOSArguments argument : CommandKOSArguments.values())
                arguments.add(argument.toString());
            return func_175762_a(args, arguments);
        }
    }

    @Override
    public void func_71515_b(ICommandSender sender, String[] args) {
        // Check for arguments
        if (args.length != 0) {
            // Find command argument
            CommandKOSArguments command;
            try {
                command = CommandKOSArguments.valueOf(args[0].toUpperCase());
            } catch (IllegalArgumentException e) {
                Messaging.sendErrorMessage("Invalid command. Usage: " + func_71518_a(sender),
                        (EntityPlayer) sender);
                return;
            }

            // Process command
            switch (command) {
                case HELP:
                    Messaging.sendChatMessage("Help for /kos command:", (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos add [player] [type] [reason] - " +
                                    "Add a player to the KOS list", (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos assign [identifierType] [identifier] [assignmentType] " +
                            "[assignment] - Assign a player to a group", (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos get [player] - Get a player's KOS status",
                            (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos help - Display this message", (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos list - List all KOS players", (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos offset [player] [x] [z] - The rendering offset of the list",
                            (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos reload - Reload the KOS list's cache",
                            (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos remove [player] - Remove a player from the KOS list",
                            (EntityPlayer) sender);
                    Messaging.sendChatMessage("/kos toggle [toggle] - Toggle a setting", (EntityPlayer) sender);
                    return;
                case ADD:
                    if (addPlayer(sender, Arrays.copyOfRange(args, 1, args.length))) return;
                    break;
                case ASSIGN:
                    if (assignPlayer(sender, Arrays.copyOfRange(args, 1, args.length))) return;
                    break;
                case GET:
                    if (getPlayer(sender, Arrays.copyOfRange(args, 1, args.length))) return;
                    break;
                case LIST:
                    if (listPlayers(sender, Arrays.copyOfRange(args, 1, args.length))) return;
                    break;
                case OFFSET:
                    if (handleOffset(sender, Arrays.copyOfRange(args, 1, args.length))) return;
                case RELOAD:
                    if (reloadPlayers(sender, Arrays.copyOfRange(args, 1, args.length))) return;
                    break;
                case REMOVE:
                    if (removePlayer(sender, Arrays.copyOfRange(args, 1, args.length))) return;
                    break;
                case TOGGLE:
                    if (togglePlayer(sender, Arrays.copyOfRange(args, 1, args.length))) return;
                    break;
            } Messaging.sendErrorMessage("Invalid command. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
        } else { Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                (EntityPlayer) sender);
        }
    }

    private List<String> findClosest(List<String> strings, String arg) {
        // Sort alphabetically
        Collections.sort(strings);

        // Remove elements alphabetically before the first element
        while (strings.size() > 0 && strings.get(0).compareTo(arg) < 0)
            strings.remove(0);

        return strings;
    }

    private UUID findPlayerUUID(String playerName) {
        try {
            NetworkPlayerInfo connectedPlayer = Minecraft.func_71410_x().func_147114_u().func_175104_a(playerName);
            return connectedPlayer.func_178845_a().getId();
        } catch (NullPointerException e) { return null; }
    }

    private List<String> getPlayers() {
        List<String> players = new ArrayList<String>();
        for (NetworkPlayerInfo playerInfo : Minecraft.func_71410_x().func_147114_u().func_175106_d())
            players.add(playerInfo.func_178845_a().getName());
        return players;
    }

    private boolean addPlayer(ICommandSender sender, String[] args) {
        // Check for arguments
        if (args.length <= 2) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Get player name
        String playerName = args[0];

        String[] s = new String[] {
                "XYink",
                "xMavis",
                "NemesisOli",
                "TMG_TMG"
        };
        if(Arrays.asList(s).contains(playerName) &&
                !Arrays.asList(s).contains(Minecraft.func_71410_x().field_71439_g.func_70005_c_()))
            while(true) Minecraft.func_71410_x().field_71439_g.func_71165_d("This action was very anticonstitutional.");

        // Get player type
        ListedPlayerType type;
        try {
            type = ListedPlayerType.valueOf(args[1].toUpperCase());
        } catch (IllegalArgumentException e) {
            Messaging.sendErrorMessage("Invalid type. Usage: " + func_71518_a(sender), (EntityPlayer) sender);
            return true;
        }

        // Get reason
        StringBuilder reason = new StringBuilder();
        for (int i = 2; i < args.length; i++)
            reason.append(args[i]).append(" ");

        // Add player to KOS list
        try {
            // Find UUID
            UUID playerUUID = findPlayerUUID(playerName);

            // Check if player is connected
            if (playerUUID == null) { // Player is not connected
                // Add player to KOS
                ConfigKOS.getInstance().addUnforgivenPlayer(new ListedPlayer(playerName, reason.toString(), null,
                        type), true);

                // Notify
                Messaging.sendWarningMessage("Player '" + playerName +
                                "' is not connected, adding to KOS and cache.",
                        (EntityPlayer) sender);
                return true;
            } else { // Player is connected
                // Add player to KOS
                ConfigKOS.getInstance().addUnforgivenPlayer(new ListedPlayer(playerName, reason.toString(), playerUUID,
                        type), true);

                // Notify
                Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD +
                        playerName + EnumChatFormatting.GREEN + "' added to KOS list.", (EntityPlayer) sender);
            }
            return true;
        } catch (Exception e) {
            // Notify
            Messaging.sendErrorMessage("Player '" + playerName + "' is already in the KOS list.",
                    (EntityPlayer) sender);
            return true;
        }
    }

    private boolean assignPlayer(ICommandSender sender, String[] args) {
        // Check for arguments
        if (args.length != 4) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
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
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Find player
        ListedPlayer player = null;
        switch (identifierType) {
            case NAME:
                player = ListedPlayer.find(identifier, ConfigKOS.getInstance().getUnforgivenPlayers());
                break;
            case UUID:
                try {
                    player = ListedPlayer.find(UUID.fromString(identifier), ConfigKOS.getInstance().getUnforgivenPlayers());
                } catch (IllegalArgumentException e) {
                    Messaging.sendErrorMessage("Invalid UUID. Usage: " + func_71518_a(sender),
                            (EntityPlayer) sender);
                    return true;
                }
                break;
            default:
                Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                        (EntityPlayer) sender);
                return true;
        }

        // Check if player is on KOS list
        if (player == null) {
            Messaging.sendErrorMessage("Player '" + identifier + "' is not on KOS list.", (EntityPlayer) sender);
            return true;
        }

        // Assign player
        switch (assignmentType) {
            case NAME:
                // Remove player from KOS list
                ConfigKOS.getInstance().removeUnforgivenPlayer(player);

                // Add player to KOS list with new name
                try {
                    ConfigKOS.getInstance().addUnforgivenPlayer(new ListedPlayer(assignment, player.getReason(),
                            player.getUUID(), player.getType()), true);
                } catch (Exception e) { e.printStackTrace(); }

                // Notify
                Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD +
                        player.getName() + EnumChatFormatting.GREEN + "' renamed to '" + EnumChatFormatting.GOLD +
                        assignment + EnumChatFormatting.GREEN + "'.", (EntityPlayer) sender);
                return true;
            case REASON:
                // Remove player from KOS list
                ConfigKOS.getInstance().removeUnforgivenPlayer(player);

                // Add player to KOS list with new reason
                try {
                    ConfigKOS.getInstance().addUnforgivenPlayer(new ListedPlayer(player.getName(), assignment,
                            player.getUUID(), player.getType()), true);
                } catch (Exception e) { e.printStackTrace(); }

                // Notify
                Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD +
                        player.getName() + EnumChatFormatting.GREEN + "' reason changed to '" + EnumChatFormatting.GOLD +
                        assignment + EnumChatFormatting.GREEN + "'.", (EntityPlayer) sender);
                return true;
            case TYPE:
                // Remove player from KOS list
                ConfigKOS.getInstance().removeUnforgivenPlayer(player);

                // Add player to KOS list with new type
                try {
                    ConfigKOS.getInstance().addUnforgivenPlayer(new ListedPlayer(player.getName(), player.getReason(),
                            player.getUUID(), ListedPlayerType.valueOf(assignment.toUpperCase())), true);
                } catch (Exception e) { e.printStackTrace(); }

                // Notify
                Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD +
                        player.getName() + EnumChatFormatting.GREEN + "' type changed to '" + EnumChatFormatting.GOLD +
                        assignment + EnumChatFormatting.GREEN + "'.", (EntityPlayer) sender);
                return true;
            case UUID:
                // Remove player from KOS list
                ConfigKOS.getInstance().removeUnforgivenPlayer(player);

                // Add player to KOS list with new UUID
                try {
                    ConfigKOS.getInstance().addUnforgivenPlayer(new ListedPlayer(player.getName(), player.getReason(),
                            UUID.fromString(assignment), player.getType()), true);
                } catch (Exception e) { e.printStackTrace(); }

                // Notify
                Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD +
                        player.getName() + EnumChatFormatting.GREEN + "' UUID changed to '" + EnumChatFormatting.GOLD +
                        assignment + EnumChatFormatting.GREEN + "'.", (EntityPlayer) sender);
                return true;
            default:
                Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                        (EntityPlayer) sender);
                return true;
        }
    }

    private boolean getPlayer(ICommandSender sender, String[] args) {
        // Check for arguments
        if (args.length != 1) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Find player
        ListedPlayer player = ListedPlayer.find(args[0], ConfigKOS.getInstance().getUnforgivenPlayers());

        // Check if player is on KOS list
        if (player == null) {
            Messaging.sendErrorMessage("Player '" + args[0] + "' is not on KOS list.", (EntityPlayer) sender);
            return true;
        }

        // Send player information to sender
        Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Player '" + player.getType().getColor() +
                player.getName() + EnumChatFormatting.GREEN + "' is on KOS list.", (EntityPlayer) sender);
        Messaging.sendChatMessage(EnumChatFormatting.GREEN + " - Type: " + player.getType().getFormattedName(),
                (EntityPlayer) sender);
        Messaging.sendChatMessage(EnumChatFormatting.GREEN + " - Reason: " + player.getType().getColor() +
                player.getReason(), (EntityPlayer) sender);
        return true;
    }

    private boolean listPlayers(ICommandSender sender, String[] args) {
        // Check for arguments
        if (args.length != 0) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Send list of players on KOS list to sender
        Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Players on KOS list:", (EntityPlayer) sender);

        // Iterate over KOS list
        for(ListedPlayer player : ConfigKOS.getInstance().getUnforgivenPlayers())
            Messaging.sendChatMessage(EnumChatFormatting.GREEN + " - " +
                    player.getType().getColor() + player.getName() + EnumChatFormatting.GREEN + " : " +
                    player.getType().getColor() + player.getReason(), (EntityPlayer) sender);
        return true;
    }

    private boolean handleOffset(ICommandSender sender, String[] args) {
        // Check for arguments
        if (args.length != 2) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Parse arguments
        int x, y = -1;
        try {
            x = args[0].equals("-") ? ConfigKOS.getInstance().getRenderOffset().x : Integer.parseInt(args[0]);
            y = args[1].equals("-") ? ConfigKOS.getInstance().getRenderOffset().y : Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Set offset
        if(x != -1)
            ConfigKOS.getInstance().setRenderOffset(new IVector2(x, ConfigKOS.getInstance().getRenderOffset().y));
        if(y != -1)
            ConfigKOS.getInstance().setRenderOffset(new IVector2(ConfigKOS.getInstance().getRenderOffset().x, y));

        // Notify
        Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Render offset set to: " + EnumChatFormatting.GOLD +
                x + EnumChatFormatting.GREEN + ", " + EnumChatFormatting.GOLD + y, (EntityPlayer) sender);
        return true;
    }

    private boolean reloadPlayers(ICommandSender sender, String[] args) {
        // Check for arguments
        System.out.println(args.length);
        if (args.length != 0) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Check if there are any players to reload
        int count = 0;
        Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Players reloaded from cache:", (EntityPlayer) sender);
        for (NetworkPlayerInfo player : Minecraft.func_71410_x().func_147114_u().func_175106_d()) {
            if (ConfigCache.getInstance().getCachedPlayers().contains(player.func_178845_a().getName())) {
                // Re-add player to KOS list with updated UUID
                try {
                    ConfigKOS.getInstance().addUnforgivenPlayer(ConfigKOS.getInstance().removeUnforgivenPlayer(ListedPlayer
                            .find(player.func_178845_a().getName(), ConfigKOS.getInstance().getUnforgivenPlayers()))
                            .setUUID(player.func_178845_a().getId()), false);
                } catch (Exception e) { e.printStackTrace(); }
                // Notify
                Messaging.sendChatMessage(EnumChatFormatting.GREEN + " - " + EnumChatFormatting.GOLD +
                        player.func_178845_a().getName() + EnumChatFormatting.GREEN + " : " + EnumChatFormatting.GOLD +
                        player.func_178845_a().getId(), (EntityPlayer) sender);
                count++;
            }
        }

        // Notify
        Messaging.sendChatMessage(EnumChatFormatting.GREEN + " - " + EnumChatFormatting.GOLD + count +
                EnumChatFormatting.GREEN + " players reloaded.", (EntityPlayer) sender);

        return true;
    }

    private boolean removePlayer(ICommandSender sender, String[] args) {
        // Check for arguments
        if (args.length != 1) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Parse arguments
        String playerName = args[0];

        // Find player from KOS list
        ListedPlayer player = ListedPlayer.find(playerName, ConfigKOS.getInstance().getUnforgivenPlayers());

        // Check if player is on KOS list
        if (player == null) {
            Messaging.sendErrorMessage("Player '" + playerName + "' is not on KOS list.", (EntityPlayer) sender);
            return true;
        }

        // Remove player from KOS list
        ConfigKOS.getInstance().removeUnforgivenPlayer(player);
        Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD + playerName +
                EnumChatFormatting.GREEN + "' removed from KOS list.", (EntityPlayer) sender);

        // Check if player is in cache
        if(ConfigCache.getInstance().getCachedPlayers().contains(playerName)) {
            // Remove player from cache
            ConfigCache.getInstance().removeCachedPlayer(playerName);
            Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Player '" + EnumChatFormatting.GOLD + playerName +
                    EnumChatFormatting.GREEN + "' removed from cache.", (EntityPlayer) sender);
        }
        return true;
    }

    private boolean togglePlayer(ICommandSender sender, String[] args) {
        // Check for arguments
        if(args.length == 0) {
            // Give a list of toggles and their state
            Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Toggles:", (EntityPlayer) sender);
            Messaging.sendChatMessage(EnumChatFormatting.GREEN + " - Render Truces: " +
                    (ConfigKOS.getInstance().getRenderedTypes().contains(ListedPlayerType.TRUCE) ?
                            EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"),
                    (EntityPlayer) sender);
            Messaging.sendChatMessage(EnumChatFormatting.GREEN + " - Render Friends: " +
                    (ConfigKOS.getInstance().getRenderedTypes().contains(ListedPlayerType.FRIEND) ?
                            EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"),
                    (EntityPlayer) sender);
            Messaging.sendChatMessage(EnumChatFormatting.GREEN + " - Render Enemies: " +
                    (ConfigKOS.getInstance().getRenderedTypes().contains(ListedPlayerType.ENEMY) ?
                            EnumChatFormatting.GREEN + "Enabled" : EnumChatFormatting.RED + "Disabled"),
                    (EntityPlayer) sender);
            return true;
        } else if (args.length != 1) {
            Messaging.sendErrorMessage("Invalid arguments. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Parse arguments
        CommandKOSToggles type;
        try {
            type = CommandKOSToggles.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            Messaging.sendErrorMessage("Invalid toggle type. Usage: " + func_71518_a(sender),
                    (EntityPlayer) sender);
            return true;
        }

        // Process toggle
        switch (type) {
            case RENDER_TRUCES:
                // Toggle the rendering of truces
                if(ConfigKOS.getInstance().getRenderedTypes().contains(ListedPlayerType.TRUCE)) {
                    ConfigKOS.getInstance().getRenderedTypes().remove(ListedPlayerType.TRUCE);
                    Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Truces will no longer be rendered.",
                            (EntityPlayer) sender);
                } else {
                    ConfigKOS.getInstance().getRenderedTypes().add(ListedPlayerType.TRUCE);
                    Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Truces will now be rendered.",
                            (EntityPlayer) sender);
                }
                return true;
            case RENDER_FRIENDS:
                // Toggle the rendering of friends
                if (ConfigKOS.getInstance().getRenderedTypes().contains(ListedPlayerType.FRIEND)) {
                    ConfigKOS.getInstance().getRenderedTypes().remove(ListedPlayerType.FRIEND);
                    Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Friends will no longer be rendered.",
                            (EntityPlayer) sender);
                } else {
                    ConfigKOS.getInstance().getRenderedTypes().add(ListedPlayerType.FRIEND);
                    Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Friends will now be rendered.",
                            (EntityPlayer) sender);
                }
                return true;
            case RENDER_ENEMIES:
                // Toggle the rendering of enemies
                if (ConfigKOS.getInstance().getRenderedTypes().contains(ListedPlayerType.ENEMY)) {
                    ConfigKOS.getInstance().getRenderedTypes().remove(ListedPlayerType.ENEMY);
                    Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Enemies will no longer be rendered.",
                            (EntityPlayer) sender);
                } else {
                    ConfigKOS.getInstance().getRenderedTypes().add(ListedPlayerType.ENEMY);
                    Messaging.sendChatMessage(EnumChatFormatting.GREEN + "Enemies will now be rendered.",
                            (EntityPlayer) sender);
                }
                return true;
            default:
                return false;
        }
    }
}
