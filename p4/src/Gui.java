import java.awt.*;
import javax.swing.*;
import java.util.Optional;

class Gui extends JFrame {
    public static AccountStorage accounts = new AccountStorage();
    JTabbedPane tabbedPane = new JTabbedPane();

    public class Authenticated extends JFrame {
        JLabel nameLabel = new JLabel("Имя пользователя");
        JLabel passLabel = new JLabel("Пароль");
        JLabel emailLabel = new JLabel("Email");

        JButton back = new JButton("Назад");

        public Authenticated(Account account) {
            super("Меню пользователя");

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setPreferredSize(new Dimension(280, 300));

            setLayout(null);
            setVisible(true);
            pack();
            
            nameLabel.setText("Имя: " + account.name);
            passLabel.setText("Пароль: " + account.password);
            emailLabel.setText("Email: " + account.email);

            nameLabel.setBounds(30, 30, 200, 30);
            passLabel.setBounds(30, 60, 200, 30);
            emailLabel.setBounds(30, 90, 200, 30);

            back.setBounds(30, 120, 200, 30);
            back.addActionListener(e -> Back());

            add(nameLabel);
            add(passLabel);
            add(emailLabel);
            add(back);

            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        }

        public void Back() {
            dispose();
            Gui.this.setVisible(true);
        }
    }

    public class AuthenticationPanel extends JPanel {
        JLabel title;
        JLabel name_label;
        JTextField name;
        JLabel pass_label;
        JPasswordField password;
        JButton login;
        JLabel accountCount;

        AuthenticationPanel() {
            setLayout(null);
            
            title = new JLabel("Авторизация", SwingConstants.CENTER);
            title.setFont(getFont().deriveFont(36f));
            title.setBounds(30, 120, 500, 50);
            
            name_label = new JLabel("Имя пользователя");
            name_label.setBounds(180, 170, 200, 30);

            name = new JTextField();
            name.setBounds(180, 200, 200, 30);

            pass_label = new JLabel("Пароль");
            pass_label.setBounds(180, 230, 200, 30);

            password = new JPasswordField();
            password.setBounds(180, 260, 200, 30);
            
            login = new JButton("Войти");
            login.setBounds(180, 310, 200, 30);
            
            accountCount = new JLabel();
            accountCount.setBounds(180, 380, 200, 30);

            add(title);
            add(name_label);
            add(name);
            add(password);
            add(pass_label);
            add(accountCount);
            add(login);

            UpdateAccountsCount();

            login.addActionListener(e -> {
                Optional<Account> account = accounts.get(name.getText());

                if (account.isPresent() && account.get().password.equals(new String(password.getPassword()))) {
                    new Authenticated(account.get());
                    UpdateAccountsCount();
                    Gui.this.setVisible(false);
                }else{
                    JOptionPane.showMessageDialog(
                        null, "Неверное имя пользователя или пароль!");
                }
            });
        }

        public void UpdateAccountsCount() {
            accountCount.setText("Количество аккаунтов: " + accounts.count());
        }
    }

    public class RegistrationPanel extends JPanel {
        JLabel title;
        JLabel name_label;
        JTextField name;
        JLabel pass_label;
        JPasswordField password;
        JLabel email_label;
        JTextField email;
        JButton register;

        RegistrationPanel() {
            setLayout(null);
    
            title = new JLabel("Регистрация", SwingConstants.CENTER);
            title.setFont(getFont().deriveFont(36f));
            title.setBounds(30, 120, 500, 50);
            
            name_label = new JLabel("Имя пользователя");
            name_label.setBounds(180, 170, 200, 30);
    
            name = new JTextField();
            name.setBounds(180, 200, 200, 30);
    
            pass_label = new JLabel("Пароль");
            pass_label.setBounds(180, 230, 200, 30);
    
            password = new JPasswordField();
            password.setBounds(180, 260, 200, 30);
    
    
            email_label = new JLabel("Email");
            email_label.setBounds(180, 290, 200, 30);
    
            email = new JTextField();
            email.setBounds(180, 320, 200, 30);
    
            register = new JButton("Зарегистрироваться");
            register.setBounds(180, 370, 200, 30);
    
            register.addActionListener(e -> RegisterAccount());

            add(title);
            add(name_label);
            add(name);
            add(pass_label);
            add(password);
            add(email_label);
            add(email);
            add(register);
        }

        public void RegisterAccount() {
            Account account = new Account();
            account.name = name.getText();
            account.password = new String(password.getPassword());
            account.email = email.getText();

            int validated = accounts.validate(account);

            switch(validated) {
                case 0:
                    accounts.insert(account);
                    accounts.save();

                    new Authenticated(account);
                    this.setVisible(false);

                    break;
                case 1:
                    JOptionPane.showMessageDialog(
                        null, "Имя пользователя слишком короткое!");
                    break;
                case 2:
                    JOptionPane.showMessageDialog(
                        null, "Пароль слишком короткий!");
                    break;
                case 3:
                    JOptionPane.showMessageDialog(
                        null, "Email слишком короткий!");
                    break;
            }
        }
    }

    public Gui() {
        super("GUI");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(600, 800));
        setResizable(false);
    
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        
        tabbedPane.add("Авторизация", new AuthenticationPanel());
        tabbedPane.add("Регистрация", new RegistrationPanel());

        add(tabbedPane);
        
        pack();
    }

    public static void main(String[] args) {
        accounts.load();
        new Gui();
    }
}