package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bottled.BottledRelicsMod.makeID;

public class StrikeDummyPower extends BasePower {
    public static final String POWER_ID = makeID("StrikeDummyPower");

    // Power type and duration settings
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    public StrikeDummyPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/StrikeDummyPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/StrikeDummyPower.png"), 0, 0, 32, 32);
    }

    // Calculate damage increase
    private int getStrikeDamageIncrease() {
        return this.amount * 4;
    }

    // Modify damage dealt by strike cards
    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return card.hasTag(AbstractCard.CardTags.STRIKE) ? damage + getStrikeDamageIncrease() : damage;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + getStrikeDamageIncrease() + DESCRIPTIONS[1];
    }
}