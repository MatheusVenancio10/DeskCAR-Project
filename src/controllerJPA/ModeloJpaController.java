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
import model.Modelo;

/**
 *
 * @author Garcia
 */
public class ModeloJpaController implements Serializable {

    public ModeloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Modelo modelo) throws PreexistingEntityException, Exception {
        if (modelo.getCarroList() == null) {
            modelo.setCarroList(new ArrayList<Carro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Carro> attachedCarroList = new ArrayList<Carro>();
            for (Carro carroListCarroToAttach : modelo.getCarroList()) {
                carroListCarroToAttach = em.getReference(carroListCarroToAttach.getClass(), carroListCarroToAttach.getIdCarro());
                attachedCarroList.add(carroListCarroToAttach);
            }
            modelo.setCarroList(attachedCarroList);
            em.persist(modelo);
            for (Carro carroListCarro : modelo.getCarroList()) {
                Modelo oldModeloOfCarroListCarro = carroListCarro.getModelo();
                carroListCarro.setModelo(modelo);
                carroListCarro = em.merge(carroListCarro);
                if (oldModeloOfCarroListCarro != null) {
                    oldModeloOfCarroListCarro.getCarroList().remove(carroListCarro);
                    oldModeloOfCarroListCarro = em.merge(oldModeloOfCarroListCarro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findModelo(modelo.getIdModelo()) != null) {
                throw new PreexistingEntityException("Modelo " + modelo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Modelo modelo) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Modelo persistentModelo = em.find(Modelo.class, modelo.getIdModelo());
            List<Carro> carroListOld = persistentModelo.getCarroList();
            List<Carro> carroListNew = modelo.getCarroList();
            List<String> illegalOrphanMessages = null;
            for (Carro carroListOldCarro : carroListOld) {
                if (!carroListNew.contains(carroListOldCarro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carro " + carroListOldCarro + " since its modelo field is not nullable.");
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
            modelo.setCarroList(carroListNew);
            modelo = em.merge(modelo);
            for (Carro carroListNewCarro : carroListNew) {
                if (!carroListOld.contains(carroListNewCarro)) {
                    Modelo oldModeloOfCarroListNewCarro = carroListNewCarro.getModelo();
                    carroListNewCarro.setModelo(modelo);
                    carroListNewCarro = em.merge(carroListNewCarro);
                    if (oldModeloOfCarroListNewCarro != null && !oldModeloOfCarroListNewCarro.equals(modelo)) {
                        oldModeloOfCarroListNewCarro.getCarroList().remove(carroListNewCarro);
                        oldModeloOfCarroListNewCarro = em.merge(oldModeloOfCarroListNewCarro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = modelo.getIdModelo();
                if (findModelo(id) == null) {
                    throw new NonexistentEntityException("The modelo with id " + id + " no longer exists.");
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
            Modelo modelo;
            try {
                modelo = em.getReference(Modelo.class, id);
                modelo.getIdModelo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The modelo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Carro> carroListOrphanCheck = modelo.getCarroList();
            for (Carro carroListOrphanCheckCarro : carroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Modelo (" + modelo + ") cannot be destroyed since the Carro " + carroListOrphanCheckCarro + " in its carroList field has a non-nullable modelo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(modelo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Modelo> findModeloEntities() {
        return findModeloEntities(true, -1, -1);
    }

    public List<Modelo> findModeloEntities(int maxResults, int firstResult) {
        return findModeloEntities(false, maxResults, firstResult);
    }

    private List<Modelo> findModeloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Modelo.class));
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

    public Modelo findModelo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Modelo.class, id);
        } finally {
            em.close();
        }
    }

    public int getModeloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Modelo> rt = cq.from(Modelo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
