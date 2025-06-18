# MC Firewall

Advanced server protection plugin for Spigot.

Secure your Minecraft server with comprehensive player verification and account protection features:

- **Discord Integration:** Authenticate players through your Discord server
- **IP Binding:** Link player account to their IP address
- **Discord Account Binding:** Link player account to their Discord account
- **Restricted Access:** Ensure players can only access their accounts from linked IP

### How it works

1. Player joins your server for the first time
2. Player gets disconnected and receives a code (eg. 1234)
3. Players messages Discord Bot with that code
4. A link is created between: user IP address, Minecraft account and Discord account
5. User now can join the server

### FAQ

1. Player joins your server for the first time
2. Player gets disconnected and receives a code (eg. 1234)
3. Players messages Discord Bot with that code
4. A link is created between: user IP address, Minecraft account and Discord account
5. User now can join the server

### Configuration

|                            Field | Description                                                                                             |
|---------------------------------:|---------------------------------------------------------------------------------------------------------|
|                     database.url | JDBC URL for MySQL database. Leave blank to use file based database.                                    |
|                    database.user | User for MySQL database.                                                                                |
|                database.password | Password to MySQL database.                                                                             |
|           discord.requiredRoleID | ID of Discord role that each player must have to be allowed to play. Leave blank to disable this check. |
|                 discord.serverID | ID of your Discord server.                                                                              |
|      limits.maxAccountCountPerIP | Max count of Minecraft accounts per IP address.                                                         |
| limits.maxAccountCountPerDiscord | Max count of Minecraft accounts per Discord account.                                                    |
|                          botname | Name that will be assigned to the bot.                                                                  |

```yaml
#Example configuration
database:
  #Leave blank to use file based database
  #Example: jdbc:mysql://localhost:3306/firewall
  url: "jdbc:mysql://db:3306/firewall"
  user: "plugin"
  password: "75FTUXFEK7DLETAQHS9"

discord:
  roleID: "9339295342339279247"
  serverID: "9592973327274497532"
  botName: "Firewall Bot"

limits:
  maxAccountCountPerIP: 1
  maxAccountCountPerDiscord: 1

text:
  onConnect:
    whenNotOnServer: "You are not on the Discord server."
    whenRequiredRoleMissing: "You do not have the required role on the Discord server."
    whenRelinkRequired: "Your account requires re-verification through the Discord server.\nSend a message to <botName> with <code> to link accounts."
    whenLinkRequired: "Your account requires re-verification through the Discord server.\nSend a message to <botName> with <code> to link accounts."
    onError: "Server temporarily unavailable"
  onBotMessage:
    whenNotOnServer: "❌ You are not a member of the server."
    whenRequiredRoleMissing: "❌ You do not have the required role."
    whenLinkSuccessful: "✅ Your Discord account has been linked with "
    whenRelinkSuccessful: "✅ Connection renewed with "
    whenInvalidCodeSupplied: "❌ No such code found."
    whenPlayerBanned: "❌ Your access to the server is restricted."
    whenCodeNotFound: "❌ No such code found."
    whenMinecraftAlreadyLinked: "❌ This Minecraft account is already linked to another Discord account."
    whenAccountCountPerDiscord: "❌ Your Discord account is already linked to the maximum number of Minecraft accounts."
    whenAccountCountPerIP: "❌ Your IP address is associated with the maximum number of Minecraft accounts."
    onError: "❌ Failed to link accounts."
```

### Commands

|         Command | Description                                          |
|----------------:|------------------------------------------------------|
|   **/fw allow** | Allow player in by the code                          |
|     **/fw ban** | Ban player by selector                               |
|    **/fw deny** | Remove code from database                            |
|    **/fw help** | Print help                                           |
|    **/fw link** | Create new link                                      |
| **/fw disable** | Temporary disable the firewall                       |
|  **/fw pardon** | Lift every ban that matches all filters              |
|   **/fw purge** | Remove links older than time threshold from database |
|  **/fw select** | Fetch a record from the database                     |
|  **/fw enable** | Enable the firewall                                  |
|  **/fw unlink** | Remove a link                                        |
|   **/fw whois** | Print discord username                               |