package cn.dlj1.file_sender;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@Component
public class DoIt implements CommandLineRunner {

    @Autowired
    LinuxInfos linuxInfos;

    @Value("${listenerDir}")
    private String listenerDir;
    @Value("${toDir}")
    String toDir;

    @Async
    public void doIt(File file) throws Exception {
        System.out.println("上传文件:" + file.getAbsolutePath());
        for (LinuxInfo info : linuxInfos.getList()) {
            JSch jsch = new JSch();
            Session session = jsch.getSession(info.getUsername(), info.getHost());
            session.setPassword(info.getPassword());
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();

            ChannelSftp ftp = (ChannelSftp) session.openChannel("sftp");
            ftp.connect();

            FileInputStream inputStream = new FileInputStream(file);
            ftp.put(inputStream, toDir + "/" + file.getName());
            ftp.disconnect();
            session.disconnect();

            inputStream.close();
        }
        System.out.println("删除文件:" + new File(file.getAbsolutePath()).delete());
    }

    @Override
    public void run(String... args) throws Exception {
        long interval = TimeUnit.SECONDS.toMillis(1);
        FileAlterationObserver observer = new FileAlterationObserver(new File(listenerDir));
        observer.addListener(new FileAlter() {
            @Override
            public void onFileCreate(File file) {
                try {
                    doIt(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);
        // 开始监控
        monitor.start();

    }

}
