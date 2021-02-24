package controleur;

import java.util.List;

import modele.Activite;

public class ControleurHistorique {

    private List<Activite> listeActivite;


    public List<Activite> getListeActivite() {
        return listeActivite;
    }

    public void setListeActivite(List<Activite> listeActivite) {
        this.listeActivite = listeActivite;
    }
}
