package tschallacka.mods.rats.proxy;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tschallacka.mods.rats.Rats;
import tschallacka.mods.rats.entity.Entities;
import tschallacka.mods.rats.entity.EntityRat;
import tschallacka.mods.rats.items.RatCorpse;
import tschallacka.mods.rats.items.RatTail;

@Mod.EventBusSubscriber
public class CommonProxy 
{
    public void preInit(FMLPreInitializationEvent e) 
    {
         Entities.init();
    }
    
    public void init(FMLInitializationEvent e) {
    	
    }

    public void postInit(FMLPostInitializationEvent e) {

    }

    @SubscribeEvent
    public static void canBabySpawn(BabyEntitySpawnEvent event) 
    {
    	if(event.getParentA() instanceof EntityRat) {
    		World world = event.getParentA().getEntityWorld();
    		if(world.getEntitiesWithinAABB(EntityRat.class, event.getParentA().getEntityBoundingBox().grow(8 *16.0D, 128.0D, 8*16.0D)).size() > 100) {
    			event.setCanceled(true);
    		};
    	}
    }
    
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) 
    {

    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) 
    {
        event.getRegistry().register(new RatTail());
        event.getRegistry().register(new RatCorpse());
    }

}