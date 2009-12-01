package se.smrt.core.remote;

public interface RemoteProtocol {
    void sendVersion(String smrtVersion, String protocolVersion, byte[] protocolChecksum);
}
