package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bottled.BottledRelicsMod.makeID;

public class SneckoEyePower extends BasePower {
    public static final String POWER_ID = makeID("SneckoEyePower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public SneckoEyePower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/SneckoEyePower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/SneckoEyePower.png"), 0, 0, 32, 32);
    }

    // Calculate amount of cards to draw
    private int getDrawAmount() {
        return 2 * this.amount;
    }

    // Draw new cards
    @Override
    public void atStartOfTurn() {
        addToBot(new DrawCardAction(owner, getDrawAmount()));
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + getDrawAmount() + DESCRIPTIONS[1];
    }
}
