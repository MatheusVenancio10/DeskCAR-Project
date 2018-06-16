/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllerJPA;

import controllerJPA.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Comprador;
import model.Carro;
import model.Compra;

/**
 *
 * @author Garcia
 */
public class CompraJpaController implements Serializable {

    public CompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compra compra) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comprador comprador = compra.getComprador();
            if (comprador != null) {
                comprador = em.getReference(comprador.getClass(), comprador.getCpf());
                compra.setComprador(comprador);
            }
            Carro carro = compra.getCarro();
            if (carro != null) {
                carro = em.getReference(carro.getClass(), carro.getIdCarro());
                compra.setCarro(carro);
            }
            em.persist(compra);
            if (comprador != null) {
                comprador.getCompraList().add(compra);
                comprador = em.merge(comprador);
            }
            if (carro != null) {
                carro.getCompraList().add(compra);
                carro = em.merge(carro);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compra compra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra persistentCompra = em.find(Compra.class, compra.getIdCompra());
            Comprador compradorOld = persistentCompra.getComprador();
            Comprador compradorNew = compra.getComprador();
            Carro carroOld = persistentCompra.getCarro();
            Carro carroNew = compra.getCarro();
            if (compradorNew != null) {
                compradorNew = em.getReference(compradorNew.getClass(), compradorNew.getCpf());
                compra.setComprador(compradorNew);
            }
            if (carroNew != null) {
                carroNew = em.getReference(carroNew.getClass(), carroNew.getIdCarro());
                compra.setCarro(carroNew);
            }
            compra = em.merge(compra);
            if (compradorOld != null && !compradorOld.equals(compradorNew)) {
                compradorOld.getCompraList().remove(compra);
                compradorOld = em.merge(compradorOld);
            }
            if (compradorNew != null && !compradorNew.equals(compradorOld)) {
                compradorNew.getCompraList().add(compra);
                compradorNew = em.merge(compradorNew);
            }
            if (carroOld != null && !carroOld.equals(carroNew)) {
                carroOld.getCompraList().remove(compra);
                carroOld = em.merge(carroOld);
            }
            if (carroNew != null && !carroNew.equals(carroOld)) {
                carroNew.getCompraList().add(compra);
                carroNew = em.merge(carroNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = compra.getIdCompra();
                if (findCompra(id) == null) {
                    throw new NonexistentEntityException("The compra with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra compra;
            try {
                compra = em.getReference(Compra.class, id);
                compra.getIdCompra();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compra with id " + id + " no longer exists.", enfe);
            }
            Comprador comprador = compra.getComprador();
            if (comprador != null) {
                comprador.getCompraList().remove(compra);
                comprador = em.merge(comprador);
            }
            Carro carro = compra.getCarro();
            if (carro != null) {
                carro.getCompraList().remove(compra);
                carro = em.merge(carro);
            }
            em.remove(compra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compra> findCompraEntities() {
        return findCompraEntities(true, -1, -1);
    }

    public List<Compra> findCompraEntities(int maxResults, int firstResult) {
        return findCompraEntities(false, maxResults, firstResult);
    }

    private List<Compra> findCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compra.class));
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

    public Compra findCompra(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compra.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compra> rt = cq.from(Compra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
