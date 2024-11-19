package pe.com.bn.wsrestsate.transvesal.util;

import com.jcraft.jsch.*;

import pe.com.bn.wsrestsate.transvesal.components.ParametrosComp;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.apache.commons.pool2.PooledObjectFactory;

import java.util.Properties;

@Component
public class SftpFactory extends BasePooledObjectFactory<ChannelSftp> {

    @Autowired
    private ParametrosComp parametros;

    @Override
    public ChannelSftp create() throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(
                parametros.getUsuarioFtp(),
                parametros.getIpFtp(),
                Integer.parseInt(parametros.getPuertoFtp())
        );
        session.setPassword(parametros.getClaveFtp());
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.connect();

        Channel channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    @Override
    public PooledObject<ChannelSftp> wrap(ChannelSftp channel) {
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public boolean validateObject(PooledObject<ChannelSftp> pooledObject) {
        return pooledObject.getObject().isConnected();
    }

    @Override
    public void destroyObject(PooledObject<ChannelSftp> pooledObject) throws Exception {
        ChannelSftp channel = pooledObject.getObject();
        if (channel.isConnected()) {
            channel.disconnect();
            channel.getSession().disconnect();
        }
    }
}
