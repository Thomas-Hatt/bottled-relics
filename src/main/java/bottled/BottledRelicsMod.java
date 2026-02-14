package bottled;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.abstracts.CustomPotion;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import bottled.potions.*;
import bottled.util.GeneralUtils;
import bottled.util.TextureLoader;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglFileHandle;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@SpireInitializer
public class BottledRelicsMod implements
        EditStringsSubscriber,
        PostInitializeSubscriber {
    private static final String resourcesFolder = checkResourcesPath();
    private static final String defaultLanguage = "eng";
    public static ModInfo info;
    public static String modID;
    public static final Logger logger = LogManager.getLogger(modID);

    static {
        loadModInfo();
    }

    public BottledRelicsMod() {
        BaseMod.subscribe(this);
        logger.info(modID + " subscribed to BaseMod.");
    }

    public static String makeID(String id) {
        return modID + ":" + id;
    }

    public static void initialize() {
        new BottledRelicsMod();
    }


    public static void addPotions() {
        Class[] potions = new Class[]{
                Shuriken.class,
                Kunai.class,
                LetterOpener.class,
                OrnamentalFan.class,
                PocketWatch.class,
                SneckoEye.class,
                StrikeDummy.class,
                WarpedTongs.class,
                DeadBranch.class,
                GremlinHorn.class
        };

        for (Class potionClass : potions) {
            try {
                String potionId = (String) potionClass.getField("POTION_ID").get(null);

                // Instantiate the potion
                CustomPotion potion = (CustomPotion) potionClass.getDeclaredConstructor().newInstance();

                // Use the colors from the potion itself
                BaseMod.addPotion(potionClass, potion.liquidColor, potion.hybridColor, potion.spotsColor, potionId);

                if (Loader.isModLoaded("widepotions")) {
                    Consumer<String> whitelist = getWidePotionsWhitelistMethod();
                    whitelist.accept(potionId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Consumer<String> getWidePotionsWhitelistMethod() {
        // To avoid the need for a dependency of any kind, we call Wide Potions through reflection
        try {
            Method whitelistMethod = Class.forName("com.evacipated.cardcrawl.mod.widepotions.WidePotionsMod").getMethod("whitelistSimplePotion", String.class);
            return s -> {
                try {
                    whitelistMethod.invoke(null, s);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error trying to whitelist wide potion for " + s, e);
                }
            };
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not find method WidePotionsMod.whitelistSimplePotion", e);
        }
    }

    /*----------Localization----------*/
    private static String getLangString() {
        return Settings.language.name().toLowerCase();
    }

    public static String powerPath(String file) {
        return resourcesFolder + "/images/powers/" + file;
    }

    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String imagePath(String file) {
        return resourcesFolder + "/images/" + file;
    }

    private static String checkResourcesPath() {
        String name = BottledRelicsMod.class.getName();
        int separator = name.indexOf('.');
        name = name.substring(0, separator);

        FileHandle resources = new LwjglFileHandle(name, Files.FileType.Internal);

        if (!resources.exists()) {
            throw new RuntimeException("\n\tFailed to find resources folder; expected it to be at  \"resources/" + name + "\"." +
                    " Either make sure the folder under resources has the same name as your mod's package, or change the line\n" +
                    "\t\"private static final String resourcesFolder = checkResourcesPath();\"\n" +
                    "\tat the top of the " + BottledRelicsMod.class.getSimpleName() + " java file.");
        }
        if (!resources.child("images").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'images' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "images folder is in the correct location.");
        }
        if (!resources.child("localization").exists()) {
            throw new RuntimeException("\n\tFailed to find the 'localization' folder in the mod's 'resources/" + name + "' folder; Make sure the " +
                    "localization folder is in the correct location.");
        }

        return name;
    }

    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(BottledRelicsMod.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receivePostInitialize() {
        addPotions();
        ModPanel settingsPanel = new ModPanel();
        Texture badgeTexture = TextureLoader.getTexture(imagePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, settingsPanel);
    }

    @Override
    public void receiveEditStrings() {
        loadLocalization(defaultLanguage);
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        BaseMod.loadCustomStringsFile(PotionStrings.class,
                localizationPath(lang, "PotionStrings.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                localizationPath(lang, "PowerStrings.json"));
        //BaseMod.loadCustomStringsFile(RelicStrings.class,
        //        localizationPath(lang, "RelicStrings.json"));
    }
}