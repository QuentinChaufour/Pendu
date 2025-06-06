import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Génère la vue d'un clavier et associe le contrôleur aux touches
 * le choix ici est d'un faire un héritié d'un TilePane
 */
public class Clavier extends TilePane{
    /**
     * il est conseillé de stocker les touches dans un ArrayList
     */
    private List<Button> clavier;

    /**
     * constructeur du clavier
     * @param touches une chaine de caractères qui contient les lettres à mettre sur les touches
     * @param actionTouches le contrôleur des touches
     * @param tailleLigne nombre de touches par ligne
     */
    public Clavier(String touches, EventHandler<ActionEvent> actionTouches) {
        this.clavier = new ArrayList<>();
        for(char lettre : touches.toCharArray()){
            Button btnLettre = new Button(lettre + "");
            TilePane.setMargin(btnLettre, new Insets(5));
            btnLettre.setOnAction(actionTouches);
            btnLettre.setStyle("-fx-background-radius: 25px;-fx-padding: 7 21 7 21;");
            this.clavier.add(btnLettre);
            this.getChildren().add(btnLettre);
        }
    }

    /**
     * permet de désactiver certaines touches du clavier (et active les autres)
     * @param touchesDesactivees une chaine de caractères contenant la liste des touches désactivées
     */
    public void desactiveTouches(Set<String> touchesDesactivees){
        
        for(Button btnLettre : this.clavier){
            if(touchesDesactivees.contains(btnLettre.getText())){
                btnLettre.setDisable(true);
                btnLettre.setStyle("-fx-background-color : #878787");
            }
        }
    }
}
