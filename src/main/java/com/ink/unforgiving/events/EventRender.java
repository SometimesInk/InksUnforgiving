package com.ink.unforgiving.events;

import com.ink.unforgiving.configs.ConfigKOS;
import com.ink.unforgiving.types.FormattedPlayer;
import com.ink.unforgiving.utils.PlayerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.UUID;

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
        for (UUID id : PlayerManager.players_in_lobby) {
            FormattedPlayer p = FormattedPlayer.find(id, ConfigKOS.get_instance().get_players());
            if (p != null && ConfigKOS.get_instance().get_rendered_types().contains(p.type))
                lines.add(p.type.get_color() + p.name);
        }

        // Create string
        for (int i = 0; i < lines.size(); i++) {
            String s = lines.get(i);
            fRender.drawString(s, ConfigKOS.get_instance().get_render_offset().x,
                    ConfigKOS.get_instance().get_render_offset().y + i * 10, 0);
        }
    }
}
