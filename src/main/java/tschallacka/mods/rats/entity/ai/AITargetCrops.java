package tschallacka.mods.rats.entity.ai;


import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockMushroom;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tschallacka.mods.rats.entity.EntityRat;

public class AITargetCrops extends EntityAIMoveToBlock
{
    /** Villager that is harvesting */
    private final EntityRat rat;

    public AITargetCrops(EntityRat ratIn, double speedIn)
    {
        super(ratIn, speedIn, 16);
        this.rat = ratIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.runDelay <= 0)
        {
            if (!net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.rat.world, this.rat))
            {
                return false;
            }
        }

        return super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return super.shouldContinueExecuting();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        super.updateTask();
        this.rat.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)this.destinationBlock.getZ() + 0.5D, 10.0F, (float)this.rat.getVerticalFaceSpeed());

        if (this.getIsAboveDestination())
        {
            World world = this.rat.world;
            BlockPos blockpos = this.destinationBlock.up();
            IBlockState iblockstate = world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();

            if ((block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate)) 
            		|| block instanceof BlockMelon 
            		|| block instanceof BlockPumpkin 
            		|| block instanceof BlockMushroom 
            		|| block instanceof BlockTallGrass)
            	
            {
                world.destroyBlock(blockpos, true);
            }
            
            this.runDelay = 200;
        }
    }

    /**
     * Return true to set given position as destination
     */
    protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
    	IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (block == Blocks.FARMLAND)
        {
            pos = pos.up();
            iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();

            if (block instanceof BlockCrops && ((BlockCrops)block).isMaxAge(iblockstate))
            {
                return true;
            }
        }        
        else {
        		pos = pos.up();
                iblockstate = worldIn.getBlockState(pos);
                block = iblockstate.getBlock();
	        	if(block instanceof BlockMelon 
	            		|| block instanceof BlockPumpkin 
	            		|| block instanceof BlockMushroom 
	            		|| block == Blocks.TALLGRASS) {
	        		return true;
	        	}
        	
        }
        return false;
    }
}
