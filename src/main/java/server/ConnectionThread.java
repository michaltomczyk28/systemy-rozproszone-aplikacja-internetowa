package server;

import server.context.ApplicationContext;
import server.context.ClientApplicationContext;
import server.state.ApplicationState;
import server.state.ClientStateFactory;
import server.state.StateFactory;
import server.state.UnauthenticatedState;
import shared.communication.SocketCommunicationBus;

import java.net.Socket;

public abstract class ConnectionThread implements Runnable {
    private Socket clientSocket;
    private ApplicationContext applicationContext;

    public ConnectionThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            this.applicationContext = new ClientApplicationContext(
                    new SocketCommunicationBus(clientSocket),
                    this.createStateFactory()
            );

            SocketCommunicationBus communicationBus = this.applicationContext.getCommunicationBus();
            StateFactory stateFactory = this.applicationContext.getStateFactory();

            UnauthenticatedState unauthenticatedState = stateFactory.createUnauthenticatedState(this.applicationContext);
            this.applicationContext.setApplicationState(unauthenticatedState);

            while(true) {
                ApplicationState state = this.applicationContext.getApplicationState();
                state.next();

                communicationBus.handleIncomingInput();
            }
        } catch (Exception ignored) {}
    }

    protected abstract ClientStateFactory createStateFactory();
}
