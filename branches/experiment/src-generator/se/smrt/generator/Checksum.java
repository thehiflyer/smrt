package se.smrt.generator;

import se.smrt.generator.parser.ProtocolData;
import se.smrt.generator.types.Method;
import se.smrt.generator.types.Parameter;

import java.io.*;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.HashSet;

public class Checksum {
    private final byte[] bytes;
    private final String source;

    public Checksum(ProtocolData protocolData) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringWriter sourceWriter = new StringWriter();
        digestProtocol(protocolData, new HashSet<ProtocolData>(), new PrintWriter(sourceWriter));

        source = sourceWriter.getBuffer().toString().replace("\r\n", "\n");
        
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        bytes = digest.digest(source.getBytes("UTF-8"));
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getSource() {
        return source;
    }

    private void digestProtocol(ProtocolData protocolData, Set<ProtocolData> visitedProtocols, PrintWriter writer) {
        if (visitedProtocols.contains(protocolData)) {
            return;
        }
        visitedProtocols.add(protocolData);


        writer.println("@SmrtProtocol(\"" + protocolData.getName() + "\")");
        writer.println("interface " + protocolData.getType().getFullNameWithGenerics() + " {");
        for (Method method : protocolData.getMethods()) {
            digestMethod(method, visitedProtocols, writer);
        }
        writer.println("}");

        for (ProtocolData data : protocolData.getSubProtocols()) {
            digestProtocol(data, visitedProtocols, writer);
        }
    }

    private void digestMethod(Method method, Set<ProtocolData> visitedProtocols, PrintWriter writer) {
        writer.print("\t" + method.getReturnType().getFullNameWithGenerics() + " " + method.getName() + "(");

        boolean first = true;
        for (Parameter parameter : method.getParameters()) {
            if (first) {
                first = false;
            } else {
                writer.print(", ");
            }
            writer.print(parameter.getType().getFullNameWithGenerics() + " " + parameter.getName());
        }
        writer.println(");");
    }
}
