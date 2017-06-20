package br.unisinos.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Vinicius, Fabio e Eduardo.
 *
 * @since ${PROJECT_VERSION}
 */
public class Utils {

    private Utils() {

    }

    public static List<List<String>> split(List<String> list) {
        List<List<String>> lists = new ArrayList<>();
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

    public static String formatException(Throwable throwable) {
        StringJoiner sj = new StringJoiner(System.lineSeparator() + "\t", throwable.getMessage() + System.lineSeparator(), "");
        for (StackTraceElement ele : throwable.getStackTrace()) {
            sj.add(ele.toString());
        }
        return sj.toString();
    }
}
