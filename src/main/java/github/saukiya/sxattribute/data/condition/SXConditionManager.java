package github.saukiya.sxattribute.data.condition;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.condition.sub.*;
import lombok.Getter;

/**
 * 条件管理器
 *
 * @author Saukiya
 */
public class SXConditionManager {

    @Getter
    private final ConditionMap conditionMap = SubCondition.conditionMap;

    public SXConditionManager(SXAttribute plugin) {
        new MainHandCondition().registerCondition(plugin);
        new OffHandCondition().registerCondition(plugin);
        new HandCondition(plugin).registerCondition(plugin);
        new LimitLevelCondition().registerCondition(plugin);
        new RoleCondition().registerCondition(plugin);
        new ExpiryTimeCondition().registerCondition(plugin);
        new AttackSpeedCondition().registerCondition(plugin);
    }

}
