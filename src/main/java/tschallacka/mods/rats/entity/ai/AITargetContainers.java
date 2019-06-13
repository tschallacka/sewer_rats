package tschallacka.mods.rats.entity.ai;


import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import tschallacka.mods.rats.Rats;
import tschallacka.mods.rats.entity.EntityRat;

public class AITargetContainers extends EntityAIBase
{
	private final EntityAnimal creature;
    private TileEntity targetContainer;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    
    /** Controls task execution delay */
    protected int runDelay;
    private int timeoutCounter;
    private int maxStayTicks;
    /** Block to move to */
    private boolean isAboveDestination;
    
    private final double speed;
    private boolean isAboveDistination;
    /** If the distance to the target entity is further than this, this AI task will not run. */
    private final float maxTargetDistance;
    private boolean hasEaten = false;
    public AITargetContainers(EntityAnimal creature, double speedIn, float targetMaxDistance)
    {
        this.creature = creature;
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(1);
        
    }

    public boolean isEdibleItem(Item item) 
    {
    	
    	if(item == null) return false;
    	
    	return  item instanceof ItemFood 
    			|| item instanceof ItemSeeds 
    			|| item instanceof ItemSoup 
    			|| item instanceof ItemAppleGold
    			|| item instanceof ItemEgg
    			|| item instanceof IPlantable;
    	
    }
   
    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
    	if (this.creature.isInLove()) {
    		return false;
    	}
    	if (this.runDelay > 0)
        {
            --this.runDelay;
            return false;
        }
        else
        {
        	this.hasEaten = false;
            this.runDelay = 20 + this.creature.getRNG().nextInt(200);
            return this.searchForDestination();
        }
    	
    }
    
    public boolean searchForDestination() 
    {
    	double followdistance = this.getFollowRange();
    	World world = this.creature.getEntityWorld();
    	
    	List<TileEntity> allTEs = world.loadedTileEntityList;
    	double maxDistance = (double)(this.maxTargetDistance * this.maxTargetDistance);
    	for (TileEntity t : Collections2
    			.filter(allTEs, 
    					new Predicate<TileEntity>() { @Override public boolean apply(TileEntity te) { 
    						return te instanceof IInventory 
    								&& (AITargetContainers.this.creature.getDistanceSq(te.getPos()) < maxDistance); } 
    					} )
    			.stream()
    			.sorted((o1, o2) -> (int)(AITargetContainers.this.creature.getDistanceSq(o1.getPos()) - AITargetContainers.this.creature.getDistanceSq(o2.getPos()))).
                        collect(Collectors.toList())
    			) {
    	    IInventory inventory = (IInventory)t;
    	    BlockPos pos = t.getPos();
        	int count = inventory.getSizeInventory();
        	
        	for(int c = 0; c < count; c++) {
        		ItemStack stack = inventory.getStackInSlot(c);
        		
        		if(stack != null && !stack.isEmpty() && this.isEdibleItem(stack.getItem())) {
        			this.targetContainer = t;
        			this.movePosX = pos.getX() + 0.5d;
                    this.movePosY = pos.getY() + 0.5d;
                    this.movePosZ = pos.getZ() + 0.5d;
                    return true;
        		}
        	}
    	}
    	return false;
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        this.creature.getLookHelper().setLookPosition((double)this.movePosX, (double)(this.movePosY), (double)this.movePosZ, 10.0F, (float)this.creature.getVerticalFaceSpeed());
        
        if (this.creature.getDistanceSq(this.targetContainer.getPos()) > 1.5D)
        {
            this.isAboveDestination = false;
            ++this.timeoutCounter;

            if (this.timeoutCounter % 40 == 0)
            {
            	this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
            }
        }
        else
        {
        	
            this.isAboveDestination = true;
            
            if(targetContainer == null || targetContainer.isInvalid()) {
            	this.runDelay = 200;
            	return;
            }
            
            if(!this.creature.world.isRemote) {
            	IInventory inventory = (IInventory)this.targetContainer;
                
        	    int count = inventory.getSizeInventory();
            	int slot = -1;
            	ItemStack stack = null;
            	for(int c = 0; c < count; c++) {
            		stack = inventory.getStackInSlot(c);
            		if(stack != null && !stack.isEmpty() && this.isEdibleItem(stack.getItem())) {
            			slot = c;
            			break;
            		}
            	}
            	if(slot == -1) {
            		this.targetContainer = null;
            		this.runDelay = 200;
            		return;
            	}
            	if(this.creature.isChild()) {
            		this.creature.ageUp((int)((float)(-this.creature.getGrowingAge() / 20) * 0.1F), true);
            		this.creature.heal(0.5f);
            		inventory.decrStackSize(slot, 1);
            		this.hasEaten = true;
            	}
            	else {
            		if(!this.creature.isInLove()) {
            			this.creature.setInLove(null);
            			this.creature.heal(0.5f);            			
            			inventory.decrStackSize(slot, 1);
            			this.hasEaten = true;
            		}
            	}
            	
	            this.runDelay = 200;
            }
        }
        
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.hasEaten && !this.creature.isInLove() && !this.creature.getNavigator().noPath() && !(targetContainer == null) && !targetContainer.isInvalid() && this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.creature.getDistanceSq(this.targetContainer.getPos()) < (double)(this.maxTargetDistance * this.maxTargetDistance);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.targetContainer = null;
        this.timeoutCounter = 0;
        this.isAboveDestination = false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.timeoutCounter = 0;
        this.maxStayTicks = this.creature.getRNG().nextInt(this.creature.getRNG().nextInt(1200) + 1200) + 1200;
        this.creature.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
    }

    protected double getFollowRange()
    {
        IAttributeInstance iattributeinstance = this.creature.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }
}

