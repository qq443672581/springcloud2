package cn.dlj1.file_sender;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "linux-infos")
public class LinuxInfos {

    private List<LinuxInfo> list;

    public List<LinuxInfo> getList() {
        return list;
    }

    public void setList(List<LinuxInfo> list) {
        this.list = list;
    }
}
