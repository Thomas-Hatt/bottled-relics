package bottled.potions;

import basemod.abstracts.CustomPotion;
import bottled.BottledRelicsMod;
import bottled.powers.KunaiPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static bottled.BottledRelicsMod.makeID;

public class Kunai extends CustomPotion {
    public static final String ID = makeID(Kunai.class.getSimpleName());
    public static final String POTION_ID = BottledRelicsMod.makeID("Kunai");
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public Kunai() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.M, PotionColor.GREEN);
        this.isThrown = false;
        this.targetRequired = false;
        this.spotsColor = Color.GRAY;

        // Outline color shown in the Lab
        this.labOutlineColor = Color.BLACK.cpy();

        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void initializeData() {
        // Set potency value
        this.potency = getPotency();

        // Number of attacks required to trigger Kunai effect (displayed in description)
        int attacksRequired = 3;

        this.description =  potionStrings.DESCRIPTIONS[0] + 3 + potionStrings.DESCRIPTIONS[1] + potency + potionStrings.DESCRIPTIONS[2];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    // Defines what happens when the potion is used
    public void use(AbstractCreature target) {
        target = AbstractDungeon.player;

        // Only apply effect if currently in combat
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new KunaiPower(target, potency)));
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
        return new Kunai();
    }

    public int getPotency(int ascensionLevel) {
        return 1;
    }
}