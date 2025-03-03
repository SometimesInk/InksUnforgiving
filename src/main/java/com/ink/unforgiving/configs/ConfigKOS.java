package com.ink.unforgiving.configs;

import com.ink.unforgiving.maths.IVector2;
import com.ink.unforgiving.types.FormattedPlayer;
import com.ink.unforgiving.types.PlayerPriorities;
import net.minecraftforge.common.config.*;

import java.util.*;

public class ConfigKOS extends Config {
  public static ConfigKOS INSTANCE = new ConfigKOS();

  // This property holds the players in the KOS
  private Property players_property;
  private List<FormattedPlayer> players = new ArrayList<FormattedPlayer>();

  // This property exists for the user to control which categories will render
  // on the screen. (e.g.: a user might want to only render allies and
  // enemies)
  private Property rendered_types_property;
  private List<PlayerPriorities> rendered_types = new ArrayList<PlayerPriorities>();

  // This is the offset at which to render the KOS on the screen
  private Property render_offset_property;
  private IVector2 render_offset = new IVector2();

  @Override
  public String file_name() {
    return "kos";
  }

  @Override
  public void reload() {
    // Defining players and its property
    players_property = config.get("Unforgiving", "Unforgiven_Players", new String[0]);

    // Parse each player and add it to the list
    players.clear();
    for (String formatted_player : players_property.getStringList()) { // FIX: Potential security flaw since it is not
      players.add(new FormattedPlayer(formatted_player));              // checking if the final formatted player is valid
    }                                                                  // and the config are available to be modified by
                                                                       // users; every user modifiable value should be
                                                                       // checked.

    // Defining rendered types and its property
    rendered_types_property = config.get("Unforgiving", "Rendered_Types",
        Arrays.toString(PlayerPriorities.values()));

    rendered_types.clear();
    for (String types : rendered_types_property.getStringList()) { // FIX: Same reason as viewed above
      rendered_types.add(PlayerPriorities.valueOf(types));
    }

    // Defining render offset and its property
    render_offset_property = config.get("Unforgiving", "Render_Offset", new int[] {5, 5});

    int[] buffer = render_offset_property.getIntList(); // FIX: Same reason as viewed above
    render_offset = new IVector2(buffer[0], buffer[1]);

    // Save the list
    save();
  }

  public List<FormattedPlayer> get_players() {
    return players;
  }

  private void set_players(List<FormattedPlayer> players) {
    this.players = players;

    // Put all players into the property
    List<String> list = new ArrayList<String>();
    for(FormattedPlayer p : players) {
      list.add(p.toString());
    }
    players_property.set(list.toArray(new String[0]));

    // Save config
    save();
  }

  public void add_player(FormattedPlayer player, boolean check_duplicates) throws Exception {
    // Check if UUID is null
    if(player.uuid == null) {
      // Add to cache
      ConfigCache.get_instance().add_cached_player(player.name);
    }

    // Check if player is already in the KOS
    if(check_duplicates && (FormattedPlayer.find(player.uuid, players) != null)) {
      throw new Exception("Player is already on the unforgiven list");
    }

    players.add(player);
    set_players(players);
  }

  public FormattedPlayer remove_player(FormattedPlayer player) {
    for(FormattedPlayer p : players)
      if (p.toString().equals(player.toString())) {
        players.remove(p);
        set_players(players);
        return p;
      }
    return null;
  }

  /**
   * @throws IllegalArgumentException When the id is not found in the list.
   */
  public FormattedPlayer remove_player(UUID id) throws IllegalArgumentException {
    FormattedPlayer player_to_remove = null;

    // Find player to remove
    for(FormattedPlayer p : players) {
      if (!p.uuid.equals(id)) continue;
      player_to_remove = p;
      break;
    }

    if(player_to_remove == null) throw new IllegalArgumentException();

    players.remove(player_to_remove);

    set_players(players);

    return player_to_remove;
  }

  public List<PlayerPriorities> get_rendered_types() {
    return rendered_types;
  }

  public void set_rendered_types(List<PlayerPriorities> rendered_types) {
    this.rendered_types = rendered_types;

    // Set config
    List<String> list = new ArrayList<String>();
    for(PlayerPriorities p : rendered_types) list.add(p.toString());
    rendered_types_property.set(list.toArray(new String[0]));

    // Save config
    save();
  }

  public IVector2 get_render_offset() {
    return render_offset;
  }

  public void set_render_offset(IVector2 render_offset) {
    this.render_offset = render_offset;

    // Set config
    render_offset_property.set(new int[] {render_offset.x, render_offset.y});

    // Save config
    save();
  }

  public static ConfigKOS get_instance() {
    return INSTANCE;
  }
}
