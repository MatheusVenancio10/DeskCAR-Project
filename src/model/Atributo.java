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
@Table(name = "atributo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Atributo.findAll", query = "SELECT a FROM Atributo a"),
    @NamedQuery(name = "Atributo.findByIdAtributo", query = "SELECT a FROM Atributo a WHERE a.idAtributo = :idAtributo"),
    @NamedQuery(name = "Atributo.findByNome", query = "SELECT a FROM Atributo a WHERE a.nome = :nome"),
    @NamedQuery(name = "Atributo.findByDescri\u00e7\u00e3o", query = "SELECT a FROM Atributo a WHERE a.descri\u00e7\u00e3o = :descri\u00e7\u00e3o")})
public class Atributo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_atributo")
    private Integer idAtributo;
    @Basic(optional = false)
    @Column(name = "nome")
    private String nome;
    @Basic(optional = false)
    @Column(name = "descri\u00e7\u00e3o")
    private String descrição;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "atributo")
    private List<Carro> carroList;

    public Atributo() {
    }

    public Atributo(Integer idAtributo) {
        this.idAtributo = idAtributo;
    }

    public Atributo(Integer idAtributo, String nome, String descrição) {
        this.idAtributo = idAtributo;
        this.nome = nome;
        this.descrição = descrição;
    }

    public Integer getIdAtributo() {
        return idAtributo;
    }

    public void setIdAtributo(Integer idAtributo) {
        this.idAtributo = idAtributo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrição() {
        return descrição;
    }

    public void setDescrição(String descrição) {
        this.descrição = descrição;
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
        hash += (idAtributo != null ? idAtributo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Atributo)) {
            return false;
        }
        Atributo other = (Atributo) object;
        if ((this.idAtributo == null && other.idAtributo != null) || (this.idAtributo != null && !this.idAtributo.equals(other.idAtributo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Atributo[ idAtributo=" + idAtributo + " ]";
    }
    
}
