package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bottled.BottledRelicsMod.makeID;

public class DeadBranchPower extends BasePower {
    public static final String POWER_ID = makeID("DeadBranchPower");

    // Power type and duration settings
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public DeadBranchPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/DeadBranchPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/DeadBranchPower.png"), 0, 0, 32, 32);
    }

    public void onExhaust(AbstractCard card) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.flash();
            // Add cards to hand
            for (int i = 0; i < this.amount; i++) {
                this.addToBot(new MakeTempCardInHandAction(AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy(), false));
            }
        }
    }

    public void updateDescription() {
        // Change description depending on potency
        if (this.amount == 1) {
            // Whenever you Exhaust a card, add a random card to your hand
            this.description = DESCRIPTIONS[0];
        }
        else {
            // Whenever you Exhaust a card, add x random cards to your hand
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }
}