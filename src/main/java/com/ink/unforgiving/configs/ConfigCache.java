package com.ink.unforgiving.configs;

import com.ink.unforgiving.types.FormattedPlayer;
import com.ink.unforgiving.utils.PlayerManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.config.Property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ConfigCache extends Config {
  public static ConfigCache INSTANCE = new ConfigCache();
  
  private Property cached_players_property;
  private List<String> cached_players = new ArrayList<String>();
  
  public static ConfigCache get_instance() {
    return INSTANCE;
  }
  
  @Override
  public String file_name() {
    return "kos_cache";
  }
  
  @Override
  public void reload() {
    // Get properties from
    cached_players_property = config.get("Unforgiving", "Unforgiven_Players", new String[0]);
    
    // Populate the list
    cached_players.clear();
    cached_players.addAll(Arrays.asList(cached_players_property.getStringList()));
    
    // Save the list
    save();
  }
  
  public boolean add_cached_player(String player, boolean check_duplicate) {
    // TODO: Check duplicate here
    
    // Get pointer-less list
    List<String> cachedPlayers = get_cached_players();
    
    if (check_duplicate && cachedPlayers.contains(player)) return false;
    
    cachedPlayers.add(player);
    
    // Save
    set_cached_players(cachedPlayers);
    return true;
  }
  
  public void remove_cached_player(String player) {
    // Get pointer-less list
    List<String> cachedPlayers = get_cached_players();
    
    cachedPlayers.remove(player);
    
    // Save
    set_cached_players(cachedPlayers);
  }
  
  public List<String> get_cached_players() {
    return cached_players;
  }
  
  public void set_cached_players(List<String> players) {
    // Set variable
    this.cached_players = players;
    
    // Set config
    cached_players_property.set(players.toArray(new String[0]));
    
    // Save config
    save();
  }
  
  public void reload_players() throws Exception {
    // Look for players to un-cache
    for (UUID id : PlayerManager.players_in_lobby) {
      // Check if the player is in the cache
      
      EntityPlayer player = PlayerManager.get_player_from_uuid(id);
      String name = player.getName();
      if (ConfigCache.get_instance().get_cached_players().contains(name)) {
        // Remove player from cache
        ConfigCache.get_instance().remove_cached_player(name);
        
        // Get incomplete (uuid-less) formatted player
        FormattedPlayer fplayer = ConfigKOS.get_instance().remove_player(id);
        
        // Add back to KOS with uuid
        fplayer.uuid = id;
        ConfigKOS.get_instance().add_player(fplayer, true);
      }
    }
  }
}
