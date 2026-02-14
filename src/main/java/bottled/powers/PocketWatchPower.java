package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bottled.BottledRelicsMod.makeID;

public class PocketWatchPower extends BasePower {
    public static final String POWER_ID = makeID("PocketWatchPower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    // Tracks number of cards played this turn
    private int cardsPlayed = 0;

    public PocketWatchPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/PocketWatchPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/PocketWatchPower.png"), 0, 0, 32, 32);
    }

    // Reset cards played counter at the start of turn
    @Override
    public void atStartOfTurn() {
        this.cardsPlayed = 0;
    }

    // Draw cards after initial hand draw, based on amount
    @Override
    public void atStartOfTurnPostDraw() {
        if (cardsPlayed <= 3) {
            this.addToBot(new DrawCardAction(AbstractDungeon.player, getDrawAmount()));
        }
        cardsPlayed = 0;
    }

    // Increment cards played counter whenever a card is played
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        ++cardsPlayed;
    }

    // Calculate amount of cards to draw
    private int getDrawAmount() {
        return 3 * this.amount;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + getDrawAmount() + DESCRIPTIONS[1];
    }
}