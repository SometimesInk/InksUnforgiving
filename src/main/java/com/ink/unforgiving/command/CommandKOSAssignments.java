package com.ink.unforgiving.command;

/**
 * All arguments of the assign argument of the <code>CommandKOS</code> command.
 *
 * <p> This class contains the following elements:
 *     <ul>
 *         <li><code>UUID</code>   - The UUID of the player.</li>
 *         <li><code>TYPE</code>   - The type of the player.</li>
 *         <li><code>NAME</code>   - The name of the player.</li>
 *         <li><code>REASON</code> - The reason for the entry.</li>
 *     </ul>
 * </p>
 *
 * @see CommandKOS
 */
public enum CommandKOSAssignments {
    UUID,
    TYPE,
    NAME,
    REASON,
}
