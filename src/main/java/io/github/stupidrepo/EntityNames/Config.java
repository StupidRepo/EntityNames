package io.github.stupidrepo.EntityNames;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.awt.*;
import java.util.List;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = EntityNames.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
	private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

	private static final ModConfigSpec.ConfigValue<List<String>> FIRST_NAMES = BUILDER
			.comment("A list of first names to chose from.")
			.define("first_names", List.of(
					"Adam",
					"Bruce",
					"Charlie",
					"Dave",
					"Eli",
					"Freddie",
					"George",
					"Harry",
					"Isaac",
					"James",
					"Kevin",
					"Leo",
					"Mikey",
					"Noah",
					"Oscar",
					"Peter",
					"Quinn",
					"Reese",
					"Sam",
					"Tom",
					"Will",
					"Xander",
					"Zach",
					// To make up for no "U", "V", or "Y" names:
					"Jerry",
					"Bill",
					"Finn",
					// And one extra easter egg one:
					"Gordon" // Freeman
			), Config::validateNames);

	private static final ModConfigSpec.ConfigValue<List<String>> FEMALE_FIRST_NAMES = BUILDER
			.comment("A list of female first names to chose from.")
			.define("female_first_names", List.of(
					"Alice",
					"Betty",
					"Catherine",
					"Daisy",
					"Ella",
					"Fiona",
					"Grace",
					"Hannah",
					"Jenny",
					"Katie",
					"Lily",
					"Mary",
					"Olivia",
					"Sarah",
					"Tina",
					"Violet",
					"Wendy",
					"Zoe",
					// To make up for no "I", "N", "P", "Q", "R", "U", "X", or "Y" names:
					"Megan",
					"Abby",
					"Nina",
					"Emily",
					"Luna",
					"Ava",
					"Kara",
					"Diana"
			), Config::validateNames);

	private static final ModConfigSpec.ConfigValue<List<String>> LAST_NAMES = BUILDER
			.comment("A list of last names to chose from.")
			.define("last_names", List.of(
					"Anderson",
					"Barnes",
					"Carter",
					"Dalton",
					"Edwards",
					"Fisher",
					"Gray",
					"Hall",
					"Jones",
					"Keller",
					"Leman",
					"Morgan",
					"Nelson",
					"Olaf",
					"Peters",
					"Rogers",
					"Scholz",
					"Tanner",
					"Upton",
					"Vance",
					"Wells",
					"Xavier",
					"Yates",
					"Zorn",
					// To make up for no "Q", or "I" names:
					"Spencer",
					"Ford",
					// And one extra easter egg one:
					"Freeman" // About that beer I owed ya!
			), Config::validateNames);

	private static final ModConfigSpec.ConfigValue<Boolean> ENABLE_FEMALE_NAMES = BUILDER
			.comment("Whether or not to include female (first) names.")
			.define("do_female_names", false);

	private static final ModConfigSpec.ConfigValue<String> TEXT_COLOR = BUILDER
			.comment("The color of the text in hex format (e.g., #FFFFFF).")
			.define("text_color",
					String.format("#%06X", Color.WHITE.getRGB() & 0xFFFFFF), Config::validateColour);

	private static final ModConfigSpec.ConfigValue<DisplayFormat> DISPLAY_FORMAT = BUILDER
			.comment("The format to display the names in.")
			.defineEnum("display_format", DisplayFormat.FIRST_AND_LAST_NAME);

	static final ModConfigSpec SPEC = BUILDER.build();

	public static List<String> firstNames;
	public static List<String> lastNames;

	public static DisplayFormat displayFormat;
	public static Color textColor;

	private static boolean validateNames(final Object obj)
	{
		if(!(obj instanceof List<?> list)) return false;

		var names = list.stream().map(String.class::cast).toList();
		return !names.isEmpty() &&
				names.stream().allMatch(name -> !name.isEmpty() && name.matches("^[A-Za-z]+$"));
	}

	private static boolean validateColour(final Object obj)
	{
		return obj instanceof String color &&
				color.matches("^#[0-9A-Fa-f]{6}$");
	}

	@SubscribeEvent
	static void onLoad(final ModConfigEvent event)
	{
		firstNames = FIRST_NAMES.get();
		if(ENABLE_FEMALE_NAMES.get()) firstNames.addAll(FEMALE_FIRST_NAMES.get());

		lastNames = LAST_NAMES.get();

		displayFormat = DISPLAY_FORMAT.get();
		textColor = Color.decode(TEXT_COLOR.get());
	}
}
