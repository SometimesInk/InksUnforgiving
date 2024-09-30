package com.ink.unforgiving.command;

/**
 * All arguments of the <code>CommandKOS</code> command.
 *
 * <p> This class contains the following elements:
 *     <ul>
 *         <li><code>ADD</code>    - Add user to KOS.</li>
 *         <li><code>ASSIGN</code> - Assign UUID to cached user.</li>
 *         <li><code>GET</code>    - Get user info from KOS.</li>
 *         <li><code>HELP</code>   - Display help message.</li>
 *         <li><code>LIST</code>   - List users in KOS.</li>
 *         <li><code>OFFSET</code> - Set the rendering offset of the list.</li>
 *         <li><code>RELOAD</code> - Check for cached users to assign.</li>
 *         <li><code>REMOVE</code> - Remove user from KOS.</li>
 *         <li><code>TOGGLE</code> - Toggles a boolean setting.</li>
 *     </ul>
 * </p>
 *
 * @see CommandKOS
 */
public enum CommandKOSArguments {
    ADD,
    ASSIGN,
    GET,
    HELP,
    LIST,
    OFFSET,
    RELOAD,
    REMOVE,
    TOGGLE,
}
