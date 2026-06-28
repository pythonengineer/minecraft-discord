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
	private static StringTranslate field_20165_a = new StringTranslate();
    private final Map<String, String> field_20164_b = Maps.newHashMap();

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
	                    this.field_20164_b.put(s1, s2);
	                }
	            }
	        }
		} catch (IOException e) {
            EagRuntime.debugPrintStackTrace(e);
		}

	}

	public static StringTranslate func_20162_a() {
		return field_20165_a;
	}

	public String func_20163_a(String var1) {
        String s = (String) this.field_20164_b.get(var1);
        return s == null ? var1 : s;
	}

	public String func_20160_a(String var1, Object... var2) {
		String var3 = this.func_20163_a(var1);
		return String.format(var3, var2);
	}

	public String func_20161_b(String var1) {
        String s = (String) this.field_20164_b.get(var1 + ".name");
        return s == null ? "" : s;
	}
}
