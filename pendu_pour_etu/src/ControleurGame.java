import javafx.event.ActionEvent;
import javafx.event.EventHandler;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton param
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
     * L'action consiste à afficher la fenêtre du jeu
     * 
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        this.appliPendu.getChrono().start();
        this.appliPendu.modeJeu();
    }
}
