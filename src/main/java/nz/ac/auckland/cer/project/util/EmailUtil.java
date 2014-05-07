package nz.ac.auckland.cer.project.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;

import nz.ac.auckland.cer.common.util.TemplateEmail;
import nz.ac.auckland.cer.project.pojo.Project;
import nz.ac.auckland.cer.project.pojo.ProjectRequest;
import nz.ac.auckland.cer.project.pojo.Researcher;

public class EmailUtil {

    private Logger log = Logger.getLogger(EmailUtil.class.getName());
    @Autowired private TemplateEmail templateEmail;
    @Autowired private AffiliationUtil affUtil;
    private Resource projectRequestEmailBodyResource;
    private Resource projectRequestWithSuperviserEmailBodyResource;
    private Resource membershipRequestEmailBodyResource;
    private String projectBaseUrl;
    private String projectRequestEmailSubject;
    private String membershipRequestEmailSubject;
    private String emailFrom;
    private String emailTo;

    public void sendProjectRequestEmail(
            Project p,
            String researcherName) throws Exception {

        Map<String, String> templateParams = new HashMap<String, String>();
        templateParams.put("__RESEARCHER_NAME__", researcherName);
        templateParams.put("__PROJECT_TITLE__", p.getName());
        templateParams.put("__PROJECT_DESCRIPTION__", p.getDescription());
        templateParams.put("__PROJECT_LINK__", this.projectBaseUrl + "?id=" + p.getProjectId());
        try {
            this.templateEmail.sendFromResource(this.emailFrom, this.emailTo, null, null,
                    this.projectRequestEmailSubject, this.projectRequestEmailBodyResource, templateParams);
        } catch (Exception e) {
            log.error("Failed to send project request email", e);
            throw new Exception("Failed to notify CeR staff about the new project request");
        }
    }

    public void sendProjectRequestWithSuperviserEmail(
            Project p,
            ProjectRequest pr,
            Researcher superviser,
            String researcherName) throws Exception {

        Map<String, String> templateParams = new HashMap<String, String>();
        String extraInfos = "The superviser does not yet exist in the database.";
        if (superviser != null) {
            extraInfos = "The superviser already exists in the database.";
            templateParams.put("__SUPERVISER_NAME__", superviser.getFullName());
            templateParams.put("__SUPERVISER_EMAIL__", superviser.getEmail());
            templateParams.put("__SUPERVISER_PHONE__", superviser.getPhone());
            String affil = this.affUtil.createAffiliationString(superviser.getInstitution(), superviser.getDivision(), superviser.getDepartment());
            templateParams.put("__SUPERVISER_AFFILIATION__", affil);
        } else {
            templateParams.put("__SUPERVISER_NAME__", pr.getSuperviserName());
            templateParams.put("__SUPERVISER_EMAIL__", pr.getSuperviserEmail());
            templateParams.put("__SUPERVISER_PHONE__", pr.getSuperviserPhone());
            if (pr.getSuperviserAffiliation() != null && !pr.getSuperviserAffiliation().equals("OTHER")) {
                templateParams.put("__SUPERVISER_AFFILIATION__", pr.getSuperviserAffiliation());
            } else {
                templateParams.put("__SUPERVISER_AFFILIATION__", pr.getSuperviserOtherAffiliation());                
            }
        }
        templateParams.put("__RESEARCHER_NAME__", researcherName);
        templateParams.put("__PROJECT_TITLE__", p.getName());
        templateParams.put("__PROJECT_DESCRIPTION__", p.getDescription());
        templateParams.put("__PROJECT_LINK__", this.projectBaseUrl + "?id=" + p.getId());
        templateParams.put("__SUPERVISER_EXTRA_INFOS__", extraInfos);

        try {
            this.templateEmail
                    .sendFromResource(this.emailFrom, this.emailTo, null, null, this.projectRequestEmailSubject,
                            this.projectRequestWithSuperviserEmailBodyResource, templateParams);
        } catch (Exception e) {
            log.error("Failed to send project request email", e);
            throw new Exception("Failed to notify CeR staff about the new project request");
        }
    }

    public void sendMembershipRequestRequestEmail(
            Project p,
            String researcherName) throws Exception {

        Map<String, String> templateParams = new HashMap<String, String>();
        templateParams.put("__RESEARCHER_NAME__", researcherName);
        templateParams.put("__PROJECT_TITLE__", p.getName());
        templateParams.put("__PROJECT_DESCRIPTION__", p.getDescription());
        templateParams.put("__PROJECT_LINK__", this.projectBaseUrl + "?id=" + p.getId());
        try {
            this.templateEmail.sendFromResource(this.emailFrom, this.emailTo, null, null,
                    this.membershipRequestEmailSubject, this.membershipRequestEmailBodyResource, templateParams);
        } catch (Exception e) {
            log.error("Failed to send project membership request email", e);
            throw new Exception("Failed to notify CeR staff about the project membership request");
        }
    }

    public void setEmailFrom(
            String emailFrom) {

        this.emailFrom = emailFrom;
    }

    public void setEmailTo(
            String emailTo) {

        this.emailTo = emailTo;
    }

    public void setProjectRequestEmailSubject(
            String projectRequestEmailSubject) {

        this.projectRequestEmailSubject = projectRequestEmailSubject;
    }

    public void setProjectRequestEmailBodyResource(
            Resource projectRequestEmailBodyResource) {

        this.projectRequestEmailBodyResource = projectRequestEmailBodyResource;
    }

    public void setMembershipRequestEmailSubject(
            String membershipRequestEmailSubject) {

        this.membershipRequestEmailSubject = membershipRequestEmailSubject;
    }

    public void setMembershipRequestEmailBodyResource(
            Resource membershipRequestEmailBodyResource) {

        this.membershipRequestEmailBodyResource = membershipRequestEmailBodyResource;
    }

    public void setProjectRequestWithSuperviserEmailBodyResource(
            Resource projectRequestWithSuperviserEmailBodyResource) {

        this.projectRequestWithSuperviserEmailBodyResource = projectRequestWithSuperviserEmailBodyResource;
    }

    public void setProjectBaseUrl(
            String projectBaseUrl) {

        this.projectBaseUrl = projectBaseUrl;
    }

}