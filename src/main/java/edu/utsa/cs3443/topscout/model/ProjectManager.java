package edu.utsa.cs3443.topscout.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

/**
 * Handles all reading and writing of data. Loads the blank template, creates new
 * projects from it, lists the saved projects, and saves, loads, renames, and
 * deletes each project as its own JSON file. Also copies uploaded images into
 * the project's folder. File problems are passed up as IOExceptions so the
 * controllers can show the user a friendly message.
 *
 * Each project lives in its own folder:
 *   ~/.topscout/projects/(project id)/project.json
 *   ~/.topscout/projects/(project id)/attachments/(uploaded images)
 */
public class ProjectManager {

    /** Where the blank template lives inside src/main/resources. */
    private static final String TEMPLATE_PATH = "/edu/utsa/cs3443/topscout/data/eagle_template.json";

    /** Name of the file that holds one project. */
    private static final String PROJECT_FILE_NAME = "project.json";

    /** Name of the folder inside a project that holds uploaded images. */
    private static final String ATTACHMENTS_FOLDER = "attachments";

    /** The folder that holds every project, inside the user's home folder. */
    private final Path rootDirectory;

    /** Converts Projects to JSON text and back. */
    private final Gson gson;

    /**
     * Sets up the projects folder location and the JSON converter. The folder
     * itself is created the first time something is saved.
     */
    public ProjectManager() {
        rootDirectory = Path.of(System.getProperty("user.home"), ".topscout", "projects");
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Loads every saved project for the home screen, oldest first.
     *
     * @return the saved projects (empty if there are none yet)
     * @throws IOException if the projects folder cannot be read
     */
    public ArrayList<Project> listProjects() throws IOException {
        ArrayList<Project> projects = new ArrayList<>();
        if (!Files.isDirectory(rootDirectory)) {
            return projects; // nothing has been saved yet
        }

        try (Stream<Path> folders = Files.list(rootDirectory)) {
            for (Path folder : folders.toList()) {
                Path file = folder.resolve(PROJECT_FILE_NAME);
                if (Files.isRegularFile(file)) {
                    try {
                        projects.add(readProject(file));
                    } catch (IOException e) {
                        // Skip a damaged project so one bad file does not hide all the others
                    }
                }
            }
        }

        projects.sort(Comparator.comparing(Project::getCreatedDate,
                Comparator.nullsFirst(Comparator.naturalOrder())));
        return projects;
    }

    /**
     * Creates a new project by copying the blank template, giving the copy its
     * own id, name, and date, and saving it.
     *
     * @param title the name the Scout chose
     * @return the new project
     * @throws IOException if the template cannot be read or the project cannot be saved
     */
    public Project createProject(String title) throws IOException {
        Project project = loadTemplate();
        project.setId(UUID.randomUUID().toString());
        project.setTitle(title);
        project.setCreatedDate(LocalDateTime.now().toString());
        saveProject(project);
        return project;
    }

    /**
     * Loads one saved project.
     *
     * @param id the project's id
     * @return the project
     * @throws IOException if the file is missing or damaged
     */
    public Project loadProject(String id) throws IOException {
        return readProject(projectFile(id));
    }

    /**
     * Saves a project to its file, replacing the old version.
     *
     * @param project the project to save
     * @throws IOException if the file cannot be written
     */
    public void saveProject(Project project) throws IOException {
        Path file = projectFile(project.getId());
        Files.createDirectories(file.getParent());

        // Write to a temporary file first, then swap it in, so a crash
        // in the middle of saving cannot damage the real file
        Path temp = file.resolveSibling(PROJECT_FILE_NAME + ".tmp");
        Files.writeString(temp, gson.toJson(project), StandardCharsets.UTF_8);
        try {
            Files.move(temp, file, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, file, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    /**
     * Permanently deletes a project: its file and its folder of images.
     *
     * @param id the id of the project to delete
     * @throws IOException if something cannot be deleted
     */
    public void deleteProject(String id) throws IOException {
        Path folder = projectFile(id).getParent();
        if (!Files.exists(folder)) {
            return; // already gone
        }
        try (Stream<Path> paths = Files.walk(folder)) {
            // Deepest files first, so each folder is empty by the time it is deleted
            for (Path path : paths.sorted(Comparator.reverseOrder()).toList()) {
                Files.delete(path);
            }
        }
    }

    /**
     * Changes a project's name and saves it.
     *
     * @param project  the project to rename
     * @param newTitle the new name
     * @throws IOException if the project cannot be saved
     */
    public void renameProject(Project project, String newTitle) throws IOException {
        String oldTitle = project.getTitle();
        project.setTitle(newTitle);
        try {
            saveProject(project);
        } catch (IOException e) {
            project.setTitle(oldTitle); // keep the screen and the file in agreement
            throw e;
        }
    }

    /**
     * Copies an image into the project's attachments folder. If a file with the
     * same name is already there, a number is added (photo.png, photo-1.png, ...)
     * so no picture is ever replaced.
     *
     * @param project the project the image belongs to
     * @param source  the image file the user picked
     * @return the saved path, relative to the project's folder, such as "attachments/photo.png"
     * @throws IOException if the file cannot be copied
     */
    public String addAttachment(Project project, Path source) throws IOException {
        Path folder = projectFile(project.getId()).getParent().resolve(ATTACHMENTS_FOLDER);
        Files.createDirectories(folder);

        String fileName = source.getFileName().toString();
        Path target = folder.resolve(fileName);
        int counter = 1;
        while (Files.exists(target)) {
            target = folder.resolve(addSuffix(fileName, counter));
            counter++;
        }

        Files.copy(source, target);
        return ATTACHMENTS_FOLDER + "/" + target.getFileName();
    }

    /**
     * Reads the blank template that ships inside the app.
     *
     * @return a new Project with every answer empty
     * @throws IOException if the template is missing or damaged
     */
    private Project loadTemplate() throws IOException {
        try (InputStream stream = getClass().getResourceAsStream(TEMPLATE_PATH)) {
            if (stream == null) {
                throw new IOException("Could not find the template file " + TEMPLATE_PATH);
            }
            Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            return gson.fromJson(reader, Project.class);
        } catch (JsonParseException e) {
            throw new IOException("The template file is damaged.", e);
        }
    }

    /**
     * Works out where a project's file is, and refuses ids that point outside
     * the projects folder (such as "../../Documents"), so a bad id can never
     * touch other files.
     *
     * @param id the project's id
     * @return the path of that project's project.json
     * @throws IOException if the id is missing or not allowed
     */
    private Path projectFile(String id) throws IOException {
        if (id == null || id.isBlank()) {
            throw new IOException("The project has no id.");
        }
        Path folder = rootDirectory.resolve(id).normalize();
        if (!folder.startsWith(rootDirectory) || folder.equals(rootDirectory)) {
            throw new IOException("Invalid project id: " + id);
        }
        return folder.resolve(PROJECT_FILE_NAME);
    }

    /**
     * Reads one project file and turns the JSON into a Project.
     *
     * @param file the project.json file to read
     * @return the project
     * @throws IOException if the file cannot be read or is not valid JSON
     */
    private Project readProject(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            Project project = gson.fromJson(reader, Project.class);
            if (project == null) {
                throw new IOException("The project file is empty: " + file);
            }
            return project;
        } catch (JsonParseException e) {
            throw new IOException("The project file is damaged: " + file, e);
        }
    }

    /**
     * Adds a number before a file's extension: ("photo.png", 2) becomes "photo-2.png".
     *
     * @param fileName the original file name
     * @param number   the number to add
     * @return the new file name
     */
    private String addSuffix(String fileName, int number) {
        int dot = fileName.lastIndexOf('.');
        if (dot < 0) {
            return fileName + "-" + number;
        }
        return fileName.substring(0, dot) + "-" + number + fileName.substring(dot);
    }
}