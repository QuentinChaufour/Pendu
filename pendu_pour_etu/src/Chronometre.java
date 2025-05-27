import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;


/**
 * Permet de gérer un Text associé à une Timeline pour afficher un temps écoulé
 */
public class Chronometre extends Text{
    /**
     * timeline qui va gérer le temps
     */
    private Timeline timeline;
    /**
     * la fenêtre de temps
     */
    private KeyFrame keyFrame;
    /**
     * le contrôleur associé au chronomètre
     */
    private ControleurChronometre actionTemps;

    /**
     * Constructeur permettant de créer le chronomètre
     * avec un label initialisé à "0:0:0"
     * Ce constructeur créer la Timeline, la KeyFrame et le contrôleur
     */
    public Chronometre(){
        super("0:0:0");
        this.keyFrame = new KeyFrame(new Duration(100));
        this.timeline = new Timeline(this.keyFrame);
        this.actionTemps =  new ControleurChronometre(this);
        this.timeline.setOnFinished(this.actionTemps);

    }

    /**
     * Permet au controleur de mettre à jour le text
     * la durée est affichée sous la forme m:s
     * @param tempsMillisec la durée depuis à afficher
     */
    public void setTime(long tempsMillisec){

        int minute = (int) tempsMillisec % 60000;
        int secondes = (int) (tempsMillisec - (minute*60000))%1000;

        if(minute < 1){
            this.setText(secondes + " S");
        }

        this.setText(minute+" m :"+secondes + " S");
    }

    /**
     * Permet de démarrer le chronomètre
     */
    public void start(){
        this.timeline.play();
    }

    /**
     * Permet d'arrêter le chronomètre
     */
    public void stop(){
        this.timeline.pause();
    }

    /**
     * Permet de remettre le chronomètre à 0
     */
    public void resetTime(){
        this.setText("0 S");
    }
}
