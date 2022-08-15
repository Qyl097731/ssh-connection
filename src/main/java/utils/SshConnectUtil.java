package utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;
import config.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author qyl
 * @program SshConnectUtils.java
 * @Description ssh连接linux
 * @createTime 2022-08-15 09:03
 */
public class SshConnectUtil {
    // 连接对象
    private static Connection connection;
    // 是否连接
    private static boolean isConnect = false;

    /**
     * @return boolean
     * @description 登录ssh
     * @author qyl
     * @date 2022/8/15 9:16
     */
    public static void login() throws IOException {
        connection = new Connection(Config.ip, Config.port);
        if (!isConnect) {
            connection.connect();
            isConnect = connection.authenticateWithPassword(Config.username, Config.password);
        }
    }

    /**
     * @description 运行ssh命令
     * @param command
     * @return String
     * @author qyl
     * @date 2022/8/15 18:12
     */
    public static String execute(String command) {
        Session session = null;
        try {
            session = connection.openSession();
            if (session != null) {
                session.execCommand(command);
                return processStdout(session.getStdout(), "UTF-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 关闭连接
     * @return boolean
     * @author qyl
     * @date 2022/8/15 18:13
     */
    public static boolean closeConnection() {
        connection.close();
        return isConnect = false;
    }

    /**
     * 获取控制台输出
     * @param in
     * @param charset
     * @return
     */
    public static String processStdout(InputStream in, String charset) {
        StringBuffer buffer = new StringBuffer();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new StreamGobbler(in), charset))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
