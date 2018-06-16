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
import model.Atributo;

/**
 *
 * @author Garcia
 */
public class AtributoJpaController implements Serializable {

    public AtributoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Atributo atributo) throws PreexistingEntityException, Exception {
        if (atributo.getCarroList() == null) {
            atributo.setCarroList(new ArrayList<Carro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Carro> attachedCarroList = new ArrayList<Carro>();
            for (Carro carroListCarroToAttach : atributo.getCarroList()) {
                carroListCarroToAttach = em.getReference(carroListCarroToAttach.getClass(), carroListCarroToAttach.getIdCarro());
                attachedCarroList.add(carroListCarroToAttach);
            }
            atributo.setCarroList(attachedCarroList);
            em.persist(atributo);
            for (Carro carroListCarro : atributo.getCarroList()) {
                Atributo oldAtributoOfCarroListCarro = carroListCarro.getAtributo();
                carroListCarro.setAtributo(atributo);
                carroListCarro = em.merge(carroListCarro);
                if (oldAtributoOfCarroListCarro != null) {
                    oldAtributoOfCarroListCarro.getCarroList().remove(carroListCarro);
                    oldAtributoOfCarroListCarro = em.merge(oldAtributoOfCarroListCarro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAtributo(atributo.getIdAtributo()) != null) {
                throw new PreexistingEntityException("Atributo " + atributo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Atributo atributo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Atributo persistentAtributo = em.find(Atributo.class, atributo.getIdAtributo());
            List<Carro> carroListOld = persistentAtributo.getCarroList();
            List<Carro> carroListNew = atributo.getCarroList();
            List<String> illegalOrphanMessages = null;
            for (Carro carroListOldCarro : carroListOld) {
                if (!carroListNew.contains(carroListOldCarro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carro " + carroListOldCarro + " since its atributo field is not nullable.");
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
            atributo.setCarroList(carroListNew);
            atributo = em.merge(atributo);
            for (Carro carroListNewCarro : carroListNew) {
                if (!carroListOld.contains(carroListNewCarro)) {
                    Atributo oldAtributoOfCarroListNewCarro = carroListNewCarro.getAtributo();
                    carroListNewCarro.setAtributo(atributo);
                    carroListNewCarro = em.merge(carroListNewCarro);
                    if (oldAtributoOfCarroListNewCarro != null && !oldAtributoOfCarroListNewCarro.equals(atributo)) {
                        oldAtributoOfCarroListNewCarro.getCarroList().remove(carroListNewCarro);
                        oldAtributoOfCarroListNewCarro = em.merge(oldAtributoOfCarroListNewCarro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = atributo.getIdAtributo();
                if (findAtributo(id) == null) {
                    throw new NonexistentEntityException("The atributo with id " + id + " no longer exists.");
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
            Atributo atributo;
            try {
                atributo = em.getReference(Atributo.class, id);
                atributo.getIdAtributo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atributo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Carro> carroListOrphanCheck = atributo.getCarroList();
            for (Carro carroListOrphanCheckCarro : carroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Atributo (" + atributo + ") cannot be destroyed since the Carro " + carroListOrphanCheckCarro + " in its carroList field has a non-nullable atributo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(atributo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Atributo> findAtributoEntities() {
        return findAtributoEntities(true, -1, -1);
    }

    public List<Atributo> findAtributoEntities(int maxResults, int firstResult) {
        return findAtributoEntities(false, maxResults, firstResult);
    }

    private List<Atributo> findAtributoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Atributo.class));
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

    public Atributo findAtributo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Atributo.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtributoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Atributo> rt = cq.from(Atributo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
