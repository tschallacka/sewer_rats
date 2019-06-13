package tschallacka.mods.rats.entity;

import java.util.Collection;
import java.util.LinkedList;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEnd;
import net.minecraft.world.biome.BiomeHell;
import net.minecraft.world.biome.BiomeVoid;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tschallacka.mods.rats.Rats;
import tschallacka.mods.rats.render.entity.RenderRat;

public class Entities {

    public static void init() {
        // Every entity in our mod has an ID (local to this mod)
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(Rats.MODID, "rat"), EntityRat.class, Rats.MODID+".Rat", id++, Rats.instance, 64, 3, true, 0x996600, 0x00ff00);

        // We want our mob to spawn in Plains and ice plains biomes. If you don't add this then it will not spawn automatically
        // but you can of course still make it spawn manually
        EntityRegistry.addSpawn(EntityRat.class, 100, 3, 5, EnumCreatureType.CREATURE, getAllSpawnBiomes());

        // This is the loot table for our mob
        LootTableList.register(EntityRat.LOOT);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() 
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityRat.class, RenderRat.FACTORY);
    }
    
    private static Biome[] getAllSpawnBiomes() 
    {
        LinkedList<Biome> list = new LinkedList<>();
        Collection<Biome> biomes = ForgeRegistries.BIOMES.getValuesCollection();
        for (Biome bgb : biomes) {
            if (!(bgb instanceof BiomeVoid || bgb instanceof BiomeEnd ||  bgb instanceof BiomeHell) && !list.contains(bgb)) {
            	list.add(bgb);
            }
        }
        return list.toArray(new Biome[0]);
    }
}