import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Contrôleur du chronomètre
 */
public class ControleurChronometre implements EventHandler<ActionEvent> {
    /**
     * temps enregistré lors du dernier événement
     */
    private long tempsPrec;
    /**
     * temps écoulé depuis le début de la mesure
     */
    private long tempsEcoule;
    /**
     * Vue du chronomètre
     */
    private Chronometre chrono;

    /**
     * Constructeur du contrôleur du chronomètre
     * noter que le modèle du chronomètre est tellement simple
     * qu'il est inclus dans le contrôleur
     * @param chrono Vue du chronomètre
     */
    public ControleurChronometre (Chronometre chrono){
        
        this.chrono = chrono;
        this.tempsPrec = -1;
        this.tempsEcoule = 0;

    }

    /**
     * Actions à effectuer tous les pas de temps
     * essentiellement mesurer le temps écoulé depuis la dernière mesure
     * et mise à jour de la durée totale
     * @param actionEvent événement Action
     */
    @Override
    public void handle(ActionEvent actionEvent) {

        long currTime = System.currentTimeMillis();

        if(this.tempsPrec != -1){
            this.tempsEcoule += currTime - this.tempsPrec; // ajout du delta entre temps actuel et temps précédent
            this.tempsPrec = currTime; // mettre a jour le temps actuel

            this.chrono.setTime(tempsEcoule);
        }
        else{
            this.tempsPrec = currTime;
        }

    }

    /**
     * Remet la durée à 0
     */
    public void reset(){
        this.tempsPrec = -1;
        this.tempsEcoule = 0;
    }
}
