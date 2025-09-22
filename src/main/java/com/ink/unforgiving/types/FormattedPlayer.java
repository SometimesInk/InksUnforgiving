package com.ink.unforgiving.types;

import java.util.List;
import java.util.UUID;

public class FormattedPlayer {
  public UUID uuid;
  public PlayerPriorities type;
  public String name;
  public String reason;
  
  public FormattedPlayer(UUID uuid, PlayerPriorities type, String name, String reason) {
    this.uuid = uuid;
    this.type = type;
    this.reason = reason;
    this.name = name;
  }
  
  public FormattedPlayer(String formatted_entry) {
    FormattedPlayer parsed_entry = parse(formatted_entry);
    this.uuid = parsed_entry.uuid;
    this.type = parsed_entry.type;
    this.name = parsed_entry.name;
    this.reason = parsed_entry.reason;
  }
  
  private static FormattedPlayer parse(String formatted_entry) {
    // Parse the formatted entry according to this format:
    // uuid¦type¦name¦reason
    String[] split = formatted_entry.split("¦");
    return new FormattedPlayer(split[0].equals("null") ? null : java.util.UUID.fromString(split[0]),
        com.ink.unforgiving.types.PlayerPriorities.valueOf(split[1].toUpperCase()), split[2], split[3]);
  }
  
  public static FormattedPlayer find(UUID uuid, List<FormattedPlayer> list) {
    if (uuid == null) return null;
    for (FormattedPlayer p : list) {
      if (p.uuid == null) continue;
      if (p.uuid.equals(uuid)) return p;
    }
    return null;
  }
  
  public static FormattedPlayer find(String name, List<FormattedPlayer> list) {
    for (FormattedPlayer p : list) if (p.name.equals(name)) return p;
    return null;
  }
  
  @Override
  public String toString() {
    return (uuid == null ? "null" : uuid.toString()) + "¦" + type.toString() + "¦" + name + "¦" + reason;
  }
}
