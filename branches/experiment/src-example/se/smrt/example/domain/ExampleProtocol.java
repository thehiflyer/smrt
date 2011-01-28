package se.smrt.example.domain;

import se.smrt.core.SmrtProtocol;

@SmrtProtocol("Example")
public interface ExampleProtocol {
	FileTransfer filetransfer();
	TheUltimateChat chat();
}
