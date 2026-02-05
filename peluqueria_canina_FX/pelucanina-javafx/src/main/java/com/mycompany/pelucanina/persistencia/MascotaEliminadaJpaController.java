package com.mycompany.pelucanina.persistencia;

import com.mycompany.pelucanina.logica.MascotaEliminada;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

public class MascotaEliminadaJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public MascotaEliminadaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public MascotaEliminadaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("PeluCaninaPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MascotaEliminada mascotaEliminada) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(mascotaEliminada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MascotaEliminada mascotaEliminada) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            mascotaEliminada = em.merge(mascotaEliminada);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Long id = mascotaEliminada.getId();
            if (findMascotaEliminada(id) == null) {
                throw new Exception("La mascota eliminada con id " + id + " ya no existe.");
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MascotaEliminada mascotaEliminada;
            try {
                mascotaEliminada = em.getReference(MascotaEliminada.class, id);
                mascotaEliminada.getId();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La mascota eliminada con id " + id + " ya no existe.", enfe);
            }
            em.remove(mascotaEliminada);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MascotaEliminada> findMascotaEliminadaEntities() {
        return findMascotaEliminadaEntities(true, -1, -1);
    }

    public List<MascotaEliminada> findMascotaEliminadaEntities(int maxResults, int firstResult) {
        return findMascotaEliminadaEntities(false, maxResults, firstResult);
    }

    private List<MascotaEliminada> findMascotaEliminadaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MascotaEliminada.class));
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

    public MascotaEliminada findMascotaEliminada(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MascotaEliminada.class, id);
        } finally {
            em.close();
        }
    }

    public int getMascotaEliminadaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(em.getCriteriaBuilder().count(cq.from(MascotaEliminada.class)));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}