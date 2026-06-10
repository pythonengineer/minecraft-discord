package net.minecraft.game.entity;

import com.google.common.collect.Maps;
import com.mojang.nbt.NBTTagCompound;
import java.util.Map;

import net.lax1dude.eaglercraft.minecraft.EntityConstructor;
import net.minecraft.game.entity.animal.EntityCow;
import net.minecraft.game.entity.animal.EntityPig;
import net.minecraft.game.entity.animal.EntitySheep;
import net.minecraft.game.entity.misc.EntityBoat;
import net.minecraft.game.entity.misc.EntityFallingSand;
import net.minecraft.game.entity.misc.EntityItem;
import net.minecraft.game.entity.misc.EntityMinecart;
import net.minecraft.game.entity.misc.EntitySnowball;
import net.minecraft.game.entity.misc.EntityTNTPrimed;
import net.minecraft.game.entity.monster.EntityCreeper;
import net.minecraft.game.entity.monster.EntityGiantZombie;
import net.minecraft.game.entity.monster.EntityMob;
import net.minecraft.game.entity.monster.EntitySkeleton;
import net.minecraft.game.entity.monster.EntitySlime;
import net.minecraft.game.entity.monster.EntitySpider;
import net.minecraft.game.entity.monster.EntityZombie;
import net.minecraft.game.entity.projectile.EntityArrow;
import net.minecraft.game.world.World;

public class EntityList {
    private static Map<String, Class<? extends Entity>> stringToClassMapping = Maps.newHashMap();
    private static final Map<String, EntityConstructor<? extends Entity>> stringToConstructorMapping = Maps
            .newHashMap();
    private static Map<Class<? extends Entity>, String> classToStringMapping = Maps.newHashMap();
    private static final Map<Class<? extends Entity>, EntityConstructor<? extends Entity>> classToConstructorMapping = Maps
            .newHashMap();

    private static void addMapping(Class<? extends Entity> entityClass, 
            EntityConstructor<? extends Entity> entityConstructor, String entityName) {
        if (stringToClassMapping.containsKey(entityName)) {
            throw new IllegalArgumentException("ID is already registered: " + entityName);
        } else {
            stringToClassMapping.put(entityName, entityClass);
            stringToConstructorMapping.put(entityName, entityConstructor);
            classToStringMapping.put(entityClass, entityName);
            classToConstructorMapping.put(entityClass, entityConstructor);
        }
    }

    public static Entity createEntityByClass(Class<? extends Entity> entityClass, World worldIn) {
        Entity entity = null;

        try {
            EntityConstructor<? extends Entity> constructor = classToConstructorMapping.get(entityClass);
            if (constructor != null) {
                entity = constructor.createEntity(worldIn);
            }
        } catch (Exception exception) {
            System.out.println("Could not create entity");
            exception.printStackTrace();
        }

        return entity;
    }

    public static Entity createEntityByClassUnsafe(Class<? extends Entity> entityClass, World worldIn) {
        EntityConstructor<? extends Entity> constructor = classToConstructorMapping.get(entityClass);
        if (constructor != null) {
            return constructor.createEntity(worldIn);
        }
        return null;
    }

    public static Entity createEntityByName(String entityName, World worldIn) {
        Entity entity = null;

        try {
            EntityConstructor<? extends Entity> constructor = stringToConstructorMapping.get(entityName);
            if (constructor != null) {
                entity = constructor.createEntity(worldIn);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return entity;
    }

    public static Entity createEntityFromNBT(NBTTagCompound nbt, World worldIn) {
        Entity entity = null;

        try {
            EntityConstructor<? extends Entity> constructor = stringToConstructorMapping.get(nbt.getString("id"));
            if (constructor != null) {
                entity = constructor.createEntity(worldIn);
            }
        } catch (Exception exception) {
            System.out.println("Could not create entity");
            exception.printStackTrace();
        }

        if(entity != null) {
            entity.readFromNBT(nbt);
        } else {
            System.out.println("Skipping Entity with id " + nbt.getString("id"));
        }

        return entity;
    }

    public static String getEntityString(Entity entity) {
        return (String)classToStringMapping.get(entity.getClass());
    }

    static {
        addMapping(EntityArrow.class, EntityArrow::new, "Arrow");
        addMapping(EntitySnowball.class, EntityArrow::new, "Snowball");
        addMapping(EntityItem.class, EntityItem::new, "Item");
        addMapping(EntityPainting.class, EntityPainting::new, "Painting");
        addMapping(EntityLiving.class, EntityLiving::new, "Mob");
        addMapping(EntityMob.class, EntityMob::new, "Monster");
        addMapping(EntityCreeper.class, EntityCreeper::new, "Creeper");
        addMapping(EntitySkeleton.class, EntitySkeleton::new, "Skeleton");
        addMapping(EntitySpider.class, EntitySpider::new, "Spider");
        addMapping(EntityGiantZombie.class, EntityGiantZombie::new, "Giant");
        addMapping(EntityZombie.class, EntityZombie::new, "Zombie");
        addMapping(EntitySlime.class, EntitySlime::new, "Slime");
        addMapping(EntityPig.class, EntityPig::new, "Pig");
        addMapping(EntitySheep.class, EntitySheep::new, "Sheep");
        addMapping(EntityCow.class, EntityCow::new, "Cow");
        addMapping(EntityTNTPrimed.class, EntityTNTPrimed::new, "PrimedTnt");
        addMapping(EntityFallingSand.class, EntityFallingSand::new, "FallingSand");
        addMapping(EntityMinecart.class, EntityMinecart::new, "Minecart");
        addMapping(EntityBoat.class, EntityBoat::new, "Boat");
    }
}
