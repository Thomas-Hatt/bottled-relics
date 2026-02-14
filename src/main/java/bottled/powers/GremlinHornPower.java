package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bottled.BottledRelicsMod.makeID;

public class GremlinHornPower extends BasePower {
    public static final String POWER_ID = makeID("GremlinHornPower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public GremlinHornPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/GremlinHornPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/GremlinHornPower.png"), 0, 0, 32, 32);
    }

    public void onMonsterDeath(AbstractMonster m) {
        if (m != null) {
            this.flash();
            this.addToBot(new GainEnergyAction(this.amount));
            this.addToBot(new DrawCardAction(AbstractDungeon.player, this.amount * 2));
        }
    }

    public void updateDescription() {
        // Change description depending on potency
        if (this.amount == 1) {
            // Whenever an enemy dies, gain [E] and draw 2 cards.
            this.description = DESCRIPTIONS[0] + (this.amount * 2) + DESCRIPTIONS[1];
        }
        else if (this.amount > 1) {
            // Whenever an enemy dies, gain x [E] and draw x cards.
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3] + (this.amount * 2) + DESCRIPTIONS[4];
        }
    }
}
