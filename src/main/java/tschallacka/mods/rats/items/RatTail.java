package tschallacka.mods.rats.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tschallacka.mods.rats.Rats;

public class RatTail extends ItemFood 
{
	public RatTail() 
	{
		super(1, 1f, true);
		setRegistryName("rattail");
		setUnlocalizedName(Rats.MODID + ".rattail");
		this.setCreativeTab(CreativeTabs.FOOD);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() 
	{
	    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
