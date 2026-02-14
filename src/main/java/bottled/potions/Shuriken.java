package bottled.potions;

import basemod.abstracts.CustomPotion;
import bottled.BottledRelicsMod;
import bottled.powers.ShurikenPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bottled.BottledRelicsMod.makeID;

public class Shuriken extends CustomPotion {
    public static final String ID = makeID(Shuriken.class.getSimpleName());
    public static final String POTION_ID = BottledRelicsMod.makeID("Shuriken");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public Shuriken() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.M, PotionColor.STRENGTH);
        this.spotsColor = Color.GRAY;
        this.isThrown = false;
        this.targetRequired = false;

        // Outline color shown in the Lab
        this.labOutlineColor = Color.BLACK.cpy();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Initializes potion potency and description text
    public void initializeData() {
        // Set potency value
        this.potency = getPotency();

        // Number of attacks required to trigger Shuriken effect (displayed in description)
        int attacksRequired = 3;

        this.description = potionStrings.DESCRIPTIONS[0] + attacksRequired + potionStrings.DESCRIPTIONS[1] + potency + potionStrings.DESCRIPTIONS[2];

        // Refresh tooltip with updated description
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Defines what happens when the potion is used
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;

        // Only apply effect if currently in combat
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new ShurikenPower(target, potency)));
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
        return new Shuriken();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}
