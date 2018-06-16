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
@Table(name = "motor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Motor.findAll", query = "SELECT m FROM Motor m"),
    @NamedQuery(name = "Motor.findByIdMotor", query = "SELECT m FROM Motor m WHERE m.idMotor = :idMotor"),
    @NamedQuery(name = "Motor.findByPotencia", query = "SELECT m FROM Motor m WHERE m.potencia = :potencia")})
public class Motor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_motor")
    private Integer idMotor;
    @Basic(optional = false)
    @Column(name = "potencia")
    private String potencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "motor")
    private List<Carro> carroList;

    public Motor() {
    }

    public Motor(Integer idMotor) {
        this.idMotor = idMotor;
    }

    public Motor(Integer idMotor, String potencia) {
        this.idMotor = idMotor;
        this.potencia = potencia;
    }

    public Integer getIdMotor() {
        return idMotor;
    }

    public void setIdMotor(Integer idMotor) {
        this.idMotor = idMotor;
    }

    public String getPotencia() {
        return potencia;
    }

    public void setPotencia(String potencia) {
        this.potencia = potencia;
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
        hash += (idMotor != null ? idMotor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Motor)) {
            return false;
        }
        Motor other = (Motor) object;
        if ((this.idMotor == null && other.idMotor != null) || (this.idMotor != null && !this.idMotor.equals(other.idMotor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Motor[ idMotor=" + idMotor + " ]";
    }
    
}
