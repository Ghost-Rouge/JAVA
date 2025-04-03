package views;

import javax.swing.*;  // Importation des classes nécessaires pour l'interface graphique (Swing)
import java.awt.*;  // Importation des classes pour la gestion des composants graphiques (disposition, couleur, etc.)
import java.awt.event.ActionEvent;  // Importation de la classe qui gère les événements d'action (comme un clic sur un bouton)
import java.awt.event.ActionListener;  // Importation de l'interface pour écouter les événements d'action (comme un clic sur un bouton)
import java.sql.*;  // Importation des classes pour la gestion des bases de données (connexion, requêtes SQL, etc.)

// Déclaration de la classe LoginView qui hérite de JFrame pour créer une fenêtre d'application
public class LoginView extends JFrame {

    // Déclaration des composants de l'interface utilisateur
    private JTextField usernameField;  // Champ de texte pour le nom d'utilisateur
    private JPasswordField passwordField;  // Champ de texte pour le mot de passe (masqué)
    private JButton LoginViewButton;  // Bouton de connexion

    // Paramètres de connexion à la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306/damien_nerat_carte_grise";  // URL de la base de données (localhost, nom de la base)
    private static final String DB_USER = "root";  // Utilisateur pour se connecter à la base de données (ici, l'utilisateur 'root')
    private static final String DB_PASSWORD = "root";  // Mot de passe pour se connecter à la base de données (ici, 'root')

    // Constructeur de la classe LoginView pour initialiser l'interface graphique
    public LoginView() {
        // Configuration de la fenêtre de LoginView (paramètres de la fenêtre)
        setTitle("Connexion");  // Titre de la fenêtre qui sera affiché dans la barre de titre
        setSize(300, 200);  // Taille de la fenêtre (largeur : 300px, hauteur : 200px)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Ferme l'application lorsque la fenêtre est fermée
        setLocationRelativeTo(null);  // Centrer la fenêtre sur l'écran

        // Création des composants de l'interface utilisateur (champs de texte, bouton)
        usernameField = new JTextField(20);  // Crée un champ de texte pour l'utilisateur avec une largeur de 20 caractères
        passwordField = new JPasswordField(20);  // Crée un champ de mot de passe avec une largeur de 20 caractères
        LoginViewButton = new JButton("Se connecter");  // Crée un bouton avec le texte "Se connecter"

        // Création d'un panneau pour organiser les composants à l'intérieur de la fenêtre
        JPanel panel = new JPanel();  // Création d'un panneau qui va contenir les composants
        panel.setLayout(new GridLayout(3, 2, 10, 10));  // Définition de la disposition en grille (3 lignes, 2 colonnes, espacées de 10px)

        // Ajout des éléments (labels et champs de texte) dans le panneau
        panel.add(new JLabel("Nom d'utilisateur:"));  // Ajoute un label pour le nom d'utilisateur
        panel.add(usernameField);  // Ajoute le champ de texte pour le nom d'utilisateur

        panel.add(new JLabel("Mot de passe:"));  // Ajoute un label pour le mot de passe
        panel.add(passwordField);  // Ajoute le champ de texte pour le mot de passe

        panel.add(new JLabel());  // Ajoute un label vide pour l'alignement du bouton
        panel.add(LoginViewButton);  // Ajoute le bouton de connexion

        // Ajout du panneau à la fenêtre principale
        add(panel);  // Ajoute le panneau (avec tous les composants) à la fenêtre

        // Action lorsque l'utilisateur clique sur le bouton "Se connecter"
        LoginViewButton.addActionListener(new ActionListener() {  // Ajoute un écouteur d'événement sur le bouton
            @Override
            public void actionPerformed(ActionEvent e) {  // Méthode exécutée lorsque l'utilisateur clique sur le bouton
                // Récupérer les valeurs saisies par l'utilisateur dans les champs de texte
                String username = usernameField.getText();  // Récupère le texte saisi dans le champ nom d'utilisateur
                String password = new String(passwordField.getPassword());  // Récupère le mot de passe saisi (converti en String)

                // Connexion à la base de données et authentification de l'utilisateur
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {  // Essaie de se connecter à la base de données
                    // Préparer une requête SQL sécurisée pour vérifier les identifiants de l'utilisateur
                    String query = "SELECT * FROM utilisateur WHERE login_user = ? AND mdp_user = ?";  // Requête pour chercher un utilisateur avec le nom d'utilisateur et mot de passe donnés
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {  // Crée un PreparedStatement pour exécuter la requête
                        stmt.setString(1, username);  // Lier le premier paramètre de la requête (LoginView_user) au texte du champ usernameField
                        stmt.setString(2, password);  // Lier le second paramètre de la requête (mdp_user) au texte du champ passwordField

                        // Exécuter la requête et récupérer les résultats
                        ResultSet resultSet = stmt.executeQuery();  // Exécute la requête et récupère les résultats dans un ResultSet
                        if (resultSet.next()) {  // Si un résultat est trouvé (un utilisateur correspondant aux identifiants)
                            // Si l'authentification est réussie, ouvrir la fenêtre MainView
                            new MainView().setVisible(true);  // Crée et affiche la fenêtre principale de l'application
                            dispose();  // Ferme la fenêtre de connexion (LoginView)
                        } else {  // Si l'authentification échoue (aucun utilisateur trouvé)
                            // Affiche un message d'erreur à l'utilisateur
                            JOptionPane.showMessageDialog(null, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);  // Affiche une boîte de dialogue d'erreur
                        }
                    }
                } catch (SQLException ex) {  // Si une exception se produit lors de la connexion ou de l'exécution de la requête SQL
                    ex.printStackTrace();  // Affiche la trace de l'exception dans la console
                    // Affiche un message d'erreur indiquant que la connexion à la base de données a échoué
                    JOptionPane.showMessageDialog(null, "Erreur de connexion à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);  // Affiche une boîte de dialogue d'erreur
                }
            }
        });
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        // Crée et affiche la fenêtre de connexion lorsque l'application démarre
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginView().setVisible(true);  // Crée une nouvelle instance de la fenêtre LoginView et l'affiche
            }
        });
    }
}
