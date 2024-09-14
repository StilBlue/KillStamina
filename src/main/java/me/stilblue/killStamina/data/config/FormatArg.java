package me.stilblue.killStamina.data.config;

public class FormatArg {
    private final String toReplace;
    private final String replaceWith;

    public FormatArg(String toReplace, String replaceWith) {
        this.toReplace = toReplace;
        this.replaceWith = replaceWith;
    }

    /**
     * Apply the format to the specified string
     *
     * @param toApply
     * @return
     */
    public String applyToString(String toApply) {
        return toApply.replace(toReplace, replaceWith);
    }

    public String getToReplace() {
        return toReplace;
    }

    public String getReplaceWith() {
        return replaceWith;
    }
}