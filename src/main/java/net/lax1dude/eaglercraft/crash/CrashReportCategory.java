package net.lax1dude.eaglercraft.crash;

import java.util.List;
import java.util.concurrent.Callable;

import com.google.common.collect.Lists;

import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.HString;

/**
 * Minecraft 1.8.8 bytecode is (c) 2015 Mojang AB. "Do not distribute!" Mod
 * Coder Pack v9.18 deobfuscation configs are (c) Copyright by the MCP Team
 * 
 * EaglercraftX 1.8 patch files (c) 2022-2025 lax1dude, ayunami2000. All Rights
 * Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */
public class CrashReportCategory {
    private final CrashReport crashReport;
    private final String name;
    private final List<CrashReportCategory.Entry> children = Lists.newArrayList();
    private String[] stackTrace = new String[0];

    public CrashReportCategory(CrashReport report, String name) {
        this.crashReport = report;
        this.name = name;
    }

    public static String getCoordinateInfo(double x, double y, double z) {
        return HString.format("%.2f,%.2f,%.2f", new Object[]{Double.valueOf(x), Double.valueOf(y), Double.valueOf(z)
        });
    }

    /**
     * + Adds a Crashreport section with the given name with the value set to the
     * result of the given Callable;
     */
    public void addCrashSectionCallable(String sectionName, Callable<String> callable) {
        try {
            this.addCrashSection(sectionName, callable.call());
        } catch (Throwable throwable) {
            this.addCrashSectionThrowable(sectionName, throwable);
        }

    }

    /**
     * + Adds a Crashreport section with the given name with the given value
     * (convered .toString())
     */
    public void addCrashSection(String sectionName, Object value) {
        this.children.add(new CrashReportCategory.Entry(sectionName, value));
    }

    /**
     * + Adds a Crashreport section with the given name with the given Throwable
     */
    public void addCrashSectionThrowable(String sectionName, Throwable throwable) {
        this.addCrashSection(sectionName, throwable);
    }

    /**
     * + Resets our stack trace according to the current trace, pruning the deepest
     * 3 entries. The parameter indicates how many additional deepest entries to
     * prune. Returns the number of entries in the resulting pruned stack trace.
     */
    public int getPrunedStackTrace(int size) {
        String[] astacktraceelement = EagRuntime.getStackTraceElements(new Exception());
        if (astacktraceelement.length - 3 - size <= 0) {
            return 0;
        } else {
            this.stackTrace = new String[astacktraceelement.length - 3 - size];
            System.arraycopy(astacktraceelement, 3 + size, this.stackTrace, 0, this.stackTrace.length);
            return this.stackTrace.length;
        }
    }

    /**
     * + Do the deepest two elements of our saved stack trace match the given
     * elements, in order from the deepest?
     */
    public boolean firstTwoElementsOfStackTraceMatch(String s1, String s2) {
        if (this.stackTrace.length != 0 && s1 != null) {
            String stacktraceelement = this.stackTrace[0];
            if (s1.equals(stacktraceelement)) {
                if (s2 != null != this.stackTrace.length > 1) {
                    return false;
                } else if (s2 != null && !this.stackTrace[1].equals(s2)) {
                    return false;
                } else {
                    this.stackTrace[0] = s1;
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * + Removes the given number entries from the bottom of the stack trace.
     */
    public void trimStackTraceEntriesFromBottom(int amount) {
        String[] astacktraceelement = new String[this.stackTrace.length - amount];
        System.arraycopy(this.stackTrace, 0, astacktraceelement, 0, astacktraceelement.length);
        this.stackTrace = astacktraceelement;
    }

    public void appendToStringBuilder(StringBuilder builder) {
        builder.append("-- ").append(this.name).append(" --\n");
        builder.append("Details:");

        for (int i = 0, l = this.children.size(); i < l; ++i) {
            CrashReportCategory.Entry crashreportcategory$entry = this.children.get(i);
            builder.append("\n\t");
            builder.append(crashreportcategory$entry.getKey());
            builder.append(": ");
            builder.append(crashreportcategory$entry.getValue());
        }

        if (this.stackTrace != null && this.stackTrace.length > 0) {
            builder.append("\nStacktrace:");

            for (int i = 0; i < this.stackTrace.length; ++i) {
                builder.append("\n\tat ");
                builder.append(this.stackTrace[i]);
            }
        }
    }

    public String[] getStackTrace() {
        return this.stackTrace;
    }

    static class Entry {
        private final String key;
        private final String value;

        public Entry(String key, Object value) {
            this.key = key;
            if (value == null) {
                this.value = "~~NULL~~";
            } else if (value instanceof Throwable) {
                Throwable throwable = (Throwable)value;
                this.value = "~~ERROR~~ " + throwable.getClass().getName() + ": " + throwable.getMessage();
            } else {
                this.value = value.toString();
            }

        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }
}
