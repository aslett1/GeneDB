package org.genedb.querying.tmpquery;

import org.genedb.querying.core.QueryClass;
import org.genedb.querying.core.QueryParam;

import org.apache.log4j.Logger;
import org.hibernate.Query;

@QueryClass(
        title="Transcripts by their type",
        shortDesc="Get a list of transcripts by type",
        longDesc=""
    )
public class GenesByDbQuery extends OrganismHqlQuery {

    private static final Logger logger = Logger.getLogger(GenesByDbQuery.class);

    @QueryParam(
            order=1,
            title="DB Accession"
    )
    private String accession;

    @Override
    public String getQueryDescription() {
    	return "Searches for features that have a certain controlled vocabulary term associated with them.";
    }

    @Override
    public String getQueryName() {
        return "Database Accession";
    }


    @Override
    protected String getHql() {
        return "select f.uniqueName from Feature f, FeatureCvTerm fc, CvTerm c where fc.feature=f and fc.cvTerm=c and c.dbXRef.accession=:accession @ORGANISM@ order by f.organism";
    }

    // ------ Autogenerated code below here

    public String getAccession() {
        return accession;
    }


    public void setAccession(String accession) {
        this.accession = accession;
    }

    @Override
    protected String[] getParamNames() {
        return new String[] {"accession"};
    }

    @Override
    protected void populateQueryWithParams(Query query) {
        super.populateQueryWithParams(query);
        logger.error(String.format("accession='%s'", accession));
        query.setString("accession", accession);
    }


}
