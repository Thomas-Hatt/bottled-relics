package bottled.potions;

import basemod.abstracts.CustomPotion;
import bottled.BottledRelicsMod;
import bottled.powers.GremlinHornPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bottled.BottledRelicsMod.makeID;

public class GremlinHorn extends CustomPotion {
    public static final String ID = makeID(GremlinHorn.class.getSimpleName());
    public static final String POTION_ID = BottledRelicsMod.makeID("GremlinHorn");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;

    public GremlinHorn() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.BOTTLE, PotionColor.ANCIENT);
        this.hybridColor = Color.YELLOW;
        this.isThrown = false;
        this.targetRequired = false;

        // Lab outline color
        this.labOutlineColor = Color.BLACK.cpy();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void initializeData() {
        // Set potency value
        this.potency = getPotency();

        // Change description depending on potency
        if (this.potency == 1) {
            // Whenever an enemy dies, gain [E] and draw 2 cards.
            this.description = potionStrings.DESCRIPTIONS[0] + (potency * 2) + potionStrings.DESCRIPTIONS[1];
        }
        else {
            // Whenever an enemy dies, gain x [E] and draw x cards.
            this.description = potionStrings.DESCRIPTIONS[2] + (potency * 2) + potionStrings.DESCRIPTIONS[3] + potency + potionStrings.DESCRIPTIONS[4];
        }

        // Refresh tooltip
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Defines potion effect when used
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            // Apply GremlinHornPower to player
            addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new GremlinHornPower(AbstractDungeon.player, potency)));
        }
    }

    // Determines if potion can be used
    public boolean canUse() {
        // Cannot use if turn has ended
        if (AbstractDungeon.actionManager.turnHasEnded) return false;

        // Allow use during combat
        return AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    public CustomPotion makeCopy() {
        return new GremlinHorn();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}