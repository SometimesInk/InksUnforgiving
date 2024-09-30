package com.ink.unforgiving.event;

import com.ink.unforgiving.config.ConfigKOS;
import com.ink.unforgiving.type.ListedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

public class EventRender {

    @SubscribeEvent
    public void render(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        FontRenderer fRender = Minecraft.getMinecraft().fontRendererObj;

        // Render unforgiven players
        List<String> lines = new java.util.ArrayList<String>();

        // Get all players in lobby
        for (NetworkPlayerInfo player : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            ListedPlayer listedPlayer = ListedPlayer.find(player.getGameProfile().getId(),
                    ConfigKOS.getInstance().getUnforgivenPlayers());
            if (listedPlayer != null && ConfigKOS.getInstance().getRenderedTypes().contains(listedPlayer.getType()))
                lines.add(listedPlayer.getType().getColor() + listedPlayer.getName());
        }

        // Create string
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            fRender.drawString(s, ConfigKOS.getInstance().getRenderOffset().x,
                    ConfigKOS.getInstance().getRenderOffset().y + i * 10, 0);
        }
    }
}
