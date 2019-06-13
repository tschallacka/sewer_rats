package tschallacka.mods.rats.entity;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tschallacka.mods.rats.Rats;
import tschallacka.mods.rats.entity.ai.AIEatItem;
import tschallacka.mods.rats.entity.ai.AIStickTogether;
import tschallacka.mods.rats.entity.ai.AITargetContainers;
import tschallacka.mods.rats.entity.ai.AITargetCrops;
import tschallacka.mods.rats.entity.ai.FollowFoodHolder;

import javax.annotation.Nullable;

public class EntityRat extends EntityAnimal 
{

    public static final ResourceLocation LOOT = new ResourceLocation(Rats.MODID, "entities/rat");

    public EntityRat(World worldIn) 
    {
        super(worldIn);
        super.setNoAI(false);
        setSize(0.3F, 0.3F);
    }

    @Override
    protected void entityInit() 
    {
        super.entityInit();
    }
    
    @Override
    public SoundEvent getSwimSound() 
    {
    	return SoundEvents.ENTITY_GENERIC_SWIM;
    }
    
    @Override
    public SoundEvent getSplashSound()
    {
    	return SoundEvents.ENTITY_BOBBER_SPLASH;
    }
    
    @Nullable
    public SoundEvent getAmbientSound()
    {
        return this.rand.nextInt(4) != 0 ? null : SoundEvents.ENTITY_BAT_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_BAT_HURT;
    }

    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_BAT_DEATH;
    }

    @Override
    protected void applyEntityAttributes() 
    {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        // Here we set various attributes for our mob. Like maximum health, armor, speed, ...
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(0.5D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).setBaseValue(1.5D);
        
         
    }
    
    @Override
    protected void initEntityAI() 
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAIRestrictSun(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this,1.0d, true));
        this.tasks.addTask(5, new AITargetContainers(this, 1.0d, 32));
        this.tasks.addTask(6, new FollowFoodHolder(this, 1.0D, false));
        this.tasks.addTask(7, new AITargetCrops(this, 1.0d));
        this.tasks.addTask(8, new AIEatItem(this, 1.0d, 32f));
        this.tasks.addTask(9, new EntityAIMate(this, 1D));
        this.tasks.addTask(10, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.tasks.addTask(10, new EntityAIFollowParent(this, 1.0D));
        this.tasks.addTask(10, new AIStickTogether(this, 1.0d));
        this.applyEntityAI();
    }

    private void applyEntityAI() 
    {
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityZombie.class, true, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPigZombie.class, true, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityGiantZombie.class, true, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityZombieHorse.class, true, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityZombieVillager.class, true, true));
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer))
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }
    
    @Override
    @Nullable
    protected ResourceLocation getLootTable()
    {	
        return LOOT;
    }

    @Override
    public int getMaxSpawnedInChunk() 
    {
        return 5;
    }
    
    @Override
	public EntityRat createChild(EntityAgeable ageable) 
    {
		return new EntityRat(world);
	}

	@Override
	public float getEyeHeight() 
	{
		return this.isChild() ? this.height : 0.2F;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) 
	{
		return !(stack == null) && !stack.isEmpty() && stack.getItem() != null && stack.getItem() instanceof ItemFood;
	}

}