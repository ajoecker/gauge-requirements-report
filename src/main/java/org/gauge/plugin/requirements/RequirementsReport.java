package org.gauge.plugin.requirements;

import com.thoughtworks.gauge.Messages.Message;
import org.gauge.plugin.requirements.reporter.JSONReport;

import java.io.IOException;
import java.net.Socket;

public final class RequirementsReport {
    private static final String LOCALHOST = "127.0.0.1";

    public static void main(String[] args) {
        String portEnv = System.getenv("plugin_connection_port");
        int port = Integer.parseInt(portEnv);
        Socket socket;
        while (true) {
            try {
                socket = new Socket(LOCALHOST, port);
                break;
            } catch (Exception ignored) {
            }
        }

        try {
            while (!socket.isClosed() && socket.isConnected()) {
                Message message = Message.parseDelimitedFrom(socket.getInputStream());
                if (message.getMessageType() == Message.MessageType.SuiteExecutionResult) {
                    new RequirementResultHandler().handleResult(message.getSuiteExecutionResult(), new JSONReport());
                    System.exit(0);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

