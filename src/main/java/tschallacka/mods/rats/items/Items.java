package tschallacka.mods.rats.items;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Items 
{
	 @GameRegistry.ObjectHolder("sewerrats:rattail")
     public static RatTail RAT_TAIL;
	 @GameRegistry.ObjectHolder("sewerrats:ratcorpse")
     public static RatCorpse RAT_CORPSE;
	 
	 @SideOnly(Side.CLIENT)
     public static void initModels() 
     {
		 RAT_TAIL.initModel();
		 RAT_CORPSE.initModel();
     }
}
