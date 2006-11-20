package org.gmod.schema.sequence;
// Generated Jun 29, 2006 5:26:39 PM by Hibernate Tools 3.1.0.beta5


import org.gmod.schema.analysis.AnalysisFeature;
import org.gmod.schema.cv.CvTerm;
import org.gmod.schema.general.DbXRef;
import org.gmod.schema.organism.Organism;
import org.gmod.schema.phylogeny.Phylonode;
import org.gmod.schema.utils.CollectionUtils;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * BaseFeature generated by hbm2java
 */
@Entity
@Table(name="feature")
public class Feature implements java.io.Serializable {

    
	private Collection<Phylonode> phylonodes;
    @OneToMany(cascade={CascadeType.ALL}, fetch=FetchType.LAZY, mappedBy="feature")
    public Collection<Phylonode> getPhylonodes() {
        return this.phylonodes;
    }
    
    public void setPhylonodes(Collection<Phylonode> phylonodes) {
        this.phylonodes = phylonodes;
    }
    
    // Fields    
    @GenericGenerator(name="generator", strategy="seqhilo", parameters = {  @Parameter(name="max_lo", value="100"), @Parameter(name="sequence", value="feature_feature_id_seq") } )
    @Id @GeneratedValue(generator="generator")
        
        @Column(name="feature_id", unique=false, nullable=false, insertable=true, updatable=true)
     private int featureId;
     
    @ManyToOne(cascade={},fetch=FetchType.LAZY)
    @JoinColumn(name="organism_id", unique=false, nullable=false, insertable=true, updatable=true)
    private Organism organism;
     
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
        @JoinColumn(name="type_id", unique=false, nullable=false, insertable=true, updatable=true)
     private CvTerm cvTerm;
     
    @ManyToOne(cascade={}, fetch=FetchType.LAZY)
        @JoinColumn(name="dbxref_id", unique=false, nullable=true, insertable=true, updatable=true)
     private DbXRef dbXRef;
     
    @Column(name="name", unique=false, nullable=true, insertable=true, updatable=true)
     private String name;
     
    @Column(name="uniquename", unique=false, nullable=false, insertable=true, updatable=true)
     private String uniqueName;
     
    @Column(name="residues", unique=false, nullable=true, insertable=true, updatable=true)
     private byte residues[];
     
    @Column(name="seqlen", unique=false, nullable=true, insertable=true, updatable=true)
     private Integer seqLen;
     
    @Column(name="md5checksum", unique=false, nullable=true, insertable=true, updatable=true, length=32)
     private String md5Checksum;
     
    @Column(name="is_analysis", unique=false, nullable=false, insertable=true, updatable=true)
     private boolean analysis;
     
    @Column(name="is_obsolete", unique=false, nullable=false, insertable=true, updatable=true)
     private boolean obsolete;
     
    @Column(name="timeaccessioned", unique=false, nullable=false, insertable=true, updatable=true, length=29)
     private Timestamp timeAccessioned;
     
