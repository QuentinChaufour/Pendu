import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.ButtonBar.ButtonData ;

import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.ArrayList;


/**
 * Vue du jeu du pendu
 */
public class Pendu extends Application {

    private Stage stage;

    /**
     * modèle du jeu
     **/
    private MotMystere modelePendu;
    /**
     * Liste qui contient les images du jeu
     */
    private ArrayList<Image> lesImages;
    /**
     * Liste qui contient les noms des niveaux
     */    
    public List<String> niveaux;

    // les différents contrôles qui seront mis à jour ou consultés pour l'affichage
    /**
     * le dessin du pendu
     */
    private ImageView dessin;
    /**
     * le mot à trouver avec les lettres déjà trouvé
     */
    private Text motCrypte;
    /**
     * la barre de progression qui indique le nombre de tentatives
     */
    private ProgressBar pg;
    /**
     * le clavier qui sera géré par une classe à implémenter
     */
    private Clavier clavier;
    /**
     * le text qui indique le niveau de difficulté
     */
    private Text leNiveau;
    /**
     * le chronomètre qui sera géré par une clasee à implémenter
     */
    private Chronometre chrono;
    /**
     * le panel Central qui pourra être modifié selon le mode (accueil ou jeu)
     */
    private BorderPane panelCentral;
    /**
     * le bouton Paramètre / Engrenage
     */
    private Button boutonParametres;
    /**
     * le bouton Accueil / Maison
     */    
    private Button boutonMaison;

    private Button boutonInfo;
    /**
     * le bouton qui permet de (lancer ou relancer une partie)
     */ 
    private Button bJouer;

    private Button bNewWord;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./pendu_pour_etu/img");
        this.dessin = new ImageView(this.lesImages.get(0));
        this.dessin.setSmooth(true);
        this.dessin.setPreserveRatio(true);
        this.dessin.setFitHeight(1000);
        this.dessin.setFitHeight(500);

        this.motCrypte = new Text(this.modelePendu.getMotCrypte());
        this.motCrypte.setStyle("-fx-font-size:32px");

        this.clavier = new Clavier("ABCDEFGHIJKLMNOPQRSTUVWXYZ-", new ControleurLettres(this.modelePendu, this));
        
        this.leNiveau = new Text();
        
        this.leNiveau.setStyle("-fx-font-size:36px");

        //buttons

        this.boutonMaison = new Button();
        ImageView home = new ImageView(new Image("file:pendu_pour_etu/img/home.png"));
        home.setFitHeight(32);
        home.setFitWidth(32);
        this.boutonMaison.setGraphic(home);
        this.boutonMaison.setOnAction(new RetourAccueil(modelePendu, this));

        this.boutonParametres = new Button();
        ImageView param = new ImageView(new Image("file:pendu_pour_etu/img/parametres.png"));
        param.setFitHeight(32);
        param.setFitWidth(32);
        this.boutonParametres.setGraphic(param);

        this.boutonInfo = new Button();
        ImageView info = new ImageView(new Image("file:pendu_pour_etu/img/info.png"));
        info.setFitHeight(32);
        info.setFitWidth(32);
        this.boutonInfo.setGraphic(info);

        this.bJouer = new Button("Lancer une partie");
        this.bNewWord = new Button("Nouveau Mot");

