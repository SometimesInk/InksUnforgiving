package com.ink.unforgiving.types;

import net.minecraft.util.EnumChatFormatting;

public enum PlayerPriorities {
    ENEMY(EnumChatFormatting.RED),
    FRIEND(EnumChatFormatting.GREEN),
    TRUCE(EnumChatFormatting.GOLD);

    private final EnumChatFormatting color;

    PlayerPriorities(EnumChatFormatting color) {
        this.color = color;
    }

    public EnumChatFormatting get_color() {
        return color;
    }
    
    public String get_formatted_name() {
      return color + this.toString();
    }
}
