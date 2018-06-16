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
import model.Carro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Combustivel;

/**
 *
 * @author Garcia
 */
public class CombustivelJpaController implements Serializable {

    public CombustivelJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Combustivel combustivel) throws PreexistingEntityException, Exception {
        if (combustivel.getCarroList() == null) {
            combustivel.setCarroList(new ArrayList<Carro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Carro> attachedCarroList = new ArrayList<Carro>();
            for (Carro carroListCarroToAttach : combustivel.getCarroList()) {
                carroListCarroToAttach = em.getReference(carroListCarroToAttach.getClass(), carroListCarroToAttach.getIdCarro());
                attachedCarroList.add(carroListCarroToAttach);
            }
            combustivel.setCarroList(attachedCarroList);
            em.persist(combustivel);
            for (Carro carroListCarro : combustivel.getCarroList()) {
                Combustivel oldCombustivelOfCarroListCarro = carroListCarro.getCombustivel();
                carroListCarro.setCombustivel(combustivel);
                carroListCarro = em.merge(carroListCarro);
                if (oldCombustivelOfCarroListCarro != null) {
                    oldCombustivelOfCarroListCarro.getCarroList().remove(carroListCarro);
                    oldCombustivelOfCarroListCarro = em.merge(oldCombustivelOfCarroListCarro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCombustivel(combustivel.getIdCombustivel()) != null) {
                throw new PreexistingEntityException("Combustivel " + combustivel + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Combustivel combustivel) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Combustivel persistentCombustivel = em.find(Combustivel.class, combustivel.getIdCombustivel());
            List<Carro> carroListOld = persistentCombustivel.getCarroList();
            List<Carro> carroListNew = combustivel.getCarroList();
            List<String> illegalOrphanMessages = null;
            for (Carro carroListOldCarro : carroListOld) {
                if (!carroListNew.contains(carroListOldCarro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carro " + carroListOldCarro + " since its combustivel field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Carro> attachedCarroListNew = new ArrayList<Carro>();
            for (Carro carroListNewCarroToAttach : carroListNew) {
                carroListNewCarroToAttach = em.getReference(carroListNewCarroToAttach.getClass(), carroListNewCarroToAttach.getIdCarro());
                attachedCarroListNew.add(carroListNewCarroToAttach);
            }
            carroListNew = attachedCarroListNew;
            combustivel.setCarroList(carroListNew);
            combustivel = em.merge(combustivel);
            for (Carro carroListNewCarro : carroListNew) {
                if (!carroListOld.contains(carroListNewCarro)) {
                    Combustivel oldCombustivelOfCarroListNewCarro = carroListNewCarro.getCombustivel();
                    carroListNewCarro.setCombustivel(combustivel);
                    carroListNewCarro = em.merge(carroListNewCarro);
                    if (oldCombustivelOfCarroListNewCarro != null && !oldCombustivelOfCarroListNewCarro.equals(combustivel)) {
                        oldCombustivelOfCarroListNewCarro.getCarroList().remove(carroListNewCarro);
                        oldCombustivelOfCarroListNewCarro = em.merge(oldCombustivelOfCarroListNewCarro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = combustivel.getIdCombustivel();
                if (findCombustivel(id) == null) {
                    throw new NonexistentEntityException("The combustivel with id " + id + " no longer exists.");
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
            Combustivel combustivel;
            try {
                combustivel = em.getReference(Combustivel.class, id);
                combustivel.getIdCombustivel();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The combustivel with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Carro> carroListOrphanCheck = combustivel.getCarroList();
            for (Carro carroListOrphanCheckCarro : carroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Combustivel (" + combustivel + ") cannot be destroyed since the Carro " + carroListOrphanCheckCarro + " in its carroList field has a non-nullable combustivel field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(combustivel);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Combustivel> findCombustivelEntities() {
        return findCombustivelEntities(true, -1, -1);
    }

    public List<Combustivel> findCombustivelEntities(int maxResults, int firstResult) {
        return findCombustivelEntities(false, maxResults, firstResult);
    }

    private List<Combustivel> findCombustivelEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Combustivel.class));
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

    public Combustivel findCombustivel(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Combustivel.class, id);
        } finally {
            em.close();
        }
    }

    public int getCombustivelCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Combustivel> rt = cq.from(Combustivel.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
