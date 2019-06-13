package tschallacka.mods.rats;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import tschallacka.mods.rats.proxy.CommonProxy;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Rats.MODID, 
version = Rats.VERSION, 
name = Rats.MODNAME)
public class Rats 
{
	public static final String MODID = "sewerrats";
	public static final String VERSION = "1.0.0";
	public static final String MODNAME = "Sewer Rats";
	
	@Mod.Instance(Rats.MODID)
	public static Rats instance;
	
	/**
	* The proxy instance.
	* @see tschallacka.mods.rats.proxy.CommonProxy
	* @see tschallacka.mods.rats.proxy.ClientProxy
	* @see tschallacka.mods.rats.proxy.ServerProxy
	*/
	@SidedProxy(clientSide = "tschallacka.mods.rats.proxy.ClientProxy", 
	           serverSide = "tschallacka.mods.rats.proxy.ServerProxy")
	public static CommonProxy proxy;
	
	public static Logger logger;

	public static void log(String stuff) {
		logger.info(stuff);
	}
	
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) 
    {	
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) 
    {
        proxy.postInit(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) 
    {
        
    }
	
}
