import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

/**
 * Controleur du clavier
 */
public class ControleurLettres implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;
    /**
     * vue du jeu
     */
    private Pendu vuePendu;

    /**
     * @param modelePendu modèle du jeu
     * @param vuePendu vue du jeu
     */
    ControleurLettres(MotMystere modelePendu, Pendu vuePendu){
        this.modelePendu = modelePendu;
        this.vuePendu = vuePendu;
    }

    /**
     * Actions à effectuer lors du clic sur une touche du clavier
     * Il faut donc: Essayer la lettre, mettre à jour l'affichage et vérifier si la partie est finie
     * @param actionEvent l'événement
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        
        Button btnSource = (Button) actionEvent.getSource();
        btnSource.setDisable(true);

        this.modelePendu.essaiLettre(btnSource.getText().charAt(0));

        this.vuePendu.majAffichage();  

        if(this.modelePendu.gagne()){
            this.vuePendu.getChrono().stop();
            this.vuePendu.popUpMessageGagne().showAndWait();
            this.vuePendu.switchGameState();
            this.vuePendu.modeAccueil();
            this.vuePendu.deactivateBtn();
        }
        if(this.modelePendu.perdu()){
            this.vuePendu.getChrono().stop();
            this.vuePendu.popUpMessagePerdu().showAndWait();
            this.vuePendu.modeAccueil();
            this.vuePendu.switchGameState();
            this.vuePendu.deactivateBtn();
        }
    }
}
