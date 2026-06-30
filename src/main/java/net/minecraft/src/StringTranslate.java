package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.IOUtils;

public class StringTranslate {
    private static final Pattern numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter equalSignSplitter = Splitter.on('=').limit(2);
	private static StringTranslate instance = new StringTranslate();
    private final Map<String, String> translateTable = Maps.newHashMap();

    private StringTranslate() {
        try (InputStream inputstream = EagRuntime.getRequiredResourceStream("/assets/lang/en_US.lang")) {
            List<String> strs = IOUtils.readLines(inputstream, StandardCharsets.UTF_8);
            for (int i = 0, l = strs.size(); i < l; ++i) {
                String s = strs.get(i);
                if (!s.isEmpty() && s.charAt(0) != 35) {
                    String[] astring = (String[]) Iterables.toArray(equalSignSplitter.split(s), String.class);
                    if (astring != null && astring.length == 2) {
                        String s1 = astring[0];
                        String s2 = numericVariablePattern.matcher(astring[1]).replaceAll("%s"); // TODO: originally "%$1s"
                                                                                                    // but must be "%s" to
                                                                                                    // work with TeaVM
                                                                                                    // (why?)
                        this.translateTable.put(s1, s2);
                    }
                }
            }
        } catch (IOException e) {
            EagRuntime.debugPrintStackTrace(e);
        }

    }

	public static StringTranslate getInstance() {
		return instance;
	}

	public String translateKey(String var1) {
        String s = (String) this.translateTable.get(var1);
        return s == null ? var1 : s;
	}

	public String translateKeyFormat(String var1, Object... var2) {
        String var3 = this.translateKey(var1);
		return String.format(var3, var2);
	}

	public String translateNamedKey(String var1) {
        String s = (String) this.translateTable.get(var1 + ".name");
        return s == null ? "" : s;
	}
}
