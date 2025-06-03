import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;

/**
 * Controleur des radio boutons gérant le niveau
 */
public class ControleurNiveau implements EventHandler<ActionEvent> {

    /**
     * modèle du jeu
     */
    private MotMystere modelePendu;


    /**
     * @param modelePendu modèle du jeu
     */
    public ControleurNiveau(MotMystere modelePendu) {
        this.modelePendu = modelePendu;

    }

    /**
     * gère le changement de niveau
     * @param actionEvent
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        
        RadioButton radiobouton = (RadioButton) actionEvent.getTarget();
        String nomDuRadiobouton = radiobouton.getText();

        switch (nomDuRadiobouton) {
            case "Facile" -> {
                this.modelePendu.setNiveau(MotMystere.FACILE);
            }
            case "Médium" -> {
                this.modelePendu.setNiveau(MotMystere.MOYEN);
            }
            case "Difficile" -> {
                this.modelePendu.setNiveau(MotMystere.DIFFICILE);
            }
            case "Expert" -> {
                this.modelePendu.setNiveau(MotMystere.EXPERT);
            }
            
            default -> {
                this.modelePendu.setNiveau(MotMystere.FACILE);
            }
        }
        System.out.println(nomDuRadiobouton);
    }
}
