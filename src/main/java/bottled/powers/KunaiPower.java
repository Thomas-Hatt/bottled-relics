package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;

import static bottled.BottledRelicsMod.makeID;

public class KunaiPower extends BasePower {
    public static final String POWER_ID = makeID("KunaiPower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    // Number of attacks required to trigger Dexterity
    private final int attacksRequired = 3;

    // Tracks attacks played in current cycle
    private int attacksPlayed = 0;

    public KunaiPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/KunaiPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/KunaiPower.png"), 0, 0, 32, 32);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        // Only count ATTACK cards
        if (card.type == AbstractCard.CardType.ATTACK) {
            attacksPlayed++;

            if (attacksPlayed % attacksRequired == 0) {
                attacksPlayed = 0;

                int scaledDexterityGain = this.amount;

                // Apply Dexterity to player
                this.addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new DexterityPower(AbstractDungeon.player, scaledDexterityGain), scaledDexterityGain));
            }
        }
    }

    // Reset attack counter at the start of each turn
    @Override
    public void atStartOfTurn() {
        this.attacksPlayed = 0;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + attacksRequired + DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
    }
}