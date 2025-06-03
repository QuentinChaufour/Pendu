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
import javafx.scene.Node;

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

    private boolean enCours;

    /**
     * initialise les attributs (créer le modèle, charge les images, crée le chrono ...)
     */
    @Override
    public void init() {
        this.modelePendu = new MotMystere("/usr/share/dict/french", 3, 10, MotMystere.FACILE, 10);
        this.lesImages = new ArrayList<Image>();
        this.chargerImages("./pendu_pour_etu/img");
        this.enCours = false;
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

        this.pg = new ProgressBar();
        this.pg.setPrefWidth(100);

        //buttons

        this.boutonMaison = new Button();
        ImageView home = new ImageView(new Image("file:pendu_pour_etu/img/home.png"));
        home.setFitHeight(32);
        home.setFitWidth(32);
        this.boutonMaison.setGraphic(home);
        this.boutonMaison.setOnAction(new ControleurHome(this));

        this.boutonParametres = new Button();
        ImageView param = new ImageView(new Image("file:pendu_pour_etu/img/parametres.png"));
        param.setFitHeight(32);
        param.setFitWidth(32);
        this.boutonParametres.setGraphic(param);
        this.boutonParametres.setOnAction(new ControleurGame(this));

        this.boutonInfo = new Button();
        ImageView info = new ImageView(new Image("file:pendu_pour_etu/img/info.png"));
        info.setFitHeight(32);
        info.setFitWidth(32);
        this.boutonInfo.setGraphic(info);
        this.boutonInfo.setOnAction(new ControleurInfos(this));

        this.bJouer = new Button("Lancer une partie");
        this.bNewWord = new Button("Nouveau Mot");
        this.bNewWord.setOnAction(new ControleurNouveauMot(this));

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
        
        VBox timer = new VBox(this.chrono);
        this.chrono.setTextAlignment(TextAlignment.CENTER);
        
        timer.setStyle("fx-font-size:40px");
        VBox.setMargin(timer, new Insets(20));
        TitledPane time = new TitledPane("Chronomètre",timer);

        return time;
    }

    /**
    / * @return la fenêtre de jeu avec le mot crypté, l'image, la barre
    / *         de progression et le clavier
    / */
    private Pane fenetreJeu(){
        
        this.modelePendu.setMotATrouver(); 
        this.boutonMaison.setDisable(false);
        this.boutonInfo.setDisable(false);
        this.boutonParametres.setDisable(true);
        this.pg.setProgress(0);

        BorderPane center = new BorderPane();
        VBox content = new VBox(10);

        BorderPane.setMargin(content, new Insets(50));

        content.getChildren().addAll(this.motCrypte,this.dessin,this.pg,this.clavier);

        center.setCenter(content);
        BorderPane.setAlignment(this.pg, Pos.CENTER);

        BorderPane.setAlignment(content, Pos.CENTER);
        
        VBox right = new VBox(10);

        TitledPane time = this.leChrono();
        
        switch (this.modelePendu.getNiveau()) {
            case MotMystere.FACILE -> {
                this.leNiveau.setText("Niveau Facile");
            }
            case MotMystere.MOYEN -> {
                this.leNiveau.setText("Niveau Médium");
            }
            case MotMystere.DIFFICILE -> {
                this.leNiveau.setText("Niveau Difficile");
            }
            case MotMystere.EXPERT -> {
                this.leNiveau.setText("Niveau Expert");
            }
        
            default -> {
                this.leNiveau.setText("Niveau Facile");
            }
        }

        right.getChildren().addAll(this.leNiveau,time,this.bNewWord);

        BorderPane.setMargin(this.bNewWord, new Insets(40, 0, 0, 0));

        BorderPane.setAlignment(time, Pos.CENTER);
        BorderPane.setAlignment(this.leNiveau, Pos.CENTER);
        BorderPane.setAlignment(this.leNiveau, Pos.CENTER_LEFT);

        center.setRight(right);

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

        ControleurNiveau cNiveau = new ControleurNiveau(this.modelePendu);
        RadioButton facile = new RadioButton("Facile");
        facile.setOnAction(cNiveau);
        RadioButton medium = new RadioButton("Médium");
        medium.setOnAction(cNiveau);
        RadioButton difficile = new RadioButton("Difficile");
        difficile.setOnAction(cNiveau);
        RadioButton expert = new RadioButton("Expert");
        expert.setOnAction(cNiveau);

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
        this.stage.setScene(this.laScene());
    }
    
    public void modeJeu(){
        
        this.panelCentral = (BorderPane) this.fenetreJeu();
        this.stage.setScene(this.laScene());

    }
    
    public void modeParametres(){

    }

    /** lance une partie */
    public void lancePartie(){
        this.chrono.resetTime();
        this.changerMot();
        this.modeJeu();
        this.chrono.start();
    }

    /**
     * raffraichit l'affichage selon les données du modèle
     * this.appliPendu.getChrono().stop();
     * this.appliPendu.modeAccueil();
     */
    public void majAffichage(){
        this.motCrypte.setText(this.modelePendu.getMotCrypte());
        this.dessin.setImage(this.lesImages.get(this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()));

        this.pg.setProgress((float) (this.modelePendu.getNbErreursMax() - this.modelePendu.getNbErreursRestants()) / this.modelePendu.getNbErreursMax());
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
        alert.setContentText("Vous avez Perdu !\nLe mot était : " + this.modelePendu.getMotATrouve());
        return alert;
    }

    public boolean enCours(){
        return this.enCours;
    }

    public void switchGameState(){
        this.enCours = !this.enCours;
    }

    public void changerMot(){
        this.modelePendu.setMotATrouver();
        this.motCrypte.setText(this.modelePendu.getMotCrypte());

        for(Node btn : this.clavier.getChildren()){
            Button tmp = (Button) btn;
            tmp.setDisable(false);
        }

        this.dessin.setImage(this.lesImages.get(0));
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
