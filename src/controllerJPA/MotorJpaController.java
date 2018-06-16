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
import model.Motor;

/**
 *
 * @author Garcia
 */
public class MotorJpaController implements Serializable {

    public MotorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Motor motor) throws PreexistingEntityException, Exception {
        if (motor.getCarroList() == null) {
            motor.setCarroList(new ArrayList<Carro>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Carro> attachedCarroList = new ArrayList<Carro>();
            for (Carro carroListCarroToAttach : motor.getCarroList()) {
                carroListCarroToAttach = em.getReference(carroListCarroToAttach.getClass(), carroListCarroToAttach.getIdCarro());
                attachedCarroList.add(carroListCarroToAttach);
            }
            motor.setCarroList(attachedCarroList);
            em.persist(motor);
            for (Carro carroListCarro : motor.getCarroList()) {
                Motor oldMotorOfCarroListCarro = carroListCarro.getMotor();
                carroListCarro.setMotor(motor);
                carroListCarro = em.merge(carroListCarro);
                if (oldMotorOfCarroListCarro != null) {
                    oldMotorOfCarroListCarro.getCarroList().remove(carroListCarro);
                    oldMotorOfCarroListCarro = em.merge(oldMotorOfCarroListCarro);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMotor(motor.getIdMotor()) != null) {
                throw new PreexistingEntityException("Motor " + motor + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Motor motor) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Motor persistentMotor = em.find(Motor.class, motor.getIdMotor());
            List<Carro> carroListOld = persistentMotor.getCarroList();
            List<Carro> carroListNew = motor.getCarroList();
            List<String> illegalOrphanMessages = null;
            for (Carro carroListOldCarro : carroListOld) {
                if (!carroListNew.contains(carroListOldCarro)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Carro " + carroListOldCarro + " since its motor field is not nullable.");
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
            motor.setCarroList(carroListNew);
            motor = em.merge(motor);
            for (Carro carroListNewCarro : carroListNew) {
                if (!carroListOld.contains(carroListNewCarro)) {
                    Motor oldMotorOfCarroListNewCarro = carroListNewCarro.getMotor();
                    carroListNewCarro.setMotor(motor);
                    carroListNewCarro = em.merge(carroListNewCarro);
                    if (oldMotorOfCarroListNewCarro != null && !oldMotorOfCarroListNewCarro.equals(motor)) {
                        oldMotorOfCarroListNewCarro.getCarroList().remove(carroListNewCarro);
                        oldMotorOfCarroListNewCarro = em.merge(oldMotorOfCarroListNewCarro);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = motor.getIdMotor();
                if (findMotor(id) == null) {
                    throw new NonexistentEntityException("The motor with id " + id + " no longer exists.");
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
            Motor motor;
            try {
                motor = em.getReference(Motor.class, id);
                motor.getIdMotor();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The motor with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Carro> carroListOrphanCheck = motor.getCarroList();
            for (Carro carroListOrphanCheckCarro : carroListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Motor (" + motor + ") cannot be destroyed since the Carro " + carroListOrphanCheckCarro + " in its carroList field has a non-nullable motor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(motor);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Motor> findMotorEntities() {
        return findMotorEntities(true, -1, -1);
    }

    public List<Motor> findMotorEntities(int maxResults, int firstResult) {
        return findMotorEntities(false, maxResults, firstResult);
    }

    private List<Motor> findMotorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Motor.class));
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

    public Motor findMotor(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Motor.class, id);
        } finally {
            em.close();
        }
    }

    public int getMotorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Motor> rt = cq.from(Motor.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
