package com.dbapp.service.sender.snmptrap;

import com.dbapp.bean.EffectiveConfig;
import com.dbapp.service.DisplayService;
import com.dbapp.service.sender.AbstractSender;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDUv1;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.List;

public class SnmpTrapSender extends AbstractSender {
    private Snmp snmp = null;
    private Address targetAddress = null;
    private TransportMapping<UdpAddress> transport = null;

    @Override
    public String getName() {
        return "SnmpTrapSender";
    }

    @Override
    public void init(EffectiveConfig config) throws Exception {
        targetAddress = GenericAddress.parse("udp:" + config.getIp() + "/" + config.getPort());
        transport = new DefaultUdpTransportMapping();
        transport.listen();
        snmp = new Snmp(transport);
    }

    @Override
    public void send(List<byte[]> msg) {
        try {
            final PDUv1 pdu = new PDUv1();
            for (byte[] m : msg) {
                final VariableBinding v = new VariableBinding();
                v.setOid(SnmpConstants.sysName);
                v.setVariable(new OctetString(m));
                pdu.add(v);
            }
            pdu.setType(-89);
            final CommunityTarget target = new CommunityTarget();
            target.setCommunity(new OctetString("public"));
            target.setAddress(targetAddress);
            target.setRetries(2);
            target.setTimeout(1500L);
            target.setVersion(1);
            snmp.send(pdu, target, transport);
        } catch (IOException e) {
            DisplayService.directPrint("snmp trap error", e);
        }
    }

    @Override
    public void stop() {
        if (snmp != null) {
            try {
                snmp.close();
            } catch (Exception e) {
                DisplayService.directPrint("snmp close failed.", e);
            } finally {
                snmp = null;
            }
        }
        targetAddress = null;
        if (transport != null) {
            try {
                transport.close();
            } catch (Exception e) {
                DisplayService.directPrint("snmp transport close failed.", e);
            } finally {
                transport = null;
            }
        }
    }
}
