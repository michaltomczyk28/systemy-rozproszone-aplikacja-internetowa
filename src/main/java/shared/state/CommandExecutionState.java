package shared.state;

import server.commands.ApplicationCommand;
import server.commands.BalanceCommand;
import server.commands.TransferCommand;
import server.context.ApplicationContext;
import shared.communication.SocketCommunicationBus;

public class CommandExecutionState implements ApplicationState {
    private SocketCommunicationBus communicationBus;
    private ApplicationContext applicationContext;
    private ApplicationCommand currentCommand;
    private boolean didDisplayMenu = false;

    public CommandExecutionState(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.communicationBus = applicationContext.getCommunicationBus();
        this.communicationBus.registerListener(this);
    }

    @Override
    public void onInput(String input) {
        if(currentCommand == null) {
            // Create base on input value
            this.currentCommand = new BalanceCommand(this.applicationContext);

            return;
        }

        this.currentCommand.onInput(input);
    }

    @Override
    public void next() {
        if(!this.didDisplayMenu) {
            this.displayMenu();
            return;
        }

        if(this.currentCommand != null) {
            this.currentCommand.next();
        }
    }

    private void displayMenu() {
        this.communicationBus.sendMessage("""
            \n
            BANK APPLICATION
            ――――――――――――――――――――
            Available commands:
            
            1. Transfer money: ⟪ transfer ⟫
            2. Check your balance: ⟪ balance ⟫
            3. Withdraw money: ⟪ withdraw ⟫
            4. Make a deposit: ⟪ deposit ⟫
            
            Type a command:""");

        this.didDisplayMenu = true;
    }
}
