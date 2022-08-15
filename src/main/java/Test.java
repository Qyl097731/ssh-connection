import utils.SshConnectUtil;

import java.io.IOException;

/**
 * @author qyl
 * @program Test.java
 * @Description 测试
 * @createTime 2022-08-15 18:14
 */
public class Test {
    public static void main(String[] args) throws IOException {
        SshConnectUtil.login();

        String ret = SshConnectUtil.execute("python3 hello.py");

        System.out.println(ret);
    }
}
