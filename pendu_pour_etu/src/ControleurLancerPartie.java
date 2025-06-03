import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

import java.util.Optional;

/**
 * Contrôleur à activer lorsque l'on clique sur le bouton rejouer ou Lancer une partie
 */
public class ControleurLancerPartie implements EventHandler<ActionEvent> {
    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     **/
    private Pendu vuePendu;

    private ToggleGroup radioNiveau;

    /**
     * @param modelePendu modèle du jeu
     * @param p vue du jeu
     */
    public ControleurLancerPartie(MotMystere modelePendu, Pendu vuePendu,ToggleGroup radioNiveau) {
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
        this.radioNiveau = radioNiveau;
    }

    /**
     * L'action consiste à recommencer une partie. Il faut vérifier qu'il n'y a pas une partie en cours
     * @param actionEvent l'événement action
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        // A implémenter
        if (this.vuePendu.enCours()) {
            Optional<ButtonType> reponse = this.vuePendu.popUpPartieEnCours().showAndWait(); // on lance la fenêtre on attends la réponse si la réponse est oui
            if (reponse.isPresent() && reponse.get().equals(ButtonType.YES)) {
                this.vuePendu.lancePartie();
            }
        }
        else{
            this.vuePendu.switchGameState();
            this.vuePendu.lancePartie();
        }
        
    }
}
