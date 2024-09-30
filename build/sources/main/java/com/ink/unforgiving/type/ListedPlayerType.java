package com.ink.unforgiving.type;

import net.minecraft.util.EnumChatFormatting;

public enum ListedPlayerType {
    ENEMY(EnumChatFormatting.RED),
    FRIEND(EnumChatFormatting.GREEN),
    TRUCE(EnumChatFormatting.GOLD);

    private EnumChatFormatting color;

    ListedPlayerType(EnumChatFormatting color) {
        this.color = color;
    }

    public EnumChatFormatting getColor() {
        return color;
    }

    public String getFormattedName() {
        return color + this.name();
    }
}
