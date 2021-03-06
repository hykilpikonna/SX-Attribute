package github.saukiya.sxattribute.api;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.RandomStringManager;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.condition.SXConditionType;
import github.saukiya.sxattribute.data.condition.SubCondition;
import github.saukiya.sxattribute.inventory.RepairInventory;
import github.saukiya.sxattribute.inventory.SellInventory;
import github.saukiya.sxattribute.util.ItemUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * API 获取方式为 SXAttribute.getApi()
 *
 * @author Saukiya
 * @since 2018年3月25日
 */

public class SXAttributeAPI {

    private final Map<UUID, Map<Class<?>, SXAttributeData>> map = new HashMap<>();
    private final SXAttribute plugin;

    /**
     * 加载SXAttributeAPI
     *
     * @param plugin SXAttribute
     */
    public SXAttributeAPI(SXAttribute plugin) {
        this.plugin = plugin;
    }

    /**
     * 获取实体总API属性数据
     *
     * @param uuid 实体UUID
     * @return 来自所有附属插件提供的 SXAttributeData
     */
    public SXAttributeData getAPIStats(UUID uuid) {
        SXAttributeData attributeData = new SXAttributeData();
        if (map.containsKey(uuid)) {
            for (Class<?> c : map.get(uuid).keySet()) {
                attributeData.add(map.get(uuid).get(c));
            }
        }
        return attributeData;
    }

    /**
     * 获取 ItemUtil(NBT反射类)
     * key值结构为:SX-Attribute-{key}
     *
     * @return ItemUtil
     */
    public ItemUtil getItemUtil() {
        return plugin.getItemUtil();
    }

    /**
     * 获取 RandomStringManager(随机字符管理)
     *
     * @return RandomStringManager
     */
    public RandomStringManager getRandomStringManager() {
        return plugin.getRandomStringManager();
    }

    /**
     * 为抛射物设定数据，例如箭、雪球、烈焰球。
     * 本插件只会在玩家射箭的时候附加属性
     * 如需添加其他请自行添加抛射物
     *
     * @param uuid          实体UUID
     * @param attributeData 属性
     */
    public void setProjectileData(UUID uuid, SXAttributeData attributeData) {
        plugin.getAttributeManager().setProjectileData(uuid, attributeData);
    }

    /**
     * 获取实体属性数据 更改无效
     *
     * @param livingEntity LivingEntity
     * @return SXAttributeData
     */
    public SXAttributeData getEntityAllData(LivingEntity livingEntity) {
        return plugin.getAttributeManager().getEntityData(livingEntity);
    }

    /**
     * 获取实体与插件关联的属性数据
     *
     * @param c    Class
     * @param uuid UUID
     * @return SXAttributeData / null
     */
    public SXAttributeData getEntityAPIData(Class<?> c, UUID uuid) {
        return map.containsKey(uuid) ? map.get(uuid).get(c) : null;
    }

    /**
     * 判断插件是否有注册该实体的属性
     *
     * @param c    Class
     * @param uuid UUID
     * @return boolean
     */
    public boolean isEntityAPIData(Class<?> c, UUID uuid) {
        return map.containsKey(uuid) && map.get(uuid).containsKey(c);
    }

    /**
     * 设置插件关联的实体属性数据
     *
     * @param c             Class
     * @param uuid          UUID
     * @param attributeData SXAttributeData
     */
    public void setEntityAPIData(Class<?> c, UUID uuid, SXAttributeData attributeData) {
        Map<Class<?>, SXAttributeData> statsMap = new HashMap<>();
        if (map.containsKey(uuid)) {
            statsMap = map.get(uuid);
        } else {
            map.put(uuid, statsMap);
        }
        statsMap.put(c, attributeData);
    }

    /**
     * 清除插件关联的实体属性数据
     * 会返回清除前的数据
     *
     * @param c    插件Class
     * @param uuid 实体UUID
     * @return SXAttributeData / null
     */
    public SXAttributeData removeEntityAPIData(Class<?> c, UUID uuid) {
        SXAttributeData attributeData = null;
        if (map.containsKey(uuid) && map.get(uuid).containsKey(c)) {
            attributeData = map.get(uuid).remove(c);
        }
        return attributeData;
    }

    /**
     * 清除插件关联的所有实体属性数据
     *
     * @param c Class
     */
    public void removePluginAllEntityData(Class<?> c) {
        for (Map<Class<?>, SXAttributeData> statsMap : map.values()) {
            statsMap.remove(c);
        }
    }

