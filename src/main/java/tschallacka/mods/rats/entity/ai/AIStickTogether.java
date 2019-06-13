package tschallacka.mods.rats.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityAnimal;

public class AIStickTogether extends EntityAIBase
{
    /** The child that is following its parent. */
    EntityAnimal animal;
    EntityAnimal parentAnimal;
    double moveSpeed;
    private int delayCounter;

    public AIStickTogether(EntityAnimal animal, double speed)
    {
        this.animal = animal;
        this.moveSpeed = speed;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List<EntityAnimal> list = this.animal.world.<EntityAnimal>getEntitiesWithinAABB(this.animal.getClass(), this.animal.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D));
        EntityAnimal entityanimal = null;
        double d0 = Double.MAX_VALUE;

        for (EntityAnimal entityanimal1 : list)
        {
            if (entityanimal1.getGrowingAge() >= 0)
            {
                double d1 = this.animal.getDistanceSq(entityanimal1);

                if (d1 <= d0)
                {
                    d0 = d1;
                    entityanimal = entityanimal1;
                }
            }
        }

        if (entityanimal == null)
        {
            return false;
        }
        else if (d0 < 9.0D)
        {
            return false;
        }
        else
        {
            this.parentAnimal = entityanimal;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        if (!this.parentAnimal.isEntityAlive())
        {
            return false;
        }
        else
        {
            double d0 = this.animal.getDistanceSq(this.parentAnimal);
            return d0 >= 9.0D && d0 <= 256.0D;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.delayCounter = 0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.parentAnimal = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        if (--this.delayCounter <= 0)
        {
            this.delayCounter = 10;
            this.animal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
        }
    }
}
