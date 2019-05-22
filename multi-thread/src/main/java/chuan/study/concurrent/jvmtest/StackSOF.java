package chuan.study.concurrent.jvmtest;

import lombok.extern.slf4j.Slf4j;

/**
 * VM Args: -Xss128k
 * @author chuan.jiang <jiang.chuan@yanwei365.com>
 * @version 2.0.0
 * @date 2019-02-10
 */
@Slf4j
public class StackSOF {
    private int stackLen = 1;

    public void stackLeak() {
        stackLen++;
        stackLeak();
    }

    public static void main(String[] args) {
        StackSOF oom = new StackSOF();
        try {
            oom.stackLeak();
        } catch (Exception ex) {
            log.error("stack length:", oom.stackLen);
            throw ex;
        }
    }
}
