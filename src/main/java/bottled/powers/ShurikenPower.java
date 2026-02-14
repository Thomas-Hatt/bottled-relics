package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static bottled.BottledRelicsMod.makeID;

public class ShurikenPower extends BasePower {
    public static final String POWER_ID = makeID("ShurikenPower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    // Number of attacks required to trigger the effect
    private final int attacksRequired = 3;

    // Counter for attacks played
    private int attacksPlayed = 0;

    public ShurikenPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/ShurikenPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/ShurikenPower.png"), 0, 0, 32, 32);
    }

    // Triggered when a card is used
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            attacksPlayed++;
            if (attacksPlayed % attacksRequired == 0) {
                attacksPlayed = 0;
                int scaledStrengthAmount = this.amount;
                this.addToBot(new ApplyPowerAction( AbstractDungeon.player, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, scaledStrengthAmount), scaledStrengthAmount));
            }
        }
    }

    // Reset attack counter at start of turn
    @Override
    public void atStartOfTurn() {
        this.attacksPlayed = 0;
    }

    // Update power description
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + attacksRequired + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
}