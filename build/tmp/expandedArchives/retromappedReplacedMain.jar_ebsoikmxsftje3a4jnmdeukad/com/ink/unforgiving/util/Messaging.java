package com.ink.unforgiving.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Messaging {
    public static void sendChatMessage(String message, EntityPlayer player) {
        player.func_145747_a(new ChatComponentText(message));
    }

    public static void sendWarningMessage(String message, EntityPlayer player) {
        sendChatMessage(EnumChatFormatting.YELLOW + message, player);
    }

    public static void sendErrorMessage(String message, EntityPlayer player) {
        sendChatMessage(EnumChatFormatting.RED + message, player);
    }
}
