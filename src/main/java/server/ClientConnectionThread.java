package server;

import server.state.factory.ClientStateFactory;
import server.state.factory.StateFactory;

import java.net.Socket;

public class ClientConnectionThread extends ConnectionThread {
    public ClientConnectionThread(Socket clientSocket) {
        super(clientSocket);
    }

    @Override
    protected StateFactory createStateFactory() {
        return new ClientStateFactory();
    }
}
