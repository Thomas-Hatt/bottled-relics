package bottled.util;

import bottled.powers.GremlinHornPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = {}
)
public class GremlinHornPatch {
    @SpirePostfixPatch
    public static void onMonsterDie(AbstractMonster __instance) {
        for (AbstractPower power : AbstractDungeon.player.powers) {
            if (power instanceof GremlinHornPower) {
                ((GremlinHornPower) power).onMonsterDeath(__instance);
            }
        }
    }
}