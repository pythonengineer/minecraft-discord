package net.minecraft.src;

import com.google.common.collect.Maps;
import java.util.Map;
import net.lax1dude.eaglercraft.minecraft.EntityConstructor;

public class EntityList {
    private static Map<String, Class<? extends Entity>> stringToClassMapping = Maps.newHashMap();
    private static final Map<String, EntityConstructor<? extends Entity>> stringToConstructorMapping = Maps
            .newHashMap();
    private static Map<Class<? extends Entity>, String> classToStringMapping = Maps.newHashMap();
    private static Map<Integer, EntityConstructor<? extends Entity>> IDtoConstructorMapping = Maps.newHashMap();
    private static Map<Class<? extends Entity>, Integer> classToIDMapping = Maps.newHashMap();
    private static final Map<Class<? extends Entity>, EntityConstructor<? extends Entity>> classToConstructorMapping = Maps
            .newHashMap();

    private static void addMapping(Class<? extends Entity> entityClass, 
            EntityConstructor<? extends Entity> entityConstructor, String entityName, int entityID) {
        if (stringToClassMapping.containsKey(entityName)) {
            throw new IllegalArgumentException("ID is already registered: " + entityName);
        } else {
            stringToClassMapping.put(entityName, entityClass);
            stringToConstructorMapping.put(entityName, entityConstructor);
            classToStringMapping.put(entityClass, entityName);
            IDtoConstructorMapping.put(entityID, entityConstructor);
            classToIDMapping.put(entityClass, entityID);
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

    public static Entity createEntityByID(int entityID, World worldIn) {
        Entity entity = null;

        try {
            EntityConstructor<? extends Entity> constructor = IDtoConstructorMapping.get(entityID);
            if (constructor != null) {
                entity = constructor.createEntity(worldIn);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if(entity == null) {
            System.out.println("Skipping Entity with id " + entityID);
        }

        return entity;
    }

    public static int getEntityID(Entity entity) {
        return ((Integer)classToIDMapping.get(entity.getClass())).intValue();
    }

    public static String getEntityString(Entity entity) {
        return (String)classToStringMapping.get(entity.getClass());
    }

    static {
        addMapping(EntityArrow.class, EntityArrow::new, "Arrow", 10);
        addMapping(EntitySnowball.class, EntitySnowball::new, "Snowball", 11);
        addMapping(EntityItem.class, EntityItem::new, "Item", 1);
        addMapping(EntityPainting.class, EntityPainting::new, "Painting", 9);
        addMapping(EntityLiving.class, null, "Mob", 48);
        addMapping(EntityMobs.class, EntityMobs::new, "Monster", 49);
        addMapping(EntityCreeper.class, EntityCreeper::new, "Creeper", 50);
        addMapping(EntitySkeleton.class, EntitySkeleton::new, "Skeleton", 51);
        addMapping(EntitySpider.class, EntitySpider::new, "Spider", 52);
        addMapping(EntityZombieSimple.class, EntityZombieSimple::new, "Giant", 53);
        addMapping(EntityZombie.class, EntityZombie::new, "Zombie", 54);
        addMapping(EntitySlime.class, EntitySlime::new, "Slime", 55);
        addMapping(EntityGhast.class, EntityGhast::new, "Ghast", 56);
        addMapping(EntityPigZombie.class, EntityPigZombie::new, "PigZombie", 57);
        addMapping(EntityPig.class, EntityPig::new, "Pig", 90);
        addMapping(EntitySheep.class, EntitySheep::new, "Sheep", 91);
        addMapping(EntityCow.class, EntityCow::new, "Cow", 92);
        addMapping(EntityChicken.class, EntityChicken::new, "Chicken", 93);
        addMapping(EntitySquid.class, EntitySquid::new, "Squid", 94);
        addMapping(EntityTNTPrimed.class, EntityTNTPrimed::new, "PrimedTnt", 20);
        addMapping(EntityFallingSand.class, EntityFallingSand::new, "FallingSand", 21);
        addMapping(EntityMinecart.class, EntityMinecart::new, "Minecart", 40);
        addMapping(EntityBoat.class, EntityBoat::new, "Boat", 41);
    }
}