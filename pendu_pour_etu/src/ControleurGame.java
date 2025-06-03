import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton info
 */
public class ControleurGame implements EventHandler<ActionEvent> {

    private Pendu appliPendu;

    /**
     * @param appliPendu vue du jeu
     */
    public ControleurGame(Pendu appliPendu) {
        this.appliPendu = appliPendu;
    }

    /**
     * L'action consiste à afficher une fenêtre popup précisant les règles du jeu.
     * 
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        this.appliPendu.getChrono().start();
        this.appliPendu.modeJeu();
    }
}
