package it.casadidavide.casadidavide.model;

import jakarta.persistence.*;

@Entity
public class Campagna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campagna")
    private Long id;
    @Column(name="descrizione_campagna")
    private String descrizioneCampagna;
    
    
    // Getters e Setters...

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescrizioneCampagna() {
		return descrizioneCampagna;
	}
	public void setDescrizioneCampagna(String descrizioneCampagna) {
		this.descrizioneCampagna = descrizioneCampagna;
	}

}

