package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bottled.BottledRelicsMod.makeID;

public class OrnamentalFanPower extends BasePower {
    public static final String POWER_ID = makeID("OrnamentalFanPower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    // Tracks number of attacks played in the current cycle
    private int attacksPlayed = 0;

    // Block to gain
    private final int ornamentalFanBlock = 5;

    public OrnamentalFanPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/OrnamentalFanPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/OrnamentalFanPower.png"), 0, 0, 32, 32);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            attacksPlayed++;

            int attacksRequired = 3;

            // Give the player block
            if (attacksPlayed % attacksRequired == 0) {
                attacksPlayed = 0;
                int scaledBlockGain = this.amount * ornamentalFanBlock;
                this.addToBot(new GainBlockAction( AbstractDungeon.player, AbstractDungeon.player, scaledBlockGain));
            }
        }
    }

    // Reset attack counter at the start of each turn
    @Override
    public void atStartOfTurn() {
        attacksPlayed = 0;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + (this.amount * ornamentalFanBlock) + DESCRIPTIONS[1];
    }
}
