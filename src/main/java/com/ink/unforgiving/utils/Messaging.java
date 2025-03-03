package com.ink.unforgiving.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;

public class Messaging {
  public static void send_chat_message(String message) {
    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
  }

  public static void send_warning_message(String message) {
    send_chat_message(EnumChatFormatting.YELLOW + message);
  }

  public static void send_error_message(String message) {
    send_chat_message(EnumChatFormatting.RED + "ERROR: " + message);
  }
}
