package bottled.potions;

import basemod.abstracts.CustomPotion;
import bottled.BottledRelicsMod;
import bottled.powers.DeadBranchPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bottled.BottledRelicsMod.makeID;

public class DeadBranch extends CustomPotion {
    public static final String ID = makeID(DeadBranch.class.getSimpleName());
    public static final String POTION_ID = BottledRelicsMod.makeID("DeadBranch");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public DeadBranch() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.BOLT, PotionColor.FEAR);
        this.isThrown = false;
        this.targetRequired = false;

        // Outline color shown in the Lab
        this.labOutlineColor = Color.BLACK.cpy();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void initializeData() {
        // Set potency value
        this.potency = getPotency();

        // Number of cards to add to the players hands
        int cardsToAdd = potency;

        // Change description depending on potency
        if (this.potency == 1) {
            // Whenever you Exhaust a card, add a random card to your hand
            this.description = potionStrings.DESCRIPTIONS[0];
        }
        else {
            // Whenever you Exhaust a card, add x random cards to your hand
            this.description = potionStrings.DESCRIPTIONS[1] + cardsToAdd + potionStrings.DESCRIPTIONS[2];
        }

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Defines what happens when the potion is used
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;

        // Only apply effect if currently in combat
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new DeadBranchPower(target, potency)));
        }
    }

    // Determines if potion can be used
    public boolean canUse() {
        // Cannot use if turn has ended
        if (AbstractDungeon.actionManager.turnHasEnded) return false;

        // Allow use during combat
        return AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }

    // Creates a new copy of this potion
    public CustomPotion makeCopy() {
        return new DeadBranch();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}