    /**
     * 清除插件所有关联的实体属性数据
     *
     * @param uuid 实体UUID
     */
    public void removeEntityAllPluginData(UUID uuid) {
        map.remove(uuid);
    }


    /**
     * 获取List的SXAttributeData数据
     * (entity/type 为null 时不进行条件判断)
     * 如果不满足条件则返回null
     *
     * @param entity   LivingEntity
     * @param type     SXConditionType
     * @param loreList List
     * @return SXAttributeData
     */
    public SXAttributeData getLoreData(LivingEntity entity, SXConditionType type, List<String> loreList) {
        return plugin.getAttributeManager().getListStats(entity, type, loreList);
    }

    /**
     * 获取物品的SXAttributeData数据，可以是多个
     * (entity/type 为null 时不进行条件判断)
     * 不满足条件的ItemStack将会在数组内设置为null
     *
     * @param livingEntity LivingEntity
     * @param type         SXConditionType
     * @param item         ItemStack[]
     * @return SXAttributeData
     */
    public SXAttributeData getItemData(LivingEntity livingEntity, SXConditionType type, ItemStack... item) {
        return plugin.getAttributeManager().getItemData(livingEntity, type, item);
    }

    //

    /**
     * 判断玩家是否达到使用物品要求
     * SXConditionType 为判断位置 一般情况为ALL
     *
     * @param entity LivingEntity
     * @param type   SXConditionType
     * @param item   ItemStack
     * @return boolean
     */
    public boolean isUse(LivingEntity entity, SXConditionType type, ItemStack item) {
        return plugin.getAttributeManager().isUse(entity, type, item);
    }

    /**
     * 获取物品的限制等级
     *
     * @param item ItemStack
     * @return int / -1
     */
    public int getItemLevel(ItemStack item) {
        return SubCondition.getItemLevel(item);
    }

    /**
     * 获取实体等级(如果SX-Level工作时 那将会获取SL等级)
     * 怪物目前为10000
     *
     * @param entity LivingEntity
     * @return level
     */
    public int getEntityLevel(LivingEntity entity) {
        return SubCondition.getLevel(entity);
    }

    /**
     * 更新玩家装备属性
     * RPGInventory运行的情况下，不更新装备属性(特殊情况)
     *
     * @param entity LivingEntity
     */
    public void updateEquipmentData(LivingEntity entity) {
        plugin.getAttributeManager().loadEquipmentData(entity);
    }

    /**
     * 更新玩家手持属性
     *
     * @param entity LivingEntity
     */
    public void updateHandData(LivingEntity entity) {
        plugin.getAttributeManager().loadHandData(entity);
    }

    /**
     * 更新玩家自定义槽属性
     *
     * @param player Player
     */
    public void updateSlotData(Player player) {
        plugin.getAttributeManager().loadSlotData(player);
    }

    /**
     * UPDATE类属性更新
     *
     * @param entity LivingEntity
     */
    public void updateStats(LivingEntity entity) {
        plugin.getAttributeManager().updateStatsEvent(entity);
    }

    /**
     * 获取物品
     * 代入Player 则支持Placeholder变量
     *
     * @param itemKey String
     * @param player  Player
     * @return ItemStack
     */
    public ItemStack getItem(String itemKey, Player player) {
        return plugin.getItemDataManager().getItem(itemKey, player);
    }

    /**
     * 获取物品编号列表
     *
     * @return Set
     */
    public Set<String> getItemList() {
        return plugin.getItemDataManager().getItemList();
    }

    /**
     * 打开属性界面
     *
     * @param player Player
     */
    public void openStatsInventory(Player player) {
        plugin.getStatsInventory().openStatsInventory(player);
    }

    /**
     * 打开修复界面
     *
     * @param player Player
     */
    public void openRepairInventory(Player player) {
        RepairInventory.openRepairInventory(player);
    }

    /**
     * 打开出售界面
     *
     * @param player Player
     */
    public void openSellInventory(Player player) {
        SellInventory.openSellInventory(player);
    }

    /**
     * 打开显示displaySlot界面
     *
     * @param player Player
     */
    public void openDisplaySlotInventory(Player player) {
        plugin.getDisplaySlotInventory().openDisplaySlotInventory(player);
    }
}
