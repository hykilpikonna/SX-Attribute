package github.saukiya.sxattribute.data.attribute.sub.damage;

import github.saukiya.sxattribute.SXAttribute;
import github.saukiya.sxattribute.data.attribute.SXAttributeType;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import github.saukiya.sxattribute.data.eventdata.EventData;
import github.saukiya.sxattribute.data.eventdata.sub.DamageEventData;
import github.saukiya.sxattribute.util.Config;
import github.saukiya.sxattribute.util.Message;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

/**
 * 中毒
 * @author Saukiya
 */
public class PoisonAttribute extends SubAttribute {

    /**
     * double[0] 中毒几率
     */
    public PoisonAttribute() {
        super("Poison", 1, SXAttributeType.DAMAGE);
    }

    @Override
    public void eventMethod(EventData eventData) {
        if (eventData instanceof DamageEventData) {
            DamageEventData damageEventData = (DamageEventData) eventData;
            if (getAttributes()[0] > 0 && probability(getAttributes()[0] - damageEventData.getEntityAttributeDoubles("Toughness")[0])) {
                int tick = 40 + SXAttribute.getRandom().nextInt(60);
                damageEventData.getEntity().addPotionEffect(new PotionEffect(PotionEffectType.POISON, tick, SXAttribute.getRandom().nextInt(2) + 1));
                damageEventData.sendHolo(Message.getMsg(Message.PLAYER__HOLOGRAPHIC__POISON, getDf().format(tick / 20D)));
                Message.send(damageEventData.getDamager(), Message.PLAYER__BATTLE__POISON, damageEventData.getEntityName(), getFirstPerson());
                Message.send(damageEventData.getEntity(), Message.PLAYER__BATTLE__POISON, getFirstPerson(), damageEventData.getDamagerName());
            }
        }
    }

    @Override
    public String getPlaceholder(String string) {
        return string.equalsIgnoreCase("Poison") ? getDf().format(getAttributes()[0]) : null;
    }

    @Override
    public List<String> getPlaceholders() {
        return Collections.singletonList("Poison");
    }

    @Override
    public boolean loadAttribute(String lore) {
        if (lore.contains(Config.getConfig().getString(Config.NAME_POISON))) {
            getAttributes()[0] += Double.valueOf(getNumber(lore));
            return true;
        }
        return false;
    }

    @Override
    public double getValue() {
        return getAttributes()[0] * Config.getConfig().getInt(Config.VALUE_POISON);
    }
}