        this.chrono = new Chronometre();
    }

    /**
     * @return  le graphe de scène de la vue à partir de methodes précédantes
     */
    private Scene laScene(){
        BorderPane fenetre = new BorderPane();
        fenetre.setTop(this.titre());
        fenetre.setCenter(this.panelCentral);
        BorderPane.setMargin(this.panelCentral, new Insets(30));
        return new Scene(fenetre, 800, 1000);
    }

    /**
     * @return le panel contenant le titre du jeu
     */
    private Pane titre(){

        BorderPane banniere = new BorderPane();

        Text titreJeu = new Text("Jeu du Pendu");
        titreJeu.setStyle("-fx-font-size:35 px;");
        BorderPane.setMargin(titreJeu, new Insets(20));
        BorderPane.setAlignment(titreJeu, Pos.CENTER);

        HBox imgs = new HBox(10);

        imgs.getChildren().addAll(this.boutonMaison, this.boutonParametres, this.boutonInfo);

        BorderPane.setAlignment(imgs, Pos.CENTER);
        BorderPane.setAlignment(this.boutonMaison, Pos.CENTER);
        BorderPane.setAlignment(this.boutonParametres, Pos.CENTER);
        BorderPane.setAlignment(this.boutonInfo, Pos.CENTER);
        BorderPane.setMargin(imgs, new Insets(20));

        banniere.setLeft(titreJeu);
        banniere.setStyle("-fx-background-color: #c9ecff");
        banniere.setRight(imgs);

        return banniere;
    }

    /**
     * @return le panel du chronomètre
     */
    private TitledPane leChrono(){
        
        TitledPane time = new TitledPane("Chronomètre",this.chrono);
        time.setStyle("fx-font-size:40px");
        return time;
    }

    /**
    / * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
    / *         de progression et le clavier
    / */
    private Pane fenetreJeu(String niveau){
        
        this.boutonMaison.setDisable(false);
        this.boutonInfo.setDisable(false);
        this.boutonParametres.setDisable(true);

        BorderPane center = new BorderPane();

        VBox content = new VBox(10);
        content.getChildren().addAll(this.motCrypte,this.dessin,this.clavier);

        center.setCenter(content);

        BorderPane.setAlignment(this.motCrypte, Pos.CENTER);
        BorderPane.setAlignment(this.dessin, Pos.CENTER);
        BorderPane.setAlignment(this.clavier, Pos.CENTER);
        
        VBox right = new VBox(10);

        TitledPane time = this.leChrono();
        this.leNiveau.setText("Niveau" + niveau);

        right.getChildren().addAll(this.leNiveau,time,this.bNewWord);
        center.setRight(right);

        BorderPane.setAlignment(time, Pos.CENTER);
        BorderPane.setAlignment(this.leNiveau, Pos.CENTER);
        BorderPane.setAlignment(this.leNiveau, Pos.CENTER_LEFT);


        return center;
    }

    /**
    / * @return la fenêtre d'accueil sur laquelle on peut choisir les paramètres de jeu
    */
    private Pane fenetreAccueil(){
        
        this.boutonMaison.setDisable(true);
        this.boutonInfo.setDisable(false);
        this.boutonParametres.setDisable(false);

        BorderPane paramJeu = new BorderPane();
        VBox content = new VBox(10);

        // radio btn de difficulté

        ToggleGroup groupRadioDiff = new ToggleGroup();

        RadioButton facile = new RadioButton("Facile");
        RadioButton medium = new RadioButton("Médium");
        RadioButton difficile = new RadioButton("Difficile");
        RadioButton expert = new RadioButton("Expert");

        facile.setToggleGroup(groupRadioDiff);
        facile.setSelected(true);
        medium.setToggleGroup(groupRadioDiff);
        difficile.setToggleGroup(groupRadioDiff);
        expert.setToggleGroup(groupRadioDiff);

        VBox radio = new VBox(10);
        radio.getChildren().addAll(facile,medium,difficile,expert);

        TitledPane difficuly = new TitledPane("Niveau de difficulté",radio);
        difficuly.setCollapsible(false);

        this.bJouer.setOnAction(new ControleurLancerPartie(modelePendu, this,groupRadioDiff));
        content.getChildren().addAll(this.bJouer,difficuly);
        paramJeu.setCenter(content);

        return paramJeu;
    }

    /**
     * charge les images à afficher en fonction des erreurs
     * @param repertoire répertoire où se trouvent les images
     */
    private void chargerImages(String repertoire){
        for (int i=0; i<this.modelePendu.getNbErreursMax()+1; i++){
            File file = new File(repertoire+"/pendu"+i+".png");
            System.out.println(file.toURI().toString());
            this.lesImages.add(new Image(file.toURI().toString()));
        }
    }

    public void modeAccueil(){
        this.panelCentral = (BorderPane) this.fenetreAccueil();
    }
    
    public void modeJeu(String niveau){
        this.panelCentral = (BorderPane) this.fenetreJeu(niveau);
        this.stage.setScene(this.laScene());

    }
    
    public void modeParametres(){

    }

    /** lance une partie */
    public void lancePartie(String niveau){
        this.modeJeu(niveau);
        this.chrono.start();


    }

    /**
     * raffraichit l'affichage selon les données du modèle
     */
    public void majAffichage(){
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.dessin.setImage(this.lesImages.get(this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()));
    }

    /**
     * accesseur du chronomètre (pour les controleur du jeu)
     * @return le chronomètre du jeu
     */
    public Chronometre getChrono(){
        return this.chrono;
    }

    public Alert popUpPartieEnCours(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"La partie est en cours!\n Etes-vous sûr de l'interrompre ?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Attention");
        return alert;
    }
        
    public Alert popUpReglesDuJeu(){
        // A implementer
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        return alert;
    }
    
    public Alert popUpMessageGagne(){

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Vous avez Gagné !");        
        return alert;
    }
    
    public Alert popUpMessagePerdu(){
        // A implementer    
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Vous avez Perdu !");
        return alert;
    }

    /**
     * créer le graphe de scène et lance le jeu
     * @param stage la fenêtre principale
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("IUTEAM'S - La plateforme de jeux de l'IUTO");
        this.modeAccueil();
        this.stage.setScene(this.laScene());
        this.stage.show();
    }

    /**
     * Programme principal
     * @param args inutilisé
     */
    public static void main(String[] args) {
        launch(args);
    }    
}