    @Column(name="timelastmodified", unique=false, nullable=false, insertable=true, updatable=true, length=29)
     private Timestamp timeLastModified;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="featureBySrcfeatureId")
     private Collection<FeatureLoc> featureLocsForSrcFeatureId;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="featureByObjectId")
     private Collection<FeatureRelationship> featureRelationshipsForObjectId;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="featureBySubjectId")
     private Collection<FeatureRelationship> featureRelationshipsForSubjectId;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="feature")
     private Collection<FeatureDbXRef> featureDbXRefs;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="featureByFeatureId")
     private Collection<FeatureLoc> featureLocsForFeatureId;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="feature")
     private Collection<FeatureCvTerm> featureCvTerms;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="feature")
    //@Cascade( {CascadeType.ALL, CascadeType.DELETE_ORPHAN} )
     private Collection<FeatureProp> featureProps;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="feature")
     private Collection<FeaturePub> featurePubs;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="feature")
     private Collection<AnalysisFeature> analysisFeatures;
     
    @OneToMany(cascade={}, fetch=FetchType.LAZY, mappedBy="feature")
     private Collection<FeatureSynonym> featureSynonyms;
    
    
    // TJC
    private FeatureLoc featureLoc;
    
     // Constructors

    /** default constructor */
    public Feature() {
    }

	/** minimal constructor */
    public Feature(Organism organism, CvTerm cvTerm, String uniqueName, boolean analysis, boolean obsolete, Timestamp timeAccessioned, Timestamp timeLastModified) {
        this.organism = organism;
        this.cvTerm = cvTerm;
        this.uniqueName = uniqueName;
        this.analysis = analysis;
        this.obsolete = obsolete;
        this.timeAccessioned = timeAccessioned;
        this.timeLastModified = timeLastModified;
    }
    /** full constructor */
    private Feature(Organism organism, CvTerm cvTerm, DbXRef dbXRef, String name, String uniqueName, 
            byte[] residues, Integer seqLen, String md5Checksum, boolean analysis, boolean obsolete, 
            Timestamp timeAccessioned, Timestamp timeLastModified, Set<FeatureLoc> featureLocsForSrcFeatureId, 
            Set<FeatureRelationship> featureRelationshipsForObjectId, 
            Set<FeatureRelationship> featureRelationshipsForSubjectId, 
            Set<FeatureDbXRef> featureDbXRefs, Set<FeatureLoc> featureLocsForFeatureId, 
            Set<FeatureCvTerm> featureCvTerms, Set<FeatureProp> featureProps, 
            Set<FeaturePub> featurePubs, Set<AnalysisFeature> analysisFeatures, 
            Set<FeatureSynonym> featureSynonyms) {
       this.organism = organism;
       this.cvTerm = cvTerm;
       this.dbXRef = dbXRef;
       this.name = name;
       this.uniqueName = uniqueName;
       this.residues = residues;
       this.seqLen = seqLen;
       this.md5Checksum = md5Checksum;
       this.analysis = analysis;
       this.obsolete = obsolete;
       this.timeAccessioned = timeAccessioned;
       this.timeLastModified = timeLastModified;
       this.featureLocsForSrcFeatureId = featureLocsForSrcFeatureId;
       this.featureRelationshipsForObjectId = featureRelationshipsForObjectId;
       this.featureRelationshipsForSubjectId = featureRelationshipsForSubjectId;
       this.featureDbXRefs = featureDbXRefs;
       this.featureLocsForFeatureId = featureLocsForFeatureId;
       this.featureCvTerms = featureCvTerms;
       this.featureProps = featureProps;
       this.featurePubs = featurePubs;
       this.analysisFeatures = analysisFeatures;
       this.featureSynonyms = featureSynonyms;
    }
    
   
    // Property accessors

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeatureId()
     */
    public int getFeatureId() {
        return this.featureId;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeatureId(int)
     */
    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getOrganism()
     */
    public Organism getOrganism() {
        return this.organism;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setOrganism(org.gmod.schema.organism.OrganismI)
     */
    public void setOrganism(Organism organism) {
        this.organism = organism;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getCvTerm()
     */
    public CvTerm getCvTerm() {
        return this.cvTerm;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setCvTerm(org.gmod.schema.cv.CvTermI)
     */
    public void setCvTerm(CvTerm cvTerm) {
        this.cvTerm = cvTerm;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getDbxref()
     */
    public DbXRef getDbXRef() {
        return this.dbXRef;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setDbxref(org.gmod.schema.general.DbXRefI)
     */
    public void setDbXRef(DbXRef dbXRef) {
        this.dbXRef = dbXRef;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getName()
     */
    public String getName() {
        return this.name;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getUniquename()
     */
    public String getUniqueName() {
        return this.uniqueName;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setUniquename(java.lang.String)
     */
    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getResidues()
     */
    public byte[] getResidues() {
        return this.residues;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setResidues(java.lang.String)
     */
    public void setResidues(byte[] residues) {
        this.residues = residues;
        if (residues == null) {
            seqLen = 0;
            md5Checksum = "";
            return;
        }
        seqLen = residues.length;
        this.md5Checksum = calcMD5(this.residues);
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getSeqlen()
     */
    public Integer getSeqLen() {
        return this.seqLen;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setSeqlen(java.lang.Integer)
     */
    public void setSeqLen(Integer seqLen) {
        this.seqLen = seqLen;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getMd5Checksum()
     */
    public String getMd5Checksum() {
        return this.md5Checksum;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setMd5Checksum(java.lang.String)
     */
    public void setMd5Checksum(String md5Checksum) {
        this.md5Checksum = md5Checksum;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#isAnalysis()
     */
    public boolean isAnalysis() {
        return this.analysis;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setAnalysis(boolean)
     */
    public void setAnalysis(boolean analysis) {
        this.analysis = analysis;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#isObsolete()
     */
    public boolean isObsolete() {
        return this.obsolete;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setObsolete(boolean)
     */
    public void setObsolete(boolean obsolete) {
        this.obsolete = obsolete;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getTimeAccessioned()
     */
    public Date getTimeAccessioned() {
        return this.timeAccessioned;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setTimeAccessioned(java.util.Date)
     */
    public void setTimeAccessioned(Timestamp timeAccessioned) {
        this.timeAccessioned = timeAccessioned;
    }
    

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getTimeLastModified()
     */
    public Timestamp getTimeLastModified() {
        return this.timeLastModified;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setTimeLastModified(java.util.Date)
     */
    public void setTimeLastModified(Timestamp timeLastModified) {
        this.timeLastModified = timeLastModified;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeaturelocsForSrcfeatureId()
     */
    public Collection<FeatureLoc> getFeatureLocsForSrcFeatureId() {
        return (featureLocsForSrcFeatureId = CollectionUtils.safeGetter(featureLocsForSrcFeatureId));
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeaturelocsForSrcfeatureId(java.util.Set)
     */
    public void setFeatureLocsForSrcFeatureId(Collection<FeatureLoc> featureLocsForSrcFeatureId) {
        this.featureLocsForSrcFeatureId = featureLocsForSrcFeatureId;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeatureRelationshipsForObjectId()
     */
    public Collection<FeatureRelationship> getFeatureRelationshipsForObjectId() {
        return (featureRelationshipsForObjectId = CollectionUtils.safeGetter(featureRelationshipsForObjectId));
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeatureRelationshipsForObjectId(java.util.Set)
     */
    public void setFeatureRelationshipsForObjectId(Collection<FeatureRelationship> featureRelationshipsForObjectId) {
        this.featureRelationshipsForObjectId = featureRelationshipsForObjectId;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeatureRelationshipsForSubjectId()
     */
    public Collection<FeatureRelationship> getFeatureRelationshipsForSubjectId() {
        return (featureRelationshipsForSubjectId = CollectionUtils.safeGetter(featureRelationshipsForSubjectId));
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeatureRelationshipsForSubjectId(java.util.Set)
     */
    public void setFeatureRelationshipsForSubjectId(Collection<FeatureRelationship> featureRelationshipsForSubjectId) {
        this.featureRelationshipsForSubjectId = featureRelationshipsForSubjectId;
    }
    
    public void addFeatureRelationshipsForSubjectId(FeatureRelationship featureRelationshipForSubjectId) {
      featureRelationshipForSubjectId.setFeatureBySubjectId(this);
      this.featureRelationshipsForSubjectId.add(featureRelationshipForSubjectId);
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeatureDbxrefs()
     */
    public Collection<FeatureDbXRef> getFeatureDbXRefs() {
        return this.featureDbXRefs;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeatureDbxrefs(java.util.Set)
     */
    public void setFeatureDbXRefs(Collection<FeatureDbXRef> featureDbXRefs) {
        this.featureDbXRefs = featureDbXRefs;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeaturelocsForFeatureId()
     */
    public Collection<FeatureLoc> getFeatureLocsForFeatureId() {
        return (featureLocsForFeatureId = CollectionUtils.safeGetter(featureLocsForFeatureId));
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeaturelocsForFeatureId(java.util.Set)
     */
    public void addFeatureLocsForFeatureId(FeatureLoc featureLocForFeatureId) {
        featureLocForFeatureId.setFeatureByFeatureId(this);
        this.featureLocsForFeatureId.add(featureLocForFeatureId);
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeatureCvterms()
     */
    public Collection<FeatureCvTerm> getFeatureCvTerms() {
        return this.featureCvTerms;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeatureCvterms(java.util.Set)
     */
    public void setFeatureCvTerms(Collection<FeatureCvTerm> featureCvTerms) {
        this.featureCvTerms = featureCvTerms;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeatureProps()
     */
    public Collection<FeatureProp> getFeatureProps() {
        return (featureProps = CollectionUtils.safeGetter(featureProps));
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeatureProps(java.util.Set)
     */
    public void addFeatureProp(FeatureProp featureProp) {
        featureProp.setFeature(this);
        getFeatureProps().add(featureProp);
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeaturePubs()
     */
    public Collection<FeaturePub> getFeaturePubs() {
        return this.featurePubs;
    }
    
    public void setFeaturePubs(Collection<FeaturePub> featurePubs) {
        this.featurePubs = featurePubs;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getAnalysisfeatures()
     */
    public Collection<AnalysisFeature> getAnalysisFeatures() {
        return this.analysisFeatures;
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setAnalysisfeatures(java.util.Set)
     */
    public void setAnalysisFeatures(Collection<AnalysisFeature> analysisFeatures) {
        this.analysisFeatures = analysisFeatures;
    }

    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#getFeatureSynonyms()
     */
    public Collection<FeatureSynonym> getFeatureSynonyms() {
        return (featureSynonyms = CollectionUtils.safeGetter(featureSynonyms));
    }
    
    /* (non-Javadoc)
     * @see org.genedb.db.jpa.FeatureI#setFeatureSynonyms(java.util.Set)
     */
    public void setFeatureSynonyms(Collection<FeatureSynonym> featureSynonyms) {
        this.featureSynonyms = featureSynonyms;
    }

    public String getDisplayName() {
        //System.err.println("getName is returning '"+getName()+"'");
        return (getName() != null) ? getName() : getUniqueName(); 
    }

    @SuppressWarnings("unused")
    public void setFeatureLocsForFeatureId(Collection<FeatureLoc> featureLocsForFeatureId) {
        this.featureLocsForFeatureId = featureLocsForFeatureId;
    }

    @SuppressWarnings("unused")
    public void setFeatureProps(Collection<FeatureProp> featureProps) {
        this.featureProps = featureProps;
    }

    private String calcMD5(byte[] residue) {
        //byte[] residue = in.getBytes();
        try {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
//          MessageDigest tc1 = md.clone();
        byte[] md5Bytes = md5.digest(residue);

            StringBuilder hexValue = new StringBuilder();
        for (int i=0 ; i<md5Bytes.length ; i++) {
            int val = md5Bytes[i] & 0xff; 
            if (val < 16) hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
            
        }
        catch (NoSuchAlgorithmException exp) {
        exp.printStackTrace(); // Shouldn't happen
        }
        return null;
    }

    public FeatureLoc getFeatureLoc()
    {
      return featureLoc;
    }

    public void setFeatureLoc(FeatureLoc featureLoc)
    {
      this.featureLoc = featureLoc;
    }

}


