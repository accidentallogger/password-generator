import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordManager {

    private static List<Account> accounts = new ArrayList<>();
    private static final String FILE_NAME = "passwords.bin";

    private static String caps = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String smallCaps = "abcdefghijklmnopqrstuvwxyz";
    private static String numeric = "1234567890";
    private static String specialChar = "~!@$%^&*(_+{#}|:_[?]>=<";

    private static String generatePassword(int length, boolean useCaps, boolean useSmallCaps, boolean useNumeric,
            boolean useSpecialChar) {
        if (length < 4) {
            JOptionPane.showMessageDialog(null, "Password length must be greater than 4!");
            return null;
        }

        String characters = "";
        if (useCaps)
            characters += caps;
        if (useSmallCaps)
            characters += smallCaps;
        if (useNumeric)
            characters += numeric;
        if (useSpecialChar)
            characters += specialChar;

        if (characters.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select at least one character type.");
            return null;
        }

        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        JOptionPane.showMessageDialog(null, "Generated Password: " + password);
        return password.toString();
    }

    private static void storePassword() {
        String accountName = JOptionPane.showInputDialog("Enter the account name");
        String accountPassword = JOptionPane.showInputDialog("Enter the account password");
        if (accountName != null && accountPassword != null) {
            accounts.add(new Account(accountName, accountPassword));
            saveData();
            JOptionPane.showMessageDialog(null, "Account added successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Account name and password cannot be empty!");
        }
    }

    private static boolean isStrongPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            } else {
                hasSpecialChar = true;
            }
        }

        return hasUppercase && hasLowercase && hasDigit && hasSpecialChar;
    }

    private static void searchPassword() {
        String accountName = JOptionPane.showInputDialog("Enter the account name to search");
        if (accountName != null) {
            for (Account account : accounts) {
                if (account.getName().equals(accountName)) {
                    JOptionPane.showMessageDialog(null, "Password: " + account.getPassword());
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Account not found!");
        }
    }

    private static void deletePassword() {
        String accountName = JOptionPane.showInputDialog("Enter the account name to delete");
        if (accountName != null) {
            for (Account account : accounts) {
                if (account.getName().equals(accountName)) {
                    accounts.remove(account);
                    saveData();
                    JOptionPane.showMessageDialog(null, "Password deleted successfully!");
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Account not found!");
        }
    }

    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(accounts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadData() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (List<Account>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void exitApp() {
        int option = JOptionPane.showConfirmDialog(null, "Do you want to close the application?");
        if (option == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }

    private static void showAllAccounts() {
        if (accounts.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No accounts stored yet!");
        } else {
            StringBuilder allAccounts = new StringBuilder("Accounts:\n");
            for (Account account : accounts) {
                allAccounts.append("Name: ").append(account.getName()).append(", Password: ")
                        .append(account.getPassword()).append("\n");
            }
            JOptionPane.showMessageDialog(null, allAccounts.toString());
        }
    }

    private static void changePassword() {
        String accountName = JOptionPane.showInputDialog("Enter the account name to change password");
        if (accountName != null) {
            for (Account account : accounts) {
                if (account.getName().equals(accountName)) {
                    String newPassword = JOptionPane.showInputDialog("Enter the new password");
                    if (newPassword != null) {
                        saveData();
                        JOptionPane.showMessageDialog(null, "Password changed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Password cannot be empty!");
                    }
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Account not found!");
        }
    }

    public static void main(String[] args) {
        loadData();

        JFrame frame = new JFrame("Password Manager");
        JLabel label = new JLabel("Welcome to Password Manager!");
        label.setBounds(10, 10, 250, 25);

        frame.add(label);
        frame.setSize(100, 100);
        // frame.setResizable(false);
        frame.validate();

        JButton generatePasswordButton = new JButton("Generate Password");
        generatePasswordButton.setBounds(10, 40, 200, 25);
        generatePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int length = Integer.parseInt(JOptionPane.showInputDialog("Enter the password length"));
                if (length > 4) {
                    boolean useCaps = JOptionPane.showConfirmDialog(null,
                            "Use uppercase letters?") == JOptionPane.YES_OPTION;
                    boolean useSmallCaps = JOptionPane.showConfirmDialog(null,
                            "Use lowercase letters?") == JOptionPane.YES_OPTION;
                    boolean useNumeric = JOptionPane.showConfirmDialog(null, "Use numbers?") == JOptionPane.YES_OPTION;
                    boolean useSpecialChar = JOptionPane.showConfirmDialog(null,
                            "Use special characters?") == JOptionPane.YES_OPTION;
                    generatePassword(length, useCaps, useSmallCaps, useNumeric, useSpecialChar);
                } else {
                    JOptionPane.showMessageDialog(null, "Password length must be greater than 4!");
                }
            }
        });
        frame.add(generatePasswordButton);

        JButton storePasswordButton = new JButton("Store Password");
        storePasswordButton.setBounds(10, 70, 200, 25);
        storePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                storePassword();
            }
        });
        frame.add(storePasswordButton);

        JButton searchPasswordButton = new JButton("Search Password");
        searchPasswordButton.setBounds(10, 100, 200, 25);
        searchPasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchPassword();
            }
        });
        frame.add(searchPasswordButton);

        JButton deletePasswordButton = new JButton("Delete Password");
        deletePasswordButton.setBounds(10, 130, 200, 25);
        deletePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deletePassword();
            }
        });
        frame.add(deletePasswordButton);

        JButton showAllAccountsButton = new JButton("Show All Accounts");
        showAllAccountsButton.setBounds(10, 160, 200, 25);
        showAllAccountsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAllAccounts();
            }
        });
        frame.add(showAllAccountsButton);

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(10, 190, 200, 25);
        changePasswordButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePassword();
            }
        });
        frame.add(changePasswordButton);

        JButton isPassStrong = new JButton("pass strength test");
        isPassStrong.setBounds(10, 220, 200, 25);
        isPassStrong.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String pass = JOptionPane.showInputDialog("Enter the password");
                if (isStrongPassword(pass)) {
                    JOptionPane.showMessageDialog(null,
                            "Password is pretty strong!!");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Password is not strong enough! Please ensure that the password has at least 8 characters, including uppercase letters, lowercase letters, digits, and special characters.");
                }
            }
        });
        frame.add(isPassStrong);

        JButton exitAppButton = new JButton("Exit");
        exitAppButton.setBounds(10, 250, 200, 25);
        exitAppButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitApp();
            }
        });
        frame.add(exitAppButton);

        frame.setSize(250, 300);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static class Account implements Serializable {
        private String name;
        private String password;

        public Account(String name, String password) {
            this.name = name;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getPassword() {
            return password;
        }
    }
}
