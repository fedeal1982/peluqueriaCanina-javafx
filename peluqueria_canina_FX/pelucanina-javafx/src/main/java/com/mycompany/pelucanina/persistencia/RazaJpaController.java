package com.mycompany.pelucanina.persistencia;

import com.mycompany.pelucanina.logica.Raza;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

public class RazaJpaController implements Serializable {

    private EntityManagerFactory emf = null;

    public RazaJpaController() {
        this.emf = Persistence.createEntityManagerFactory("PeluCaninaPU");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Raza raza) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(raza);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Raza raza) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            raza = em.merge(raza);
            em.getTransaction().commit();
        } catch (Exception ex) {
            Integer id = raza.getIdRaza();
            if (findRaza(id) == null) {
                throw new Exception("La raza con id " + id + " ya no existe.");
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Raza raza;
            try {
                raza = em.getReference(Raza.class, id);
                raza.getIdRaza();
            } catch (EntityNotFoundException enfe) {
                throw new Exception("La raza con id " + id + " ya no existe.", enfe);
            }
            em.remove(raza);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Raza> findRazaEntities() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Raza.class));
            Query q = em.createQuery(cq);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Raza findRaza(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Raza.class, id);
        } finally {
            em.close();
        }
    }
}