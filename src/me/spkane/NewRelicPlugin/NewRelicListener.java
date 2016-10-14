package me.spkane.NewRelicPlugin;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.RemoteServerCommandEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.newrelic.api.agent.NewRelic;
import com.newrelic.api.agent.Trace;

public class NewRelicListener implements Listener {
	
	NewRelicPlugin configGetter;
	
	public NewRelicListener(NewRelicPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		configGetter = plugin;
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onEntityDeath(EntityDeathEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.entity.death")) {
			NewRelic.setTransactionName(null, "EntityDeathEvent");
			Entity entity = e.getEntity();
		    if (e.getEntity().getKiller() != null) {           
		    	Entity killer = e.getEntity().getKiller();           
		    	if (killer instanceof Player ) {
		    		Player player = (Player) killer;
		    		NewRelic.addCustomParameter("killedByPlayer", "true");
		    		NewRelic.addCustomParameter("playerName", player.getName());
		    	} else {
		    		NewRelic.addCustomParameter("killedByPlayer", "false");
		    		NewRelic.addCustomParameter("playerName", "");
		    	}
		    } else {
		    	NewRelic.addCustomParameter("killedByPlayer", "false");
	    		NewRelic.addCustomParameter("playerName", "");
		    }
		    String entityname = entity.getType().toString();
		    NewRelic.addCustomParameter("entityType", entityname);
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.creature.spawn")) {
			NewRelic.setTransactionName(null, "CreatureSpawnEvent");
			Entity entity = e.getEntity();
		    String entityname = entity.getType().toString();
		    NewRelic.addCustomParameter("entityType", entityname);
		    if (e.getSpawnReason() != null) {
		      String spawnreason = e.getSpawnReason().toString();
		      NewRelic.addCustomParameter("spawnReason", spawnreason);
		    } else {
		    	NewRelic.addCustomParameter("spawnReason", "");
		    }
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.player.death")) {
			NewRelic.setTransactionName(null, "PlayerDeathEvent");
			Player player = e.getEntity();
			NewRelic.addCustomParameter("playerName", player.getName());
			NewRelic.addCustomParameter("playerDeathMessage", e.getDeathMessage());
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerJoin(PlayerJoinEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.player.join")) {
			NewRelic.setTransactionName(null, "PlayerJoinEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
			if (!player.hasPlayedBefore()) {
				NewRelic.addCustomParameter("playerNew", "true");
			} else {
				NewRelic.addCustomParameter("playerNew", "false");
			}
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerKick(PlayerKickEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.player.kick")) {
			NewRelic.setTransactionName(null, "PlayerKickEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
			if (e.getReason() != null) {
			    NewRelic.addCustomParameter("playerKickReason", e.getReason());
			} else {
			    NewRelic.addCustomParameter("playerKickReason", "");
			}
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerQuit(PlayerQuitEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.player.quit")) {
			NewRelic.setTransactionName(null, "PlayerQuitEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.player.respawn")) {
			NewRelic.setTransactionName(null, "PlayerRespawnEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
			NewRelic.addCustomParameter("playerRespawnLocation", e.getRespawnLocation().toString());
			String bedspawn = "";
			if (e.isBedSpawn()) {
				bedspawn = "true";
			} else {
				bedspawn = "false";
			}
			NewRelic.addCustomParameter("playerIsBedSpawn", bedspawn);
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onPlayerTeleport(PlayerTeleportEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.player.teleport")) {
			NewRelic.setTransactionName(null, "PlayerTeleportEvent");
			Player player = e.getPlayer();
			NewRelic.addCustomParameter("playerName", player.getName());
			if (e.getCause() != null) {
			    NewRelic.addCustomParameter("playerTeleportCause", e.getCause().toString());
			} else {
				NewRelic.addCustomParameter("playerTeleportCause", "");
			}
			NewRelic.addCustomParameter("playerTeleportFrom", e.getFrom().toString());
			NewRelic.addCustomParameter("playerTeleportTo", e.getTo().toString());
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onBlockPlace(BlockPlaceEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.block.place")) {
			NewRelic.setTransactionName(null, "BlockPlaceEvent");
			Player player = e.getPlayer();
			Block block = e.getBlock();
			NewRelic.addCustomParameter("playerName", player.getName());
			NewRelic.addCustomParameter("blockType", block.getType().toString());
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onBlockBreak(BlockBreakEvent e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.block.break")) {
			NewRelic.setTransactionName(null, "BlockBreakEvent");
			Player player = e.getPlayer();
			Block block = e.getBlock();
			NewRelic.addCustomParameter("playerName", player.getName());
			NewRelic.addCustomParameter("blockType", block.getType().toString());
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onRemoteServerCommand(RemoteServerCommandEvent  e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.server.remotecommand")) {
			NewRelic.setTransactionName(null, "RemoteCommandEvent");
			NewRelic.addCustomParameter("commandSender", e.getSender().toString());
			NewRelic.addCustomParameter("command", e.getCommand());
		} else {
			NewRelic.ignoreTransaction();
		}
	}

	@EventHandler
	@Trace (dispatcher=true)
	public void onServerCommand(ServerCommandEvent  e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.server.command")) {
			NewRelic.setTransactionName(null, "CommandEvent");
			NewRelic.addCustomParameter("commandSender", e.getSender().toString());
			NewRelic.addCustomParameter("command", e.getCommand());
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onChunkLoad(ChunkLoadEvent  e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.chunk.load")) {
			NewRelic.setTransactionName(null, "ChunkLoadEvent");
			NewRelic.addCustomParameter("chunkName", e.getChunk().toString());
			String newchunk = "";
			if (e.isNewChunk()) {
				newchunk = "true";
			} else {
				newchunk = "false";
			}
			NewRelic.addCustomParameter("chunkNew", newchunk);
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
	@EventHandler
	@Trace (dispatcher=true)
	public void onChunkUnload(ChunkUnloadEvent  e) {
		if (configGetter.getConfig().getBoolean("enabled") &&
				configGetter.getConfig().getBoolean("track.chunk.unload")) {
			NewRelic.setTransactionName(null, "ChunkUnloadEvent");
			NewRelic.addCustomParameter("chunkName", e.getChunk().toString());
		} else {
			NewRelic.ignoreTransaction();
		}
	}
	
}
