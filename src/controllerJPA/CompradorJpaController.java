/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllerJPA;

import controllerJPA.exceptions.IllegalOrphanException;
import controllerJPA.exceptions.NonexistentEntityException;
import controllerJPA.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Compra;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Comprador;

/**
 *
 * @author Garcia
 */
public class CompradorJpaController implements Serializable {

    public CompradorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Comprador comprador) throws PreexistingEntityException, Exception {
        if (comprador.getCompraList() == null) {
            comprador.setCompraList(new ArrayList<Compra>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : comprador.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getIdCompra());
                attachedCompraList.add(compraListCompraToAttach);
            }
            comprador.setCompraList(attachedCompraList);
            em.persist(comprador);
            for (Compra compraListCompra : comprador.getCompraList()) {
                Comprador oldCompradorOfCompraListCompra = compraListCompra.getComprador();
                compraListCompra.setComprador(comprador);
                compraListCompra = em.merge(compraListCompra);
                if (oldCompradorOfCompraListCompra != null) {
                    oldCompradorOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldCompradorOfCompraListCompra = em.merge(oldCompradorOfCompraListCompra);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findComprador(comprador.getCpf()) != null) {
                throw new PreexistingEntityException("Comprador " + comprador + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Comprador comprador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Comprador persistentComprador = em.find(Comprador.class, comprador.getCpf());
            List<Compra> compraListOld = persistentComprador.getCompraList();
            List<Compra> compraListNew = comprador.getCompraList();
            List<String> illegalOrphanMessages = null;
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Compra " + compraListOldCompra + " since its comprador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getIdCompra());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            comprador.setCompraList(compraListNew);
            comprador = em.merge(comprador);
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Comprador oldCompradorOfCompraListNewCompra = compraListNewCompra.getComprador();
                    compraListNewCompra.setComprador(comprador);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldCompradorOfCompraListNewCompra != null && !oldCompradorOfCompraListNewCompra.equals(comprador)) {
                        oldCompradorOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldCompradorOfCompraListNewCompra = em.merge(oldCompradorOfCompraListNewCompra);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = comprador.getCpf();
                if (findComprador(id) == null) {
                    throw new NonexistentEntityException("The comprador with id " + id + " no longer exists.");
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
            Comprador comprador;
            try {
                comprador = em.getReference(Comprador.class, id);
                comprador.getCpf();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The comprador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Compra> compraListOrphanCheck = comprador.getCompraList();
            for (Compra compraListOrphanCheckCompra : compraListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Comprador (" + comprador + ") cannot be destroyed since the Compra " + compraListOrphanCheckCompra + " in its compraList field has a non-nullable comprador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(comprador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Comprador> findCompradorEntities() {
        return findCompradorEntities(true, -1, -1);
    }

    public List<Comprador> findCompradorEntities(int maxResults, int firstResult) {
        return findCompradorEntities(false, maxResults, firstResult);
    }

    private List<Comprador> findCompradorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Comprador.class));
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

    public Comprador findComprador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Comprador.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompradorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Comprador> rt = cq.from(Comprador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
