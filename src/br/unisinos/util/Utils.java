package br.unisinos.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since ${PROJECT_VERSION}
 */
public class Utils {

    private Utils() {

    }

    public static <T> List<List<T>> split(List<T> list) {
        List<List<T>> lists = new ArrayList<>();
        int lastSplit = 0;
        boolean splited = false;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("//FILE_SEPARATION_LINE")) {
                lists.add(list.subList(lastSplit, i));
                lastSplit = i + 1;
                splited = true;
            }
        }
        if (!splited) {
            lists.add(list);
        }
        return lists;
    }
}
