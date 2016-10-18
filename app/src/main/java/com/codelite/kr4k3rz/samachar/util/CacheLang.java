package com.codelite.kr4k3rz.samachar.util;

import io.paperdb.Paper;

/**
 * Created by kr4k3rz on 10/17/16.
 */
public class CacheLang {
    public static String findLang(String CACHE_NAME) {
        String lang = Paper.book().read("language");
        return CACHE_NAME + lang;
    }

    /**
     * Returns the language saved eg. EN or NP.
     *
     * @return the string
     */
    public static String lang() {
        return Paper.book().read("language");
    }
}
