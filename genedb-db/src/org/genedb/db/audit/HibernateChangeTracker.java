package org.genedb.db.audit;

import org.gmod.schema.cfg.ChadoAnnotationConfiguration;
import org.gmod.schema.cfg.ChadoSessionFactoryBean;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

public class HibernateChangeTracker implements ChangeTracker {
    private static final Logger logger = Logger.getLogger(HibernateChangeTracker.class);

    @Resource(name="sessionFactory")
    private SessionFactory sessionFactory;

    @Resource(name="&sessionFactory")
    private ChadoSessionFactoryBean sessionFactoryBean;

    private long currentAuditId;

    @Override
    @Transactional
    public HibernateChangeSet changes(String key) throws SQLException {
        Session session = SessionFactoryUtils.getSession(sessionFactory, false);

        BigInteger checkpointAuditIdInteger = (BigInteger) session.createSQLQuery(
            "select audit_id from audit.checkpoint where key = :key"
        ).setString("key", key)
        .uniqueResult();
        int checkpointAuditId = checkpointAuditIdInteger == null ? 0 : checkpointAuditIdInteger.intValue();
        logger.debug("CheckPointAuditId: '" + checkpointAuditId + "'");

        Configuration configuration = sessionFactoryBean.getConfiguration();
        if (!(configuration instanceof ChadoAnnotationConfiguration)) {
            throw new RuntimeException();
        }
        
        currentAuditId =  ((Number)session.createSQLQuery(
            Dialect.getDialect(configuration.getProperties()).getSequenceNextValString("audit.audit_seq")
        ).uniqueResult()).longValue();
        logger.debug("Current Audit ID: " + currentAuditId);


        HibernateChangeSet changeSet = new HibernateChangeSet(session, key, currentAuditId);
        changeSet.setChadoAnnotationConfiguration((ChadoAnnotationConfiguration) configuration);

        processFeatureAuditRecords(checkpointAuditId, changeSet);
        processFeatureRelationshipAuditRecords(checkpointAuditId, changeSet);
        processFeatureLocAuditRecords(checkpointAuditId, changeSet);


        return changeSet;
    }

    /**
     * @param session
     * @param checkpointAuditId
     * @param changeSet
     */
    private void processFeatureAuditRecords(int checkpointAuditId,
            HibernateChangeSet changeSet) {

        Session session = SessionFactoryUtils.getSession(sessionFactory, false);

        SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(
                "select audit_id, feature_id, type, uniquename, type_id" +
                " from audit.feature" +
                " where audit_id > :checkpoint and audit_id < :currentAuditId" +
                " order by audit_id"
            ).addScalar("audit_id", Hibernate.INTEGER)
            .addScalar("feature_id", Hibernate.INTEGER)
            .addScalar("type", Hibernate.STRING)
            .addScalar("uniquename", Hibernate.STRING)
            .addScalar("type_id", Hibernate.INTEGER)
            .setInteger("checkpoint", checkpointAuditId)
            .setLong("currentAuditId", currentAuditId);
        sqlQuery.setReadOnly(true);
        sqlQuery.setCacheMode(CacheMode.IGNORE);
        sqlQuery.setCacheable(false);
        sqlQuery.setFetchSize(1000);


        ScrollableResults sr = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);

