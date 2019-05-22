package chuan.study.concurrent.jvmtest;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * VM Args: -Xms10m -Xmx10m -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError

 * @author chuan.jiang <jiang.chuan@yanwei365.com>
 * @version 2.0.0
 * @date 2019-02-10
 */
@Slf4j
public class HeapOOM {

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        Stream.iterate(1, i -> i+1).forEach(idx ->{
            // log.error("Add {} Object", idx );
            list.add(new OOMObject());
        });
    }

    static class OOMObject{

    }
}
