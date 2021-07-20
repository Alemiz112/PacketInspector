# Bukkit/Spigot PacketInspector plugin
> PacketInspector is a simple tool which allows editing Minecraft packets using own serializers

## Goals
- Hook into the Minecraft server packet pipeline.
- Able to listen to and edit packets being sent from the Minecraft server.
- Able to listen to and edit packets arriving from the user.
  
## Test scenario
Implement inbound and outbound Chat packets which are handled using `TestPacketHandler`.
When Chat packet is sent by a user, append "Arrived" to the end of message.
When Chat packet is sent to the user, append "Sent" to the message.
 
## Software Support
Plugin was build for Minecraft 1.16 version and tested with Spigot 1.16.4 server.