package com.ink.unforgiving.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

public class Messaging {
  public static void send_chat_message(String message) {
    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
  }
  
  public static void send(String key) {
    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(key)));
  }
  
  public static void send(String key, Object... format) {
    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(key, format)));
  }
}
