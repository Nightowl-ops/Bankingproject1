package com.ga.bank.repository;

import com.ga.bank.model.User.Banker;
import com.ga.bank.model.User.Customer;
import com.ga.bank.model.User.User;
import com.ga.bank.util.FileStorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserRepository implements UserRepository {
// this file implments user repostry
    /// we have two vrablaes the STRING basedir and STRING all user files so that all user are in one folder
    /// when we implmented the userrepostry we forced it too use the methods find byid and find all and findby name and update
    /// the basedir is the folder path where the user files live
    /// and the second is for a single file which will be used to group up all the users
    ///
    private final String baseDir;
    // Name of the master file that collects everyone
    private static final String ALL_USERS_FILENAME = "all_users.txt";

    // the first constrctor is a fallback when a user doesnt enter a pathe for the file

    public FileUserRepository() {
        this("data/users");
    }
//if we want the file to be saved in a specific file we use the second constructor when declauring the fileuserrepostry

    // the paramters are the new directory and we use a method from filestorage unitls to ensure that this directroy exists or not if not will create it yes it exist

    public FileUserRepository(String baseDir) {
        this.baseDir = baseDir;
        FileStorageUtils.ensureDirectoryExists(baseDir);
    }

    // this save will do the following it will first check if the user is null or the userid is null
    @Override
    // why do i cjeck the id becuase the naming of the file i dont want it to be null you can see the exmple in the data folder

    public void save(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Cannot save null user or user with null ID.");
        }
        // then the object user is passed if those things exist so its good to save and the
        // the info from the object is passed in the user object and writtern in the file created or if it exist go to it

        writeUserToFile(user);
        syncMasterUserFile(); // Rebuilds the combined master file
    }
// this method is used to update the user info forth etxt file how does that work
    // first the same check if its null and user id exist

    @Override
    public void update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("Cannot update null user.");
        }
        writeUserToFile(user);

        syncMasterUserFile(); // Updates the combined master file
    }


    @Override
    public Optional<User> findbyid(String id) {
        if (id == null) return Optional.empty();
        return findall().stream()
                .filter(u -> id.equalsIgnoreCase(u.getId()))
                .findFirst();
    }

    @Override
    public Optional<User> findbyname(String name) {
        if (name == null) return Optional.empty();
        return findall().stream()
                .filter(u -> u.getName() != null && u.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<User> findall() {
        List<User> users = new ArrayList<>();
        File folder = new File(baseDir);

        // Filter out all_users.txt so we don't treat the combined file as an individual user
        File[] files = folder.listFiles((dir, name) ->
                name.endsWith(".txt") && !name.equalsIgnoreCase(ALL_USERS_FILENAME)
        );

        if (files == null) {
            return users;
        }

        for (File file : files) {
            User u = parseUserFile(file.getAbsolutePath());
            if (u != null) {
                users.add(u);
            }
        }
        return users;
    }
//
    private void writeUserToFile(User user) {
        //checks weather its a banker or customer
        String rolePrefix = (user instanceof Banker) ? "Banker" : "Customer";
        String fileName = rolePrefix + "-" + user.getName() + "-" + user.getId() + ".txt";
        String fullPath = baseDir + "/" + fileName;

        String line = formatUserToLine(user);
        // so this line is used to write the info into the packge name if it doesn exist it creates it
        FileStorageUtils.writeLines(fullPath, List.of(line));
    }

    private void syncMasterUserFile() {
        List<User> allCurrentUsers = findall();
        List<String> lines = new ArrayList<>();

        for (User u : allCurrentUsers) {
            lines.add(formatUserToLine(u));
        }

        String masterFilePath = baseDir + "/" + ALL_USERS_FILENAME;
        FileStorageUtils.writeLines(masterFilePath, lines);
    }

    // this method is used to take the object and make it to string join with | between them
// this is usedt o turn an object intoraw text row
    private String formatUserToLine(User user) {
        return String.join("|",
                user.getId(),
                user.getName(),
                user.getPasswordHash(),
                // covert the role enum to string
                (user.getRole() != null ? user.getRole().name() : "CUSTOMER"),
                // the same is said about these take the int and convert it to string for both of them

                String.valueOf(user.getFailedLoginAttempts()),
                String.valueOf(user.isLocked())
        );
        //example :c-1001|ahmed|e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855|CUSTOMER|0|false
    }
// this is used to read file fro disk to reconstructe a user
    // this is used in the find all to diplay the users when called upon

    private User parseUserFile(String fullPath) {
        List<String> lines = FileStorageUtils.readLines(fullPath);
        if (lines.isEmpty()) return null;

        String[] parts = lines.get(0).split("\\|");
        if (parts.length < 6) return null;

        String id = parts[0];
        String name = parts[1];
        String passwordHash = parts[2];
        String roleStr = parts[3];
        int failedAttempts = Integer.parseInt(parts[4]);
        boolean isLocked = Boolean.parseBoolean(parts[5]);

        User user;
        if ("BANKER".equalsIgnoreCase(roleStr)) {
            user = new Banker(id, name, passwordHash);
        } else {
            user = new Customer(id, name, passwordHash);
        }

        user.setFailedLoginAttempts(failedAttempts);
        user.setLocked(isLocked);
        return user;
    }
}