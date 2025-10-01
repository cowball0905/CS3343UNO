package controller;

import javax.swing.SwingUtilities;

public class Main {
    // main function that starts the UNO game
    public static void main(String[] args) {
        System.out.println("=== UNO Game Starting ===");
        System.out.println("Step 1: About to create Swing thread...");
        
        // Run on the Event Dispatch Thread for proper Swing behavior
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("Step 2: Inside Swing thread, about to create UNOController...");
                    
                    // Start the UNO game and show the window
                    UNOController uno = UNOController.getInstance();
                    System.out.println("Step 3: UNOController created successfully!");
                    
                    System.out.println("Step 4: About to call showMenu()...");
                    uno.showMenu();
                    System.out.println("Step 5: showMenu() completed!");
                    
                    System.out.println("Step 6: Window should be visible now!");
                    
                } catch (Exception e) {
                    System.err.println("=== ERROR OCCURRED ===");
                    System.err.println("Error message: " + e.getMessage());
                    System.err.println("Error type: " + e.getClass().getSimpleName());
                    e.printStackTrace();
                    System.err.println("=== END ERROR ===");
                }
            }
        });
        
        System.out.println("Step 7: Main thread finished, Swing should be running...");
    }
}