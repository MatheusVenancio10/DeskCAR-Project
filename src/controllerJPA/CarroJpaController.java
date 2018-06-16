/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllerJPA;

import controllerJPA.exceptions.IllegalOrphanException;
import controllerJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Combustivel;
import model.Motor;
import model.Atributo;
import model.Cor;
import model.Modelo;
import model.Compra;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Carro;

/**
 *
 * @author Garcia
 */
public class CarroJpaController implements Serializable {

    public CarroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Carro carro) {
        if (carro.getCompraList() == null) {
            carro.setCompraList(new ArrayList<Compra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Combustivel combustivel = carro.getCombustivel();
            if (combustivel != null) {
                combustivel = em.getReference(combustivel.getClass(), combustivel.getIdCombustivel());
                carro.setCombustivel(combustivel);
            }
            Motor motor = carro.getMotor();
            if (motor != null) {
                motor = em.getReference(motor.getClass(), motor.getIdMotor());
                carro.setMotor(motor);
            }
            Atributo atributo = carro.getAtributo();
            if (atributo != null) {
                atributo = em.getReference(atributo.getClass(), atributo.getIdAtributo());
                carro.setAtributo(atributo);
            }
            Cor cor = carro.getCor();
            if (cor != null) {
                cor = em.getReference(cor.getClass(), cor.getIdCor());
                carro.setCor(cor);
            }
            Modelo modelo = carro.getModelo();
            if (modelo != null) {
                modelo = em.getReference(modelo.getClass(), modelo.getIdModelo());
                carro.setModelo(modelo);
            }
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : carro.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getIdCompra());
                attachedCompraList.add(compraListCompraToAttach);
            }
            carro.setCompraList(attachedCompraList);
            em.persist(carro);
            if (combustivel != null) {
                combustivel.getCarroList().add(carro);
                combustivel = em.merge(combustivel);
            }
            if (motor != null) {
                motor.getCarroList().add(carro);
                motor = em.merge(motor);
            }
            if (atributo != null) {
                atributo.getCarroList().add(carro);
                atributo = em.merge(atributo);
            }
            if (cor != null) {
                cor.getCarroList().add(carro);
                cor = em.merge(cor);
            }
            if (modelo != null) {
                modelo.getCarroList().add(carro);
                modelo = em.merge(modelo);
            }
            for (Compra compraListCompra : carro.getCompraList()) {
                Carro oldCarroOfCompraListCompra = compraListCompra.getCarro();
                compraListCompra.setCarro(carro);
                compraListCompra = em.merge(compraListCompra);
                if (oldCarroOfCompraListCompra != null) {
                    oldCarroOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldCarroOfCompraListCompra = em.merge(oldCarroOfCompraListCompra);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Carro carro) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carro persistentCarro = em.find(Carro.class, carro.getIdCarro());
            Combustivel combustivelOld = persistentCarro.getCombustivel();
            Combustivel combustivelNew = carro.getCombustivel();
            Motor motorOld = persistentCarro.getMotor();
            Motor motorNew = carro.getMotor();
            Atributo atributoOld = persistentCarro.getAtributo();
            Atributo atributoNew = carro.getAtributo();
            Cor corOld = persistentCarro.getCor();
            Cor corNew = carro.getCor();
            Modelo modeloOld = persistentCarro.getModelo();
            Modelo modeloNew = carro.getModelo();
            List<Compra> compraListOld = persistentCarro.getCompraList();
            List<Compra> compraListNew = carro.getCompraList();
            List<String> illegalOrphanMessages = null;
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraListOldCompra + " since its carro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (combustivelNew != null) {
                combustivelNew = em.getReference(combustivelNew.getClass(), combustivelNew.getIdCombustivel());
                carro.setCombustivel(combustivelNew);
            }
            if (motorNew != null) {
                motorNew = em.getReference(motorNew.getClass(), motorNew.getIdMotor());
                carro.setMotor(motorNew);
            }
            if (atributoNew != null) {
                atributoNew = em.getReference(atributoNew.getClass(), atributoNew.getIdAtributo());
                carro.setAtributo(atributoNew);
            }
            if (corNew != null) {
                corNew = em.getReference(corNew.getClass(), corNew.getIdCor());
                carro.setCor(corNew);
            }
            if (modeloNew != null) {
                modeloNew = em.getReference(modeloNew.getClass(), modeloNew.getIdModelo());
                carro.setModelo(modeloNew);
            }
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getIdCompra());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            carro.setCompraList(compraListNew);
            carro = em.merge(carro);
            if (combustivelOld != null && !combustivelOld.equals(combustivelNew)) {
                combustivelOld.getCarroList().remove(carro);
                combustivelOld = em.merge(combustivelOld);
            }
            if (combustivelNew != null && !combustivelNew.equals(combustivelOld)) {
                combustivelNew.getCarroList().add(carro);
                combustivelNew = em.merge(combustivelNew);
            }
            if (motorOld != null && !motorOld.equals(motorNew)) {
                motorOld.getCarroList().remove(carro);
                motorOld = em.merge(motorOld);
            }
            if (motorNew != null && !motorNew.equals(motorOld)) {
                motorNew.getCarroList().add(carro);
                motorNew = em.merge(motorNew);
            }
            if (atributoOld != null && !atributoOld.equals(atributoNew)) {
                atributoOld.getCarroList().remove(carro);
                atributoOld = em.merge(atributoOld);
            }
            if (atributoNew != null && !atributoNew.equals(atributoOld)) {
                atributoNew.getCarroList().add(carro);
                atributoNew = em.merge(atributoNew);
            }
            if (corOld != null && !corOld.equals(corNew)) {
                corOld.getCarroList().remove(carro);
                corOld = em.merge(corOld);
            }
            if (corNew != null && !corNew.equals(corOld)) {
                corNew.getCarroList().add(carro);
                corNew = em.merge(corNew);
            }
            if (modeloOld != null && !modeloOld.equals(modeloNew)) {
                modeloOld.getCarroList().remove(carro);
                modeloOld = em.merge(modeloOld);
            }
            if (modeloNew != null && !modeloNew.equals(modeloOld)) {
                modeloNew.getCarroList().add(carro);
                modeloNew = em.merge(modeloNew);
            }
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Carro oldCarroOfCompraListNewCompra = compraListNewCompra.getCarro();
                    compraListNewCompra.setCarro(carro);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldCarroOfCompraListNewCompra != null && !oldCarroOfCompraListNewCompra.equals(carro)) {
                        oldCarroOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldCarroOfCompraListNewCompra = em.merge(oldCarroOfCompraListNewCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = carro.getIdCarro();
                if (findCarro(id) == null) {
                    throw new NonexistentEntityException("The carro with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Carro carro;
            try {
                carro = em.getReference(Carro.class, id);
                carro.getIdCarro();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The carro with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Compra> compraListOrphanCheck = carro.getCompraList();
            for (Compra compraListOrphanCheckCompra : compraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Carro (" + carro + ") cannot be destroyed since the Compra " + compraListOrphanCheckCompra + " in its compraList field has a non-nullable carro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Combustivel combustivel = carro.getCombustivel();
            if (combustivel != null) {
                combustivel.getCarroList().remove(carro);
                combustivel = em.merge(combustivel);
            }
            Motor motor = carro.getMotor();
            if (motor != null) {
                motor.getCarroList().remove(carro);
                motor = em.merge(motor);
            }
            Atributo atributo = carro.getAtributo();
            if (atributo != null) {
                atributo.getCarroList().remove(carro);
                atributo = em.merge(atributo);
            }
            Cor cor = carro.getCor();
            if (cor != null) {
                cor.getCarroList().remove(carro);
                cor = em.merge(cor);
            }
            Modelo modelo = carro.getModelo();
            if (modelo != null) {
                modelo.getCarroList().remove(carro);
                modelo = em.merge(modelo);
            }
            em.remove(carro);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Carro> findCarroEntities() {
        return findCarroEntities(true, -1, -1);
    }

    public List<Carro> findCarroEntities(int maxResults, int firstResult) {
        return findCarroEntities(false, maxResults, firstResult);
    }

    private List<Carro> findCarroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Carro.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Carro findCarro(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Carro.class, id);
        } finally {
            em.close();
        }
    }

    public int getCarroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Carro> rt = cq.from(Carro.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
