package se.smrt.example.domain;

import se.smrt.core.SmrtProtocol;

import java.util.Map;

@SmrtProtocol
public interface FileTransfer {
	void sendFile(String fileName, byte[] contents);
	void sendMap(Map<String, Object> theMap);
}