import java.io.*;
import java.util.ArrayList;

public class SaveManager {

    private File saveDir;

    public SaveManager() {
        // Define the save directory
        saveDir = new File("saves");
        // Create it if it doesn't exist
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    /**
     * Returns a list of all valid save files (.txt) in the save directory.
     */
    public File[] getSaveFiles() {
        File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files == null) {
            return new File[0];
        }
        return files;
    }

    /**
     * Saves the current game state to a file.
     */
    public void saveScore(String filename, int p1Score, int aiScore) {
        // Ensure filename ends with .txt
        if (!filename.endsWith(".txt")) {
            filename += ".txt";
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(saveDir, filename)))) {
            writer.println(p1Score + ":" + aiScore);
            System.out.println("Game saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    /**
     * Loads a game state from a file.
     * Returns an int array where [0] is p1Score and [1] is aiScore.
     * Returns null if loading fails.
     */
    public int[] loadScore(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(saveDir, filename)))) {
            String line = reader.readLine();
            if (line != null) {
                String[] scores = line.split(":");
                if (scores.length >= 2) {
                    int[] result = new int[2];
                    result[0] = Integer.parseInt(scores[0]);
                    result[1] = Integer.parseInt(scores[1]);
                    return result;
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
        return null;
    }
}