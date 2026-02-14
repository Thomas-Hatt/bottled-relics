package bottled.potions;

import basemod.abstracts.CustomPotion;
import bottled.BottledRelicsMod;
import bottled.powers.SneckoEyePower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bottled.BottledRelicsMod.makeID;

public class SneckoEye extends CustomPotion {
    public static final String ID = makeID(SneckoEye.class.getSimpleName());
    public static final String POTION_ID = BottledRelicsMod.makeID("SneckoEye");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;

    public SneckoEye() {
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.EYE, PotionColor.SNECKO);
        this.isThrown = false;
        this.targetRequired = false;

        // Lab outline color
        this.labOutlineColor = Color.BLACK.cpy();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Initializes potency and description
    public void initializeData() {
        // Set potency value
        this.potency = getPotency();

        this.description = potionStrings.DESCRIPTIONS[0] + (potency * 2) + potionStrings.DESCRIPTIONS[1];

        // Refresh tooltip
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Defines potion effect when used
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // Apply SneckoEyePower to player
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SneckoEyePower(AbstractDungeon.player, potency)));

            // Apply ConfusionPower to player (randomizes card costs)
            this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new ConfusionPower(AbstractDungeon.player)));
        }
    }

    // Determines if potion can be used
    public boolean canUse() {
        // Cannot use if turn has ended
        if (AbstractDungeon.actionManager.turnHasEnded) return false;

        // Allow use during combat
        return AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    // Creates a copy of this potion
    public CustomPotion makeCopy() {
        return new SneckoEye();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}