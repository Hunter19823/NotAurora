package pie.ilikepiefoo2.borealis.minecraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import pie.ilikepiefoo2.borealis.BorealisConfigHandler;
import pie.ilikepiefoo2.borealis.BorealisHomePageEvent;
import pie.ilikepiefoo2.borealis.BorealisPageEvent;
import pie.ilikepiefoo2.borealis.page.HomePageEntry;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class BorealisMinecraftHandler {

    @SubscribeEvent
    public void onHomeEvent(BorealisHomePageEvent event)
    {
        event.add(new HomePageEntry("Mod List", "modlist", "https://i.imgur.com/yQNrfg7.png"));
        event.add(new HomePageEntry("Minecraft", "minecraft", "https://i.imgur.com/1aRpzK0.png")
                .add(new HomePageEntry("Online Players", "online_players", "https://i.imgur.com/a5dkvFu.png"))
                .add(new HomePageEntry("Online Players API", "online_players.json", "https://i.imgur.com/a5dkvFu.png"))
                .add(new HomePageEntry("World Info API", "world_info.json", "https://i.imgur.com/OVxZy1w.png"))
        );
    }

    @SubscribeEvent
    public void onPageEvent(BorealisPageEvent event){
        if (event.checkPath("modlist", "*"))
        {
            HashSet<String> set = new HashSet(Arrays.asList(BorealisConfigHandler.COMMON.blacklistedMods.get()));

            if (!set.contains(event.getSplitUri()[1]))
            {
                if (ModList.get().isLoaded(event.getSplitUri()[1]))
                {
                    event.returnPage(new ModPage(ModList.get().getModContainerById(event.getSplitUri()[1]).get().getModInfo()));
                }
            }
        }
        else if (event.checkPath("modlist"))
        {
            event.returnPage(new ModListPage(new HashSet<>(BorealisConfigHandler.COMMON.blacklistedMods.get())));
        }
        else if (event.checkPath("minecraft", "online_players"))
        {
            event.returnPage(new PlayerListPage(event.getBorealisServer().getMinecraftServer()));
        }
        else if (event.checkPath("minecraft", "online_players.json"))
        {
            event.returnPage(new PlayerListJson(event.getBorealisServer().getMinecraftServer()));
        }
        else if (event.checkPath("minecraft", "world_info.json"))
        {
            LogManager.getLogger().info("World JSON accessed");
            event.returnPage(new WorldInfoJSON(event.getBorealisServer().getMinecraftServer()));
        }
    }
}
