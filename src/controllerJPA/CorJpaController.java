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
import model.Cor;

/**
 *
 * @author Garcia
 */
public class CorJpaController implements Serializable {

    public CorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cor cor) throws PreexistingEntityException, Exception {
        if (cor.getCarroList() == null) {
            cor.setCarroList(new ArrayList<Carro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Carro> attachedCarroList = new ArrayList<Carro>();
            for (Carro carroListCarroToAttach : cor.getCarroList()) {
                carroListCarroToAttach = em.getReference(carroListCarroToAttach.getClass(), carroListCarroToAttach.getIdCarro());
                attachedCarroList.add(carroListCarroToAttach);
            }
            cor.setCarroList(attachedCarroList);
            em.persist(cor);
            for (Carro carroListCarro : cor.getCarroList()) {
                Cor oldCorOfCarroListCarro = carroListCarro.getCor();
                carroListCarro.setCor(cor);
                carroListCarro = em.merge(carroListCarro);
                if (oldCorOfCarroListCarro != null) {
                    oldCorOfCarroListCarro.getCarroList().remove(carroListCarro);
                    oldCorOfCarroListCarro = em.merge(oldCorOfCarroListCarro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCor(cor.getIdCor()) != null) {
                throw new PreexistingEntityException("Cor " + cor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cor cor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Cor persistentCor = em.find(Cor.class, cor.getIdCor());
            List<Carro> carroListOld = persistentCor.getCarroList();
            List<Carro> carroListNew = cor.getCarroList();
            List<String> illegalOrphanMessages = null;
            for (Carro carroListOldCarro : carroListOld) {
                if (!carroListNew.contains(carroListOldCarro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carro " + carroListOldCarro + " since its cor field is not nullable.");
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
            cor.setCarroList(carroListNew);
            cor = em.merge(cor);
            for (Carro carroListNewCarro : carroListNew) {
                if (!carroListOld.contains(carroListNewCarro)) {
                    Cor oldCorOfCarroListNewCarro = carroListNewCarro.getCor();
                    carroListNewCarro.setCor(cor);
                    carroListNewCarro = em.merge(carroListNewCarro);
                    if (oldCorOfCarroListNewCarro != null && !oldCorOfCarroListNewCarro.equals(cor)) {
                        oldCorOfCarroListNewCarro.getCarroList().remove(carroListNewCarro);
                        oldCorOfCarroListNewCarro = em.merge(oldCorOfCarroListNewCarro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cor.getIdCor();
                if (findCor(id) == null) {
                    throw new NonexistentEntityException("The cor with id " + id + " no longer exists.");
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
            Cor cor;
            try {
                cor = em.getReference(Cor.class, id);
                cor.getIdCor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Carro> carroListOrphanCheck = cor.getCarroList();
            for (Carro carroListOrphanCheckCarro : carroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cor (" + cor + ") cannot be destroyed since the Carro " + carroListOrphanCheckCarro + " in its carroList field has a non-nullable cor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(cor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Cor> findCorEntities() {
        return findCorEntities(true, -1, -1);
    }

    public List<Cor> findCorEntities(int maxResults, int firstResult) {
        return findCorEntities(false, maxResults, firstResult);
    }

    private List<Cor> findCorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cor.class));
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

    public Cor findCor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cor.class, id);
        } finally {
            em.close();
        }
    }

    public int getCorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cor> rt = cq.from(Cor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
