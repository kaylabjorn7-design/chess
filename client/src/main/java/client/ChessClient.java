package client;

import java.util.Scanner;

public class ChessClient {
    private boolean running = true;
    String username = null;
    String authToken = null;

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Chess!");
        System.out.println();
        System.out.println("Type 'help' to see available commands");
        while (running) {
            if (authToken != null) System.out.print("Chess [" + username + "] > ");
            else System.out.print("Chess > ");

            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("help")) {
                System.out.println();
                System.out.println("Available Commands:");
                if (authToken != null) {
                    postloginHelp();
                }
                else {
                    preloginHelp();
                }
            }
            else if (command.equalsIgnoreCase("quit") ||
                     command.equalsIgnoreCase("exit")) {
                running = false;
            }
            else {
                System.out.println("Command not recognized. Type 'help' to see available commands");
            }
        }
    }

    private void handlePreloginCommand(String command) {}

    private void handlePostloginCommand(String command) {}

    private void preloginHelp() {
        System.out.println("help      - Show available commands");
        System.out.println("login     - Login to your account");
        System.out.println("register  - Create a new account");
        System.out.println("quit      - Exit the program");
    }

    private void postloginHelp() {
        System.out.println("help          - Show available commands");
        System.out.println("create game   - Create a new game");
        System.out.println("list games    - List all active games");
        System.out.println("play game     - join an existing game");
        System.out.println("observe game  - watch an existing game");
        System.out.println("logout        - logout of your account");
    }
}