        boolean more = sr.next();
        while (more) {

            int    auditId    = sr.getInteger(0);
            int    featureId  = sr.getInteger(1);
            String type       = sr.getString(2);
            String uniqueName = sr.getString(3);
            int    typeId     = sr.getInteger(4);

            logger.trace(String.format("[%d] %s of feature '%s' (ID=%d)",
                auditId, type, uniqueName, featureId));

            if (type.equals("INSERT")) {
                changeSet.insertedFeature(auditId, featureId, typeId);
            } else if (type.equals("UPDATE")) {
                changeSet.updatedFeature(auditId, featureId, typeId);
            } else if (type.equals("DELETE")) {
                changeSet.deletedFeature(auditId, featureId, typeId);
            }
            more = sr.next();
        }
    }

    private void processFeatureRelationshipAuditRecords(int checkpointAuditId,
            HibernateChangeSet changeSet) {

        Session session = SessionFactoryUtils.getSession(sessionFactory, false);

        SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(
            "select feature_relationship.audit_id" +
            "     , feature_relationship.type" +
            "     , feature_relationship.feature_relationship_id" +
            "     , feature_relationship.object_id" +
            "     , feature.type_id" +
            " from audit.feature_relationship" +
            " join public.feature on feature.feature_id = feature_relationship.object_id" +
            " where audit_id > :checkpoint and audit_id < :currentAuditId" +
            " order by audit_id"
        ).addScalar("audit_id", Hibernate.INTEGER)
        .addScalar("type", Hibernate.STRING)
        .addScalar("feature_relationship_id", Hibernate.INTEGER)
        .addScalar("object_id", Hibernate.INTEGER)
        .addScalar("type_id", Hibernate.INTEGER)
        .setInteger("checkpoint", checkpointAuditId)
        .setLong("currentAuditId", currentAuditId);

        sqlQuery.setReadOnly(true);
        sqlQuery.setCacheMode(CacheMode.IGNORE);
        sqlQuery.setCacheable(false);
        sqlQuery.setFetchSize(1000);


        ScrollableResults sr = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);

        boolean more = sr.next();
        while (more) {
            int auditId    = sr.getInteger(0);
            String type  = sr.getString(1);
            int featureRelationshipId = sr.getInteger(2);
            int featureId  = sr.getInteger(3);
            int typeId     = sr.getInteger(4);

            logger.trace(String.format("[%d] %s of feature_relationship ID=%d, " +
                    "counts as update of object feature ID=%d (type ID=%d)",
                auditId, type, featureRelationshipId, featureId, typeId));

            if (type.equals("INSERT") || type.equals("DELETE")) {
                changeSet.updatedFeature(auditId, featureId, typeId);
            }
            more = sr.next();
        }
    }

    private void processFeatureLocAuditRecords(int checkpointAuditId,
            HibernateChangeSet changeSet) {

        Session session = SessionFactoryUtils.getSession(sessionFactory, false);

        SQLQuery sqlQuery = (SQLQuery) session.createSQLQuery(
            "select featureloc.audit_id" +
            "     , featureloc.type" +
            "     , featureloc.featureloc_id" +
            "     , featureloc.srcfeature_id" +
            "     , feature.type_id" +
            " from audit.featureloc" +
            " join public.feature on feature.feature_id = featureloc.srcfeature_id" +
            " where audit_id > :checkpoint and audit_id < :currentAuditId" +
            " order by audit_id"
        ).addScalar("audit_id", Hibernate.INTEGER)
        .addScalar("type", Hibernate.STRING)
        .addScalar("featureloc_id", Hibernate.INTEGER)
        .addScalar("srcfeature_id", Hibernate.INTEGER)
        .addScalar("type_id", Hibernate.INTEGER)
        .setInteger("checkpoint", checkpointAuditId)
        .setLong("currentAuditId", currentAuditId);

        sqlQuery.setReadOnly(true);
        sqlQuery.setCacheMode(CacheMode.IGNORE);
        sqlQuery.setCacheable(false);
        sqlQuery.setFetchSize(1000);


        ScrollableResults sr = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);

        boolean more = sr.next();
        while (more) {

            int    auditId      = sr.getInteger(0);
            String type         = sr.getString(1);
            int    featureLocId = sr.getInteger(2);
            int    featureId    = sr.getInteger(3);
            int    typeId       = sr.getInteger(4);

            logger.trace(String.format("[%d] %s of featureloc ID=%d, " +
                    "counts as update of source feature ID=%d (type ID=%d)",
                auditId, type, featureLocId, featureId, typeId));

            if (type.equals("INSERT") || type.equals("DELETE")) {
                changeSet.updatedFeature(auditId, featureId, typeId);
            }
            more = sr.next();
        }
    }


    public long getCurrentAuditId() {
        return currentAuditId;
    }
}
