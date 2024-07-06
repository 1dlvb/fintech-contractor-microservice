package com.fintech.contractor.util;

public final class WildcatEnhancer {

    private WildcatEnhancer() {}

    public static String enhanceWithWildcatMatching(String string) {
        return String.format("%%%s%%", string);
    }

}
