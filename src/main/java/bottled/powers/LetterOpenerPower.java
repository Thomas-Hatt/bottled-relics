package bottled.powers;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static bottled.BottledRelicsMod.makeID;

public class LetterOpenerPower extends BasePower {
    public static final String POWER_ID = makeID("LetterOpenerPower");
    private static final AbstractPower.PowerType TYPE = AbstractPower.PowerType.BUFF;
    private static final boolean TURN_BASED = false;

    // Tracks number of Skills played in the current cycle
    private int skillsPlayed = 0;

    // Letter Opener damage
    private final int letterOpenerDamage = 6;

    public LetterOpenerPower(AbstractCreature owner, int amount) {
        super(POWER_ID, TYPE, TURN_BASED, owner, amount);

        // Images
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/large/LetterOpenerPower.png"), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage("bottled/images/powers/LetterOpenerPower.png"), 0, 0, 32, 32);
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.SKILL) {
            skillsPlayed++;

            int skillsRequired = 3;

            // Deal damage to all enemies
            if (skillsPlayed % skillsRequired == 0) {
                skillsPlayed = 0;
                this.addToBot(new DamageAllEnemiesAction( null, DamageInfo.createDamageMatrix(letterOpenerDamage * this.amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY));
            }
        }
    }

    // Reset Skills counter at the start of each turn
    @Override
    public void atStartOfTurn() {
        skillsPlayed = 0;
    }

    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + letterOpenerDamage * this.amount + DESCRIPTIONS[1];
    }
}