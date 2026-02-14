package bottled.potions;

import basemod.abstracts.CustomPotion;
import bottled.BottledRelicsMod;
import bottled.powers.WarpedTongsPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bottled.BottledRelicsMod.makeID;

public class WarpedTongs extends CustomPotion {
    public static final String ID = makeID(WarpedTongs.class.getSimpleName());
    public static final String POTION_ID = BottledRelicsMod.makeID("WarpedTongs");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;

    public WarpedTongs() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPIKY, PotionColor.EXPLOSIVE);
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
            // At the start of your turn, #yUpgrade a random card in your hand for the rest of combat.
            this.description = potionStrings.DESCRIPTIONS[0];
        }
        else {
            // At the start of your turn, #yUpgrade x random cards in your hand for the rest of combat.
            this.description = potionStrings.DESCRIPTIONS[1] + potency + potionStrings.DESCRIPTIONS[2];
        }

        // Refresh tooltip
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new WarpedTongsPower(target, potency)));
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
        return new WarpedTongs();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}
