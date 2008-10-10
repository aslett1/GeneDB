/*
 * Copyright (c) 2006-2007 Genome Research Limited.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published
 * by  the Free Software Foundation; either version 2 of the License or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; see the file COPYING.LIB.  If not, write to
 * the Free Software Foundation Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307 USA
 */

package org.genedb.web.mvc.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.genedb.db.dao.CvDao;
import org.genedb.querying.core.Query;
import org.genedb.querying.tmpquery.BrowseCategory;

import org.gmod.schema.utils.CountedName;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Returns cvterms based on a particular cv
 *
 * @author Chinmay Patel (cp2)
 * @author Adrian Tivey (art)
 */
@Controller
@RequestMapping("/BrowseCategory")
public class BrowseCategoryController {

    private String formView;
    private String successView;

    private static final Logger logger = Logger.getLogger(BrowseCategoryController.class);

    private CvDao cvDao;

    @RequestMapping(method = RequestMethod.GET)
    public String setUpForm(Model model) {
        logger.warn("called method 1");
        model.addAttribute("categories", BrowseCategory.values());
        BrowseCategoryBean bc = new BrowseCategoryController.BrowseCategoryBean();
        model.addAttribute("browseCategory", bc);
        return formView;
    }

    @RequestMapping(method = RequestMethod.GET, params = "category")
    public ModelAndView setUpForm(
            @RequestParam(value="category") BrowseCategory category,
            @RequestParam(value="organism", required=false) String[] orgs,
            Model model) {
        logger.warn("called method 2");

        //-------------------------------------------------------------------------------
        //Collection<String> orgNames = TaxonUtils.getOrgNames(bcb.getOrganism());
        Collection<String> orgNames = Arrays.asList(new String[] {"Pfalciparum"});
        /* This is to include all the cvs starting with CC. In future when the other cvs have more terms in,
         * this can be removed and the other cvs starting with CC can be added to BrowseCategory
         */
        List<CountedName> results = cvDao.getCountedNamesByCvNamePatternAndOrganism(category.getLookupName(), orgNames);

        if (results .isEmpty()) {
            logger.info("result is null");
            //be.reject("no.results");
            // FIXME return showForm(request, response, be);
        }
        logger.debug(results.get(0));

        // Go to list results page
        ModelAndView mav = new ModelAndView(successView);
        mav.addObject("results", results);
        mav.addObject("category", category);
        //mav.addObject("organism",bcb.getOrganism());
        return mav;
        //-------------------------------------------------------------------------------
    }

    protected Map<String,BrowseCategory[]> referenceData(@SuppressWarnings("unused") HttpServletRequest request) throws Exception {
        Map<String,BrowseCategory[]> reference = new HashMap<String,BrowseCategory[]>();
        reference.put("categories", BrowseCategory.values());
        return reference;
    }

//    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException be) throws Exception {
//        BrowseCategoryBean bcb = (BrowseCategoryBean) command;
//        String category = bcb.getCategory().toString();
//        Collection<String> orgNames = TaxonUtils.getOrgNames(bcb.getOrganism());
//
//        /* This is to include all the cvs starting with CC. In future when the other cvs have more terms in,
//         * this can be removed and the other cvs starting with CC can be added to BrowseCategory
//         */
//        List<CountedName> results;
//        if(category.equals("ControlledCuration")) {
//            results = cvDao.getCountedNamesByCvNamePatternAndOrganism(category, orgNames);
//        } else {
//            results = cvDao.getCountedNamesByCvNameAndOrganism(category, orgNames);
//        }
//
//        if (results .isEmpty()) {
//            logger.info("result is null");
//            be.reject("no.results");
//            // FIXME return showForm(request, response, be);
//        }
//        logger.debug(results.get(0));
//
//        // Go to list results page
//        ModelAndView mav = new ModelAndView(successView);
//        mav.addObject("results", results);
//        mav.addObject("category", category);
//        mav.addObject("organism",bcb.getOrganism());
//        return mav;
//    }

    public void setCvDao(CvDao cvDao) {
        this.cvDao = cvDao;
    }

    public static class BrowseCategoryBean {

        private BrowseCategory category;
        private String organism;

        public String getOrganism() {
            return organism;
        }
        public void setOrganism(String organism) {
            this.organism = organism;
        }
        public BrowseCategory getCategory() {
            return this.category;
        }
        public void setCategory(BrowseCategory category) {
            this.category = category;
        }
    }

    public void setFormView(String formView) {
        this.formView = formView;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }
}