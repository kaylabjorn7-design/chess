package client;

import chess.ChessBoard;
import chess.ChessGame;
import dto.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class ChessClient {
    private final ServerFacade facade;
    Scanner scanner;
    private boolean running = true;
    private String username = null;
    private String authToken = null;
    private List<GameSummary> gameList = new ArrayList<>();
    ChessBoardRenderer renderer = new ChessBoardRenderer();

    public ChessClient(int port) {
        facade = new ServerFacade(port);
        scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Welcome to Chess!");
        System.out.println();
        System.out.println("Type 'help' to see available commands");

        while (running) {
            if (authToken != null) {
                System.out.print("Chess [" + username + "] > ");
                String command = scanner.nextLine();
                handlePostLoginCommand(command);
            }
            else {
                System.out.print("Chess > ");
                String command = scanner.nextLine();
                handlePreLoginCommand(command);
            }
        }
    }

    private void handlePreLoginCommand(String command) {
        switch (command.trim().toLowerCase()) {
            case "help" -> preLoginHelp();
            case "login" -> login();
            case "register" -> register();
            case "quit", "exit" -> quit();
            default -> unknownCommand();
        }
    }

    private void handlePostLoginCommand(String command) {
        switch (command.trim().toLowerCase()) {
            case "help" -> postLoginHelp();
            case "create game" -> createGame();
            case "list games" -> listGames();
            case "play game" -> joinGame();
            case "observe game" -> observeGame();
            case "logout" -> logout();
            case "quit", "exit" -> quit();
            default -> unknownCommand();
        }
    }

    private void preLoginHelp() {
        System.out.println("Available Commands:");
        System.out.println();
        System.out.println("help      - Show available commands");
        System.out.println("login     - Login to your account");
        System.out.println("register  - Create a new account");
        System.out.println("quit      - Exit the program");
    }

    private void login() {
        System.out.print("username: ");
        String username = scanner.nextLine();
        System.out.print("password: ");
        String password = scanner.nextLine();

        LoginRequest request = new LoginRequest(username, password);
        try {
            LoginResult result = facade.login(request);
            this.username = result.username();
            this.authToken = result.authToken();

            System.out.println("Welcome back, " + username + "!");
        }
        catch (IOException e) {
            System.out.println("Login failed. Please try again");
        }
    }

    private void register() {
        System.out.print("username: ");
        String username = scanner.nextLine();
        System.out.print("password: ");
        String password = scanner.nextLine();
        System.out.print("email: ");
        String email = scanner.nextLine();

        RegisterRequest request = new RegisterRequest(username, password, email);
        try {
            RegisterResult result = facade.register(request);
            this.username = result.username();
            this.authToken = result.authToken();

            System.out.println("Welcome, " + username + "!");
        }
        catch (IOException e) {
            System.out.println("Registration failed. Please try again");
        }
    }

    private void postLoginHelp() {
        System.out.println("Available Commands:");
        System.out.println();
        System.out.println("help          - Show available commands");
        System.out.println("create game   - Create a new game");
        System.out.println("list games    - List all active games");
        System.out.println("play game     - Join an existing game");
        System.out.println("observe game  - Watch an existing game");
        System.out.println("logout        - Logout of your account");
        System.out.println("quit          - Exit the program");
    }

    private void createGame() {
        System.out.print("Game Name: ");
        String gameName = scanner.nextLine();

        CreateGameRequest request = new CreateGameRequest(gameName);
        try {
            facade.createGame(request, authToken);

            System.out.println(gameName + " created!");
        }
        catch (IOException e) {
            System.out.println("Unable to create game. Please try again");
        }
    }

    private void listGames() {
        try {
            ListGamesResult result = facade.listGames(authToken);
            gameList = new ArrayList<>(result.games());
            int gameNumber = 1;
            for (GameSummary game : gameList) {
                System.out.println(gameNumber + ". " + game.gameName());
                if (game.whiteUsername() == null) {
                    System.out.println("   White: Open");
                }
                else {
                    System.out.println("   White: " + game.whiteUsername());
                }
                if (game.blackUsername() == null) {
                    System.out.println("   Black: Open");
                }
                else {
                    System.out.println("   Black: " + game.blackUsername());
                }
                gameNumber++;
            }
        }
        catch (IOException e) {
            System.out.println("Unable to list games. Please try again");
        }
    }

    private void joinGame() {
        GameSummary selectedGame = selectGame();
        if (selectedGame == null) return;

        System.out.print("Which color would you like to be? ");
        String selectedColor = scanner.nextLine().trim().toLowerCase();
        if (!selectedColor.equals("black") && !selectedColor.equals("white")) {
            System.out.println("Please choose a valid color: 'black' or 'white'");
            return;
        }

        JoinGameRequest request = new JoinGameRequest(selectedColor, selectedGame.gameID());

        try {
            facade.joinGame(request, authToken);
            System.out.println("Joined" + selectedGame.gameName() + "as " + selectedColor + "!");
            createBoard(selectedColor);
        }
        catch(IOException e) {
            System.out.println("Failed to join game. Please try again");
        }
    }

    private void observeGame() {
        GameSummary selectedGame = selectGame();
        if (selectedGame == null) return;

        System.out.println("Observing " + selectedGame.gameName() + "!");
        createBoard("white");
    }

    private void createBoard(String perspectiveColor) {
        ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(perspectiveColor.toUpperCase());
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        renderer.drawBoard(board, color);
    }

    private GameSummary selectGame() {
        if (gameList.isEmpty()) {
            System.out.println("Sorry, there are no games");
            return null;
        }
        System.out.print("Select a game by typing its number: ");
        try {
            int gameNumber = Integer.parseInt(scanner.nextLine());
            if (gameNumber > gameList.size() || gameNumber < 1) {
                System.out.println("Invalid game number. Type 'list games' to see available games");
                return null;
            }
            return gameList.get(gameNumber - 1);
        }
        catch (NumberFormatException e) {
            System.out.println("Please enter a valid number");
        }
        return null;
    }

    private void logout() {
        try {
            facade.logout(authToken);
            System.out.println("Logged out successfully");
            this.username = null;
            this.authToken = null;
            gameList.clear();
        }
        catch(IOException e) {
            System.out.println("Logout failed. Please try again");
        }
    }

    private void quit() {
        running = false;
        if (authToken != null) {
            logout();
        }
    }

    private void unknownCommand() {
        System.out.println("Command not recognized. Type 'help' to see available commands");
    }
}
