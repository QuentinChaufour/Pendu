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

    private int dureeSeconde;

    /**
     * Constructeur permettant de créer le chronomètre
     * avec un label initialisé à "0:0:0"
     * Ce constructeur créer la Timeline, la KeyFrame et le contrôleur
     */
    public Chronometre(){
        super("");
        this.actionTemps = new ControleurChronometre(this);
        this.keyFrame = new KeyFrame(new Duration(100),this.actionTemps);
        this.timeline = new Timeline(this.keyFrame);
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.dureeSeconde = 120;
    }

    /**
     * Permet au controleur de mettre à jour le text
     * la durée est affichée sous la forme m:s
     * @param tempsMillisec la durée depuis à afficher
     */
    public void setTime(long tempsMillisec){

        long remainingTime = (this.dureeSeconde * 1000) - tempsMillisec;

        int minute = (int) remainingTime / (60 * 1000);
        int secondes = (int) (remainingTime - (minute * 60000)) / 1000;

        if (minute == 0) {
            this.setText(secondes + " S");
        } else {
            this.setText(minute + " min " + secondes + " S");
        }
  
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

        if(this.dureeSeconde > 60){
            int min = (int) dureeSeconde / 60;
            int sec = this.dureeSeconde - min*60;
            this.setText(min + " min " + sec + " S");
        }
        else{
            this.setText(this.dureeSeconde + "S");
        }

        this.actionTemps.reset();
    }
}
