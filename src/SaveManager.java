import java.io.*;

public class SaveManager {

    private File saveDir;

    public SaveManager() {
        saveDir = new File("saves");
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    public File[] getSaveFiles() {
        File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".txt"));
        return files == null ? new File[0] : files;
    }

    public void saveScore(String filename, int p1Score, int aiScore) {
        if (!filename.endsWith(".txt")) filename += ".txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(new File(saveDir, filename)))) {
            writer.println(p1Score + ":" + aiScore);
            System.out.println("Game saved to " + filename);
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }
    }

    public int[] loadScore(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(saveDir, filename)))) {
            String line = reader.readLine();
            if (line != null) {
                String[] scores = line.split(":");
                if (scores.length >= 2) {
                    return new int[]{Integer.parseInt(scores[0]), Integer.parseInt(scores[1])};
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
        return null;
    }

    // New method to delete a save file
    public void deleteSave(String filename) {
        File file = new File(saveDir, filename);
        if (file.exists()) {
            file.delete();
            System.out.println("Deleted save: " + filename);
        }
    }
}