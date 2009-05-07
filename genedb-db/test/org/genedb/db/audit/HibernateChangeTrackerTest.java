package org.genedb.db.audit;

import org.gmod.schema.feature.AbstractGene;
import org.gmod.schema.feature.Gene;
import org.gmod.schema.feature.Polypeptide;
import org.gmod.schema.feature.TopLevelFeature;
import org.gmod.schema.feature.Transcript;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Collection;

import junit.framework.Assert;

/**
 *
 * @author lo2@sangerinstitute
 *
 *       // newTopLevelFeature = 1;//Pf3D7_01
 *       // changedTopLevelFeature = 886;//Pf3D7_02
 *       // deletedTopLevelFeature = 9493;//Pf3D7_03
 *       // newPolyPep = 810;//PFA0010c:pep
 *       // changedPep = 614;//PFA0005w:pep
 *       // newGeneId = 2;//PFA0170c
 *       // changedGeneId = 610;//PFA0005w
 *       // newTranscriptId = 7;//PFA0315w:mRNA
 *       // changedTranscriptId = 14;//PFA0380w:mRNA
 *       // deletedTranscriptId = 19;//PFA0440w:mRNA
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class HibernateChangeTrackerTest {
    @Autowired
    private HibernateChangeTracker changeTracker;

    @Autowired
    private SessionFactory sessionFactory;

    private static Logger logger;

    @BeforeClass
    public static void configureLogging() {
        String logFile = "/log4j.test.properties";
        URL url = HibernateChangeTrackerTest.class.getResource(logFile);
        if (url == null) {
            throw new RuntimeException("Could not find classpath resource " + logFile);
        }
        System.out.printf("Configuring Log4J from '%s'\n", url);
        PropertyConfigurator.configure(url);

        logger = Logger.getLogger(HibernateChangeTrackerTest.class);
    }

    @Before
    @Transactional
    public void truncateAuditDB(){
        logger.debug("Starting trauncateAuditDB...");
        executeDML("delete from audit.checkpoint");
        executeDML("delete from audit.feature");

        //shutDownTestDB();
        logger.debug("Ended truncateAuditDB\n");
    }
    
    @Test
    @Transactional
    public void testChangeSetFeatures()throws Exception{
        logger.debug("Starting testChangeSetFeatures...");

        String insertFeatureStm =
            "insert into audit.feature " +
            " (audit_id, type, username, time, feature_id, uniquename, type_id, organism_id, is_obsolete, " +
            " is_analysis, timelastmodified)" +
            "   select next value for audit.audit_seq, 'INSERT', 'theusername', Now(), " +
            "   f.feature_id, f.uniquename, f.type_id, f.organism_id, f.is_obsolete, f.is_analysis, f.timelastmodified" +
            "   from public.feature f" +
            "   where f.feature_id in (1, 810, 2, 7, 14)";
        executeDML(insertFeatureStm);

        //Add Features with Update Types
        String updateFeatureStm =
            "insert into audit.feature " +
            " (audit_id, type, username, time, feature_id, uniquename, type_id, organism_id, is_obsolete, " +
            " is_analysis, timelastmodified)" +
            "   select next value for audit.audit_seq, 'UPDATE', 'theusername', Now(), " +
            "   f.feature_id, f.uniquename, f.type_id, f.organism_id, f.is_obsolete, f.is_analysis, f.timelastmodified" +
            "   from public.feature f" +
            "   where f.feature_id in (886, 610, 14)";
        executeDML(updateFeatureStm);

        //Add Features Delete Types
        String deleteFeatureStm =
            "insert into audit.feature " +
            " (audit_id, type, username, time, feature_id, uniquename, type_id, organism_id, is_obsolete, " +
            " is_analysis, timelastmodified)" +
            "   select next value for audit.audit_seq, 'DELETE', 'theusername', Now(), " +
            "   f.feature_id, f.uniquename, f.type_id, f.organism_id, f.is_obsolete, f.is_analysis, f.timelastmodified" +
            "   from public.feature f" +
            "   where f.feature_id in (9493, 19)";
        executeDML(deleteFeatureStm);

        //Execute class under test
        String currentUser = HibernateChangeTrackerTest.class.getName();
        ChangeSet changeSet = changeTracker.changes(currentUser);

        //Get the new features changeSet
        Collection<Integer> inserts = changeSet.newFeatureIds(TopLevelFeature.class);
        inserts.addAll(changeSet.newFeatureIds(Transcript.class));
        inserts.addAll(changeSet.newFeatureIds(Gene.class));
        inserts.addAll(changeSet.newFeatureIds(Polypeptide.class));
        Assert.assertTrue(inserts.contains(1));
        Assert.assertTrue(inserts.contains(7));
        Assert.assertTrue(inserts.contains(2));
        Assert.assertTrue(inserts.contains(810));

        //Get the updated features changeSet
        Collection<Integer> updates = changeSet.changedFeatureIds(TopLevelFeature.class);
        updates.addAll(changeSet.changedFeatureIds(Transcript.class));
        updates.addAll(changeSet.changedFeatureIds(AbstractGene.class));
        Assert.assertTrue(updates.contains(886));
        Assert.assertTrue(updates.contains(610));
        Assert.assertTrue(updates.contains(14));

        //Get the deleted features changeSet
        Collection<Integer> deletes = changeSet.deletedFeatureIds(TopLevelFeature.class);
        deletes.addAll(changeSet.deletedFeatureIds(Transcript.class));
        Assert.assertTrue(deletes.contains(19));
        Assert.assertTrue(deletes.contains(9493));


    }

    @Test
    @Transactional
    public void testChangeSetFilter()throws Exception{
        logger.debug("Starting testChangeSetFilter...");

        String insertFeatureStm =
            "insert into audit.feature " +
            " (audit_id, type, username, time, feature_id, uniquename, type_id, organism_id, is_obsolete, " +
            " is_analysis, timelastmodified)" +
            "   select next value for audit.audit_seq, 'INSERT', 'theusername', Now(), " +
            "   f.feature_id, f.uniquename, f.type_id, f.organism_id, f.is_obsolete, f.is_analysis, f.timelastmodified" +
            "   from public.feature f" +
            "   where f.feature_id in (1, 810, 2, 7, 14)";
        executeDML(insertFeatureStm);

        //add a Delete Type feature that has already been added as a Insert Type feature
        String deleteFeatureStm =
            "insert into audit.feature " +
            " (audit_id, type, username, time, feature_id, uniquename, type_id, organism_id, is_obsolete, " +
            " is_analysis, timelastmodified)" +
            "   select next value for audit.audit_seq, 'DELETE', 'theusername', Now(), " +
            "   f.feature_id, f.uniquename, f.type_id, f.organism_id, f.is_obsolete, f.is_analysis, f.timelastmodified" +
            "   from public.feature f" +
            "   where f.feature_id = 7";
        executeDML(deleteFeatureStm);

        //Execute class under test
        String currentUser = HibernateChangeTrackerTest.class.getName();
        ChangeSet changeSet = changeTracker.changes(currentUser);

        //Get the changeSet
        Collection<Integer> inserts = changeSet.newFeatureIds(Transcript.class);
        Collection<Integer> deletes = changeSet.deletedFeatureIds(Transcript.class);

        //This should be in the Insert list
        Assert.assertTrue(inserts.contains(14));

        //Feature ID 7 should not be in either Insert or Delete
        Assert.assertFalse(inserts.contains(7));
        Assert.assertFalse(deletes.contains(7));

        logger.debug("Ended testChangeSetFilter");
    }

    @Test
    @Transactional
    public void testChangeSetCommit() throws Exception{
        logger.debug("Starting testChangeSetCommit...");

        String insertFeatureStm =
            "insert into audit.feature " +
            " (audit_id, type, username, time, feature_id, uniquename, type_id, organism_id, is_obsolete, " +
            " is_analysis, timelastmodified)" +
            "   select next value for audit.audit_seq, 'INSERT', 'theusername', Now(), " +
            "   f.feature_id, f.uniquename, f.type_id, f.organism_id, f.is_obsolete, f.is_analysis, f.timelastmodified" +
            "   from public.feature f" +
            "   where f.feature_id in (1, 810, 2, 7, 14)";
        executeDML(insertFeatureStm);

        //Execute class under test
        String currentUser = HibernateChangeTrackerTest.class.getName();
        ChangeSet changeSet = changeTracker.changes(currentUser);

        //Get the ID of newest/max(Feature_Id) record of the audit
        long currentSequenceId = changeTracker.getCurrentAuditId();
        logger.debug("currentSequenceId: " + currentSequenceId);

        //This should update the final checkpoint
        changeSet.commit();

        //Get the latest checkpoint of the current user
        Number finalCheckPoint = (Number)executeSingleResultQuery(
                "select audit_id from audit.checkpoint where key = :key", "key", currentUser);
        logger.debug("Final CheckPoint: " + finalCheckPoint);

        //Assert current user's checkpoint is the ID of last record
        Assert.assertEquals(currentSequenceId, finalCheckPoint.longValue());

        logger.debug("Ended testChangeSetCommit");
    }


    private void executeDML(String queryStr){
        logger.debug("Starting executeDML...");
        Session session = SessionFactoryUtils.getSession(sessionFactory, false);
        Query query = session.createSQLQuery(queryStr);
        int updateCount = query.executeUpdate();
        logger.debug("Executed on rows...#" + updateCount);
        logger.debug("Ended executeDML.");
    }


    private Object executeSingleResultQuery(String queryStr, String paramName, String paramValue){
        logger.debug("Starting executeQuery...");
        Session session = SessionFactoryUtils.getSession(sessionFactory, false);
        Query query = session.createSQLQuery(queryStr).setString(paramName, paramValue);
        Object obj = query.uniqueResult();
        logger.debug("Ended executeQuery.");
        return obj;
    }
    /**
     * Required to close the HSQL DB properly
     */
    private void shutDownTestDB(){
        logger.debug("Starting shutDownDB...");
        Session session = SessionFactoryUtils.getSession(sessionFactory, false);
        Query query = session.createSQLQuery("shutdown");
        query.executeUpdate();
        logger.debug("Ended shutDownDB.");
    }
}
