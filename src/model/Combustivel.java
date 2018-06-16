/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Garcia
 */
@Entity
@Table(name = "combustivel")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Combustivel.findAll", query = "SELECT c FROM Combustivel c"),
    @NamedQuery(name = "Combustivel.findByIdCombustivel", query = "SELECT c FROM Combustivel c WHERE c.idCombustivel = :idCombustivel"),
    @NamedQuery(name = "Combustivel.findByTipo", query = "SELECT c FROM Combustivel c WHERE c.tipo = :tipo")})
public class Combustivel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_combustivel")
    private Integer idCombustivel;
    @Basic(optional = false)
    @Column(name = "tipo")
    private String tipo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "combustivel")
    private List<Carro> carroList;

    public Combustivel() {
    }

    public Combustivel(Integer idCombustivel) {
        this.idCombustivel = idCombustivel;
    }

    public Combustivel(Integer idCombustivel, String tipo) {
        this.idCombustivel = idCombustivel;
        this.tipo = tipo;
    }

    public Integer getIdCombustivel() {
        return idCombustivel;
    }

    public void setIdCombustivel(Integer idCombustivel) {
        this.idCombustivel = idCombustivel;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    public List<Carro> getCarroList() {
        return carroList;
    }

    public void setCarroList(List<Carro> carroList) {
        this.carroList = carroList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCombustivel != null ? idCombustivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Combustivel)) {
            return false;
        }
        Combustivel other = (Combustivel) object;
        if ((this.idCombustivel == null && other.idCombustivel != null) || (this.idCombustivel != null && !this.idCombustivel.equals(other.idCombustivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Combustivel[ idCombustivel=" + idCombustivel + " ]";
    }
    
}
