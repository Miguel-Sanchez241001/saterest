package pe.com.bn.wsrestsate.transvesal.components;

import com.jcraft.jsch.ChannelSftp;
 

import pe.com.bn.wsrestsate.transvesal.util.SftpFactory;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SftpPool {

    private final ObjectPool<ChannelSftp> pool;

    @Autowired
    public SftpPool(SftpFactory sftpFactory) {
        GenericObjectPoolConfig<ChannelSftp> config = new GenericObjectPoolConfig<>();
        config.setMaxTotal(10); // Máximo de conexiones
        config.setMinIdle(2);   // Conexiones mínimas en espera
        this.pool = new GenericObjectPool<>(sftpFactory, config);
    }

    public ChannelSftp borrowObject() throws Exception {
        return pool.borrowObject();
    }

    public void returnObject(ChannelSftp channel) throws Exception {
        pool.returnObject(channel);
    }

    public void close() {
        pool.close();
    }
}
