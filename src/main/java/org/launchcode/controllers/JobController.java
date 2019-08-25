package org.launchcode.controllers;

import org.launchcode.models.Job;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        // Get the Job with the given ID and pass it into the view

        Job theJob = jobData.findById(id);
        model.addAttribute("job", theJob);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(Model model, @ModelAttribute @Valid JobForm jobForm, Errors errors) {

        // Validate the JobForm model, if there are errors, redisplay the form with errors

        if (errors.hasErrors()) {
            return "new-job";
        }

        // if valid, get the data entered from the form
        Job newJob = new Job();
        newJob.setEmployer(jobData.getEmployers().findById(jobForm.getEmployerId()));
        newJob.setLocation(jobData.getLocations().findById(jobForm.getLocationId()));
        newJob.setCoreCompetency(jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId()));
        newJob.setPositionType(jobData.getPositionTypes().findById(jobForm.getPositionTypeId()));
        newJob.setName(jobForm.getName());

        // Add the new job to the jobData data store
        jobData.add(newJob);

        Integer newJobId = newJob.getId();

        model.addAttribute("job", newJob);
        model.addAttribute("id", newJobId);

        /*
        Redirect to the job detail view for the new Job.

        The following is the only way I found to make the URL match
        what is requested in the assignment
        */
        return "redirect:http://localhost:8080/job?id=" + newJobId;
    }
}
