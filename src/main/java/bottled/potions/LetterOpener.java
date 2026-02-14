package bottled.potions;

import basemod.abstracts.CustomPotion;
import bottled.BottledRelicsMod;
import bottled.powers.LetterOpenerPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bottled.BottledRelicsMod.makeID;

public class LetterOpener extends CustomPotion {
    public static final String ID = makeID(LetterOpener.class.getSimpleName());
    public static final String POTION_ID = BottledRelicsMod.makeID("LetterOpener");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;

    public LetterOpener() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.BOLT, PotionColor.FRUIT);
        this.hybridColor = Color.YELLOW;
        this.isThrown = false;
        this.targetRequired = false;

        // Outline color shown in the Lab
        this.labOutlineColor = Color.BLACK.cpy();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Initializes potion potency and dynamically builds description text
    public void initializeData() {
        // Set potency value
        this.potency = getPotency();

        // Base damage per trigger of Letter Opener effect
        int letterOpenerDamage = 6;

        this.description = potionStrings.DESCRIPTIONS[0] + (letterOpenerDamage * potency) + potionStrings.DESCRIPTIONS[1];

        // Refresh tooltip with updated description
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Defines what happens when the potion is used
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;

        // Only apply effect if currently in combat
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new LetterOpenerPower(target, potency)));
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
        return new LetterOpener();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}