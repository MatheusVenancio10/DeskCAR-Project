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
@Table(name = "cor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cor.findAll", query = "SELECT c FROM Cor c"),
    @NamedQuery(name = "Cor.findByIdCor", query = "SELECT c FROM Cor c WHERE c.idCor = :idCor"),
    @NamedQuery(name = "Cor.findByNome", query = "SELECT c FROM Cor c WHERE c.nome = :nome")})
public class Cor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_cor")
    private Integer idCor;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cor")
    private List<Carro> carroList;

    public Cor() {
    }

    public Cor(Integer idCor) {
        this.idCor = idCor;
    }

    public Cor(Integer idCor, String nome) {
        this.idCor = idCor;
        this.nome = nome;
    }

    public Integer getIdCor() {
        return idCor;
    }

    public void setIdCor(Integer idCor) {
        this.idCor = idCor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
        hash += (idCor != null ? idCor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cor)) {
            return false;
        }
        Cor other = (Cor) object;
        if ((this.idCor == null && other.idCor != null) || (this.idCor != null && !this.idCor.equals(other.idCor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Cor[ idCor=" + idCor + " ]";
    }
    
}
