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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "carro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Carro.findAll", query = "SELECT c FROM Carro c"),
    @NamedQuery(name = "Carro.findByIdCarro", query = "SELECT c FROM Carro c WHERE c.idCarro = :idCarro"),
    @NamedQuery(name = "Carro.findByRoda", query = "SELECT c FROM Carro c WHERE c.roda = :roda"),
    @NamedQuery(name = "Carro.findByDisponibilidade", query = "SELECT c FROM Carro c WHERE c.disponibilidade = :disponibilidade"),
    @NamedQuery(name = "Carro.findByValor", query = "SELECT c FROM Carro c WHERE c.valor = :valor")})
public class Carro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_carro")
    private Integer idCarro;
    @Basic(optional = false)
    @Column(name = "roda")
    private int roda;
    @Basic(optional = false)
    @Column(name = "disponibilidade")
    private int disponibilidade;
    @Basic(optional = false)
    @Column(name = "valor")
    private String valor;
    @JoinColumn(name = "combustivel", referencedColumnName = "id_combustivel")
    @ManyToOne(optional = false)
    private Combustivel combustivel;
    @JoinColumn(name = "motor", referencedColumnName = "id_motor")
    @ManyToOne(optional = false)
    private Motor motor;
    @JoinColumn(name = "atributo", referencedColumnName = "id_atributo")
    @ManyToOne(optional = false)
    private Atributo atributo;
    @JoinColumn(name = "cor", referencedColumnName = "id_cor")
    @ManyToOne(optional = false)
    private Cor cor;
    @JoinColumn(name = "modelo", referencedColumnName = "id_modelo")
    @ManyToOne(optional = false)
    private Modelo modelo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "carro")
    private List<Compra> compraList;

    public Carro() {
    }

    public Carro(Integer idCarro) {
        this.idCarro = idCarro;
    }

    public Carro(Integer idCarro, int roda, int disponibilidade, String valor) {
        this.idCarro = idCarro;
        this.roda = roda;
        this.disponibilidade = disponibilidade;
        this.valor = valor;
    }

    public Integer getIdCarro() {
        return idCarro;
    }

    public void setIdCarro(Integer idCarro) {
        this.idCarro = idCarro;
    }

    public int getRoda() {
        return roda;
    }

    public void setRoda(int roda) {
        this.roda = roda;
    }

    public int getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(int disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Combustivel getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(Combustivel combustivel) {
        this.combustivel = combustivel;
    }

    public Motor getMotor() {
        return motor;
    }

    public void setMotor(Motor motor) {
        this.motor = motor;
    }

    public Atributo getAtributo() {
        return atributo;
    }

    public void setAtributo(Atributo atributo) {
        this.atributo = atributo;
    }

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    @XmlTransient
    public List<Compra> getCompraList() {
        return compraList;
    }

    public void setCompraList(List<Compra> compraList) {
        this.compraList = compraList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCarro != null ? idCarro.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Carro)) {
            return false;
        }
        Carro other = (Carro) object;
        if ((this.idCarro == null && other.idCarro != null) || (this.idCarro != null && !this.idCarro.equals(other.idCarro))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Carro[ idCarro=" + idCarro + " ]";
    }
    
}
