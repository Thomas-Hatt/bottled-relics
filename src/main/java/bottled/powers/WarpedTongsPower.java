package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bottled.BottledRelicsMod.makeID;

public class WarpedTongsPower extends BasePower {
    public static final String POWER_ID = makeID("WarpedTongsPower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public WarpedTongsPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/WarpedTongsPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/WarpedTongsPower.png"), 0, 0, 32, 32);
    }

    // Triggered at start of turn after drawing cards
    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        // Upgrade cards based on potency
        for (int i = 0; i < this.amount; i++) {
            this.addToBot(new UpgradeRandomCardAction());
        }
    }

    public void updateDescription() {
        // Change description depending on potency
        if (this.amount == 1) {
            // At the start of your turn, #yUpgrade a random card in your hand for the rest of combat.
            this.description = DESCRIPTIONS[0];
        }
        else if (this.amount > 1){
            // At the start of your turn, #yUpgrade x random cards in your hand for the rest of combat.
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }
}