package chuan.study.concurrent.jvmtest;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author chuan.jiang <jiang.chuan@yanwei365.com>
 * @version 2.0.0
 * @date 2019-03-09
 */
public class TestHashMap {
    public static void main(String[] args) {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return super.removeEldestEntry(eldest);
            }
        };

        TreeMap<Integer, String> treeMap = new TreeMap<>(Comparator.comparing(k -> k));
    }
}
