package tschallacka.mods.rats.entity.ai;


import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

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
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import tschallacka.mods.rats.Rats;
import tschallacka.mods.rats.entity.EntityRat;

public class AIEatItem extends EntityAIBase
{
	private final EntityAnimal creature;
    private EntityItem targetEntity;
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
    Predicate predicate;
    /** If the distance to the target entity is further than this, this AI task will not run. */
    private final float maxTargetDistance;
    private boolean hasEaten = false;
    public AIEatItem(EntityAnimal creature, double speedIn, float targetMaxDistance)
    {
        this.creature = creature;
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(1);
        this.predicate = new Predicate<EntityItem>()
        {
            public boolean apply(@Nullable EntityItem item)
            {
                double d0 = AIEatItem.this.getFollowRange();

                return (double)item.getDistance(AIEatItem.this.creature) > d0 ? false : AIEatItem.this.isEdibleItem(item);
            }
        };
    }

    public boolean isEdibleItem(EntityItem entityItem) 
    {
    	ItemStack stack  = entityItem.getItem();
    	if(stack == null) return false;
    	if(stack.isEmpty()) return false;
    	Item item = entityItem.getItem().getItem();
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
        List<EntityItem> list = this.creature.world.<EntityItem>getEntitiesWithinAABB(EntityItem.class, this.creature.getEntityBoundingBox().grow(followdistance, 4.0D, followdistance),this.predicate);
        if(list == null) return false;
        if(list.isEmpty()) return false;
        this.targetEntity = list.get(0);
        
        if (this.targetEntity == null)
        {
            return false;
        }
        else if (this.targetEntity.getDistanceSq(this.creature) > (double)(this.maxTargetDistance * this.maxTargetDistance))
        {
            return false;
        }
        else
        {
            
            this.movePosX = this.targetEntity.posX;
            this.movePosY =  this.targetEntity.posY;
            this.movePosZ = this.targetEntity.posZ;
            return true;
            
        }
    }
    
    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        this.creature.getLookHelper().setLookPosition((double)this.movePosX, (double)(this.movePosY), (double)this.movePosZ, 10.0F, (float)this.creature.getVerticalFaceSpeed());
        
        if (this.creature.getDistanceSq(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ) > 1.0D)
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
            
            if(!this.creature.world.isRemote) {
            	if(this.creature.isChild()) {
            		this.creature.ageUp((int)((float)(-this.creature.getGrowingAge() / 20) * 0.1F), true);
            	}
            	else {
            		if(!this.creature.isInLove()) {
            			this.creature.setInLove(null);
            		}
            	}
            	this.creature.heal(0.5f);
	            this.targetEntity.getItem().shrink(1);
	            this.hasEaten = true;
	            if(this.targetEntity.getItem().isEmpty()) {
	            	this.targetEntity.setDead();
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
        return !this.hasEaten && !this.creature.isInLove() && !this.creature.getNavigator().noPath() && this.timeoutCounter >= -this.maxStayTicks && this.timeoutCounter <= 1200 && this.targetEntity.getDistanceSq(this.creature) < (double)(this.maxTargetDistance * this.maxTargetDistance);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.targetEntity = null;
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
