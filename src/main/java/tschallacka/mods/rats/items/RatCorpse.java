package tschallacka.mods.rats.items;

import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tschallacka.mods.rats.Rats;

public class RatCorpse extends ItemFood
{
	public final static int RAT_CORPSE = 0;
	public final static int RAT_BACK_LEFT_LEG = 1;
	public final static int RAT_BACK_RIGHT_LEG = 2;
	public final static int RAT_BODY = 3;
	public final static int RAT_BODY_TAIL = 4;
	public final static int RAT_HEAD = 5; 
	public final static int RAT_FRONT_LEFT_LEG = 6;
	public final static int RAT_FRONT_RIGHT_LEG = 7;
	
	public RatCorpse() 
	{
		super(3,7,true);
		super.setPotionEffect(new PotionEffect(MobEffects.NAUSEA, 600, 0), 0.8F);
		setRegistryName("ratcorpse");
		setUnlocalizedName(Rats.MODID + ".ratcorpse");
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.FOOD);
	}

	@SideOnly(Side.CLIENT)
	public void initModel()  
	{
	    ModelLoader.setCustomModelResourceLocation(this, RAT_CORPSE, new ModelResourceLocation(Rats.MODID+":ratcorpse", "inventory"));
	    ModelLoader.setCustomModelResourceLocation(this, RAT_BACK_LEFT_LEG, new ModelResourceLocation(Rats.MODID+":ratbackleftleg", "inventory"));
	    ModelLoader.setCustomModelResourceLocation(this, RAT_BACK_RIGHT_LEG, new ModelResourceLocation(Rats.MODID+":ratbackrightleg", "inventory"));
	    ModelLoader.setCustomModelResourceLocation(this, RAT_BODY, new ModelResourceLocation(Rats.MODID+":ratbody", "inventory"));
	    ModelLoader.setCustomModelResourceLocation(this, RAT_BODY_TAIL, new ModelResourceLocation(Rats.MODID+":ratbodytail", "inventory"));
	    ModelLoader.setCustomModelResourceLocation(this, RAT_HEAD, new ModelResourceLocation(Rats.MODID+":rathead", "inventory"));
	    ModelLoader.setCustomModelResourceLocation(this, RAT_FRONT_LEFT_LEG, new ModelResourceLocation(Rats.MODID+":ratleftleg", "inventory"));
	    ModelLoader.setCustomModelResourceLocation(this, RAT_FRONT_RIGHT_LEG, new ModelResourceLocation(Rats.MODID+":ratrightleg", "inventory"));
	}
	
	@Override
	@SideOnly(Side.CLIENT) 
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this, 1, RAT_CORPSE));
		/*items.add(new ItemStack(this, 1,  RAT_BACK_LEFT_LEG));
		items.add(new ItemStack(this, 1,  RAT_BACK_RIGHT_LEG));
		items.add(new ItemStack(this, 1,  RAT_BODY));
		items.add(new ItemStack(this, 1,  RAT_BODY_TAIL));
		items.add(new ItemStack(this, 1,  RAT_HEAD));
		items.add(new ItemStack(this, 1,  RAT_FRONT_LEFT_LEG));
		items.add(new ItemStack(this, 1,  RAT_FRONT_RIGHT_LEG));*/
	}
}
